package pl.moderr.moderrkowo.core.services.opening.data;

import pl.moderr.moderrkowo.core.api.util.WeightedList;

import java.util.List;

public interface ModerrCase {
    String name();

    String guiName();

    String description();

    WeightedList<ModerrCaseItem> randomList();

    List<ModerrCaseItem> itemList();
}
