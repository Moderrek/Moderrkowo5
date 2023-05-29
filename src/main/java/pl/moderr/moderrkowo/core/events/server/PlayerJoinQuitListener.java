package pl.moderr.moderrkowo.core.events.server;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.util.ChatUtil;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.HexResolver;
import pl.moderr.moderrkowo.core.api.util.Logger;
import pl.moderr.moderrkowo.core.commands.admin.VanishCommand;
import pl.moderr.moderrkowo.core.services.mysql.UserManager;

import java.util.UUID;

public class PlayerJoinQuitListener implements Listener {

    public static @NotNull String getJoinMessage(@NotNull Player p) {
        return ColorUtil.color("&e" + p.getName() + " &7dołączył");
    }

    public static @NotNull String getQuitMessage(@NotNull Player p) {
        return ColorUtil.color("&e" + p.getName() + " &7opuścił serwer");
    }

    @EventHandler
    public void onPlayerJoin(@NotNull PlayerJoinEvent e) {
        Player p = e.getPlayer();
        p.sendTitle(ColorUtil.color("&6⚔ ") + ModerrkowoPlugin.getServerName() + ColorUtil.color(" &r&6⚔"), ColorUtil.color("&fWitaj ponownie"));
        Bukkit.getScheduler().runTaskLater(ModerrkowoPlugin.getInstance(), () -> {
            p.playSound(p.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1.0F, 1.0F);
            p.sendMessage(ChatUtil.centerMotdLine("⚔ Moderrkowo ⚔").replace("⚔ Moderrkowo ⚔", ColorUtil.color("&6⚔ ") + ModerrkowoPlugin.getServerName() + ColorUtil.color(" &r&6⚔")));
            p.sendMessage(ColorUtil.color("  &8▪ &7Witaj ponownie, &6" + p.getName() + " &7na &6Moderrkowo.PL!"));
            p.sendMessage(" ");
            p.sendMessage(ColorUtil.color("  &8▪ &7Discord serwera &9/discord"));
            p.sendMessage(ColorUtil.color("  &8▪ &7Granie oznacza akceptację regulaminu &c/regulamin"));
            p.sendMessage(ColorUtil.color("  &8▪ &6Moderrkowo &7to gwarancja satysfakcji zabawy i bezpieczeństwa!"));
            p.sendMessage(" ");
            p.sendMessage(ColorUtil.color("  &8▪ &7Miłej gry życzy administracja &eModerrkowo.PL"));
            p.sendMessage(" ");
        }, 40L);
        // Update the highest value of players
        int maxPlayer = ModerrkowoPlugin.getInstance().dataConfig.getInt("MaxPlayer");
        if (maxPlayer < Bukkit.getOnlinePlayers().size()) {
            ModerrkowoPlugin.getInstance().dataConfig.set("MaxPlayer", Bukkit.getOnlinePlayers().size());
            Bukkit.broadcastMessage(ColorUtil.color("  &fRekord graczy został pobity!"));
        }
        // Load User
        p.setPlayerListName(ColorUtil.color("&c" + "Ładowanie.."));
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
                p.sendMessage(ColorUtil.color("  &a" + hidden.getName() + " &fjest ukryty ale ty masz permisje aby go widzieć"));
                continue;
            }
            assert hidden != null;
            p.hidePlayer(ModerrkowoPlugin.getInstance(), hidden);
        }
        // Message
        if (p.isOp()) {
            e.setJoinMessage(PlayerJoinQuitListener.getJoinMessage(p));
        } else {
            Logger.logAdminLog(PlayerJoinQuitListener.getJoinMessage(e.getPlayer()));
            e.setJoinMessage(null);
        }
    }

    public void updateTab(Player player) {
        int administracja = 0;
        int gracze = 0;
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (VanishCommand.hidden.contains(onlinePlayer.getUniqueId())) continue;
            gracze++;
            if (onlinePlayer.hasPermission("moderr.admin")) administracja++;
        }
        String header
                = " \n "
                + HexResolver.parseHexString("&6⚔ <gradient:#FD4F1D:#FCE045>Moderrkowo") + " &r&6⚔"
                + " \n "
                + " \n&7Administracja online  &8» &6" + administracja
                + " \n&7Gracze online &8» &6" + gracze
                + " \n&7Rekord graczy &8» &6" + ModerrkowoPlugin.getInstance().dataConfig.getInt("MaxPlayer")
                + " \n ";
        String footer
                = " \n&7Adres serwera: &amoderrkowo.pl"
                + " \n&7Discord: &a/discord"
                + " \n&7Strona: &awww.moderrkowo.pl"
                + " \n&7Sklep: &asklep.moderrkowo.pl"
                + " \n ";
        player.setPlayerListHeader(ColorUtil.color(header));
        player.setPlayerListFooter(ColorUtil.color(footer));
    }

    @EventHandler
    public void onPlayerQuit(@NotNull PlayerQuitEvent event) {
        UserManager.unloadUser(event.getPlayer().getUniqueId());
        // Update Tab
        Bukkit.getScheduler().runTaskLater(ModerrkowoPlugin.getInstance(), () -> Bukkit.getOnlinePlayers().forEach(this::updateTab), 1L);
        // Vanish
        if (VanishCommand.hidden.contains(event.getPlayer().getUniqueId())) {
            VanishCommand.hidden.remove(event.getPlayer().getUniqueId());
            Logger.logAdminLog("  &fGracz &a" + event.getPlayer().getName() + " &fwyszedł z serwera i został pokazany");
        }
        // Message
        if (event.getPlayer().isOp()) {
            event.setQuitMessage(PlayerJoinQuitListener.getQuitMessage(event.getPlayer()));
        } else {
            Logger.logAdminLog(PlayerJoinQuitListener.getQuitMessage(event.getPlayer()));
            event.setQuitMessage(null);
        }
    }

}
