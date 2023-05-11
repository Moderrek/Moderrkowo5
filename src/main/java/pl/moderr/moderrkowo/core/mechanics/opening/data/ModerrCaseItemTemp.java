package pl.moderr.moderrkowo.core.mechanics.opening.data;

import org.bukkit.inventory.ItemStack;

public class ModerrCaseItemTemp {

    ItemStack item;
    ModerrCaseItemRarity rarity;

    public ModerrCaseItemTemp(ItemStack item, ModerrCaseItemRarity rarity) {
        this.item = item;
        this.rarity = rarity;
    }

    public ItemStack item() {
        return item.clone();
    }

    public ModerrCaseItemRarity rarity() {
        return rarity;
    }
}
