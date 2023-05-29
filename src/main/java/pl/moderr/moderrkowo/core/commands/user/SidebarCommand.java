package pl.moderr.moderrkowo.core.commands.user;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.executor.UserCommand;
import pl.moderr.moderrkowo.core.api.util.ComponentUtil;
import pl.moderr.moderrkowo.core.user.User;

import java.util.UUID;

public class SidebarCommand implements UserCommand {
    @Override
    public boolean execute(@NotNull User user, @NotNull Player player, @NotNull UUID uuid, @NotNull String name, @NotNull Command command, @NotNull String commandName, @NotNull String[] args) {
        user.setSidebar(!user.isSidebar());
        Title title;
        if (user.isSidebar()) {
            title = Title.title(Component.space(), ComponentUtil.coloredText("Włączono sidebar", NamedTextColor.GREEN));
            user.updateScoreboard();
        } else {
            title = Title.title(Component.space(), ComponentUtil.coloredText("Wyłączono sidebar", NamedTextColor.RED));
            player.setScoreboard(ModerrkowoPlugin.getInstance().getServer().getScoreboardManager().getMainScoreboard());
        }
        player.showTitle(title);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        return true;
    }
}
