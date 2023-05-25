package pl.moderr.moderrkowo.core.commands.user.teleportation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.Logger;

import java.text.MessageFormat;
import java.util.List;

public class HomeCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (ModerrkowoPlugin.getInstance().getAntyLogoutService().isFighting(p.getUniqueId())) {
                p.sendMessage(ColorUtil.color("&cNie możesz uciec podczas walki"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                Logger.logAdminLog(p.getName() + " chciał uciec podczas walki [home]");
                return false;
            }
            if (args.length > 0) {
                Location loc = ModerrkowoPlugin.getInstance().dataConfig.getLocation("homes." + p.getUniqueId() + "." + args[0]);
                if (loc == null) {
                    p.sendMessage(ColorUtil.color("&cNajpierw ustaw dom /sethome <nazwa>"));
                    return false;
                }
                p.teleport(loc);
                //Logger.logAdminLog(p.getName() + " przeteleportował się do domu");
                p.sendMessage(ColorUtil.color("&8[!] &aWitaj w domu"));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                return true;
            }
            List<String> homes = ModerrkowoPlugin.getInstance().dataConfig.getStringList(MessageFormat.format("homeslist.{0}", p.getUniqueId()));
            if (!homes.isEmpty()) {
                TextComponent.Builder builder = Component.text();
                for (String homeName : homes) {
                    builder.appendSpace().append(Component.text(homeName));
                }
                p.sendMessage(Component.text("Twoje domy:").append(builder.build()));
            }
            p.sendMessage(Component.text("Uzyj: /home <nazwa>, bądź /sethome <nazwa>").color(NamedTextColor.YELLOW));
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }
        if (args.length == 1) {
            return ModerrkowoPlugin.getInstance().dataConfig.getStringList(MessageFormat.format("homeslist.{0}", ((Player) sender).getUniqueId()));
        }
        return null;
    }
}
