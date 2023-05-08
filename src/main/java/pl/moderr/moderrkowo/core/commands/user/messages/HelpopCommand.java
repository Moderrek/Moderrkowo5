package pl.moderr.moderrkowo.core.commands.user.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.ServerOperator;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

public class HelpopCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length > 0) {
                Logger.logHelpMessage(player, Logger.getMessage(args, 0, true));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                player.sendMessage(ColorUtils.color("&8[!] &aPomyślnie wysłano!"));
                player.sendMessage(ColorUtils.color("&8[&9Pomoc&8] &7" + player.getName() + "&8: &e" + Logger.getMessage(args, 0, true)));
                final TextComponent mainTitle = Component.text("Moderrkowo")
                                .color(NamedTextColor.GOLD)
                                .decoration(TextDecoration.BOLD, true);
                final TextComponent subtitle = Component.text("Wysłano wiadomość.")
                                .color(NamedTextColor.GREEN);
                final Title title = Title.title(mainTitle, subtitle);
                player.showTitle(title);
                int administratorOnlineCount = (int) Bukkit.getOnlinePlayers().stream().filter(ServerOperator::isOp).count();
                Main.getInstance().discordManager.sendHelpop(player, Logger.getMessage(args, 0, true), administratorOnlineCount != 0);
                return true;
            } else {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                player.sendMessage(ColorUtils.color("&cUżycie: &e/helpop <wiadomość>"));
                return false;
            }
        } else {
            sender.sendMessage("Nie jesteś graczem!");
            return false;
        }
    }
}
