package pl.moderr.moderrkowo.core.services.opening.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import pl.moderr.moderrkowo.core.api.util.RandomUtil;

import java.util.ArrayList;

public class ModerrCaseRandomPotion extends ModerrCaseItem {

    public ModerrCaseRandomPotion(ModerrCaseItemRarity rarity, int weight) {
        super(null, rarity, weight);
    }

    public ItemStack getPotionItemStack(PotionType type, boolean extend, boolean upgraded) {
        ItemStack potion = new ItemStack(Material.POTION, 1);
        PotionMeta meta = (PotionMeta) potion.getItemMeta();
        try {
            meta.setBasePotionData(new PotionData(type, extend, upgraded));
        } catch (Exception e) {
            meta.setBasePotionData(new PotionData(type, extend, false));
        }
        potion.setItemMeta(meta);
        return potion;
    }

    @Override
    public ItemStack item() {
        ArrayList<PotionType> type = new ArrayList<PotionType>() {
            {
                add(PotionType.STRENGTH);
                add(PotionType.JUMP);
                add(PotionType.FIRE_RESISTANCE);
                add(PotionType.SPEED);
                add(PotionType.REGEN);
            }
        };
        return getPotionItemStack(type.get(RandomUtil.getRandomInt(0, type.size() - 1)), false, true);
    }
}
