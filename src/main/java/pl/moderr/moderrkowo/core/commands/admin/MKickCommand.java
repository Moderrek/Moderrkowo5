package pl.moderr.moderrkowo.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.Logger;

public class MKickCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p != null) {
                sender.sendMessage(ColorUtil.color("&aPomyślnie wyrzucono gracza"));
                p.kickPlayer(ColorUtil.color("&e&lModerrkowo\n&7Zostałeś wyrzucony z serwera\n \n&c" + Logger.getMessage(args, 1, true)));
                Logger.logAdminLog(ColorUtil.color("&6" + sender.getName() + " &7wyrzucił gracza &6" + p.getName() + " &7z powodem &6" + Logger.getMessage(args, 1, true)));
            } else {
                sender.sendMessage(ColorUtil.color("&cPodany gracz jest offline!"));
            }
        } else {
            sender.sendMessage(ColorUtil.color("&cPodaj nick i powód!"));
        }
        return false;
    }
}
