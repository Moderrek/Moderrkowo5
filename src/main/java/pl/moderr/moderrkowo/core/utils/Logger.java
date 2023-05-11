package pl.moderr.moderrkowo.core.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;

import java.util.logging.Level;

public class Logger {
    public static void logHelpMessage(Player sender, String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                TextComponent tc = new TextComponent();
                tc.setText(ColorUtils.color(String.format("&8[&9Pomoc&8] &7%s&8: &e%s", sender.getName(), message)));
                tc.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/ahelpop " + sender.getName() + " "));
                tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ColorUtils.color(String.format("&eOdpowiedz &a%s", sender.getName())))));
                p.spigot().sendMessage(tc);
            }
        }
    }

    public static void logWorldManager(String worldName, String message) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.isOp()) {
                p.sendMessage(ColorUtils.color("&8[&9WM&8] &e" + worldName + " &8» &e" + message));
            }
        }
    }

    public static void logNpcMessage(String message) {
        final Component component = Component.text().content("[NPC]").color(TextColor.color(0x66cdaa)).appendSpace().append(LegacyComponentSerializer.legacyAmpersand().deserialize(message).color(NamedTextColor.WHITE)).build();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("moderrkowo.logs")) {
                p.sendMessage(component);
            }
        }
    }

    public static void logAdminChat(String message) {
        final Component component = Component.text().content("[ADMINLOG]").color(TextColor.color(0xff3442)).appendSpace().append(LegacyComponentSerializer.legacyAmpersand().deserialize(message).color(NamedTextColor.WHITE)).build();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("moderrkowo.logs")) {
                p.sendMessage(component);
            }
        }
    }

    public static void logAdminLog(String message) {
        logAdminChat(message);
    }

    public static void logPluginMessage(String message) {
        final Component component = Component.text().content("[PLUGIN]").color(TextColor.color(0xF29111)).appendSpace().append(LegacyComponentSerializer.legacyAmpersand().deserialize(message).color(NamedTextColor.WHITE)).build();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("moderrkowo.logs")) {
                p.sendMessage(component);
            }
        }
    }

    public static void logCaseMessage(String message) {
        final Component component = Component.text().content("[CASE]").color(TextColor.color(0xeef200)).appendSpace().append(LegacyComponentSerializer.legacyAmpersand().deserialize(message).color(NamedTextColor.WHITE)).build();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("moderrkowo.logs")) {
                p.sendMessage(component);
            }
        }
    }

    public static void logDatabaseMessage(String message) {
        final Component component = Component.text().content("[MySQL]").color(TextColor.color(0x00758F)).appendSpace().append(LegacyComponentSerializer.legacyAmpersand().deserialize(message).color(NamedTextColor.WHITE)).build();
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.hasPermission("moderrkowo.logs")) {
                p.sendMessage(component);
            }
        }
        ModerrkowoPlugin.getInstance().getLogger().log(Level.SEVERE, PlainTextComponentSerializer.plainText().serialize(component));
    }

    public static String getMessage(String[] args, int startFromArg, boolean removeFirstSpace) {
        StringBuilder out = new StringBuilder();
        for (int i = startFromArg; i != args.length; i++) {
            out.append(" ").append(args[i]);
        }
        if (removeFirstSpace) {
            return out.substring(1);
        }
        return out.toString();
    }
}
