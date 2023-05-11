package pl.moderr.moderrkowo.core.mechanics.npc.data.tasks;

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