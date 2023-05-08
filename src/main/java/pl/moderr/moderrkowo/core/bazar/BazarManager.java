package pl.moderr.moderrkowo.core.bazar;

import lombok.Getter;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.mysql.User;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BazarManager{

    @Getter
    private final Main plugin;
    @Getter
    private final BazarCommand command;
    @Getter
    private final BazarListener listener;
    @Getter
    private final BazarInventory inventory;
    @Getter
    private final Map<UUID, BazarGUICache> guiCache;

    public BazarManager(@NotNull Main plugin){
        this.plugin = plugin;
        this.command = new BazarCommand(this);
        PluginCommand pluginCommand = plugin.getCommand("bazar");
        if(pluginCommand == null){
            Logger.logAdminLog("&cKomenda /bazar nie może zostać zarejestrowana!");
        }else{
            pluginCommand.setExecutor(command);
        }
        this.listener = new BazarListener(this);
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        this.inventory = new BazarInventory(this);
        this.guiCache = new HashMap<>();
    }

    public void openInventory(User user, Player player, ItemCategory category){
        if(player == null){
            return;
        }
        final UUID uuid = player.getUniqueId();
        final BazarGUICallback bazarGUICallback = new BazarGUICallback(this, user, player);
        final BazarGUICache cache = new BazarGUICache(bazarGUICallback);
        cache.setSelectedCategory(category);
        guiCache.put(uuid, cache);
        Inventory inv = inventory.create(cache.getSelectedCategory());
        bazarGUICallback.onOpen();
        player.openInventory(inv);
    }

}
