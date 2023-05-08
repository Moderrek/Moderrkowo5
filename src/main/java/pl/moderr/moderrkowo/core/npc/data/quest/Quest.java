package pl.moderr.moderrkowo.core.npc.data.quest;

import org.jetbrains.annotations.Contract;
import pl.moderr.moderrkowo.core.npc.data.rewards.IQuestReward;
import pl.moderr.moderrkowo.core.npc.data.tasks.IQuestItem;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

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
    public String getDifficulty(){
        switch (difficulty){
            case EASY:
                return ColorUtils.color("&aŁATWY");
            case NORMAL:
                return ColorUtils.color("&9NORMALNY");
            case HARD:
                return ColorUtils.color("&eCIĘŻKI");
            case TRYHARD:
                return ColorUtils.color("&cTRYHARD");
        }
        return ColorUtils.color("&8Nieznany");
    }

}
