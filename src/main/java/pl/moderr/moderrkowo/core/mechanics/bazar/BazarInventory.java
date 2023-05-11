package pl.moderr.moderrkowo.core.mechanics.bazar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.ItemStackUtils;

import java.util.ArrayList;
import java.util.List;

public class BazarInventory {

    public final String title = ColorUtils.color("&eBazar");
    private final BazarManager manager;

    public BazarInventory(BazarManager manager) {
        this.manager = manager;
    }

    private void fillRow(Inventory inventory, int row, ItemStack itemStack) {
        for (int i = 9 * row; i < 9 * row + 9; i += 1) {
            inventory.setItem(i, itemStack);
        }
    }

    public ItemStack createValuableItem(@NotNull ValuableMaterial valuableMaterial, User user){
        final Material material = valuableMaterial.getMaterial();
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(Component.translatable(material.translationKey()).color(valuableMaterial.getCategory().getColorTheme()).decoration(TextDecoration.ITALIC, false));
        List<Component> lore = new ArrayList<>();
        if (valuableMaterial.canBuy()) {
            lore.add(Component.empty());
            lore.add(Component.text().content("Cena kupna").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GOLD).build());
            final double cost = valuableMaterial.getBuyCost();
            TextColor costColor = user.hasMoney(cost) ? NamedTextColor.GREEN : NamedTextColor.RED;
            lore.add(Component.text().content(ChatUtil.getMoney(cost)).decoration(TextDecoration.ITALIC, false).color(costColor).build());
            lore.add(Component.text()
                    .content("Kliknij LPM")
                    .color(TextColor.color(0x6F85AB))
                    .decoration(TextDecoration.ITALIC, false)
                    .appendSpace()
                    .append(
                            Component.text("[").color(NamedTextColor.DARK_GRAY)
                    ).append(
                            Component.text("▟").color(NamedTextColor.AQUA)
                    ).append(
                            Component.text("▙").color(NamedTextColor.GRAY)
                    ).append(
                            Component.text("]").color(NamedTextColor.DARK_GRAY)
                    ).appendSpace()
                    .append(
                            Component.text("aby kupić")
                    ).build()
            );
        }
        if (valuableMaterial.canSell()) {
            lore.add(Component.empty());
            lore.add(Component.text()
                    .content("Cena sprzedaży")
                    .decoration(TextDecoration.ITALIC, false)
                    .color(NamedTextColor.GOLD)
                    .build()
            );
            final double cost = valuableMaterial.getSellCost();
            TextColor costColor =  ItemStackUtils.getSameItems(user.getPlayer(), new ItemStack(valuableMaterial.getMaterial())) > 0 ? NamedTextColor.GREEN : NamedTextColor.RED;
            lore.add(Component.text()
                    .content(ChatUtil.getMoney(cost))
                    .decoration(TextDecoration.ITALIC, false)
                    .color(costColor)
                    .build()
            );
            lore.add(Component.text()
                    .content("Kliknij PPM")
                    .color(TextColor.color(0x6F85AB))
                    .decoration(TextDecoration.ITALIC, false)
                    .appendSpace()
                    .append(
                            Component.text("[").color(NamedTextColor.DARK_GRAY)
                    ).append(
                            Component.text("▟").color(NamedTextColor.GRAY)
                    ).append(
                            Component.text("▙").color(NamedTextColor.AQUA)
                    ).append(
                            Component.text("]").color(NamedTextColor.DARK_GRAY)
                    ).appendSpace()
                    .append(
                            Component.text("aby sprzedać")
                    ).build()
            );
            lore.add(Component.text().content("Kliknij SHIFT aby,").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY).build());
            lore.add(Component.text().content("sprzedać cały stos.").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY).build());
        }
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public Inventory create(ItemCategory selectedCategory, User user) {
        Inventory inventory = Bukkit.createInventory(null, 54, title);
        for (int i = 0; i < ItemCategory.values().length; i += 1) {
            ItemCategory category = ItemCategory.values()[i];
            ItemStack categoryItem = new ItemStack(category.getDisplayMaterial());
            ItemMeta meta = categoryItem.getItemMeta();
            meta.displayName(Component.text(category.getDisplayName()).decoration(TextDecoration.ITALIC, false).color(category.getColorTheme()));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ITEM_SPECIFICS);
            meta.lore(List.of(Component.text("Kliknij aby zmienić").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GREEN)));
            categoryItem.setItemMeta(meta);
            if (category.equals(selectedCategory)) {
                categoryItem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            }
            inventory.setItem(i, categoryItem);
        }

        ItemStack emptyItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta emptyItemMeta = emptyItem.getItemMeta();
        emptyItemMeta.displayName(Component.empty());
        emptyItem.setItemMeta(emptyItemMeta);
        fillRow(inventory, 1, emptyItem);

        List<ValuableMaterial> items = manager.getByCategory(selectedCategory);
        for (int i = 0; i < items.size(); i += 1) {
            final int slot = i + 18;
            final ValuableMaterial valuableItem = items.get(i);
            final ItemStack item = createValuableItem(valuableItem, user);
            inventory.setItem(slot, item);
        }
        return inventory;
    }

}
