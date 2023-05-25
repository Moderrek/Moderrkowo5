package pl.moderr.moderrkowo.core.services.npc.data.tasks;

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

