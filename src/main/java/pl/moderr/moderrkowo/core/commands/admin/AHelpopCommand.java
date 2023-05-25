package pl.moderr.moderrkowo.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.Logger;

public class AHelpopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.isOp()) {
                return false;
            }
            if (args.length > 0) {
                String playerArg = args[0];
                Player answerTo = Bukkit.getPlayer(playerArg);
                if (answerTo != null) {
                    if (args.length > 1) {
                        String answer = Logger.getMessage(args, 1, true);


                        answerTo.sendMessage(" ");
                        answerTo.sendMessage(ColorUtil.color(String.format("&8[&9Pomoc&8] &c&lADM &e%s&8: &e%s", p.getName(), answer)));
                        answerTo.sendMessage(" ");
                        answerTo.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            if (player.isOp()) {
                                player.sendMessage(ColorUtil.color(String.format("&8[&9Pomoc&8] &c&lADM &e%s&8: &e%s", p.getName(), answer)));
                            }
                        }
                        //p.sendTitle(new Title(ColorUtils.color("&c&lModerrkowo"), ColorUtils.color("&eOdpowiedziano.")));
                        p.sendMessage(ColorUtil.color("&aWysłano odpowiedź."));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                        return true;
                    } else {
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        p.sendMessage(ColorUtil.color("&cUżycie: &e/ahelpop <nick> &m<odpowiedź>"));
                        return false;
                    }
                } else {
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    p.sendMessage(ColorUtil.color("&cGracz jest offline!"));
                    return false;
                }
            } else {
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                p.sendMessage(ColorUtil.color("&cUżycie: &e/ahelpop <nick> <odpowiedź>"));
                return false;
            }
        } else {
            sender.sendMessage("Nie jesteś graczem!");
        }
        return false;
    }
}
