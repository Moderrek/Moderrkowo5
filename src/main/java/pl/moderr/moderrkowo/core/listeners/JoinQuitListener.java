package pl.moderr.moderrkowo.core.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.commands.admin.VanishCommand;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.HexResolver;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.util.UUID;

public class JoinQuitListener implements Listener {

    public static @NotNull String getJoinMessage(@NotNull Player p) {
        return ColorUtils.color("&e" + p.getName() + " &7dołączył");
    }

    public static @NotNull String getQuitMessage(@NotNull Player p) {
        return ColorUtils.color("&e" + p.getName() + " &7opuścił serwer");
    }

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.sendTitle(ColorUtils.color("&6⚔ ") + Main.getServerName() + ColorUtils.color(" &r&6⚔"), ColorUtils.color("&fWitaj ponownie"));
        p.setPlayerListName(ColorUtils.color("&c" + "Ładowanie.."));
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1);
            p.sendMessage(ChatUtil.centerText("⚔ Moderrkowo ⚔").replace("⚔ Moderrkowo ⚔", ColorUtils.color("&6⚔ ") + Main.getServerName() + ColorUtils.color(" &r&6⚔")));
            p.sendMessage(ColorUtils.color("  &8▪ &7Witaj ponownie, &6" + p.getName() + " &7na &6Moderrkowo.PL!"));
            p.sendMessage(" ");
            p.sendMessage(ColorUtils.color("  &8▪ &7Discord serwera &9/discord"));
            p.sendMessage(ColorUtils.color("  &8▪ &7Granie oznacza akceptację regulaminu &c/regulamin"));
            p.sendMessage(ColorUtils.color("  &8▪ &6Moderrkowo &7to gwarancja satysfakcji zabawy i bezpieczeństwa!"));
            p.sendMessage(" ");
            p.sendMessage(ColorUtils.color("  &8▪ &7Miłej gry życzy administracja &eModerrkowo.PL"));
            p.sendMessage(" ");
        }, 40L);
        // Update the highest value of players
        int maxPlayer = Main.getInstance().dataConfig.getInt("MaxPlayer");
        if (maxPlayer < Bukkit.getOnlinePlayers().size()) {
            Main.getInstance().dataConfig.set("MaxPlayer", Bukkit.getOnlinePlayers().size());
            Bukkit.broadcastMessage(ColorUtils.color("  &fRekord graczy został pobity!"));
        }
        // Load User
        UserManager.loadUser(p);
        // TAB
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateTab(player);
        }
        // Vanish
        for (UUID uuid : VanishCommand.hidden) {
            Player hidden = Bukkit.getPlayer(uuid);
            if (p.isOp()) {
                assert hidden != null;
                p.sendMessage(ColorUtils.color("  &a" + hidden.getName() + " &fjest ukryty ale ty masz permisje aby go widzieć"));
                continue;
            }
            assert hidden != null;
            p.hidePlayer(Main.getInstance(), hidden);
        }
        // Message
        if (p.isOp()) {
            e.setJoinMessage(JoinQuitListener.getJoinMessage(p));
        } else {
            Logger.logAdminLog(JoinQuitListener.getJoinMessage(e.getPlayer()));
            e.setJoinMessage(null);
        }
    }

    public void updateTab(Player e) {
        int administracja = 0;
        int gracze = 0;
        for (Player admin : Bukkit.getOnlinePlayers()) {
            if (VanishCommand.hidden.contains(admin.getUniqueId())) {
                continue;
            }
            gracze++;
            if (admin.hasPermission("moderr.admin")) {
                administracja++;
            }
        }
        String header
                = " \n "
                + HexResolver.parseHexString("&6⚔ <gradient:#FD4F1D:#FCE045>Moderrkowo") + " &r&6⚔"
                + " \n "
                + " \n&7Administracja online  &8» &6" + administracja
                + " \n&7Gracze online &8» &6" + gracze
                + " \n&7Rekord graczy &8» &6" + Main.getInstance().dataConfig.getInt("MaxPlayer")
                + " \n ";
        String footer
                = " \n&7Adres serwera: &amoderrkowo.pl"
                + " \n&7Discord: &a/discord"
                + " \n&7Strona: &awww.moderrkowo.pl"
                + " \n&7Sklep: &asklep.moderrkowo.pl"
                + " \n ";
        e.setPlayerListHeader(ColorUtils.color(header));
        e.setPlayerListFooter(ColorUtils.color(footer));

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        UserManager.unloadUser(e.getPlayer().getUniqueId());
        // Update Tab
        for (Player players : Bukkit.getOnlinePlayers()) {
            updateTab(players);
        }
        // Vanish
        if (VanishCommand.hidden.contains(e.getPlayer().getUniqueId())) {
            VanishCommand.hidden.remove(e.getPlayer().getUniqueId());
            Logger.logAdminLog("  &fGracz &a" + e.getPlayer().getName() + " &fwyszedł z serwera i został pokazany");
        }
        // Message
        if (e.getPlayer().isOp()) {
            e.setQuitMessage(JoinQuitListener.getQuitMessage(e.getPlayer()));
        } else {
            Logger.logAdminLog(JoinQuitListener.getQuitMessage(e.getPlayer()));
            e.setQuitMessage(null);
        }
    }

}
