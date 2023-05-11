package pl.moderr.moderrkowo.core.mechanics.npc.data.tasks;

import org.bukkit.Material;

public interface IQuestItemGive extends IQuestItem {

    Material getMaterial();

    int getCount();

    @Override
    default String getQuestItemPrefix() {
        return "Przynieś";
    }

}
