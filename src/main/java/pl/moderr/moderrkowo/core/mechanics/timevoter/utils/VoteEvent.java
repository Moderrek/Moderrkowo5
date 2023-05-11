package pl.moderr.moderrkowo.core.mechanics.timevoter.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.mechanics.timevoter.TimeVoterManager;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class VoteEvent implements Listener {

    private final TimeVoterManager timeVoter;

    public VoteEvent(TimeVoterManager timeVoter) {
        this.timeVoter = timeVoter;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void sleepVote(@NotNull PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        if (Bukkit.getOnlinePlayers().size() == 1) {
            return;
        }
        if (event.isCancelled()) {
            return;
        }
        if (!timeVoter.isVoteActive()) {
            if (!timeVoter.getUtil().isInMainWorld(player)) {
                return;
            }
            timeVoter.getUtil().StartVote(player);
        } else {
            if (!timeVoter.getYesVote().contains(player.getUniqueId())) {
                timeVoter.getYesVote().add(player.getUniqueId());
                player.sendMessage(ColorUtils.color("&aPonieważ zdecydowałeś się spać, rozpoczynasz głosowanie"));
            }
        }

    }
}
