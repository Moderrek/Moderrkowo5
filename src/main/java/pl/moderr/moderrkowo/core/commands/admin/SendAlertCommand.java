package pl.moderr.moderrkowo.core.commands.admin;

import com.destroystokyo.paper.Title;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class SendAlertCommand implements CommandExecutor, TabExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()) {
                if (args.length > 0) {
                    if (args[0].equalsIgnoreCase("alert")) {
                        if (args.length > 1) {
                            String message = Logger.getMessage(args, 1, true);
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.sendMessage(" ");
                                all.sendMessage(ColorUtils.color("&8[&cALERT&8] &e" + message));
                                all.sendMessage(" ");
                                all.playSound(all.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                            }

                            p.sendMessage(ColorUtils.color("&aWysłano!"));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                            return true;
                        } else {
                            p.sendMessage(ColorUtils.color("&cZa mało argumentów!"));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            return false;
                        }
                    }
                    if (args[0].equalsIgnoreCase("wiadomosc")) {
                        if (args.length > 1) {
                            String message = Logger.getMessage(args, 1, true);
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.sendMessage(" ");
                                all.sendMessage(ColorUtils.color("&7[&6Wiadomość&7] &e" + message));
                                all.sendMessage(" ");
                                all.playSound(all.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                            }

                            p.sendMessage(ColorUtils.color("&aWysłano!"));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                            return true;
                        } else {
                            p.sendMessage(ColorUtils.color("&cZa mało argumentów!"));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            return false;
                        }
                    }
                    if (args[0].equalsIgnoreCase("title")) {
                        if (args.length > 1) {
                            String message = Logger.getMessage(args, 1, true);
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.sendTitle(new Title("", ColorUtils.color(message)));
                            }

                            p.sendMessage(ColorUtils.color("&aWysłano!"));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                            return true;
                        } else {
                            p.sendMessage(ColorUtils.color("&cZa mało argumentów!"));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            return false;
                        }
                    }
                    if (args[0].equalsIgnoreCase("pilne")) {
                        if (args.length > 1) {
                            String message = Logger.getMessage(args, 1, true);
                            for (Player all : Bukkit.getOnlinePlayers()) {
                                all.sendTitle(new Title(ColorUtils.color("&c&lPILNE!"), ColorUtils.color("&ePatrz na chat")));
                                all.sendMessage(" ");
                                all.sendMessage(ColorUtils.color("&8[&c&lPILNE&8] &e" + message));
                                all.sendMessage(" ");
                                all.playSound(all.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, 1, 1);
                                all.spawnParticle(Particle.EXPLOSION_LARGE, all.getLocation(), 20, 5, 5, 5);
                            }

                            p.sendMessage(ColorUtils.color("&aWysłano!"));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                            return true;
                        } else {
                            p.sendMessage(ColorUtils.color("&cZa mało argumentów!"));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            return false;
                        }
                    }
                } else {
                    p.sendMessage(ColorUtils.color("&cZa mało argumentów!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
            }
        } else {
            sender.sendMessage("Nie jesteś graczem!");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        if (args.length == 1) {
            ArrayList<String> tabs = new ArrayList<>();
            tabs.add("alert");
            tabs.add("wiadomosc");
            tabs.add("title");
            tabs.add("pilne");
            return tabs;
        }
        return null;
    }
}
