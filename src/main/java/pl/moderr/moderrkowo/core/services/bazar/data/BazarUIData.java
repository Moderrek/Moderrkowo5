package pl.moderr.moderrkowo.core.services.bazar.data;

import lombok.Data;
import pl.moderr.moderrkowo.core.services.bazar.BazarConstants;
import pl.moderr.moderrkowo.core.services.bazar.mechanics.GUICallback;

@Data
public class BazarUIData {

    private final GUICallback callback;
    private ItemCategory selectedCategory = BazarConstants.DEFAULT_CATEGORY;
    private double income = 0;
    private double outcome = 0;

}
