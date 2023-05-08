package pl.moderr.moderrkowo.core.bazar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.Arrays;

public class BazarInventory {

    private final BazarManager manager;
    public BazarInventory(BazarManager manager){
        this.manager = manager;
    }

    public final String title = ColorUtils.color("&eBazar");

    public Inventory create(ItemCategory selectedCategory){
        Inventory inventory = Bukkit.createInventory(null, 54, title);
        for(int i = 0; i < ItemCategory.values().length; i += 1){
            ItemCategory category = ItemCategory.values()[i];
            ItemStack categoryItem = new ItemStack(category.getDisplayMaterial());
            ItemMeta meta = categoryItem.getItemMeta();
            meta.displayName(Component.text(category.getDisplayName()).decoration(TextDecoration.ITALIC, false).color(category.getColorTheme()));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ITEM_SPECIFICS);
            meta.lore(Arrays.asList(Component.text("Kliknij aby zmienić").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GREEN)));
            categoryItem.setItemMeta(meta);
            if(category.equals(selectedCategory)){
                categoryItem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            }
            inventory.setItem(i, categoryItem);
        }
        return inventory;
    }

}
