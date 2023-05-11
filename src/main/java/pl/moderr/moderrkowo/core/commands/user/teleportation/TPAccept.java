package pl.moderr.moderrkowo.core.commands.user.teleportation;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.UUID;

public class TPAccept implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (ModerrkowoPlugin.getInstance().getAntyLogout().isFighting(p.getUniqueId())) {
                p.sendMessage(ColorUtils.color("&cNie możesz uciec podczas walki!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
            if (TPACommand.getInstance().tpaRequests.containsValue(p.getUniqueId())) {
                for (UUID request : TPACommand.getInstance().tpaRequests.keySet()) {
                    UUID to = TPACommand.getInstance().tpaRequests.get(request);
                    if (to == p.getUniqueId()) {
                        TPACommand.getInstance().tpaRequests.remove(request, to);
                        Player requestP = Bukkit.getPlayer(request);
                        if (requestP != null) {
                            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                            p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                            requestP.playSound(requestP.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                            p.sendMessage(ColorUtils.color("&aTeleportowanie..."));
                            if (ModerrkowoPlugin.getInstance().getAntyLogout().isFighting(requestP.getUniqueId())) {
                                p.sendMessage(ColorUtils.color("&cNie można teleportować gracz jest podczas walki"));
                                return true;
                            }
                            requestP.sendMessage(ColorUtils.color("&a" + p.getName() + " zaakceptował prośbę o teleportacje!"));
                            requestP.teleport(p);
                            return true;
                        }
                        p.sendMessage(ColorUtils.color("&cGracz jest już offline"));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return false;
                    }
                }
                p.sendMessage(ColorUtils.color("&cNie możesz zaakceptować żadnej teleportacji"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
        }
        return false;
    }
}
