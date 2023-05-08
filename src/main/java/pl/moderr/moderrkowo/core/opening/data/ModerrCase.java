package pl.moderr.moderrkowo.core.opening.data;

import pl.moderr.moderrkowo.core.utils.WeightedList;

import java.util.ArrayList;

public interface ModerrCase {
    String name();
    String guiName();
    String description();
    WeightedList<ModerrCaseItem> randomList();
    ArrayList<ModerrCaseItem> itemList();
}
