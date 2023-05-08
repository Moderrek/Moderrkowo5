package pl.moderr.moderrkowo.core.npc.data.rewards;

import org.bukkit.entity.Player;
import pl.moderr.moderrkowo.core.mysql.User;

public interface IQuestRewardCustom extends IQuestReward{
    String label();
    void Action(Player p, User u);
}
