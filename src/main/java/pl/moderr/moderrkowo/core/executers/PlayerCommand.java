package pl.moderr.moderrkowo.core.executers;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlayerCommand extends CommandExecutor {

    @Override
    default boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            Player player = (Player) sender;
            return commandUse(player, command, label, args);
        }
        sender.sendMessage("Musisz być graczem aby wykonać tą komendę.");
        return false;
    }

    boolean commandUse(@NotNull Player player, @NotNull Command command, @NotNull String label, @NotNull String[] args);
}

