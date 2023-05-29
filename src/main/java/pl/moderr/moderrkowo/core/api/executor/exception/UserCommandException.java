package pl.moderr.moderrkowo.core.api.executor.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Sound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents Command Exception thrown during user command execution, which can be shown to player.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserCommandException extends RuntimeException {

    private final @NotNull String displayMessage;
    private final @NotNull TextColor messageColor;
    private final @Nullable Sound sound;

    public UserCommandException(@NotNull String displayMessage, @NotNull TextColor messageColor, @Nullable Sound sound) {
        super(displayMessage);
        this.displayMessage = displayMessage;
        this.messageColor = messageColor;
        this.sound = sound;
    }

    public UserCommandException(@NotNull String displayMessage, @NotNull TextColor messageColor) {
        super(displayMessage);
        this.displayMessage = displayMessage;
        this.messageColor = messageColor;
        sound = Sound.ENTITY_VILLAGER_NO;
    }

    public UserCommandException(@NotNull String displayMessage) {
        super(displayMessage);
        this.displayMessage = displayMessage;
        messageColor = NamedTextColor.RED;
        sound = Sound.ENTITY_VILLAGER_NO;
    }

    public UserCommandException() {
        super("Wystąpił problem podczas wykonywania komendy!");
        displayMessage = "Wystąpił problem podczas wykonywania komendy!";
        messageColor = NamedTextColor.RED;
        sound = Sound.ENTITY_VILLAGER_NO;
    }

}
