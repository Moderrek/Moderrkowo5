package pl.moderr.moderrkowo.core.services.opening.data;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.core.api.util.ItemStackUtil;
import pl.moderr.moderrkowo.core.api.util.RandomUtil;

import java.util.HashMap;

public class ModerrCaseRandomEnchantment extends ModerrCaseItem {

    public ModerrCaseRandomEnchantment(ModerrCaseItemRarity rarity, int weight) {
        super(null, rarity, weight);
    }

    Enchantment getRandomEnchantment() {
        return Enchantment.values()[(int) (Math.random() * Enchantment.values().length)];
    }

    int getRandomLevel(Enchantment enchantment) {
        if (enchantment.getMaxLevel() == 1) {
            return 1;
        } else {
            return RandomUtil.getRandomInt(1, enchantment.getMaxLevel());
        }
    }

    @Override
    public ItemStack item() {
        Enchantment enchantment = getRandomEnchantment();
        HashMap<Enchantment, Integer> enchantments = new HashMap<Enchantment, Integer>() {
            {
                put(enchantment, getRandomLevel(enchantment));
            }
        };
        return ItemStackUtil.generateEnchantmentBook(enchantments);
    }
}
