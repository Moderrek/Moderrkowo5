package pl.moderr.moderrkowo.core.commands.admin;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class PlayerIDCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            // if player use this command
            if (sender.hasPermission("moderrkowo.checkid")) {
                if (args.length >= 1) {
                    Player target = Bukkit.getPlayer(args[0]);
                    if (target == null) {
                        OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
                        if (!op.hasPlayedBefore()) {
                            p.sendMessage(ColorUtils.color("&cGracz " + op.getName() + " nigdy nie grał"));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            return false;
                        } else {
                            TextComponent tc = new TextComponent("");
                            tc.setColor(ChatColor.RED);
                            tc.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, op.getUniqueId().toString()));
                            tc.setText(op.getUniqueId().toString());
                            tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(op.getUniqueId().toString())));
                            p.sendMessage(ColorUtils.color("&aUUID gracza offline &2" + op.getName()));
                            p.spigot().sendMessage(tc);
                            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                            return true;
                        }
                    } else {
                        TextComponent tc = new TextComponent("");
                        tc.setColor(ChatColor.RED);
                        tc.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, target.getUniqueId().toString()));
                        tc.setText(target.getUniqueId().toString());
                        tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(target.getUniqueId().toString())));
                        p.sendMessage(ColorUtils.color("&aUUID gracza &2" + target.getName()));
                        p.spigot().sendMessage(tc);
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        return true;
                    }
                } else {
                    sender.sendMessage(ColorUtils.color("&cPodaj nazwę gracza!"));
                    sender.sendMessage(ColorUtils.color("&c/playerid <nick>"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
            } else {
                p.sendMessage(ColorUtils.color("&cNie posiadasz permisji!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
        } else {
            // if console
            if (args.length >= 1) {
                Player p = Bukkit.getPlayer(args[0]);
                if (p == null) {
                    OfflinePlayer op = Bukkit.getOfflinePlayer(args[0]);
                    if (!op.hasPlayedBefore()) {
                        sender.sendMessage(ColorUtils.color("&cGracz nigdy nie grał na serwerze!"));
                        return false;
                    } else {
                        sender.sendMessage(ColorUtils.color("&aID gracza &2" + op.getName() + " &8>> &a" + op.getUniqueId()));
                        return true;
                    }
                } else {
                    sender.sendMessage(ColorUtils.color("&aID gracza &2" + p.getName() + " &8>> &a" + p.getUniqueId()));
                }
                return false;
            }
            sender.sendMessage(ColorUtils.color("&cPodaj nazwę gracza!"));
            sender.sendMessage(ColorUtils.color("&c/playerid <nick>"));
            return false;
        }
    }
}
