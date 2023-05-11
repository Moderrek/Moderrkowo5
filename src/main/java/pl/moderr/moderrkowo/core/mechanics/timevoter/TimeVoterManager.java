package pl.moderr.moderrkowo.core.mechanics.timevoter;

import lombok.Data;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.mechanics.ServerMechanics;
import pl.moderr.moderrkowo.core.mechanics.timevoter.utils.VoteCommand;
import pl.moderr.moderrkowo.core.mechanics.timevoter.utils.VoteEvent;
import pl.moderr.moderrkowo.core.mechanics.timevoter.utils.VoteUtil;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

@Data
public class TimeVoterManager implements ServerMechanics {

    private final Set<UUID> yesVote;
    private final Set<UUID> noVote;
    private final ModerrkowoPlugin plugin;
    private int timeToVote;
    private boolean isVoteActive;
    private int voteDelay;
    private Instant lastVote;
    private VoteUtil util;
    private VoteEvent eventHandler;
    private VoteCommand command;

    public TimeVoterManager(@NotNull ModerrkowoPlugin plugin) {
        this.plugin = plugin;
        yesVote = new ConcurrentSkipListSet<>();
        noVote = new ConcurrentSkipListSet<>();
        timeToVote = ModerrkowoPlugin.getInstance().getConfig().getInt("time-to-vote");
        voteDelay = ModerrkowoPlugin.getInstance().getConfig().getInt("vote-delay");
        util = new VoteUtil(this);
        eventHandler = new VoteEvent(this);
        command = new VoteCommand(this);
    }

    @Override
    public void Start(@NotNull ModerrkowoPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(eventHandler, ModerrkowoPlugin.getInstance());
        Objects.requireNonNull(ModerrkowoPlugin.getInstance().getCommand("timevote")).setExecutor(command);
        Logger.logPluginMessage("Wczytano TimeVoter");
    }

    @Override
    public void Disable(ModerrkowoPlugin plugin) {
        yesVote.clear();
        noVote.clear();
    }
}
