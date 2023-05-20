package pl.moderr.moderrkowo.core.tasks;

import lombok.val;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.mechanics.ServerMechanics;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class TaskManager implements ServerMechanics {

    private final Set<Integer> tasksId = new HashSet<>();

    private void addRepeatingTask(@NotNull Plugin plugin, Runnable task, long ticksPeriod){
        int id = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, task, ticksPeriod, ticksPeriod);
        tasksId.add(id);
        Logger.logPluginMessage("Nowy task id = " + id);
    }

    private long minutes(long minutes){
        return 20L * 60L * minutes;
    }

    @Override
    public void Start(ModerrkowoPlugin plugin) {
        addRepeatingTask(plugin, new TimeIncomeTask(), minutes(15));
        addRepeatingTask(plugin, new AutoMessageTask(plugin, Arrays.asList(
                "Aby przenieść się do znajomego użyj /tpa <nick>",
                "Zgłoś błąd/problem na /helpop",
                "Zadania jak i sklepy znajdziesz na /spawn",
                "Dołącz do naszego DISCORD'a /discord",
                "Pieniądze można wypłacić za pomocą /wyplac <kwota>",
                "Skrzynie mozesz otwierać na spawnie",
                "Jest brzydka pogoda? Stwórz głosowanie! /pogoda",
                "Kres został otwarty!"
        )), minutes(8));
    }

    @Override
    public void Disable(@NotNull ModerrkowoPlugin plugin) {
        final BukkitScheduler scheduler = plugin.getServer().getScheduler();
        for(val taskId : tasksId){
            scheduler.cancelTask(taskId);
            Logger.logPluginMessage("Zabito task id = " + taskId);
        }
    }
}
