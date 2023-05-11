package pl.moderr.moderrkowo.core.mechanics.npc.data.rewards;

import pl.moderr.moderrkowo.core.user.level.LevelCategory;

public interface IQuestRewardExp extends IQuestReward {
    double exp();

    LevelCategory category();
}
