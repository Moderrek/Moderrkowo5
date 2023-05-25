package pl.moderr.moderrkowo.core.commands.user;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.executor.UserCommand;
import pl.moderr.moderrkowo.core.api.util.ChatUtil;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.user.User;

import java.text.MessageFormat;
import java.util.UUID;

public class PortfelCommand implements UserCommand {
    @Override
    public boolean execute(@NotNull User user, @NotNull Player player, @NotNull UUID uuid, @NotNull String name, @NotNull Command command, @NotNull String commandName, @NotNull String[] args) {
        user.message(ColorUtil.color(MessageFormat.format("&fPosiadasz &a{0}$", ChatUtil.formatNumber(user.getMoney()))));
        user.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP);
        return true;
    }
}
