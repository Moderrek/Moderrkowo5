package pl.moderr.moderrkowo.core.bazar;

import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public enum ItemCategory {
    Materialy(Material.NETHERITE_PICKAXE, "Materiały", NamedTextColor.AQUA),
    Rolnictwo(Material.GOLDEN_HOE, "Rolnictwo", NamedTextColor.GREEN),
    Bloki(Material.STONE, "Budowlane",NamedTextColor.YELLOW),
    Moby(Material.ROTTEN_FLESH, "Moby", NamedTextColor.RED),
    Lowienie(Material.FISHING_ROD, "Łowienie", NamedTextColor.BLUE),
    Drewno(Material.OAK_LOG, "Drewno", NamedTextColor.GOLD),
    Rzadkie(Material.NAME_TAG, "Rzadkie", NamedTextColor.GOLD),
    Jedzenie(Material.COOKED_BEEF, "Jedzenie", NamedTextColor.GREEN);

    @Getter
    private final Material displayMaterial;
    @Getter
    private final String displayName;
    @Getter
    private final TextColor colorTheme;

    ItemCategory(Material displayMaterial, String displayName, TextColor colorTheme) {
        this.displayMaterial = displayMaterial;
        this.displayName = displayName;
        this.colorTheme = colorTheme;
    }
}
