package pl.moderr.moderrkowo.core.mechanics.opening.data;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.core.utils.RandomUtils;

import java.util.ArrayList;

public class ModerrCaseRandomDisc extends ModerrCaseItem {

    public ModerrCaseRandomDisc(ModerrCaseItemRarity rarity, int weight) {
        super(null, rarity, weight);
    }

    @Override
    public ItemStack item() {
        ArrayList<Material> discs = new ArrayList<Material>() {
            {
                add(Material.MUSIC_DISC_13);
                add(Material.MUSIC_DISC_CAT);
                add(Material.MUSIC_DISC_BLOCKS);
                add(Material.MUSIC_DISC_CHIRP);
                add(Material.MUSIC_DISC_FAR);
                add(Material.MUSIC_DISC_MALL);
                add(Material.MUSIC_DISC_MELLOHI);
                add(Material.MUSIC_DISC_STAL);
                add(Material.MUSIC_DISC_STRAD);
                add(Material.MUSIC_DISC_WARD);
                add(Material.MUSIC_DISC_11);
                add(Material.MUSIC_DISC_WAIT);
                add(Material.MUSIC_DISC_PIGSTEP);
            }
        };
        return new ItemStack(discs.get(RandomUtils.getRandomInt(0, discs.size() - 1)), 1);
    }
}
