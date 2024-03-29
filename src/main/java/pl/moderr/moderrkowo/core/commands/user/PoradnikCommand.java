package pl.moderr.moderrkowo.core.commands.user;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;

public class PoradnikCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (ModerrkowoPlugin.getInstance().getAntyLogoutService().isFighting(p.getUniqueId())) {
                p.sendMessage(ColorUtil.color("&cPoczekaj aż nie będziesz podczas walki!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
            p.sendTitle(ColorUtil.color(""), ColorUtil.color("&aMiłego czytania"));
            p.teleport(new Location(Bukkit.getWorld("spawn"), 447.5, 68, -393.5, 90, 0));
            p.sendMessage(ColorUtil.color("&aNa końcu tunelu znajduje się powrót"));
        }
        return false;
    }
}
