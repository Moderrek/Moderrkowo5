package pl.moderr.moderrkowo.core.economy;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class ZetonyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player p = (Player) sender;
        int count = UserManager.getUser(p.getUniqueId()).getSeasonOneCoins();
        sender.sendMessage(ColorUtils.color("&fPosiadasz &5" + count + " żetonów pierwszego sezonu &d&kx&5⛃&d&kx"));
        return false;
    }
}
