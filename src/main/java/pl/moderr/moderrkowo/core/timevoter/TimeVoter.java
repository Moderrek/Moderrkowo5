package pl.moderr.moderrkowo.core.timevoter;

import lombok.Getter;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.timevoter.utils.VoteCommand;
import pl.moderr.moderrkowo.core.timevoter.utils.VoteEvent;
import pl.moderr.moderrkowo.core.timevoter.utils.VoteUtil;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class TimeVoter {

    private final Set<UUID> noVote = new HashSet<>();
    private final Set<UUID> yesVote = new HashSet<>();

    public int timeToVote;
    public boolean isVoteActive;
    public int voteDelay;
    public Instant lastVote;
    public VoteUtil voteUtil;

    @Getter
    private final Main plugin;

    public TimeVoter(Main plugin) {
        this.plugin = plugin;
        timeToVote = Main.getInstance().getConfig().getInt("time-to-vote");
        voteDelay = Main.getInstance().getConfig().getInt("vote-delay");
        voteUtil = new VoteUtil(this);
        Main.getInstance().getServer().getPluginManager().registerEvents(new VoteEvent(this), Main.getInstance());
        Objects.requireNonNull(Main.getInstance().getCommand("timevote")).setExecutor(new VoteCommand(this));
    }

    public void Disable() {
        yesVote.clear();
        noVote.clear();
    }

    public Set<UUID> getNoVote() {
        return noVote;
    }

    public Set<UUID> getYesVote() {
        return yesVote;
    }
}
