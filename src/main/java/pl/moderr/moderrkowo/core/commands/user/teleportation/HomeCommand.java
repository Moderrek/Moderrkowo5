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

public class HomeCommand implements CommandExecutor {
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
            if (args.length > 0) {
                Location loc = Main.getInstance().dataConfig.getLocation("homes." + p.getUniqueId() + "." + args[0]);
                if (loc == null) {
                    p.sendMessage(ColorUtils.color("&cNajpierw ustaw dom /sethome <nazwa>"));
                    return false;
                }
                p.teleport(loc);
                //Logger.logAdminLog(p.getName() + " przeteleportował się do domu");
                p.sendMessage(ColorUtils.color("&8[!] &aWitaj w domu"));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            } else {
                Location loc = Main.getInstance().dataConfig.getLocation("homes." + p.getUniqueId());
                if (loc == null) {
                    p.sendMessage(ColorUtils.color("&cNajpierw ustaw dom /sethome <nazwa>"));
                    return false;
                }
                p.teleport(loc);
                //Logger.logAdminLog(p.getName() + " przeteleportował się do domu");
                p.sendMessage(ColorUtils.color("&8[!] &aWitaj w domu"));
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                p.sendMessage(ColorUtils.color("&e/home <nazwa>"));
            }
        }
        return false;
    }
}
