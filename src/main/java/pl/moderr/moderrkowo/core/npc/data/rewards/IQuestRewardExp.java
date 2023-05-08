package pl.moderr.moderrkowo.core.npc.data.rewards;

import pl.moderr.moderrkowo.core.mysql.LevelCategory;

public interface IQuestRewardExp extends IQuestReward {
    double exp();

    LevelCategory category();
}
