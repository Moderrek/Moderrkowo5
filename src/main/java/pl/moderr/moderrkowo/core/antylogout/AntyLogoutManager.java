package pl.moderr.moderrkowo.core.antylogout;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.RandomUtils;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class AntyLogoutManager implements Listener {


    private final int ANTY_LOGOUT_DURATION = 10 * 20;

    private final HashMap<UUID, AntyLogoutItem> antyLogout = new HashMap<>();

    public AntyLogoutManager() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            for (UUID uuid : antyLogout.keySet()) {
                AntyLogoutItem nowy = antyLogout.get(uuid);
                if (nowy.getTicks() <= 0) {
                    try {
                        Player player = Bukkit.getPlayer(uuid);
                        if(player != null){
                            final TextComponent component = Component.text("Wyszedłeś z walki.")
                                            .color(NamedTextColor.GREEN);
                            player.sendMessage(component);
                            nowy.getBossBar().removePlayer(player);
                        }
                        antyLogout.remove(uuid);
                    } catch (Exception ignored) {}
                    return;
                }
                nowy.getBossBar().setProgress((double) nowy.getTicks() / ANTY_LOGOUT_DURATION);
                nowy.setTicks(nowy.getTicks() - 1);
                antyLogout.replace(uuid, antyLogout.get(uuid), nowy);
            }
        }, 0, 1);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        if (antyLogout.containsKey(e.getPlayer().getUniqueId())) {
            e.getPlayer().setHealth(0);
            final String playerName = e.getPlayer().getName();
            final TextComponent component = Component.text(MessageFormat.format(" {0} wylogował się podczas walki!", playerName))
                    .color(NamedTextColor.RED);
            Main.getInstance().getServer().broadcast(component);
            antyLogout.remove(e.getPlayer().getUniqueId());
        }
    }
    @EventHandler(priority = EventPriority.MONITOR)
    public void damage(EntityDamageByEntityEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (!(e.getEntity() instanceof Player)) {
            return;
        }
        Entity whoWasHit = e.getEntity();
        Random rd = new Random();

        ArmorStand as = whoWasHit.getWorld().spawn(whoWasHit.getLocation().add(rd.nextDouble(), 0, rd.nextDouble()), ArmorStand.class);
        as.setInvulnerable(true);
        as.setGravity(false);
        as.setVisible(false);
        as.setCustomNameVisible(true);
        Double d = e.getFinalDamage() / 2;
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        df.setMinimumFractionDigits(1);
        final TextComponent component = Component.text(MessageFormat.format("-{0}❤", df.format(d)))
                        .color(NamedTextColor.RED);
        as.customName(component);
        as.setSmall(true);
        as.setRemoveWhenFarAway(true);
        as.setAI(false);
        as.setCanPickupItems(false);
        as.setCollidable(false);
        as.setSilent(true);
        as.setMarker(true);


        Bukkit.getScheduler().scheduleSyncDelayedTask(Main.getInstance(), as::remove, 20L * RandomUtils.getRandomInt(1, 2));

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
    public void setAntyLogout(Player p) {
        AntyLogoutItem item = new AntyLogoutItem(Bukkit.createBossBar(ColorUtils.color("&c⚔ Walka ⚔"), BarColor.RED, BarStyle.SOLID), ANTY_LOGOUT_DURATION);
        if (!antyLogout.containsKey(p.getUniqueId())) {
            antyLogout.put(p.getUniqueId(), item);
            item.getBossBar().addPlayer(p);
            final TextComponent component = Component.text("Wkroczyłeś do walki!")
                            .color(NamedTextColor.RED);
            p.sendMessage(component);
        } else {
            AntyLogoutItem itemA = antyLogout.get(p.getUniqueId());
            itemA.setTicks(ANTY_LOGOUT_DURATION);
        }
    }
    public boolean inFight(UUID uuid) {
        return antyLogout.containsKey(uuid);
    }

}
