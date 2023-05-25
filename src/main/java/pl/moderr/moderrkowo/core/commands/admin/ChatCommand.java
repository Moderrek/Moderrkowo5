package pl.moderr.moderrkowo.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.api.util.ChatUtil;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class ChatCommand implements CommandExecutor, TabCompleter {

    public static boolean canChat = true;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("clear")) {
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            ChatUtil.clearChat(players);
                            players.sendMessage(ColorUtil.color("&7Czat został wyczyszczony"));
                        }
                        Logger.logPluginMessage(ColorUtil.color("&6" + sender.getName() + " &7wyczyścił czat"));
                        return false;
                    }
                    if (args[0].equalsIgnoreCase("on")) {
                        canChat = true;
                        Bukkit.broadcastMessage(ColorUtil.color("&cChat &6> &aZostał włączony"));
                        Logger.logPluginMessage(ColorUtil.color("&6" + p.getName() + " &7włączył czat"));
                        return false;
                    }
                    if (args[0].equalsIgnoreCase("off")) {
                        canChat = false;
                        Bukkit.broadcastMessage(ColorUtil.color("&cChat &6> &cZostał wyłączony"));
                        Logger.logPluginMessage(ColorUtil.color("&6" + p.getName() + " &7wyłączył czat"));
                        return false;
                    }
                }
                p.sendMessage(ColorUtil.color("&cUżyj: /chat <clear/on/off>"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
        }
        return false;
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> list = new ArrayList<>();
            list.add("clear");
            list.add("on");
            list.add("off");
            return list;
        }
        return null;
    }
}
