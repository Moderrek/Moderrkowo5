package pl.moderr.moderrkowo.core.commands.admin;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.Logger;

import java.text.MessageFormat;

public class AdminChatCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (p.isOp()) {
                if (args.length > 0) {
                    String message = Logger.getMessage(args, 0, true);
                    if (message.equalsIgnoreCase("case")) {
//                        Main.shulkerDropBox.getShulkers().forEach((shulkerDrop, integer) -> p.getInventory().addItem(shulkerDrop.generateShulker()));
                    }
                    Logger.logAdminChat(MessageFormat.format("&7{0}&8: &f{1}", p.getName(), message));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                    return true;
                } else {
                    p.sendMessage(ColorUtil.color("&cNapisz wiadomość!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
            } else {
                return false;
            }
        } else {
            sender.sendMessage("Nie jesteś graczem!");
            return false;
        }
    }
}
