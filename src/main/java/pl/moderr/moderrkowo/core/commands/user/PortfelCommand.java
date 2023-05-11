package pl.moderr.moderrkowo.core.commands.user;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class PortfelCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.sendMessage(ColorUtils.color("&fPosiadasz &a" + ChatUtil.getMoney(UserManager.getUser(p.getUniqueId()).getMoney())));
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
        } else {
            sender.sendMessage(ColorUtils.color("&cNie jesteś graczem"));
        }
        return false;
    }
}
