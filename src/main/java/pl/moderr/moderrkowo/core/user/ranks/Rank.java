package pl.moderr.moderrkowo.core.user.ranks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

import java.util.List;

@AllArgsConstructor
@Getter
public enum Rank {
    None("",
            "",
            NamedTextColor.WHITE,
            Material.AIR,
            0,
            0.0D,
            1.0D,
            1.0D,
            1.5D,
            3.0D,
            List.of()),
    Zelazo("Żelazo",
            "Ż",
            NamedTextColor.WHITE,
            Material.IRON_BLOCK,
            1,
            5.0D,
            3.0D,
            1.5D,
            0.7D,
            2.0D,
            List.of()),
    Zloto("Złoto",
            "Z",
            NamedTextColor.YELLOW,
            Material.GOLD_BLOCK,
            2,
            10.0D,
            3.0D,
            1.5D,
            0.5D,
            2.0D,
            List.of()),
    Diament("Diament",
            "D",
            TextColor.fromCSSHexString("#2980B9"),
            Material.DIAMOND_BLOCK,
            3,
            25.0D,
            5.0D,
            2.0D,
            0.3D,
            1.0D,
            List.of()),
    Emerald("Emerald",
            "E",
            NamedTextColor.GREEN,
            Material.EMERALD_BLOCK,
            4,
            50.0D,
            8.0D,
            3.0D,
            0.2D,
            0.5D,
            List.of());

    private final String name;
    private final String shortName;
    private final TextColor color;
    private final Material material;
    private final int priority;
    private final double cost;
    private final double incomeMultiplierTime;
    private final double playerLevelMultiplier;
    private final double chatSecondsDelay;
    private final double commandSecondsDelay;
    private final List<RankBenefit> bonus;

}
