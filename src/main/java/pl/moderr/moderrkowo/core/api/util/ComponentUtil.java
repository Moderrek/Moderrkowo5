package pl.moderr.moderrkowo.core.api.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ComponentUtil {

    @Contract("_, _ -> new")
    public static @NotNull Component coloredText(String content, TextColor color) {
        return Component.text().content(content).color(color).build();
    }

}
