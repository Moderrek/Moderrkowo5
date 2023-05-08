package pl.moderr.moderrkowo.core.npc.data.tasks;

import org.bukkit.entity.EntityType;

public interface IQuestItemKill extends IQuestItem {

    EntityType getEntityType();

    int getCount();

    @Override
    default String getQuestItemPrefix() {
        return "Zabij";
    }

}
