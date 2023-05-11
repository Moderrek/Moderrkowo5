package pl.moderr.moderrkowo.core.mechanics.bazar;

import lombok.Data;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.utils.ItemStackUtils;

import java.util.List;

@Data
public class BazarUIEvent implements UICallback {

    private final BazarManager manager;
    private final User user;
    private final Player player;

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void onLeftClick(int slot) {
        if (slot >= 0 && slot < ItemCategory.values().length) {
            final BazarUIData data = manager.getGuiCache().get(player.getUniqueId());
            final ItemCategory currentCategory = data.getSelectedCategory();
            ItemCategory newCategory = ItemCategory.values()[slot];
            if(currentCategory == newCategory) return;
            data.setSelectedCategory(newCategory);
            Inventory newInventory = manager.getInventory().create(newCategory, user);
            player.getOpenInventory().getTopInventory().setContents(newInventory.getContents());
            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN,0.3f,2f);
        }
        if (slot >= 18) {
            final BazarUIData data = manager.getGuiCache().get(player.getUniqueId());
            final ItemCategory currentCategory = data.getSelectedCategory();
            final List<ValuableMaterial> items = manager.getByCategory(currentCategory);
            final ValuableMaterial item = items.get(slot-18);
            // buy
            if(!item.canBuy()) return;
            if(!user.hasMoney(item.getBuyCost())){
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
                return;
            }
            // Perform buy
            user.subtractMoney(item.getBuyCost());
            ItemStackUtils.addItemStackToPlayer(player, new ItemStack(item.getMaterial()));
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP,0.5f,1.5f);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,0.1f,1.2f);
            // Update item
            player.getOpenInventory().getTopInventory().setItem(slot, manager.getInventory().createValuableItem(item, user));
        }
    }

    @Override
    public void onRightClick(int slot) {
        if (slot >= 18) {
            final BazarUIData data = manager.getGuiCache().get(player.getUniqueId());
            final ItemCategory currentCategory = data.getSelectedCategory();
            final List<ValuableMaterial> items = manager.getByCategory(currentCategory);
            final ValuableMaterial item = items.get(slot-18);
            // sell
            if(!item.canSell()) return;
            boolean success = ItemStackUtils.consumeItem(player, 1, new ItemStack(item.getMaterial()));
            if(!success){
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO,1,1);
                return;
            }
            // Perform sell
            user.addMoney(item.getSellCost());
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP,0.5f,1.5f);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP,0.1f,1.2f);
            // Update item
            player.getOpenInventory().getTopInventory().setItem(slot, manager.getInventory().createValuableItem(item, user));
        }
    }
}
