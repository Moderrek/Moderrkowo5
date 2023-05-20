package pl.moderr.moderrkowo.core.tasks;

import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.RandomUtils;

import java.util.ArrayList;
import java.util.List;

@Data
public class AutoMessageTask implements Runnable{

    private final Plugin plugin;
    private final List<String> messages;

    public static void SendServerMessage(@NotNull Plugin plugin, String content) {
        final Component empty = Component.text().appendSpace().build();
        final Component message = Component.text()
                .content("MODERRKOWO.PL")
                .decoration(TextDecoration.BOLD, true)
                .color(TextColor.color(0xFFCC11))
                .appendSpace()
                .append(Component.text(content)
                        .decoration(TextDecoration.BOLD, false)
                        .color(NamedTextColor.WHITE)
                ).build();
        plugin.getServer().broadcast(empty);
        plugin.getServer().broadcast(message);
        plugin.getServer().broadcast(empty);
    }

    @Override
    public void run() {
        if (messages.size() == 0) return;
        String randomContent = messages.get(RandomUtils.getRandomInt(0, messages.size() - 1));
        SendServerMessage(plugin, randomContent);
    }
}
