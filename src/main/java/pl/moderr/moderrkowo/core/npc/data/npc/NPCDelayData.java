package pl.moderr.moderrkowo.core.npc.data.npc;

import org.jetbrains.annotations.Contract;

import java.time.Instant;
import java.util.IdentityHashMap;
import java.util.Map;

public class NPCDelayData {

    private Map<String, Instant> timers;

    @Contract(pure = true)
    public NPCDelayData(IdentityHashMap<String, Instant> timers) {
        this.timers = timers;
    }

    public Map<String, Instant> getTimers() {
        return timers;
    }

    public void setTimers(Map<String, Instant> timers) {
        this.timers = timers;
    }
}
