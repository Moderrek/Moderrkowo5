package pl.moderr.moderrkowo.core.services.bazar;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.util.ChatUtil;
import pl.moderr.moderrkowo.core.api.util.ItemStackUtil;
import pl.moderr.moderrkowo.core.services.bazar.data.ItemCategory;
import pl.moderr.moderrkowo.core.services.bazar.data.ValuableMaterial;
import pl.moderr.moderrkowo.core.services.bazar.mechanics.BazarManager;
import pl.moderr.moderrkowo.core.user.User;

import java.util.ArrayList;
import java.util.List;

public class BazarGUIBuilder {

    private final BazarManager manager;

    public BazarGUIBuilder(BazarManager manager) {
        this.manager = manager;
    }

    private void fillRow(Inventory inventory, int row, ItemStack itemStack) {
        for (int i = 9 * row; i < 9 * row + 9; i += 1) inventory.setItem(i, itemStack);
    }

    public ItemStack createValuableItem(@NotNull ValuableMaterial valuableMaterial, User user) {
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
            lore.add(Component.text().content(ChatUtil.formatMoney(cost)).decoration(TextDecoration.ITALIC, false).color(costColor).build());
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
            lore.add(Component.text().content("Kliknij SHIFT aby,").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY).build());
            lore.add(Component.text().content("kupić cały stack.").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY).build());
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
            TextColor costColor = ItemStackUtil.getSameItems(user.getPlayer(), new ItemStack(valuableMaterial.getMaterial())) > 0 ? NamedTextColor.GREEN : NamedTextColor.RED;
            lore.add(Component.text()
                    .content(ChatUtil.formatMoney(cost))
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
        }
        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack createSellCategoryItem(ItemCategory category, @NotNull User user) {
        ItemStack item = new ItemStack(Material.EMERALD);
        ItemMeta meta = item.getItemMeta();

        double value = 0;
        List<ValuableMaterial> categoryItems = manager.getByCategory(category);
        final Player player = user.getPlayer();
        ItemStack[] playerContent = player.getInventory().getContents();

        meta.displayName(Component.text().content("Sprzedaj cały ekwipunek").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GREEN).build());
        List<Component> haveItems = new ArrayList<>();
        for (ValuableMaterial categoryItem : categoryItems) {
            int count = ItemStackUtil.getSameItems(player, new ItemStack(categoryItem.getMaterial()));
            value += count * categoryItem.getSellCost();
            if (count > 0)
                haveItems.add(Component.text().content(count + "x ").color(NamedTextColor.GREEN).decoration(TextDecoration.ITALIC, false).append(Component.translatable(categoryItem.getMaterial())).build());
        }
        List<Component> lore = new ArrayList<>();
        lore.add(Component.empty());
        lore.add(Component.text().content("Wartość ekwipunku").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GREEN).build());
        lore.add(Component.text().content(ChatUtil.formatMoney(value)).decoration(TextDecoration.ITALIC, false).color(NamedTextColor.DARK_GREEN).build());
        lore.add(Component.empty());
        if (haveItems.size() > 0) {
            lore.addAll(haveItems);
            lore.add(Component.empty());
        }
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
                        Component.text("aby sprzedać")
                ).build());

        meta.lore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public Inventory create(ItemCategory selectedCategory, User user) {
        Inventory inventory = Bukkit.createInventory(null, 54, BazarConstants.GUI_TITLE);
        for (int i = 0; i < ItemCategory.values().length; i += 1) {
            ItemCategory category = ItemCategory.values()[i];
            ItemStack categoryItem = new ItemStack(category.getDisplayMaterial());
            ItemMeta meta = categoryItem.getItemMeta();
            meta.displayName(Component.text(category.getDisplayName()).decoration(TextDecoration.ITALIC, false).color(category.getColorTheme()));
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ITEM_SPECIFICS);
            if (selectedCategory == category) {
                categoryItem.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
                meta.lore(List.of(Component.text("Wybrana kategoria przedmiotów").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GRAY)));
            } else {
                meta.lore(List.of(Component.text("Kliknij aby zmienić kategorię").decoration(TextDecoration.ITALIC, false).color(NamedTextColor.GREEN)));
            }
            categoryItem.setItemMeta(meta);
            inventory.setItem(i, categoryItem);
        }

        ItemStack emptyItem = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta emptyItemMeta = emptyItem.getItemMeta();
        emptyItemMeta.displayName(Component.empty());
        emptyItem.setItemMeta(emptyItemMeta);
        fillRow(inventory, 1, emptyItem);

        inventory.setItem(13, createSellCategoryItem(selectedCategory, user));

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
