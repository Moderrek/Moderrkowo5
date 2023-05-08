package pl.moderr.moderrkowo.core.commands.user.messages;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class ReplyCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (!MessageCommand.lastMessageSender.containsKey(p)) {
                p.sendMessage(ColorUtils.color("&cNie możesz nikomu odpowiedzieć!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
            Player p2 = MessageCommand.lastMessageSender.get(p);
            if (args.length == 0) {
                p.sendMessage(ColorUtils.color("&cUżycie: /r <treść>"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
            if (p2 != null) {
                StringBuilder message = new StringBuilder();
                for (int i = 0; i != args.length; i++) {
                    message.append(" ").append(args[i]);
                }
                // SEND MESSAGE
                p.sendMessage(ColorUtils.color("&6Ja -> " + p2.getDisplayName() + "&8:&e" + message));
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);

                p2.sendMessage(ColorUtils.color("&6" + p.getDisplayName() + " &6-> Ja&8:&e" + message));
                p2.playSound(p2.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            } else {
                p.sendMessage(ColorUtils.color("&cGracz do którego chcesz odpowiedzieć jest offline!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
        } else {
            sender.sendMessage("Nie jesteś graczem!");
        }
        return false;
    }
}
