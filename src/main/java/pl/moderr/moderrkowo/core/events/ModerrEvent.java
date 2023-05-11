package pl.moderr.moderrkowo.core.events;

import org.bukkit.boss.BossBar;

public interface ModerrEvent {

    String description();

    String eventName();

    int timeSec();

    boolean broadcast();

    boolean bossBar();

    default BossBar getBossBar() {
        return null;
    }

    void PrepareEvent();

    void Action();

    void EndEvent();

    boolean getActive();

    void setActive(boolean active);

}
