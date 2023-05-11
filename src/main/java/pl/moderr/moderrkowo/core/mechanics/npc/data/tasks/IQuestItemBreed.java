package pl.moderr.moderrkowo.core.mechanics.npc.data.tasks;

import pl.moderr.moderrkowo.core.listeners.BreedingAnimal;

public interface IQuestItemBreed extends IQuestItem {
    BreedingAnimal getEntityType();

    int getCount();

    @Override
    default String getQuestItemPrefix() {
        return "Rozmnóż";
    }

}
