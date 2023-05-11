package pl.moderr.moderrkowo.core.mechanics.bazar;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BazarManager {

    @Getter
    private final ModerrkowoPlugin plugin;
    @Getter
    private final BazarCommand command;
    @Getter
    private final BazarListener listener;
    @Getter
    private final BazarInventory inventory;
    @Getter
    private final Map<UUID, BazarUIData> guiCache;
    @Getter
    private final Map<Material, ValuableMaterial> materialValue;

    private final File economyFile;

    public List<ValuableMaterial> getByCategory(ItemCategory category) {
        List<ValuableMaterial> filtered = new ArrayList<>();
        for (ValuableMaterial valuableMaterial : materialValue.values()) {
            if (valuableMaterial.getCategory().equals(category))
                filtered.add(valuableMaterial);
        }
        return filtered;
    }

    /**
     * @param plugin
     * @param economyFilePath
     */
    public BazarManager(@NotNull ModerrkowoPlugin plugin, String economyFilePath) {
        // Create instances
        this.plugin = plugin;
        this.guiCache = new HashMap<>();
        this.materialValue = new ConcurrentHashMap<>();
        this.inventory = new BazarInventory(this);
        this.listener = new BazarListener(this);
        this.command = new BazarCommand(this);
        // Register listener
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
        // Register command
        PluginCommand pluginCommand = plugin.getCommand("bazar");
        if (pluginCommand == null) {
            Logger.logAdminLog("&cKomenda /bazar nie może zostać zarejestrowana!");
        } else {
            pluginCommand.setExecutor(command);
        }
        // Load economy values
        economyFile = new File(plugin.getDataFolder(), economyFilePath);
        if (!economyFile.exists()) {
            Logger.logAdminLog("Nie można załadować ekonomii! Plik nie jest utworzony!");
        } else {
            try {
                loadEconomy(economyFile);
            } catch (IOException e) {
                e.printStackTrace();
                Logger.logAdminLog("Nie można załadować ekonomii! " + e.getLocalizedMessage());
            }
        }
    }

    public void reloadEconomy() throws IOException {
        loadEconomy(economyFile);
    }

    /**
     * @param file
     * @throws IOException
     */
    private void loadEconomy(@NotNull File file) throws IOException {
        materialValue.clear();
        final String content = Files.readString(file.toPath(), StandardCharsets.UTF_8);
        final JsonObject entries = (JsonObject) JsonParser.parseString(content);

        for (ItemCategory categoryEntry : ItemCategory.values()) {
            final String categoryEntryName = categoryEntry.toString();
            if (!entries.has(categoryEntryName)) continue;
            JsonArray array = entries.getAsJsonArray(categoryEntryName);
            for (int i = 0; i < array.size(); i += 1) {
                try {
                    final JsonElement object = array.get(i);
                    final JsonObject valuableObject = object.getAsJsonObject();
                    final Material material = Material.getMaterial(valuableObject.get("material").getAsString());
                    final double buyCost = valuableObject.get("buyCost").getAsDouble();
                    final double sellCost = valuableObject.get("sellCost").getAsDouble();
                    if (sellCost > buyCost && buyCost > 0) {
                        continue;
                    }
                    final ValuableMaterial valuableMaterial = new ValuableMaterial(material, buyCost, sellCost, categoryEntry);
                    materialValue.put(material, valuableMaterial);
                    Logger.logNpcMessage(materialValue.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * @param user
     * @param category
     */
    public void openInventory(@NotNull User user, @NotNull ItemCategory category) {
        final Player player = user.getPlayer();
        if (player == null) return;
        final BazarUIEvent eventsHandler = new BazarUIEvent(this, user, player);
        final BazarUIData dataHandler = new BazarUIData(eventsHandler);
        dataHandler.setSelectedCategory(category);
        final UUID uuid = player.getUniqueId();
        guiCache.put(uuid, dataHandler);
        final Inventory inv = inventory.create(dataHandler.getSelectedCategory(), user);
        eventsHandler.onOpen();
        player.openInventory(inv);
    }

}
