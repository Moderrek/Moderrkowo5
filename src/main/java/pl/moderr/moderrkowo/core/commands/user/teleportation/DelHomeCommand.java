package pl.moderr.moderrkowo.core.commands.user.teleportation;

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
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

public class DelHomeCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (ModerrkowoPlugin.getInstance().getAntyLogout().isFighting(p.getUniqueId())) {
                p.sendMessage(ColorUtils.color("&cNie możesz uciec podczas walki"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                Logger.logAdminLog(p.getName() + " chciał uciec podczas walki [home]");
                return false;
            }
            int temp = ModerrkowoPlugin.getInstance().dataConfig.getInt("homescount." + p.getUniqueId());
            if (args.length > 0) {
                Location loc = ModerrkowoPlugin.getInstance().dataConfig.getLocation("homes." + p.getUniqueId() + "." + args[0]);
                if (loc != null) {
                    ModerrkowoPlugin.getInstance().dataConfig.set("homes." + p.getUniqueId() + "." + args[0], null);
                    ModerrkowoPlugin.getInstance().dataConfig.set("homescount." + p.getUniqueId(), temp - 1);
                    try {
                        List<String> homes = ModerrkowoPlugin.getInstance().dataConfig.getStringList(MessageFormat.format("homeslist.{0}", p.getUniqueId()));
                        homes.remove(args[0]);
                        ModerrkowoPlugin.getInstance().dataConfig.set(MessageFormat.format("homeslist.{0}", p.getUniqueId()), homes);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        ModerrkowoPlugin.getInstance().dataConfig.save(ModerrkowoPlugin.getInstance().dataFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    p.sendMessage(ColorUtils.color("&aPomyślnie usunięto dom"));
                    return true;
                } else {
                    p.sendMessage(ColorUtils.color("&cTen dom nie istnieje!"));
                }
            } else {
                p.sendMessage(ColorUtils.color("&c/delhome <nazwa>"));
            }
            return false;
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
