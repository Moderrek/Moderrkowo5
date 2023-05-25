package pl.moderr.moderrkowo.core.services.cuboids.listeners;

import com.destroystokyo.paper.Title;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.managers.storage.StorageException;
import com.sk89q.worldguard.protection.regions.ProtectedCuboidRegion;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import org.bukkit.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.FixedMetadataValue;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.Logger;
import pl.moderr.moderrkowo.core.services.cuboids.CuboidsManager;

import java.io.IOException;
import java.util.Objects;

public class PlaceRemoveCuboid implements Listener {

    public PlaceRemoveCuboid() {
        for (World w : Bukkit.getWorlds()) {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regions = container.get(BukkitAdapter.adapt(w));
            if (regions == null) {
                Logger.logAdminLog("Wystąpił błąd podczas wczytywania działek w " + w.getName());
                continue;
            }
            for (ProtectedRegion cub : regions.getRegions().values()) {
                if (cub.getId().startsWith(CuboidsManager.getCuboidNamePrefix())) {
                    String playerName = cub.getId().replace(CuboidsManager.getCuboidNamePrefix(), "");
                    Location loc = ModerrkowoPlugin.getInstance().dataConfig.getLocation("cuboid." + w.getName() + "." + CuboidsManager.getCuboidNamePrefix() + playerName);
                    if (loc == null) {
                        Logger.logAdminLog(playerName + " ma zepsutą działkę!");
                        continue;
                    }
                    if (loc.getBlock().getType().equals(Material.AIR)) {
                        loc.getBlock().setType(Material.LODESTONE);
                    }
                    if (loc.getBlock().hasMetadata("cuboid") && loc.getBlock().hasMetadata("cuboid-owner")) {
                        Logger.logAdminLog("Działka była wczytana " + playerName + " [" + w.getName() + "]");
                    } else {
                        loc.getBlock().setMetadata("cuboid", new FixedMetadataValue(ModerrkowoPlugin.getInstance(), true));
                        loc.getBlock().setMetadata("cuboid-owner", new FixedMetadataValue(ModerrkowoPlugin.getInstance(), playerName));
                        Logger.logAdminLog("Stworzono i wczytano działka " + playerName + " [" + w.getName() + "]");
                    }
                }
            }
        }
    }

    @EventHandler
    public void cuboidPlace(BlockPlaceEvent e) {
        if (e.getItemInHand().isSimilar(CuboidsManager.getCuboidItem(1))) {
            if (!e.getBlock().getWorld().getName().equals("world")) {
                e.getPlayer().sendMessage(ColorUtil.color("&cDziałki można postawić tylko w normalnym świecie"));
                e.setCancelled(true);
                return;
            }
            boolean canBuild = !e.isCancelled();
            BlockVector3 center = BukkitAdapter.asBlockVector(e.getBlockPlaced().getLocation());
            BlockVector3 minCheck = center.subtract(64 - 1, 0, 64 - 1);
            minCheck = minCheck.withY(e.getPlayer().getLocation().getWorld().getMinHeight());
            BlockVector3 maxCheck = center.add(64 - 1, 0, 64 - 1);
            maxCheck = maxCheck.withY(e.getPlayer().getLocation().getWorld().getMaxHeight());
            ProtectedRegion checkArea = new ProtectedCuboidRegion("check", minCheck, maxCheck);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionManager regions = container.get(BukkitAdapter.adapt(e.getBlockPlaced().getWorld()));
            ApplicableRegionSet protectedRegions;
            ApplicableRegionSet protectedRegionsCheck;
            assert regions != null;
            if (regions.getRegion(CuboidsManager.getCuboidNamePrefix().toLowerCase() + e.getPlayer().getName().toLowerCase()) != null) {
                e.getPlayer().sendMessage(ModerrkowoPlugin.getServerName() + ColorUtil.color(" &cJuż masz jedną działkę w tym świecie!"));
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                e.setCancelled(true);
                return;
            }
            protectedRegions = regions.getApplicableRegions(center);
            protectedRegionsCheck = regions.getApplicableRegions(checkArea);
            for (ProtectedRegion ignored : protectedRegionsCheck) {
                e.getPlayer().sendMessage(ModerrkowoPlugin.getServerName() + ColorUtil.color(" &cNie możesz postawić tutaj działki, znajduję się za blisko innej działki!"));
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                e.setCancelled(true);
                return;
            }
            for (ProtectedRegion ignored : protectedRegions.getRegions()) {
                e.getPlayer().sendMessage(ModerrkowoPlugin.getServerName() + ColorUtil.color(" &cNie możesz postawić drugiej działki!"));
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                e.setCancelled(true);
                return;
            }
            if (canBuild) {
                BlockVector3 min = center.subtract(64 - 1, 0, 64 - 1);
                min = min.withY(e.getPlayer().getLocation().getWorld().getMinHeight());
                BlockVector3 max = center.add(64 - 1, 0, 64 - 1);
                max = max.withY(e.getPlayer().getLocation().getWorld().getMaxHeight());
                ProtectedRegion newCuboid = new ProtectedCuboidRegion(CuboidsManager.getCuboidNamePrefix().toLowerCase() + e.getPlayer().getName().toLowerCase(), min, max);
                newCuboid.getOwners().addPlayer(e.getPlayer().getUniqueId());
                newCuboid.setFlag(Flags.GREET_MESSAGE,
                        ColorUtil.color("&aWkroczyłeś na teren działki gracza &2" + newCuboid.getId().replace(CuboidsManager.getCuboidNamePrefix().toLowerCase(), "").toUpperCase()));
                newCuboid.setFlag(Flags.FAREWELL_MESSAGE,
                        ColorUtil.color("&aOpuszczasz teren działki gracza &2" + newCuboid.getId().replace(CuboidsManager.getCuboidNamePrefix().toLowerCase(), "").toUpperCase()));
                newCuboid.setFlag(Flags.DENY_MESSAGE,
                        ModerrkowoPlugin.getServerName() + ColorUtil.color(" &cNie masz uprawnień do interakcji na tej działce!"));
                regions.addRegion(newCuboid);
                e.getBlockPlaced().setMetadata("cuboid",
                        new FixedMetadataValue(ModerrkowoPlugin.getInstance(), true));
                ModerrkowoPlugin.getInstance().dataConfig.set("cuboid." + e.getBlockPlaced().getWorld().getName() + "." + CuboidsManager.getCuboidNamePrefix().toLowerCase() + e.getPlayer().getName().toLowerCase(), e.getBlockPlaced().getLocation());
                try {
                    ModerrkowoPlugin.getInstance().dataConfig.save(ModerrkowoPlugin.getInstance().dataFile);
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                e.getBlockPlaced().setMetadata("cuboid-owner",
                        new FixedMetadataValue(ModerrkowoPlugin.getInstance(), e.getPlayer().getName().toLowerCase()));
                e.getPlayer().sendMessage(ModerrkowoPlugin.getServerName() + ColorUtil.color(" &aPostawiłeś własną prywatną działkę! Aby zarządzać nią wpisz &2/dzialka"));
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
                try {
                    regions.save();
                } catch (StorageException storageException) {
                    storageException.printStackTrace();
                }
                e.getPlayer().spawnParticle(Particle.TOTEM, e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getY(), e.getPlayer().getLocation().getZ(), 50, 1, 1, 1, 0.1f);
            }
        }
    }

    @EventHandler
    public void cuboidDestroy(BlockBreakEvent e) {
        if (e.getBlock().hasMetadata("cuboid")) {
            if (e.getBlock().getMetadata("cuboid-owner").get(0).asString().equals(e.getPlayer().getName().toLowerCase())) {
                BlockVector3 block = BukkitAdapter.asBlockVector(e.getBlock().getLocation());
                RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
                RegionManager regions = container.get(BukkitAdapter.adapt(e.getBlock().getWorld()));
                assert regions != null;
                ApplicableRegionSet set = regions.getApplicableRegions(block);
                for (ProtectedRegion cuboid : set.getRegions()) {
                    if (cuboid.getId().startsWith(CuboidsManager.getCuboidNamePrefix())) {
                        e.setCancelled(true);
                        try {
                            ModerrkowoPlugin.getInstance().dataConfig.set("cuboid." + e.getBlock().getWorld().getName() + "." + CuboidsManager.getCuboidNamePrefix().toLowerCase() + e.getPlayer().getName().toLowerCase(), null);
                            ModerrkowoPlugin.getInstance().dataConfig.save(ModerrkowoPlugin.getInstance().dataFile);
                            e.getBlock().setType(Material.AIR);
                            e.getBlock().removeMetadata("cuboid", ModerrkowoPlugin.getInstance());
                            e.getBlock().removeMetadata("cuboid-owner", ModerrkowoPlugin.getInstance());
                            regions.removeRegion(cuboid.getId());
                            regions.save();
                            e.getPlayer().sendTitle(new Title(ModerrkowoPlugin.getServerName(), ColorUtil.color(" &aPomyślnie usunięto działkę!")));
                            if (!e.getPlayer().getGameMode().equals(GameMode.CREATIVE)) {
                                e.getBlock().getLocation().getWorld().dropItemNaturally(e.getBlock().getLocation(), Objects.requireNonNull(CuboidsManager.getCuboidItem(1)));
                            }
                            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                        } catch (IOException | StorageException ioException) {
                            ioException.printStackTrace();
                            e.getPlayer().playSound(e.getBlock().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            e.getPlayer().sendMessage(ColorUtil.color("&cWystąpił błąd podczas usuwania działki"));
                        }
                    }
                }
            } else {
                e.getPlayer().sendTitle(new Title(ModerrkowoPlugin.getServerName(), ColorUtil.color(" &cNie jesteś właścicielem tej działki! (" + e.getBlock().getMetadata("cuboid-owner").get(0).asString() + ")")));
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                e.getPlayer().spawnParticle(Particle.VILLAGER_ANGRY, e.getBlock().getLocation().getX() + 0.5f, e.getBlock().getLocation().getY() + 1, e.getBlock().getLocation().getZ(), 1);
                e.setCancelled(true);
            }
        }
    }

}
