package pl.moderr.moderrkowo.core.commands.user.teleportation;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.io.IOException;

public class DelHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (Main.getInstance().instanceAntyLogout.inFight(p.getUniqueId())) {
                p.sendMessage(ColorUtils.color("&cNie możesz uciec podczas walki"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                Logger.logAdminLog(p.getName() + " chciał uciec podczas walki [home]");
                return false;
            }
            int temp = Main.getInstance().dataConfig.getInt("homescount." + p.getUniqueId());
            if (args.length > 0) {
                Location loc = Main.getInstance().dataConfig.getLocation("homes." + p.getUniqueId() + "." + args[0]);
                if (loc != null) {
                    Main.getInstance().dataConfig.set("homes." + p.getUniqueId() + "." + args[0], null);
                    Main.getInstance().dataConfig.set("homescount." + p.getUniqueId(), temp - 1);
                    try {
                        Main.getInstance().dataConfig.save(Main.getInstance().dataFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    p.sendMessage(ColorUtils.color("&aPomyślnie usunięto dom"));
                    return true;
                } else {
                    p.sendMessage(ColorUtils.color("&cTen dom nie istnieje!"));
                }
                return false;
            } else {
                p.sendMessage(ColorUtils.color("&c/delhome <nazwa>"));
                return false;
            }
        }
        return false;
    }
}
