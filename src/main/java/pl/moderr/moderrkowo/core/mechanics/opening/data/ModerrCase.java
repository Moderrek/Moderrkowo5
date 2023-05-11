package pl.moderr.moderrkowo.core.mechanics.opening.data;

import pl.moderr.moderrkowo.core.utils.WeightedList;

import java.util.List;

public interface ModerrCase {
    String name();

    String guiName();

    String description();

    WeightedList<ModerrCaseItem> randomList();

    List<ModerrCaseItem> itemList();
}
