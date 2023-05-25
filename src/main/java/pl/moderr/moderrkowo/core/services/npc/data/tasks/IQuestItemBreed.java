package pl.moderr.moderrkowo.core.services.npc.data.tasks;

import pl.moderr.moderrkowo.core.events.user.quest.BreedingAnimal;

public interface IQuestItemBreed extends IQuestItem {
    BreedingAnimal getEntityType();

    int getCount();

    @Override
    default String getQuestItemPrefix() {
        return "Rozmnóż";
    }

}
