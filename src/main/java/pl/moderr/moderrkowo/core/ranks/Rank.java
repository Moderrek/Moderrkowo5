package pl.moderr.moderrkowo.core.ranks;

import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public enum Rank {
    None("","", NamedTextColor.WHITE, Material.AIR, 0, 0, new ArrayList<>()),
    Zelazo("Żelazo", "Ż", NamedTextColor.WHITE, Material.IRON_BLOCK, 1, 5, Arrays.asList()),
    Zloto("Żelazo", "Ż", NamedTextColor.YELLOW, Material.GOLD_BLOCK, 2, 10, Arrays.asList()),
    Diament("Żelazo", "Ż", TextColor.fromCSSHexString("#2980B9"), Material.DIAMOND_BLOCK, 3, 25, Arrays.asList()),
    Emerald("Emerald", "E", NamedTextColor.GREEN, Material.EMERALD_BLOCK, 4, 50, Arrays.asList());

    public final String name;
    public final String shortName;
    public final TextColor color;
    public final Material material;
    public final int priority;
    public final double cost;
    public final List<String> bonus;
    Rank(String name, String shortName, TextColor color, Material material, int priority, double cost, List<String> bonus){
        this.name = name;
        this.shortName = shortName;
        this.color = color;
        this.material = material;
        this.priority = priority;
        this.cost = cost;
        this.bonus = bonus;
    }

}
