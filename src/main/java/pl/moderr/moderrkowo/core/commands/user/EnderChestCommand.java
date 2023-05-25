package pl.moderr.moderrkowo.core.commands.user;

import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.executor.UserCommand;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.ComponentUtil;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.user.ranks.Rank;

import java.util.UUID;

public class EnderChestCommand implements UserCommand {
    @Override
    public boolean execute(@NotNull User user, @NotNull Player player, @NotNull UUID uuid, @NotNull String name, @NotNull Command command, @NotNull String commandName, @NotNull String[] args) {
        if (!user.hasRank(Rank.Diament)) {
            user.message(ColorUtil.color("&cNie posiadasz rangi &b&lDIAMENT &club &a&lEMERALD &caby otworzyć skrzyni kresu!"));
            user.playSound(Sound.ENTITY_VILLAGER_NO);
            return true;
        }
        if (user.isFighting()) {
            user.message(ComponentUtil.coloredText("Nie możesz otworzyć skrzyni kresu podczas walki!", NamedTextColor.RED));
            user.playSound(Sound.ENTITY_VILLAGER_NO);
            return true;
        }
        user.inventory(user.getEnderChest());
        user.message(ComponentUtil.coloredText("Otworzono skrzynie kresu.", NamedTextColor.GREEN));
        user.playSound(Sound.BLOCK_ENDER_CHEST_OPEN);
        return true;
    }
}
