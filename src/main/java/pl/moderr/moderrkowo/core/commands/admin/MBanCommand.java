package pl.moderr.moderrkowo.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

public class MBanCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 1) {
            Player p = Bukkit.getPlayer(args[0]);
            if (p != null) {
                sender.sendMessage(ColorUtils.color("&aPomyślnie zbanowano gracza"));
                p.banPlayerFull(ColorUtils.color("&e&lModerrkowo\n&7Zostałeś pernametnie zbanowany\n \n&c" + Logger.getMessage(args, 1, true) + "\n&8https://discord.gg/mgPAGYNu2s"));
                Logger.logAdminLog(ColorUtils.color("&6" + sender.getName() + " &7zbanował gracza &6" + p.getName() + " &7z powodem &6" + Logger.getMessage(args, 1, true)));

            } else {
                sender.sendMessage(ColorUtils.color("&cPodany gracz jest offline!"));
            }
        } else {
            sender.sendMessage(ColorUtils.color("&cPodaj nick i powód!"));
        }
        return false;
    }
}
