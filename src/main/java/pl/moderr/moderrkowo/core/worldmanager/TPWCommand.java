package pl.moderr.moderrkowo.core.worldmanager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.executors.PlayerCommand;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TPWCommand implements PlayerCommand, TabCompleter {

    private final Map<World.Environment, NamedTextColor> worldTypeNamedTextColorMap = new HashMap<>() {
        {
            put(World.Environment.CUSTOM, NamedTextColor.WHITE);
            put(World.Environment.NORMAL, NamedTextColor.GREEN);
            put(World.Environment.NETHER, NamedTextColor.RED);
            put(World.Environment.THE_END, NamedTextColor.LIGHT_PURPLE);
        }
    };

    @Override
    public boolean commandUse(@NotNull Player p, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            WorldManager.TeleportWorld(args[0], p);
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            final Title title = Title.title(Component.text(""), Component.text(args[0]).color(NamedTextColor.GREEN));
            p.showTitle(title);
            final Component component = Component.text("Pomyślnie zostałeś przeteleportowany na inny świat.")
                    .color(NamedTextColor.GREEN);
            p.sendMessage(component);
        } else {
            String worldName = p.getWorld().getName();
            final TextComponent component = Component.text("Aktualny świat: ")
                    .color(NamedTextColor.GREEN)
                    .append(Component.text(worldName)
                            .color(NamedTextColor.WHITE)
                    );
            p.sendMessage(component);
            p.sendMessage(Component.text("Wybierz świat: "));
            for (World world : Bukkit.getWorlds()) {
                final TextComponent componentWorld = Component.text(" - ")
                        .color(NamedTextColor.YELLOW)
                        .append(Component.text(world.getName() + (world.equals(p.getWorld()) ? "⋆" : ""))
                                .color(worldTypeNamedTextColorMap.get(world.getEnvironment()))
                                .hoverEvent(HoverEvent.showText(Component.text("Przenieś się do " + world.getName()).color(worldTypeNamedTextColorMap.get(world.getEnvironment()))))
                                .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, MessageFormat.format("/{0} {1}", label, world.getName()))));
                p.sendMessage(componentWorld);
            }
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            ArrayList<String> list = new ArrayList<>();
            Bukkit.getWorlds().forEach(world -> list.add(world.getName()));
            return list;
        }
        return null;
    }
}
