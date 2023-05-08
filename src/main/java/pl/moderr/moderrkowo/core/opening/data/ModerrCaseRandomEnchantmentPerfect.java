package pl.moderr.moderrkowo.core.opening.data;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.core.utils.ItemStackUtils;

import java.util.HashMap;

public class ModerrCaseRandomEnchantmentPerfect extends ModerrCaseItem{

    public ModerrCaseRandomEnchantmentPerfect(ModerrCaseItemRarity rarity, int weight) {
        super(null, rarity, weight);
    }

    Enchantment getRandomEnchantment(){
        return Enchantment.values()[(int) (Math.random()*Enchantment.values().length)];
    }
    int getRandomLevel(Enchantment enchantment){
        return enchantment.getMaxLevel();
    }

    @Override
    public ItemStack item() {
        Enchantment enchantment = getRandomEnchantment();
        Enchantment finalEnchantment = enchantment;
        HashMap<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>(){
            {
                put(finalEnchantment, getRandomLevel(finalEnchantment));
            }
        };
        return ItemStackUtils.generateEnchantmentBook(enchantments);
    }
}