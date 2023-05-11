package pl.moderr.moderrkowo.core.mechanics.bazar;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BazarListener implements Listener {

    private final BazarManager manager;

    public BazarListener(BazarManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void inventoryClose(@NotNull InventoryCloseEvent e) {
        final UUID uuid = e.getPlayer().getUniqueId();
        if (manager.getGuiCache().containsKey(uuid)) {
            BazarUIData cache = manager.getGuiCache().get(uuid);
            cache.getCallback().onClose();
            manager.getGuiCache().remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void inventoryClick(@NotNull InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        final Player player = (Player) e.getWhoClicked();
        final UUID uuid = player.getUniqueId();
        if (e.getClickedInventory() == null) {
            return;
        }
        final String inventoryTitle = e.getView().getTitle();
        if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            if (inventoryTitle.equals(manager.getInventory().title)) {
                e.setCancelled(true);
                return;
            }
            return;
        }
        if (!manager.getInventory().title.equals(inventoryTitle)) {
            return;
        }
        e.setCancelled(true);
        if (manager.getGuiCache().containsKey(uuid)) {
            BazarUIData cache = manager.getGuiCache().get(uuid);
            if (e.getAction().equals(InventoryAction.PICKUP_ALL)) {
                cache.getCallback().onLeftClick(e.getSlot());
            }
            if (e.getAction().equals(InventoryAction.PICKUP_HALF)) {
                cache.getCallback().onRightClick(e.getSlot());
            }
        }
    }

}
