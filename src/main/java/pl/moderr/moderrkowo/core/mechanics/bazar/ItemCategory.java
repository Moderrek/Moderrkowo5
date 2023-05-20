package pl.moderr.moderrkowo.core.mechanics.bazar;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

@AllArgsConstructor
public enum ItemCategory {
    MATERIALY(Material.NETHERITE_PICKAXE, "Materiały", NamedTextColor.AQUA),
    ROLNICTWO(Material.GOLDEN_HOE, "Rolnictwo", TextColor.color(0x74BF41)),
//    BLOKI(Material.STONE, "Budowlane", NamedTextColor.YELLOW),
    MOBY(Material.ROTTEN_FLESH, "Moby", NamedTextColor.RED),
    LOWIENIE(Material.FISHING_ROD, "Łowienie", TextColor.color(0x77A9DF)),
    DREWNO(Material.OAK_LOG, "Drewno", TextColor.color(0xFF641E)),
//    RZADKIE(Material.NAME_TAG, "Rzadkie", TextColor.color(0xFF62BE)),
    // TODO mechanizmy
    JEDZENIE(Material.COOKED_BEEF, "Jedzenie", NamedTextColor.GREEN);

    @Getter
    private final Material displayMaterial;
    @Getter
    private final String displayName;
    @Getter
    private final TextColor colorTheme;

}
