package pl.moderr.moderrkowo.core.events.user;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PortalListener implements Listener {

    @EventHandler
    public void portalTeleport(PlayerTeleportEvent e) {
//        if (e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
//            e.setCancelled(true);
//            e.getPlayer().sendMessage(ColorUtils.color("&8[!] &dPortal do kresu &cjest wyłączony!"));
//        }
//        if (e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
//            e.setCancelled(true);
//            e.getPlayer().sendMessage(ColorUtils.color("&8[!] &cPortal do piekła jest wyłączony!"));
//        }
    }
}
