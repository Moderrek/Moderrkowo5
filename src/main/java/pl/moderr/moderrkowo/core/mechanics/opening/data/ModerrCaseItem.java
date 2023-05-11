package pl.moderr.moderrkowo.core.mechanics.opening.data;

import org.bukkit.inventory.ItemStack;

public class ModerrCaseItem {

    public int weight;
    ItemStack item;
    ModerrCaseItemRarity rarity;

    public ModerrCaseItem(ItemStack item, ModerrCaseItemRarity rarity, int weight) {
        this.item = item;
        this.rarity = rarity;
        this.weight = weight;
    }

    public ItemStack item() {
        return item.clone();
    }

    public ModerrCaseItemRarity rarity() {
        return rarity;
    }
}
