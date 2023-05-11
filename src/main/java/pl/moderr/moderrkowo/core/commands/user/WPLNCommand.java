package pl.moderr.moderrkowo.core.commands.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.sql.SQLException;

public class WPLNCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            try {
                p.sendMessage(ColorUtils.color("&7Posiadasz: &6" + ChatUtil.getNumber(ModerrkowoPlugin.getMySQL().getQuery().getWPLN(p.getName())) + " wPLN"));
            } catch (SQLException exception) {
                p.sendMessage(ColorUtils.color("&cNie udało się pobrać wPLN"));
                exception.printStackTrace();
            }
        }
        return false;
    }
}
