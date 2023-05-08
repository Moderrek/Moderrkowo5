package pl.moderr.moderrkowo.core.bazar;

import lombok.Data;

@Data
public class BazarGUICache {

    private final GUICallback callback;
    private ItemCategory selectedCategory = ItemCategory.Materialy;

}
