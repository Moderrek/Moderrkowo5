package pl.moderr.moderrkowo.core.mechanics.bazar;

import lombok.Data;

@Data
public class BazarUIData {

    private final UICallback callback;
    private ItemCategory selectedCategory = ItemCategory.MATERIALY;

}
