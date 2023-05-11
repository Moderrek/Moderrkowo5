package pl.moderr.moderrkowo.core.commands.user.information;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class DiscordCommand implements CommandExecutor {

    private final String discordLink;

    public DiscordCommand(final String discordLink) {
        this.discordLink = discordLink;
    }

    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final TextComponent textComponent = Component.text("Discord: ")
                .decoration(TextDecoration.BOLD, TextDecoration.State.TRUE)
                .color(NamedTextColor.BLUE)
                .append(Component.text(discordLink)
                        .color(NamedTextColor.WHITE)
                        .decoration(TextDecoration.BOLD, TextDecoration.State.FALSE)
                        .hoverEvent(HoverEvent.showText(Component.text("Otwórz zaproszenie Discord")
                                .color(NamedTextColor.GREEN))
                        )
                        .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.OPEN_URL, discordLink))
                );
        sender.sendMessage(textComponent);
        return false;
    }
}
