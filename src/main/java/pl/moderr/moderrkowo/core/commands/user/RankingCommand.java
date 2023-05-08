package pl.moderr.moderrkowo.core.commands.user;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.*;

public class RankingCommand implements CommandExecutor, TabCompleter {
    public final ArrayList<String> list = new ArrayList<String>() {
        {
            add("diamenty");
            add("czasgry");
            add("smierci");
            add("redstone");
        }
    };

    public Map<UUID, Integer> sortByValue(boolean order, Map<UUID, Integer> map) {
        List<Map.Entry<UUID, Integer>> list = new LinkedList<>(map.entrySet());
        list.sort((o1, o2) -> {
            if (order) {
                return o1.getValue().compareTo(o2.getValue());
            } else {
                return o2.getValue().compareTo(o1.getValue());
            }
        });
        return map;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return false;
        }
        Player p = ((Player) sender);
        if (args.length == 1) {
            Map<UUID, Integer> unsorted = new HashMap<>();
            p.sendMessage(" \n");
            if (args[0].equalsIgnoreCase("diamenty")) {
                for (Player allPlayers : Bukkit.getOnlinePlayers()) {
                    unsorted.put(allPlayers.getUniqueId(), allPlayers.getStatistic(Statistic.MINE_BLOCK, Material.DIAMOND_ORE));
                }
                Map<UUID, Integer> sortedRanking = sortByValue(true, unsorted);
                int i = 1;
                p.sendMessage(ColorUtils.color("&8[!] &6Ranking wykopanych diamentów"));
                for (UUID id : sortedRanking.keySet()) {
                    p.sendMessage(ColorUtils.color("&8[!] &7" + i + ". &7Gracz &6" + Bukkit.getOfflinePlayer(id).getName() + " &7wykopał " + sortedRanking.get(id) + " diamentów!"));
                    i++;
                }
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                return true;
            }
            if (args[0].equalsIgnoreCase("redstone")) {
                for (Player allPlayers : Bukkit.getOnlinePlayers()) {
                    unsorted.put(allPlayers.getUniqueId(), allPlayers.getStatistic(Statistic.MINE_BLOCK, Material.REDSTONE_ORE));
                }
                Map<UUID, Integer> sortedRanking = sortByValue(true, unsorted);
                int i = 1;
                p.sendMessage(ColorUtils.color("&8[!] &6Ranking wykopanego redstone'a"));
                for (UUID id : sortedRanking.keySet()) {
                    p.sendMessage(ColorUtils.color("&8[!] &7" + i + ". &7Gracz &6" + Bukkit.getOfflinePlayer(id).getName() + " &7wykopał " + sortedRanking.get(id) + " redstone!"));
                    i++;
                }
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                return true;
            }
            if (args[0].equalsIgnoreCase("czasgry")) {
                for (Player allPlayers : Bukkit.getOnlinePlayers()) {
                    unsorted.put(allPlayers.getUniqueId(), allPlayers.getStatistic(Statistic.PLAY_ONE_MINUTE));
                }
                Map<UUID, Integer> sortedRanking = sortByValue(true, unsorted);
                int i = 1;
                p.sendMessage(ColorUtils.color("&8[!] &6Ranking czasu gry"));
                for (UUID id : sortedRanking.keySet()) {
                    p.sendMessage(ColorUtils.color("&8[!] &7" + i + ". &7Gracz &6" + Bukkit.getOfflinePlayer(id).getName() + " &7gra " + ChatUtil.getTicksToTime(sortedRanking.get(id)) + " !"));
                    i++;
                }
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                return true;
            }
            if (args[0].equalsIgnoreCase("smierci")) {
                for (Player allPlayers : Bukkit.getOnlinePlayers()) {
                    unsorted.put(allPlayers.getUniqueId(), allPlayers.getStatistic(Statistic.DEATHS));
                }
                Map<UUID, Integer> sortedRanking = sortByValue(true, unsorted);
                int i = 1;
                p.sendMessage(ColorUtils.color("&8[!] &6Ranking śmierci"));
                for (UUID id : sortedRanking.keySet()) {
                    p.sendMessage(ColorUtils.color("&8[!] &7" + i + ". &7Gracz &6" + Bukkit.getOfflinePlayer(id).getName() + " &7umarł " + sortedRanking.get(id) + " razy!"));
                    i++;
                }
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                return true;
            }
        }
        p.sendMessage(ColorUtils.color("&cNie wybrano żadnego rankingu."));
        p.sendMessage(ColorUtils.color("&7Użycie &6/ranking <diamenty/czasgry/smierci/redstone>"));
        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        return false;
    }

    public void sendHelp(Player p) {
        p.sendMessage(ColorUtils.color("&8[!]&7----------------------------------&8[!]"));
        for (String s : list) {
            p.sendMessage(ColorUtils.color("&a/ranking &7" + s));
        }
        p.sendMessage(ColorUtils.color("&8[!]&7----------------------------------&8[!]"));
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            return list;
        }
        return null;
    }
}
