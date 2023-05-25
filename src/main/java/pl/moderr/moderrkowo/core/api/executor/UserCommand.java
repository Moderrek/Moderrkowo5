package pl.moderr.moderrkowo.core.api.executor;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.executor.exception.UserCommandException;
import pl.moderr.moderrkowo.core.api.util.ComponentUtil;
import pl.moderr.moderrkowo.core.services.mysql.UserManager;
import pl.moderr.moderrkowo.core.user.User;

import java.util.Objects;
import java.util.UUID;

public interface UserCommand extends CommandExecutor {

    @Override
    default boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Musisz być zarejestrowanym użytkownikiem aby wykonać tą komendę.");
            return false;
        }
        final Player player = (Player) sender;
        final UUID uuid = player.getUniqueId();
        if (!UserManager.isUserLoaded(uuid)) {
            player.sendMessage(ComponentUtil.coloredText("Nie udało się wykonać komendy. Spróbuj ponownie dołączyć na serwer.", NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return true;
        }
        try {
            final String name = player.getName();
            execute(Objects.requireNonNull(User.Get(uuid)), player, uuid, name, command, label, args);
        } catch (UserCommandException commandException) {
            player.sendMessage(ComponentUtil.coloredText(commandException.getDisplayMessage(), commandException.getMessageColor()));
            if (commandException.getSound() != null)
                player.playSound(player.getLocation(), commandException.getSound(), 1, 1);
        } catch (Exception e) {
            player.sendMessage(ComponentUtil.coloredText("Wystąpił niezidentyfikowany błąd podczas wykonywania komendy.", NamedTextColor.YELLOW));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 0.5F, 1.0F);
        }
        return true;
    }

    boolean execute(@NotNull User user, @NotNull Player player, @NotNull UUID uuid, @NotNull String name, @NotNull Command command, @NotNull String commandName, @NotNull String[] args);

}
