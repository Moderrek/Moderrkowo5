package pl.moderr.moderrkowo.core.mechanics.npc.data.tasks;


public interface IQuestItemFishingRod extends IQuestItem {
    int getCount();

    @Override
    default String materialName() {
        return "razy";
    }

    @Override
    default String getQuestItemPrefix() {
        return "Zarzuć wędkę";
    }
}
