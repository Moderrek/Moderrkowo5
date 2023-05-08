package pl.moderr.moderrkowo.core.opening.data;

import org.bukkit.inventory.ItemStack;

public class ModerrCaseItem {

    ItemStack item;
    ModerrCaseItemRarity rarity;
    public int weight;
    public ModerrCaseItem(ItemStack item, ModerrCaseItemRarity rarity, int weight){
        this.item = item;
        this.rarity = rarity;
        this.weight = weight;
    }

    public ItemStack item(){
        return item.clone();
    }
    public ModerrCaseItemRarity rarity(){
        return rarity;
    }
}
