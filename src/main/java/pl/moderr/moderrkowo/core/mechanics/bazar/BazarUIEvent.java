package pl.moderr.moderrkowo.core.mechanics.bazar;

import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ItemStackUtils;

import java.util.List;

@Data
public class BazarUIEvent implements UICallback {

    private final BazarManager manager;
    private final User user;
    private final Player player;

    @Override
    public void onOpen() {
        player.sendMessage(Component.text("Witaj na bazarze! Wybierz kategorię i zakup bądź sprzedaj swoje przedmioty!").color(NamedTextColor.GREEN));
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_TRADE,0.5f,1.1f);
    }

    @Override
    public void onClose() {
        player.sendMessage("");
        player.sendMessage(Component.text("Dzięki! Interesy z tobą to przyjemność.").color(NamedTextColor.GREEN));
        final BazarUIData data = manager.getGuiCache().get(player.getUniqueId());
        player.sendMessage(Component.text("Sprzedaż " + ChatUtil.getMoney(data.getIncome())).color(NamedTextColor.GRAY));
        player.sendMessage(Component.text("Kupno    -" + ChatUtil.getMoney(data.getOutcome())).color(NamedTextColor.GRAY).decoration(TextDecoration.UNDERLINED, true));
        final double profit = data.getIncome() - data.getOutcome();
        TextColor profitColor;
        if(profit == 0){
            profitColor = NamedTextColor.YELLOW;
        }else if(profit > 0){
            profitColor = NamedTextColor.GREEN;
        }else{
            profitColor = NamedTextColor.RED;
        }
        player.sendMessage(Component.text("Zysk       " + ChatUtil.getMoney(profit)).color(profitColor));
        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_YES,0.8f,1.1f);
        player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.4f, 1.5f);
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.2f, 1.2f);
    }

    @Override
    public void onLeftClick(int slot) {
        if (slot >= 0 && slot < ItemCategory.values().length) {
            final BazarUIData data = manager.getGuiCache().get(player.getUniqueId());
            final ItemCategory currentCategory = data.getSelectedCategory();
            ItemCategory newCategory = ItemCategory.values()[slot];
            if (currentCategory == newCategory) return;
            data.setSelectedCategory(newCategory);
            Inventory newInventory = manager.getInventory().create(newCategory, user);
            player.getOpenInventory().getTopInventory().setContents(newInventory.getContents());
            player.playSound(player.getLocation(), Sound.ITEM_BOOK_PAGE_TURN, 0.3f, 2f);
            return;
        }
        if (slot == 13) {
            final BazarUIData data = manager.getGuiCache().get(player.getUniqueId());
            final ItemCategory currentCategory = data.getSelectedCategory();
            final List<ValuableMaterial> categoryItems = manager.getByCategory(currentCategory);
            final Player player = user.getPlayer();
            ItemStack[] playerContent = player.getInventory().getContents();
            boolean isAnySold = false;
            for (ValuableMaterial categoryItem : categoryItems) {
                final ItemStack itemStack = new ItemStack(categoryItem.getMaterial());
                int count = ItemStackUtils.getSameItems(player, itemStack);
                if (count == 0) continue;
                if (ItemStackUtils.consumeItem(player, count, itemStack)) {
                    final double income = count * categoryItem.getSellCost();
                    user.addMoney(income);
                    data.setIncome(data.getIncome() + income);
                    isAnySold = true;
                }
            }
            if (isAnySold) {
                player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5f, 1.5f);
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.1f, 1.2f);
                // Update item
                player.getOpenInventory().getTopInventory().setItem(13, manager.getInventory().createSellCategoryItem(currentCategory, user));
            }
            return;
        }
        if (slot >= 18) {
            final BazarUIData data = manager.getGuiCache().get(player.getUniqueId());
            final ItemCategory currentCategory = data.getSelectedCategory();
            final List<ValuableMaterial> items = manager.getByCategory(currentCategory);
            final ValuableMaterial item = items.get(slot - 18);
            // buy
            if (!item.canBuy()) return;
            if (!user.hasMoney(item.getBuyCost())) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }
            // Perform buy
            user.subtractMoney(item.getBuyCost());
            data.setOutcome(data.getOutcome() + item.getBuyCost());
            ItemStackUtils.addItemStackToPlayer(player, new ItemStack(item.getMaterial()));
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5f, 1.5f);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.1f, 1.2f);
            // Update item
            player.getOpenInventory().getTopInventory().setItem(slot, manager.getInventory().createValuableItem(item, user));
            player.getOpenInventory().getTopInventory().setItem(13, manager.getInventory().createSellCategoryItem(currentCategory, user));
            return;
        }
    }

    @Override
    public void onRightClick(int slot) {
        if (slot >= 18) {
            final BazarUIData data = manager.getGuiCache().get(player.getUniqueId());
            final ItemCategory currentCategory = data.getSelectedCategory();
            final List<ValuableMaterial> items = manager.getByCategory(currentCategory);
            final ValuableMaterial item = items.get(slot - 18);
            // sell
            if (!item.canSell()) return;
            boolean success = ItemStackUtils.consumeItem(player, 1, new ItemStack(item.getMaterial()));
            if (!success) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }
            // Perform sell
            user.addMoney(item.getSellCost());
            data.setIncome(data.getIncome() + item.getSellCost());
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5f, 1.5f);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.1f, 1.2f);
            // Update item
            player.getOpenInventory().getTopInventory().setItem(slot, manager.getInventory().createValuableItem(item, user));
            player.getOpenInventory().getTopInventory().setItem(13, manager.getInventory().createSellCategoryItem(currentCategory, user));
        }
    }

    @Override
    public void onShiftClick(int slot) {
        if (slot >= 18) {
            final BazarUIData data = manager.getGuiCache().get(player.getUniqueId());
            final ItemCategory currentCategory = data.getSelectedCategory();
            final List<ValuableMaterial> items = manager.getByCategory(currentCategory);
            final ValuableMaterial item = items.get(slot - 18);
            final int stackSize = item.getMaterial().getMaxStackSize();
            // buy
            if (!item.canBuy()) return;
            final double outcome = item.getBuyCost() * stackSize;
            if (!user.hasMoney(outcome)) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }
            // Perform buy
            user.subtractMoney(outcome);
            data.setOutcome(data.getIncome() + outcome);
            ItemStackUtils.addItemStackToPlayer(player, new ItemStack(item.getMaterial(), stackSize));
            player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, 0.5f, 1.5f);
            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.1f, 1.2f);
            // Update item
            player.getOpenInventory().getTopInventory().setItem(slot, manager.getInventory().createValuableItem(item, user));
            player.getOpenInventory().getTopInventory().setItem(13, manager.getInventory().createSellCategoryItem(currentCategory, user));
            return;
        }
    }
}
