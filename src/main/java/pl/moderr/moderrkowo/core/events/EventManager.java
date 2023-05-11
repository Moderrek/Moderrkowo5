package pl.moderr.moderrkowo.core.events;

import org.bukkit.Bukkit;
import org.bukkit.boss.BossBar;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.events.drop.DropEvent;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.RandomUtils;

import java.util.ArrayList;

public class EventManager {

    DropEvent dropEvent = new DropEvent(Bukkit.getWorld("world"));
    public ArrayList<ModerrEvent> events = new ArrayList<>() {
        {
            add(dropEvent);
        }
    };
    private ModerrEvent randomEvent = null;
    private BossBar eventBossBar = null;

    public EventManager() {
        Bukkit.getPluginManager().registerEvents(dropEvent, ModerrkowoPlugin.getInstance());
        Bukkit.getScheduler().runTaskLater(ModerrkowoPlugin.getInstance(), this::StartEvent, 20 * 60 * 5);
    }

    public void StartEvent() {
        if (Bukkit.getOnlinePlayers().size() >= 5) {
            randomEvent = events.get(RandomUtils.getRandomInt(0, events.size() - 1));
            if (randomEvent.bossBar()) {
                eventBossBar = randomEvent.getBossBar();
            } else {
                eventBossBar = null;
            }
            randomEvent.PrepareEvent();
            Bukkit.broadcastMessage(ColorUtils.color("  "));
            Bukkit.broadcastMessage(ColorUtils.color("  &eRozpoczęto wydarzenie &a" + randomEvent.eventName()));
            String[] lines = randomEvent.description().split("\\n");
            for (String line : lines) {
                Bukkit.broadcastMessage(ColorUtils.color("  &f" + line));
            }
            Bukkit.broadcastMessage(ColorUtils.color("  &fTrwa przez &a" + ChatUtil.getTicksToTime(randomEvent.timeSec() * 20)));
            Bukkit.broadcastMessage("  ");
            randomEvent.Action();
            randomEvent.setActive(true);
        }
    }

    public void EndEvent() {
        Bukkit.broadcastMessage(ColorUtils.color("  &fZakończono wydarzenie, &akolejne za godzinę"));
        if (eventBossBar != null) {
            this.eventBossBar.getPlayers().forEach(player -> this.eventBossBar.removePlayer(player));
            this.eventBossBar = null;
        }
        this.randomEvent = null;
        Bukkit.getScheduler().runTaskLater(ModerrkowoPlugin.getInstance(), this::StartEvent, 20 * 60 * 60);
    }

}
