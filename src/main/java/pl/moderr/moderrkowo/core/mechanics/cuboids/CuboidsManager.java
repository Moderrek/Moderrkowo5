package pl.moderr.moderrkowo.core.mechanics.cuboids;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.mechanics.cuboids.commands.CuboidCommand;
import pl.moderr.moderrkowo.core.mechanics.cuboids.listeners.PlaceRemoveCuboid;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.util.List;
import java.util.Objects;

public class CuboidsManager {

    private static final Material cuboidMaterial = Material.LODESTONE;
    private static final String cuboidDisplayName = ColorUtils.color("&bBlok działki");

    public static @NotNull ItemStack getCuboidItem(int count) {
        ItemStack item = new ItemStack(cuboidMaterial, count);
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(cuboidDisplayName);
        im.lore(List.of(
                Component.text("Rozmiar ").color(NamedTextColor.GOLD).append(Component.text("> 127 x 127").color(NamedTextColor.WHITE).decoration(TextDecoration.ITALIC, false))
        ));
        item.setItemMeta(im);
        return item;
    }

    @Contract(pure = true)
    public static @NotNull String getCuboidNamePrefix() {
        return "_cuboids_";
    }

    public void Start() {
        try {
            ModerrkowoPlugin.getInstance().getServer().getPluginManager().registerEvents(new PlaceRemoveCuboid(), ModerrkowoPlugin.getInstance());
            Objects.requireNonNull(ModerrkowoPlugin.getInstance().getCommand("dzialka")).setExecutor(new CuboidCommand());
            Logger.logPluginMessage("Wczytano działki.");
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logPluginMessage("Wystąpił błąd przy tworzeniu działek!");
        }
        NamespacedKey key = new NamespacedKey(ModerrkowoPlugin.getInstance(), "dzialka");
        if (Bukkit.getRecipe(key) == null) {
            try {
                ShapedRecipe recipe = new ShapedRecipe(key, Objects.requireNonNull(getCuboidItem(1)));
                recipe.shape(
                        "ABA",
                        "BDB",
                        "ABA"
                );
                recipe.setIngredient('A', Material.POLISHED_ANDESITE);
                recipe.setIngredient('B', Material.BLAZE_POWDER);
                recipe.setIngredient('D', Material.DIAMOND_BLOCK);
                Bukkit.addRecipe(recipe);
            } catch (Exception ignored) {

            }
        }
    }


}
