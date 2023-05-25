package pl.moderr.moderrkowo.core.commands.user.teleportation;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;

import java.util.HashMap;
import java.util.UUID;

public class TPACommand implements CommandExecutor {


    private static TPACommand instance;
    public final HashMap<UUID, UUID> tpaRequests = new HashMap<>();

    public TPACommand() {
        instance = this;
    }

    public static TPACommand getInstance() {
        return instance;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (ModerrkowoPlugin.getInstance().getAntyLogoutService().isFighting(p.getUniqueId())) {
                p.sendMessage(ColorUtil.color("&cNie możesz uciec podczas walki!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
            if (args.length > 0) {
                Player to = Bukkit.getPlayer(args[0]);
                if (to != null) {
                    if (p.getUniqueId().equals(to.getUniqueId())) {
                        p.sendMessage(ColorUtil.color("&cNie możesz się teleportować do siebie!"));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return false;
                    }
                    if (tpaRequests.containsValue(to.getUniqueId())) {
                        for (UUID key : tpaRequests.keySet()) {
                            tpaRequests.remove(key, to.getUniqueId());
                        }
                    }
                    tpaRequests.put(p.getUniqueId(), to.getUniqueId());
                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    to.playSound(to.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    p.sendMessage(ColorUtil.color("&aWysłano prośbę o teleportacje."));
                    to.sendMessage(ColorUtil.color("&6" + p.getName() + " &eprosi o teleportacje do Ciebie\n&aWpisz &c/tpaccept&a aby zaakceptować\n&aWpisz &c/tpdeny&a aby anulować"));
                } else {
                    p.sendMessage(ColorUtil.color("&cPodany gracz jest offline!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
            } else {
                p.sendMessage(ColorUtil.color("&cPodaj nick gracza!"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
        }
        return false;
    }
}
