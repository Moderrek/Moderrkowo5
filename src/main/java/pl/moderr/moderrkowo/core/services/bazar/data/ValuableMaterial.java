package pl.moderr.moderrkowo.core.services.bazar.data;

import lombok.Data;
import org.bukkit.Material;

@Data
public class ValuableMaterial {

    private final Material material;
    private final double buyCost;
    private final double sellCost;
    private final ItemCategory category;

    public boolean canSell() {
        return sellCost > 0;
    }

    public boolean canBuy() {
        return buyCost > 0;
    }

}
