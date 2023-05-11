package pl.moderr.moderrkowo.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class GameModeCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                GameMode gameMode = null;
                if (args[0].equalsIgnoreCase("adventure")) {
                    gameMode = GameMode.ADVENTURE;
                }
                if (args[0].equalsIgnoreCase("creative")) {
                    gameMode = GameMode.CREATIVE;
                }
                if (args[0].equalsIgnoreCase("spectator")) {
                    gameMode = GameMode.SPECTATOR;
                }
                if (args[0].equalsIgnoreCase("survival")) {
                    gameMode = GameMode.SURVIVAL;
                }
                if (gameMode != null) {
                    if (args.length > 1) {
                        Player p2 = Bukkit.getPlayer(args[1]);
                        if (p2 != null) {
                            p2.setGameMode(gameMode);
                            p.sendMessage(ColorUtils.color("&aZmieniono tryb gry gracza &2" + p2.getName() + " &ana &2" + gameMode));
                            Logger.logAdminLog(ColorUtils.color("&6" + p.getName() + " &7zmienił tryb &6" + p2.getName() + " &7gry na &6" + gameMode));
                        } else {
                            p.sendMessage(ColorUtils.color("&cPodany gracz jest offline!"));
                        }
                    } else {
                        p.setGameMode(gameMode);
                        p.sendMessage(ColorUtils.color("&aZmieniono tryb gry na &2" + gameMode));
                        Logger.logAdminLog(ColorUtils.color("&6" + p.getName() + " &7zmienił tryb gry na &6" + gameMode));
                    }
                    return false;
                } else {
                    p.sendMessage(ColorUtils.color("&cPodano niepoprawny tryb gry!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
            }
            p.sendMessage(ColorUtils.color("&cUżyj: /gamemode <tryb gry>"));
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return false;
        }
        sender.sendMessage(ColorUtils.color("&cNie jesteś graczem!"));
        return false;
    }


    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            ArrayList<String> list = new ArrayList<>();
            list.add("adventure");
            list.add("creative");
            list.add("spectator");
            list.add("survival");
            return list;
        }
        return null;
    }
}
