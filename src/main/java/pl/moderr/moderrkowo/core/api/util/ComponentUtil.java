package pl.moderr.moderrkowo.core.api.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * Util Class
 *
 * @since 1.3.0
 */
public class ComponentUtil {

    /**
     * Builds simple text component
     *
     * @param content The content
     * @param color   The text color
     * @return The built component
     */
    @Contract("_, _ -> new")
    public static @NotNull Component coloredText(String content, TextColor color) {
        return Component.text().content(content).color(color).build();
    }

}
