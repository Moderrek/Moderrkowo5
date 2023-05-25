package pl.moderr.moderrkowo.core.services.bazar.mechanics;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.services.bazar.BazarConstants;
import pl.moderr.moderrkowo.core.services.bazar.data.BazarUIData;

import java.util.UUID;

public class BazarListener implements Listener {

    private final BazarManager manager;

    public BazarListener(BazarManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void inventoryClose(@NotNull InventoryCloseEvent e) {
        final UUID uuid = e.getPlayer().getUniqueId();
        if (manager.getGuiData().containsKey(uuid)) {
            BazarUIData cache = manager.getGuiData().get(uuid);
            cache.getCallback().onClose();
            manager.getGuiData().remove(e.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void inventoryClick(@NotNull InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) return;
        final Player player = (Player) e.getWhoClicked();
        final UUID uuid = player.getUniqueId();
        if (e.getClickedInventory() == null) return;
        final String inventoryTitle = e.getView().getTitle();
        if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            if (inventoryTitle.equals(BazarConstants.GUI_TITLE)) {
                e.setCancelled(true);
                return;
            }
            return;
        }
        if (!inventoryTitle.equals(BazarConstants.GUI_TITLE)) return;
        e.setCancelled(true);
        if (manager.getGuiData().containsKey(uuid)) {
            BazarUIData cache = manager.getGuiData().get(uuid);
            if (e.getAction().equals(InventoryAction.PICKUP_ALL)) {
                cache.getCallback().onLeftClick(e.getSlot());
                return;
            }
            if (e.getAction().equals(InventoryAction.PICKUP_HALF)) {
                cache.getCallback().onRightClick(e.getSlot());
                return;
            }
            if (e.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
                cache.getCallback().onShiftClick(e.getSlot());
            }
        }
    }

}
