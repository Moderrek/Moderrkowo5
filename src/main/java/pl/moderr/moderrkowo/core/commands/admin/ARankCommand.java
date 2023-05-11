package pl.moderr.moderrkowo.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.ranks.Rank;
import pl.moderr.moderrkowo.core.ranks.RankManager;
import pl.moderr.moderrkowo.core.ranks.StuffRank;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;

public class ARankCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        CommandSender p = sender;
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("Set")) {
                if (args.length >= 2) {
                    if (args[1].equalsIgnoreCase("StuffRank")) {
                        Player p2 = Bukkit.getPlayer(args[2]);
                        if (p2 == null) {
                            p.sendMessage(ColorUtils.color("&cPodany gracz jest offline!"));
//                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            return false;
                        }
                        if (args.length >= 4) {
                            StuffRank stuffRank = StuffRank.valueOf(args[3]);
                            User u = UserManager.getUser(p2.getUniqueId());
                            StuffRank beforeSet = u.getStuffRank();
                            u.setStuffRank(stuffRank);

                            p.sendMessage(ColorUtils.color("&7Udało się ustawić range"));
                            p.sendMessage(RankManager.getChat(u.getRank(), beforeSet) + " -> " + RankManager.getChat(u.getRank(), u.getStuffRank()));
//                            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

                            return true;
                        }
                        p.sendMessage(ColorUtils.color("&cPodaj range, która chcesz ustawić!"));
//                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return false;
                    }
                    if (args[1].equalsIgnoreCase("Rank")) {
                        Player p2 = Bukkit.getPlayer(args[2]);
                        if (p2 == null) {
                            p.sendMessage(ColorUtils.color("&cPodany gracz jest offline!"));
//                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            return false;
                        }
                        if (args.length >= 4) {
                            Rank rank = Rank.valueOf(args[3]);
                            User u = UserManager.getUser(p2.getUniqueId());
                            Rank beforeSet = u.getRank();
                            u.setRank(rank);
                            p.sendMessage(ColorUtils.color("&7Udało się ustawić range"));
                            p.sendMessage(RankManager.getChat(beforeSet, u.getStuffRank()) + " -> " + RankManager.getChat(u.getRank(), u.getStuffRank()));
//                            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

                            return true;
                        }
                        p.sendMessage(ColorUtils.color("&cPodaj range, która chcesz ustawić!"));
//                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return false;
                    }
                }
                return false;
            }
            if (args[0].equalsIgnoreCase("Get")) {
                if (args.length >= 2) {
                    if (args[1].equalsIgnoreCase("StuffRank")) {
                        Player p2 = Bukkit.getPlayer(args[2]);
                        if (p2 == null) {
                            p.sendMessage(ColorUtils.color("&cPodany gracz jest offline!"));
//                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            return false;
                        }
                        User u = UserManager.getUser(p2.getUniqueId());
                        p.sendMessage(ColorUtils.color("&7Udało się pobrać range"));
                        p.sendMessage(RankManager.getChat(u.getRank(), u.getStuffRank()) + u.getName());
//                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("Rank")) {
                        Player p2 = Bukkit.getPlayer(args[2]);
                        if (p2 == null) {
                            p.sendMessage(ColorUtils.color("&cPodany gracz jest offline!"));
//                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            return false;
                        }
                        User u = UserManager.getUser(p2.getUniqueId());
                        p.sendMessage(ColorUtils.color("&7Udało się pobrać range"));
                        p.sendMessage(RankManager.getChat(u.getRank(), u.getStuffRank()) + u.getName());
//                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        return true;
                    }
                }
                return false;
            }
        }
        p.sendMessage(ColorUtils.color("&cMusisz podać jakiś argument!"));
//        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("Set");
            arrayList.add("Get");
            return arrayList;
        }
        if (args.length == 2) {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("StuffRank");
            arrayList.add("Rank");
            return arrayList;
        }
        if (args.length == 4 && args[0].equalsIgnoreCase("Set") && args[1].equalsIgnoreCase("StuffRank")) {
            ArrayList<String> arrayList = new ArrayList<>();
            for (StuffRank stuffRank : StuffRank.values()) {
                arrayList.add(stuffRank.toString());
            }
            return arrayList;
        }
        if (args.length == 4 && args[0].equalsIgnoreCase("Set") && args[1].equalsIgnoreCase("Rank")) {
            ArrayList<String> arrayList = new ArrayList<>();
            for (Rank rank : Rank.values()) {
                arrayList.add(rank.toString());
            }
            return arrayList;
        }
        return null;
    }
}
