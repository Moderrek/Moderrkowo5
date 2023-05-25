package pl.moderr.moderrkowo.core.commands.user;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.executor.UserCommand;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.user.ranks.Rank;

import java.util.UUID;

public class CraftingCommand implements UserCommand {
    @Override
    public boolean execute(@NotNull User user, @NotNull Player player, @NotNull UUID uuid, @NotNull String name, @NotNull Command command, @NotNull String commandName, @NotNull String[] args) {
        if (!user.hasRank(Rank.Diament)) {
            user.message(ColorUtil.color("&cNie posiadasz rangi &b&lDIAMENT &club &a&lEMERALD &caby otworzyć crafting!"));
            user.playSound(Sound.ENTITY_VILLAGER_NO);
            return true;
        }
        if (user.isFighting()) {
            user.message(ColorUtil.color("&cNie możesz użyć tego podczas walki!"));
            user.playSound(Sound.ENTITY_VILLAGER_NO);
            return true;
        }
        player.openWorkbench(null, true);
        user.message(ColorUtil.color("&aOtworzono crafting"));
        user.playSound(Sound.ENTITY_ITEM_PICKUP);
        return true;
    }
}