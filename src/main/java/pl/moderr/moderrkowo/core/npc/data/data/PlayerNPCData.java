package pl.moderr.moderrkowo.core.npc.data.data;

import java.util.HashMap;

public class PlayerNPCData {

    private String npcId;
    private int questIndex;
    private boolean activeQuest;
    private HashMap<String, Integer> QuestItemData;

    public PlayerNPCData(String npcId, int questIndex, boolean activeQuest, HashMap<String, Integer> questItemData) {
        this.npcId = npcId;
        this.questIndex = questIndex;
        this.activeQuest = activeQuest;
        this.QuestItemData = questItemData;
    }

    public String getNpcId() {
        return npcId;
    }

    public void setNpcId(String npcId) {
        this.npcId = npcId;
    }

    public boolean isActiveQuest() {
        return activeQuest;
    }

    public void setActiveQuest(boolean activeQuest) {
        this.activeQuest = activeQuest;
    }

    public int getQuestIndex() {
        return questIndex;
    }

    public void setQuestIndex(int questIndex) {
        this.questIndex = questIndex;
    }

    public HashMap<String, Integer> getQuestItemData() {
        return QuestItemData;
    }

    public void setQuestItemData(HashMap<String, Integer> questItemData) {
        QuestItemData = questItemData;
    }
}
