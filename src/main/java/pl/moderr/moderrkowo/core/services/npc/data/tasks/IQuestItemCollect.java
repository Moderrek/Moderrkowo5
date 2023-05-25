package pl.moderr.moderrkowo.core.services.npc.data.tasks;

import org.bukkit.Material;

public interface IQuestItemCollect extends IQuestItem {
    Material getMaterial();

    int getCount();

    @Override
    default String getQuestItemPrefix() {
        return "Zbierz";
    }
}
