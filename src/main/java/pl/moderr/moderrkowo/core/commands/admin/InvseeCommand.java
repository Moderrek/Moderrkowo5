package pl.moderr.moderrkowo.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

public class InvseeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!p.isOp()) {
                return false;
            }
            if (args.length == 0) {
                p.sendMessage(ColorUtils.color("&cUżycie: /ainvsee <nick>"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            } else {
                Player p2 = Bukkit.getPlayer(args[0]);
                if (p2 != null) {
                    //
                    p.openInventory(p2.getInventory());
                    //
                    p.sendMessage(ColorUtils.color("&eOtworzono ekwipunek gracza &6" + p2.getName()));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                    Logger.logAdminLog(p.getName() + " otworzył ekwipunek gracza " + p2.getName());
                } else {
                    p.sendMessage(ColorUtils.color("&cGracz jest offline!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    Logger.logAdminLog(p.getName() + " chciał otworzyć ekwipunek gracza offline " + args[0]);
                    return false;
                }
            }
        }
        return false;
    }

}

