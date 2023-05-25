package pl.moderr.moderrkowo.core.services.npc.data.quest;

import org.jetbrains.annotations.Contract;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.services.npc.data.rewards.IQuestReward;
import pl.moderr.moderrkowo.core.services.npc.data.tasks.IQuestItem;

import java.util.ArrayList;

public class Quest {

    private final String name;
    private final String description;
    private final QuestDifficulty difficulty;
    private final ArrayList<IQuestItem> questItems;
    private final ArrayList<IQuestReward> rewardItems;

    @Contract(pure = true)
    public Quest(String name, String description, QuestDifficulty difficulty, ArrayList<IQuestItem> questItems, ArrayList<IQuestReward> rewardItems) {
        this.name = name;
        this.description = description;
        this.difficulty = difficulty;
        this.questItems = questItems;
        this.rewardItems = rewardItems;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<IQuestItem> getQuestItems() {
        return questItems;
    }

    public ArrayList<IQuestReward> getRewardItems() {
        return rewardItems;
    }

    public String getDifficulty() {
        switch (difficulty) {
            case EASY:
                return ColorUtil.color("&aŁATWY");
            case NORMAL:
                return ColorUtil.color("&9NORMALNY");
            case HARD:
                return ColorUtil.color("&eCIĘŻKI");
            case TRYHARD:
                return ColorUtil.color("&cTRYHARD");
        }
        return ColorUtil.color("&8Nieznany");
    }

}
