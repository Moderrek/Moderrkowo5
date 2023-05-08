package pl.moderr.moderrkowo.core.automessage;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import pl.moderr.moderrkowo.core.utils.RandomUtils;

import java.util.List;

public class ModerrkowoAutoMessage {

    public static void SendServerMessage(Plugin plugin, String content) {
        final Component empty = Component.text().appendSpace().build();
        final Component message = Component.text().content("MODERRKOWO.PL").decoration(TextDecoration.BOLD, true).color(TextColor.color(0xFFCC11)).appendSpace().append(Component.text(content).decoration(TextDecoration.BOLD, false).color(NamedTextColor.WHITE)).build();
        plugin.getServer().broadcast(empty);
        plugin.getServer().broadcast(message);
        plugin.getServer().broadcast(empty);
    }

    public ModerrkowoAutoMessage(Plugin plugin, int secs, List<String> messages) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            if (messages.size() == 0) return;
            String randomContent = messages.get(RandomUtils.getRandomInt(0, messages.size() - 1));
            SendServerMessage(plugin, randomContent);
        }, 0, 20L * secs);
    }

}
