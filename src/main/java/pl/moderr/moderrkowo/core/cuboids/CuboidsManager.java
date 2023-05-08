package pl.moderr.moderrkowo.core.cuboids;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.cuboids.commands.CuboidCommand;
import pl.moderr.moderrkowo.core.cuboids.listeners.PlaceRemoveCuboid;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.util.Objects;

public class CuboidsManager {

    private static final Material cuboidMaterial = Material.LODESTONE;
    private static final String cuboidDisplayName = ColorUtils.color("&bBlok działki");

    public static ItemStack getCuboidItem(int count) {
        if (count > 64 || count <= 0) {
            return null;
        }
        ItemStack item = new ItemStack(cuboidMaterial, count);
        ItemMeta im = item.getItemMeta();
        im.setDisplayName(cuboidDisplayName);
        item.setItemMeta(im);
        return item;
    }

    @Contract(pure = true)
    public static @NotNull String getCuboidNamePrefix() {
        return "_cuboids_";
    }

    public void Start() {
        try {
            Main.getInstance().getServer().getPluginManager().registerEvents(new PlaceRemoveCuboid(), Main.getInstance());
            Objects.requireNonNull(Main.getInstance().getCommand("dzialka")).setExecutor(new CuboidCommand());
            Logger.logPluginMessage("Wczytano działki.");
        } catch (Exception e) {
            e.printStackTrace();
            Logger.logPluginMessage("Wystąpił błąd przy tworzeniu działek!");
        }
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "dzialka");
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
