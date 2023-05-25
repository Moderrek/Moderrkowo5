package pl.moderr.moderrkowo.core.services.npc.data.npc;

import org.jetbrains.annotations.Contract;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;

public class NPCDelayData {

    private ConcurrentHashMap<String, Instant> timers;

    @Contract(pure = true)
    public NPCDelayData(ConcurrentHashMap<String, Instant> timers) {
        this.timers = timers;
    }

    public ConcurrentHashMap<String, Instant> getTimers() {
        return timers;
    }

    public void setTimers(ConcurrentHashMap<String, Instant> timers) {
        this.timers = timers;
    }
}
