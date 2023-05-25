package pl.moderr.moderrkowo.core.services.npc.data.tasks;

import org.bukkit.Material;

public interface IQuestItemBreak extends IQuestItem {
    Material getMaterial();

    int getCount();

    boolean blockSilk();

    @Override
    default String getQuestItemPrefix() {
        return "Zniszcz";
    }
}