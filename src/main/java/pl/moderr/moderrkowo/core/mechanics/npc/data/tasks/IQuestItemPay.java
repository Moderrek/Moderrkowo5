package pl.moderr.moderrkowo.core.mechanics.npc.data.tasks;

public interface IQuestItemPay extends IQuestItem {

    int getCount();

    @Override
    default String getQuestItemPrefix() {
        return "Zapłać";
    }

    @Override
    default String materialName() {
        return "";
    }
}

