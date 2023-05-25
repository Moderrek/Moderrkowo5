package pl.moderr.moderrkowo.core.services.leaderboard;

import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.util.Logger;

public class LeaderboardManager {

    private final Plugin plugin;

    public LeaderboardManager(@NotNull Plugin plugin) {
        this.plugin = plugin;
        if (!plugin.getServer().getPluginManager().isPluginEnabled("HolographicDisplays")) {
            return;
        }
        Logger.logPluginMessage("Wczytano Leaderboards");
    }

}
