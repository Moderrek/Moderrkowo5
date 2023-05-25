package pl.moderr.moderrkowo.core.services.bazar;

import lombok.Data;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.core.api.util.ChatUtil;
import pl.moderr.moderrkowo.core.api.util.ComponentUtil;
import pl.moderr.moderrkowo.core.api.util.ItemStackUtil;
import pl.moderr.moderrkowo.core.services.bazar.data.BazarUIData;
import pl.moderr.moderrkowo.core.services.bazar.data.ItemCategory;
import pl.moderr.moderrkowo.core.services.bazar.data.ValuableMaterial;
import pl.moderr.moderrkowo.core.services.bazar.mechanics.BazarManager;
import pl.moderr.moderrkowo.core.services.bazar.mechanics.GUICallback;
import pl.moderr.moderrkowo.core.user.User;

import java.util.List;
import java.util.UUID;

@Data
public class BazarGUI implements GUICallback {

    private final BazarManager manager;
    private final User user;
    private final Player player;

    @Override
    public void onOpen() {
        user.message("Witaj na bazarze! Wybierz kategorię i zakup bądź sprzedaj swoje przedmioty!", NamedTextColor.GREEN);
        user.playSound(Sound.ENTITY_VILLAGER_TRADE, 0.5F, 1.1F);
    }

    @Override
    public void onClose() {
        user.message("");
        user.message("Dzięki! Interesy z tobą to przyjemność.", NamedTextColor.GREEN);
        final UUID uuid = player.getUniqueId();
        final BazarUIData data = manager.getGuiData().get(uuid);
        user.message("Sprzedaż " + ChatUtil.formatMoney(data.getIncome()), NamedTextColor.GRAY);
        user.message(Component.text("Kupno    -" + ChatUtil.formatMoney(data.getOutcome())).color(NamedTextColor.GRAY).decoration(TextDecoration.UNDERLINED, true));
        final double profit = data.getIncome() - data.getOutcome();
        TextColor profitColor;
        if (profit == 0) profitColor = NamedTextColor.YELLOW; else if (profit > 0) profitColor = NamedTextColor.GREEN; else profitColor = NamedTextColor.RED;
        user.message("Zysk       " + ChatUtil.formatMoney(profit), profitColor);
        user.playSound(Sound.ENTITY_VILLAGER_YES, 0.8F, 1.1F);
        user.playSound(Sound.ENTITY_ITEM_PICKUP, 0.4F, 1.5F);
        user.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.2F, 1.2F);
    }

    @Override
    public void onLeftClick(int slot) {
        final UUID uuid = player.getUniqueId();
        // Category Click
        if (slot >= 0 && slot < ItemCategory.values().length) {
            final BazarUIData data = manager.getGuiData().get(uuid);
            final ItemCategory currentCategory = data.getSelectedCategory();
            ItemCategory newCategory = ItemCategory.values()[slot];
            if (currentCategory.equals(newCategory)) return;
            data.setSelectedCategory(newCategory);
            Inventory newInventory = manager.getInventory().create(newCategory, user);
            player.getOpenInventory().getTopInventory().setContents(newInventory.getContents());
            user.playSound(Sound.ITEM_BOOK_PAGE_TURN, 0.3F, 2F);
            return;
        }
        // Sell All (in category) Click
        if (slot == 13) {
            final BazarUIData data = manager.getGuiData().get(uuid);
            final ItemCategory currentCategory = data.getSelectedCategory();
            final List<ValuableMaterial> categoryItems = manager.getByCategory(currentCategory);
            boolean isAnySold = false;
            for (ValuableMaterial categoryItem : categoryItems) {
                final ItemStack itemStack = new ItemStack(categoryItem.getMaterial());
                int count = ItemStackUtil.getSameItems(player, itemStack);
                if (count == 0) continue;
                if (ItemStackUtil.consumeItem(player, count, itemStack)) {
                    final double income = count * categoryItem.getSellCost();
                    user.addMoney(income);
                    data.setIncome(data.getIncome() + income);
                    isAnySold = true;
                }
            }
            if (isAnySold) {
                user.playSound(Sound.ENTITY_ITEM_PICKUP, 0.5F, 1.5F);
                user.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.1F, 1.2F);
                // Update item
                player.getOpenInventory().getTopInventory().setItem(13, manager.getInventory().createSellCategoryItem(currentCategory, user));
            }
            return;
        }
        // Sell Item Click
        if (slot >= 18) {
            final BazarUIData data = manager.getGuiData().get(uuid);
            final ItemCategory currentCategory = data.getSelectedCategory();
            final List<ValuableMaterial> items = manager.getByCategory(currentCategory);
            final ValuableMaterial item = items.get(slot - 18);
            // buy
            if (!item.canBuy()) return;
            if (!user.hasMoney(item.getBuyCost())) {
                user.playSound(Sound.ENTITY_VILLAGER_NO);
                return;
            }
            // Perform buy
            user.subtractMoney(item.getBuyCost());
            data.setOutcome(data.getOutcome() + item.getBuyCost());
            user.give(item.getMaterial());
            user.playSound(Sound.ENTITY_ITEM_PICKUP, 0.5F, 1.5F);
            user.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.1F, 1.2F);
            // Update item
            player.getOpenInventory().getTopInventory().setItem(slot, manager.getInventory().createValuableItem(item, user));
            player.getOpenInventory().getTopInventory().setItem(13, manager.getInventory().createSellCategoryItem(currentCategory, user));
        }
    }

    @Override
    public void onRightClick(int slot) {
        if (slot >= 18) {
            final BazarUIData data = manager.getGuiData().get(player.getUniqueId());
            final ItemCategory currentCategory = data.getSelectedCategory();
            final List<ValuableMaterial> items = manager.getByCategory(currentCategory);
            final ValuableMaterial item = items.get(slot - 18);
            // sell
            if (!item.canSell()) return;
            boolean success = ItemStackUtil.consumeItem(player, 1, new ItemStack(item.getMaterial()));
            if (!success) {
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }
            // Perform sell
            user.addMoney(item.getSellCost());
            data.setIncome(data.getIncome() + item.getSellCost());
            user.playSound(Sound.ENTITY_ITEM_PICKUP, 0.5F, 1.5F);
            user.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.1F, 1.2F);
            // Update item
            player.getOpenInventory().getTopInventory().setItem(slot, manager.getInventory().createValuableItem(item, user));
            player.getOpenInventory().getTopInventory().setItem(13, manager.getInventory().createSellCategoryItem(currentCategory, user));
        }
    }

    @Override
    public void onShiftClick(int slot) {
        if (slot >= 18) {
            final BazarUIData data = manager.getGuiData().get(player.getUniqueId());
            final ItemCategory currentCategory = data.getSelectedCategory();
            final List<ValuableMaterial> items = manager.getByCategory(currentCategory);
            final ValuableMaterial item = items.get(slot - 18);
            final int stackSize = item.getMaterial().getMaxStackSize();
            // buy
            if (!item.canBuy()) return;
            final double outcome = item.getBuyCost() * stackSize;
            if (!user.hasMoney(outcome)) {
                user.playSound(Sound.ENTITY_VILLAGER_NO);
                return;
            }
            // Perform buy
            user.subtractMoney(outcome);
            data.setOutcome(data.getOutcome() + outcome);
            user.give(new ItemStack(item.getMaterial(), stackSize));
            user.playSound(Sound.ENTITY_ITEM_PICKUP, 0.5F, 1.5F);
            user.playSound(Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.1F, 1.2F);
            // Update item
            player.getOpenInventory().getTopInventory().setItem(slot, manager.getInventory().createValuableItem(item, user));
            player.getOpenInventory().getTopInventory().setItem(13, manager.getInventory().createSellCategoryItem(currentCategory, user));
        }
    }
}
