package pl.moderr.moderrkowo.core.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.util.Logger;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;

public class ModerrkowoCommand implements CommandExecutor, TabCompleter {

    private final List<String> commands = Arrays.asList("help", "version", "performance");

    private final ModerrkowoPlugin plugin;

    public ModerrkowoCommand(@NotNull ModerrkowoPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, () -> {
            final double TPS = Bukkit.getServer().getTPS()[0];
            final String textTPS = new DecimalFormat("##.00").format(TPS);
            if (TPS <= 15) {
                Logger.logAdminLog("&eOdnotowano spadek wydajności serwera! TPS = " + textTPS);
            }
        }, 0L, 200L);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("help")) {
                TextComponent.Builder component = Component.text("Dostępne polecenia: ").color(NamedTextColor.GREEN).toBuilder();
                for (String commandName : commands) {
                    component.append(Component.text(commandName).color(NamedTextColor.WHITE));
                    component.append(Component.text(" "));
                }
                sender.sendMessage(component.build());
                return true;
            }
            if (args[0].equalsIgnoreCase("performance")) {
                final TextComponent header = Component.text("Moderrkowo Performance").color(NamedTextColor.YELLOW);
                final long memoryFree = Runtime.getRuntime().freeMemory() / 1048576;
                final long memoryMax = Runtime.getRuntime().maxMemory() / 1048576;
                final TextComponent memory = Component.text("Pamięć: ").color(NamedTextColor.GOLD).append(
                        Component.text(MessageFormat.format("{0} MB / {1} MB", memoryFree, memoryMax))
                                .color(NamedTextColor.GREEN)
                );
                final TextComponent tps = Component.text("TPS: ").color(NamedTextColor.GOLD).append(
                        Component.text(new DecimalFormat("##.00").format(Bukkit.getServer().getTPS()[0]))
                                .color(NamedTextColor.GREEN)
                );
                sender.sendMessage(header);
                sender.sendMessage(memory);
                sender.sendMessage(tps);
                return true;
            }
            if (args[0].equalsIgnoreCase("version")) {
                final Component component = Component.text("Moderrkowo ").color(NamedTextColor.GREEN).append(Component.text(ModerrkowoPlugin.getVersion()));
                sender.sendMessage(component);
                return true;
            }
        }
        final TextComponent errorComponent = Component.text(MessageFormat.format("Nie rozpoznano polecenia. Aby uzyskać pomoc wpisz /{0} help", s))
                .color(NamedTextColor.RED);
        sender.sendMessage(errorComponent);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return commands;
        }
        return null;
    }
}
