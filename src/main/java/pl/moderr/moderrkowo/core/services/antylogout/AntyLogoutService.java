package pl.moderr.moderrkowo.core.services.antylogout;

import lombok.Getter;
import me.filoghost.holographicdisplays.api.HolographicDisplaysAPI;
import me.filoghost.holographicdisplays.api.hologram.Hologram;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.ServerService;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AntyLogoutService implements ServerService, Listener {

    @Getter
    private final long antyLogoutDuration = 200L;

    private final ModerrkowoPlugin plugin;
    @Getter
    private final Map<UUID, AntyLogoutData> antyLogout;

    public AntyLogoutService(@NotNull ModerrkowoPlugin plugin) {
        this.plugin = plugin;
        this.antyLogout = new ConcurrentHashMap<>();
    }

    @Override
    public void Start(@NotNull ModerrkowoPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(ModerrkowoPlugin.getInstance(), new AntyLogoutTask(this), 0, 1);
    }

    @Override
    public void Disable(ModerrkowoPlugin plugin) {

    }

    public Set<UUID> getUUIDs() {
        return antyLogout.keySet();
    }

    public boolean isFighting(UUID uuid) {
        return antyLogout.containsKey(uuid);
    }

    @EventHandler
    public void onQuit(@NotNull PlayerQuitEvent e) {
        final Player player = e.getPlayer();
        final UUID uuid = player.getUniqueId();
        if (getUUIDs().contains(uuid)) {
            // Kill player
            player.setHealth(0);
            // Broadcast message
            final String playerName = e.getPlayer().getName();
            final TextComponent component = Component.text()
                    .content(MessageFormat.format(" {0} wylogował się podczas walki!", playerName))
                    .color(NamedTextColor.RED)
                    .build();
            ModerrkowoPlugin.getInstance().getServer().broadcast(component);
            // Remove from anty logouts
            antyLogout.remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void damage(@NotNull EntityDamageByEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Entity whoWasHit = e.getEntity();
        // Hologram
        Random rd = new Random();
        Location loc = whoWasHit.getLocation().clone().add(rd.nextDouble(), 1, rd.nextDouble());

        Double d = e.getFinalDamage() / 2;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(1);
        String content = MessageFormat.format("-{0}❤", df.format(d));

        HolographicDisplaysAPI api = HolographicDisplaysAPI.get(plugin);
        final Hologram hologram = api.createHologram(loc);
        hologram.getLines().appendText(ChatColor.RED + content);
        plugin.getServer().getScheduler().runTaskLater(plugin,
                () -> {
                    if (!hologram.isDeleted()) hologram.delete();
                },
                20L);

        if (e.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
            if (e.getDamager() instanceof Arrow) {
                Arrow arrow = (Arrow) e.getDamager();
                if (arrow.getShooter() instanceof Player) {
                    setAntyLogout(((Player) arrow.getShooter()));
                    setAntyLogout((Player) e.getEntity());
                }
            }
        }
        if (e.getDamager() instanceof Player) {
            setAntyLogout((Player) e.getEntity());
            setAntyLogout((Player) e.getDamager());
        }
    }

    public void setAntyLogout(@NotNull Player p) {
        AntyLogoutData item = new AntyLogoutData(Bukkit.createBossBar(ColorUtil.color("&c⚔ Walka ⚔"), BarColor.RED, BarStyle.SOLID), antyLogoutDuration);
        if (!antyLogout.containsKey(p.getUniqueId())) {
            antyLogout.put(p.getUniqueId(), item);
            item.getBossBar().addPlayer(p);
            final TextComponent component = Component.text()
                    .content("Wkroczyłeś do walki!")
                    .color(NamedTextColor.RED)
                    .build();
            p.sendMessage(component);
        } else {
            AntyLogoutData itemA = antyLogout.get(p.getUniqueId());
            itemA.setTicks(antyLogoutDuration);
        }
    }

}
