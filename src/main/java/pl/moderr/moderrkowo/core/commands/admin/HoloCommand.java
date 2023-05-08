package pl.moderr.moderrkowo.core.commands.admin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

public class HoloCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            ArmorStand e = p.getWorld().spawn(p.getLocation(), ArmorStand.class);
            e.setInvulnerable(true);
            e.setAI(false);
            e.setCustomNameVisible(true);
            e.setVisible(false);
            e.setSmall(true);
            e.setGravity(false);
            e.setBasePlate(false);
            e.setCustomName(ColorUtils.color(Logger.getMessage(args, 0, true).replace("\\n","\n")));
            e.setSilent(true);
            e.setRemoveWhenFarAway(false);
            Logger.logAdminLog(ColorUtils.color("&6" + p.getName() + " &7postawił hologram &8(&f" + Logger.getMessage(args, 0, true) + "&8)"));
        }
        return false;
    }

}
