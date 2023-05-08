package pl.moderr.moderrkowo.core.listeners;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

public class TNTListener implements Listener {
    public static void replaceSphere(Location centerBlock, int radius, boolean hollow, Material from, Material to, int percent) {

        List<Location> circleBlocks = new ArrayList<>();

        int bx = centerBlock.getBlockX();
        int by = centerBlock.getBlockY();
        int bz = centerBlock.getBlockZ();

        for (int x = bx - radius; x <= bx + radius; x++) {
            for (int y = by - radius; y <= by + radius; y++) {
                for (int z = bz - radius; z <= bz + radius; z++) {

                    double distance = ((bx - x) * (bx - x) + ((bz - z) * (bz - z)) + ((by - y) * (by - y)));

                    if (distance < radius * radius && !(hollow && distance < ((radius - 1) * (radius - 1)))) {

                        Location l = new Location(centerBlock.getWorld(), x, y, z);
                        World world = centerBlock.getWorld();
                        if (Math.random() * 100 < percent) {
                            if (world.getBlockAt(l).getType() == from) {
                                world.getBlockAt(l).setType(to);
                            }
                        }


                    }

                }
            }
        }

    }
    @EventHandler
    public void onExplode(EntityExplodeEvent e) {
        if (e.getEntityType() == EntityType.PRIMED_TNT) {
            e.setCancelled(true);
            e.getEntity().getWorld().createExplosion(e.getLocation(), 7);
            replaceSphere(e.getEntity().getLocation(), 8, false, Material.AIR, Material.FIRE, 10);
            replaceSphere(e.getEntity().getLocation(), 5, false, Material.STONE, Material.COBBLESTONE, 3);
            replaceSphere(e.getEntity().getLocation(), 5, false, Material.STONE, Material.ANDESITE, 6);
            replaceSphere(e.getEntity().getLocation(), 5, false, Material.STONE, Material.GRAVEL, 5);
            e.getEntity().getWorld().spawnParticle(Particle.FLAME, e.getLocation(), 400, 1, 1, 1, 0.3f);
            e.getEntity().getWorld().spawnParticle(Particle.SMOKE_LARGE, e.getLocation(), 275, 2, 2, 2, 0.1f);
            e.getEntity().getWorld().spawnParticle(Particle.SMOKE_LARGE, e.getLocation().add(0, 3, 0), 375, 2, 2, 2, 0.02f);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(e.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 0.2f);
                p.playSound(e.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 5, 2f);
            }
        }
        if (e.getEntityType() == EntityType.MINECART_TNT) {
            e.setCancelled(true);
            e.getEntity().getWorld().createExplosion(e.getLocation(), 14);
            replaceSphere(e.getEntity().getLocation(), 12, false, Material.AIR, Material.FIRE, 10);
            replaceSphere(e.getEntity().getLocation(), 12, false, Material.STONE, Material.COBBLESTONE, 3);
            replaceSphere(e.getEntity().getLocation(), 12, false, Material.STONE, Material.ANDESITE, 6);
            replaceSphere(e.getEntity().getLocation(), 12, false, Material.STONE, Material.GRAVEL, 15);
            e.getEntity().getWorld().spawnParticle(Particle.FLAME, e.getLocation(), 400, 1, 1, 1, 0.3f);
            e.getEntity().getWorld().spawnParticle(Particle.SMOKE_LARGE, e.getLocation(), 275, 2, 2, 2, 0.1f);
            e.getEntity().getWorld().spawnParticle(Particle.SMOKE_LARGE, e.getLocation().add(0, 3, 0), 375, 2, 2, 2, 0.02f);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.playSound(e.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 5, 0.2f);
                p.playSound(e.getLocation(), Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 5, 2f);
            }
        }
    }
}
