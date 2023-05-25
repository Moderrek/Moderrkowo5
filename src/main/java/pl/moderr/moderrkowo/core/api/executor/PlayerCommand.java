package pl.moderr.moderrkowo.core.api.executor;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.util.ComponentUtil;

public interface PlayerCommand extends CommandExecutor {

    @Override
    default boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ComponentUtil.coloredText("Musisz być graczem aby wykonać tą komendę!", NamedTextColor.RED));
            return false;
        }
        Player player = (Player) sender;
        return execute(player, command, label, args);
    }

    boolean execute(@NotNull Player player, @NotNull Command command, @NotNull String label, @NotNull String[] args);
}

