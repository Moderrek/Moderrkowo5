package pl.moderr.moderrkowo.core.executers;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.mysql.User;
import pl.moderr.moderrkowo.core.mysql.UserManager;

import java.util.UUID;

public interface UserCommand extends CommandExecutor {

    @Override
    default boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player){
            final Player player = (Player) sender;
            final UUID uuid = player.getUniqueId();
            if(!UserManager.isUserLoaded(uuid)){
                player.sendMessage(Component.text("Nie udało się wykonać komendy.").color(NamedTextColor.RED));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
                return false;
            }
            final User user = UserManager.getUser(uuid);
            final String name = player.getName();
            return execute(user, player, uuid, name, command, label, args);
        }
        sender.sendMessage("Musisz być zarejestrowanym użytkownikiem aby wykonać tą komendę.");
        return false;
    }

    boolean execute(@NotNull User user, @NotNull Player player, @NotNull UUID uuid, @NotNull String name, @NotNull Command command, @NotNull String commandName, @NotNull String[] args);

}
