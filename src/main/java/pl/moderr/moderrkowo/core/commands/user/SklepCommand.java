package pl.moderr.moderrkowo.core.commands.user;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.executor.PlayerCommand;

public class SklepCommand implements PlayerCommand, Listener {

    @Override
    public boolean execute(@NotNull Player player, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        player.sendMessage(Component
                .text("Sklep: ")
                .color(NamedTextColor.GRAY)
                .append(Component
                        .text("sklep.moderrkowo.pl")
                        .color(NamedTextColor.GREEN)
                        .clickEvent(ClickEvent.openUrl("https://sklep.moderrkowo.pl/category/survival-ekonomia"))
                        .hoverEvent(HoverEvent.showText(Component
                                .text("Przejdź do sklepu")
                                .color(NamedTextColor.GRAY))
                        ))
        );
        return false;
    }

}
