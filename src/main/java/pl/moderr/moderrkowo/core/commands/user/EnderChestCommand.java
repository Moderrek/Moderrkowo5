package pl.moderr.moderrkowo.core.commands.user;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.executors.UserCommand;
import pl.moderr.moderrkowo.core.ranks.Rank;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.UUID;

public class EnderChestCommand implements UserCommand {

    @Override
    public boolean execute(@NotNull User user, @NotNull Player player, @NotNull UUID uuid, @NotNull String name, @NotNull Command command, @NotNull String commandName, @NotNull String[] args) {
        if (user.hasRank(Rank.Diament)) {
            if (ModerrkowoPlugin.getInstance().getAntyLogout().isFighting(player.getUniqueId())) {
                final Component componentCannotOpen = Component.text()
                        .content("Nie możesz otworzyć skrzyni kresu podczas walki!")
                        .color(NamedTextColor.RED)
                        .build();
                player.sendMessage(componentCannotOpen);
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return true;
            }
            player.openInventory(player.getEnderChest());
            final Component componentEnderChestOpened = Component.text()
                    .content("Otworzono skrzynie kresu.")
                    .color(NamedTextColor.GREEN)
                    .build();
            player.sendMessage(componentEnderChestOpened);
            player.playSound(player.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1, 1);
        } else {
            player.sendMessage(ColorUtils.color("&cNie posiadasz rangi &b&lDIAMENT &club &a&lEMERALD &caby otworzyć EnderChesta!"));
        }
        return true;
    }
}
