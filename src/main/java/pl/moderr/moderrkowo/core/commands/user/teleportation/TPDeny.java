package pl.moderr.moderrkowo.core.commands.user.teleportation;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.UUID;

public class TPDeny implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (Main.getInstance().instanceAntyLogout.inFight(p.getUniqueId())) {
                p.sendMessage(ColorUtils.color("&cNie możesz uciec podczas walki!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
            if (TPACommand.getInstance().tpaRequests.containsValue(p.getUniqueId())) {
                for (UUID request : TPACommand.getInstance().tpaRequests.keySet()) {
                    UUID to = TPACommand.getInstance().tpaRequests.get(request);
                    if (to == p.getUniqueId()) {
                        TPACommand.getInstance().tpaRequests.remove(request, to);
                        p.sendMessage(ColorUtils.color("&cAnulowano teleportacje!"));
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        Player requestP = Bukkit.getPlayer(request);
                        if (requestP != null) {
                            requestP.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            requestP.sendMessage(ColorUtils.color("&c" + p.getName() + " anulował prośbę o teleportacje"));
                        }
                        return true;
                    }
                }
                p.sendMessage(ColorUtils.color("&cNie możesz anulować żadnej teleportacji!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            }
        }
        return false;
    }
}
