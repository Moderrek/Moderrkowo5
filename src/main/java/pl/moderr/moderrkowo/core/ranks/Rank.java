package pl.moderr.moderrkowo.core.ranks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

import java.util.List;

@AllArgsConstructor
@Getter
public enum Rank {
    None("", "", NamedTextColor.WHITE, Material.AIR, 0, 0, 1, 1, List.of()),
    Zelazo("Żelazo", "Ż", NamedTextColor.WHITE, Material.IRON_BLOCK, 1, 5, 3, 1.5, List.of()),
    Zloto("Złoto", "Z", NamedTextColor.YELLOW, Material.GOLD_BLOCK, 2, 10, 3, 1.5, List.of()),
    Diament("Diament", "D", TextColor.fromCSSHexString("#2980B9"), Material.DIAMOND_BLOCK, 3, 25, 5, 2, List.of()),
    Emerald("Emerald", "E", NamedTextColor.GREEN, Material.EMERALD_BLOCK, 4, 50, 8, 3, List.of());

    private final String name;
    private final String shortName;
    private final TextColor color;
    private final Material material;
    private final int priority;
    private final double cost;
    private final double incomeMultiplierTime;
    private final double playerLevelMultiplier;
    private final List<String> bonus;

}
