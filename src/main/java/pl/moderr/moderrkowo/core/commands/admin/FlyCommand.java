package pl.moderr.moderrkowo.core.commands.admin;

import com.destroystokyo.paper.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                Player p2 = Bukkit.getPlayer(args[0]);
                if (p2 != null) {
                    p2.setAllowFlight(!p2.getAllowFlight());
                    if (p2.getAllowFlight()) {
                        p.sendTitle(ColorUtils.color("&2" + p2.getName()), ColorUtils.color("&aMoże już latać"));
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        Logger.logAdminLog(ColorUtils.color("&6" + p.getName() + " &7włączył graczu &6" + p2.getName() + " &7latanie"));
                    } else {
                        p.sendTitle(new Title(ColorUtils.color("&4" + p2.getName()), ColorUtils.color("&cNie może już latać")));
                        Logger.logAdminLog(ColorUtils.color("&6" + p.getName() + " &7wyłączył graczu &6" + p2.getName() + " &7latanie"));
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    }
                } else {
                    p.sendMessage(ColorUtils.color("&cPodany gracz jest offline!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                }
            } else {
                p.setAllowFlight(!p.getAllowFlight());
                if (p.getAllowFlight()) {
                    p.sendTitle(new Title("", ColorUtils.color("&aMożesz już latać")));
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    Logger.logAdminLog(ColorUtils.color("&6" + p.getName() + " &7włączył latanie"));
                } else {
                    p.sendTitle(new Title("", ColorUtils.color("&cNie możesz już latać")));
                    Logger.logAdminLog(ColorUtils.color("&6" + p.getName() + " &7wyłączył latanie"));
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                }
            }
            return false;
        }
        return false;
    }
}
