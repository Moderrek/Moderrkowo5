package pl.moderr.moderrkowo.core;

import io.papermc.lib.PaperLib;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.HexResolver;
import pl.moderr.moderrkowo.core.api.util.Logger;
import pl.moderr.moderrkowo.core.api.util.WorldUtil;
import pl.moderr.moderrkowo.core.events.user.FishingListener;
import pl.moderr.moderrkowo.core.services.CommandService;
import pl.moderr.moderrkowo.core.services.EventHandlerService;
import pl.moderr.moderrkowo.core.services.antylogout.AntyLogoutService;
import pl.moderr.moderrkowo.core.services.bazar.mechanics.BazarManager;
import pl.moderr.moderrkowo.core.services.cuboids.CuboidsManager;
import pl.moderr.moderrkowo.core.services.leaderboard.LeaderboardManager;
import pl.moderr.moderrkowo.core.services.marketplace.RynekManager;
import pl.moderr.moderrkowo.core.services.mysql.MySQL;
import pl.moderr.moderrkowo.core.services.npc.NPCManager;
import pl.moderr.moderrkowo.core.services.opening.ModerrCaseManager;
import pl.moderr.moderrkowo.core.services.tasks.TaskManager;
import pl.moderr.moderrkowo.core.services.timevoter.TimeVoterManager;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Objects;
import java.util.logging.Level;

public final class ModerrkowoPlugin extends JavaPlugin {

    @Getter
    private static ModerrkowoPlugin instance;
    @Getter
    private static MySQL mySQL;
    public final java.util.logging.Logger logger;
    public final FileConfiguration config;
    private final CommandService commandService;
    private final EventHandlerService eventHandlerService;
    @Getter
    private final AntyLogoutService antyLogoutService;
    public FileConfiguration dataConfig;
    public File dataFile = new File(getDataFolder(), "data.yml");
    @Getter
    private TimeVoterManager timeVoter;
    @Getter
    private TaskManager task;
    @Getter
    private BazarManager bazar;
    @Getter
    private RynekManager rynek;
    @Getter
    private ModerrCaseManager caseManager;
    @Getter
    private CuboidsManager cuboid;
    @Getter
    private NPCManager npc;

    public ModerrkowoPlugin() {
        instance = this;
        logger = getLogger();
        config = getConfig();
        mySQL = new MySQL();
        commandService = new CommandService();
        eventHandlerService = new EventHandlerService();
        antyLogoutService = new AntyLogoutService(this);
    }

    public static @NotNull String getServerName() {
        return ColorUtil.color(HexResolver.parseHexString("<gradient:#FD4F1D:#FCE045>Moderrkowo"));
    }

    public static @NotNull String getVersion() {
        return "v1.3.0";
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        Logger.logPluginMessage("Wczytywanie ModerrkowoPlugin");
        // dependencies

        // Citizens
        if (getServer().getPluginManager().getPlugin("Citizens") == null || !Objects.requireNonNull(getServer().getPluginManager().getPlugin("Citizens")).isEnabled()) {
            Logger.logPluginMessage("Brak zainstalowanej biblioteki!");
            getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        // PaperLib
        PaperLib.suggestPaper(this);

        // configs
        LoadPluginConfig();
        LoadDataConfig();

        // worlds
        WorldUtil.TryLoadWorld(config.getString("spawn.world"));

        // services

        // MySQL Service
        mySQL.enable(config.getString("mysql.host"),
                config.getString("mysql.port"),
                config.getString("mysql.database"),
                config.getString("mysql.username"),
                config.getString("mysql.password"));
        // Listeners
        eventHandlerService.Start(this);
        // AntyLogout Service
        antyLogoutService.Start(this);
        // Commands Service
        commandService.Start(this);
        // Case Service
        caseManager = new ModerrCaseManager();
        // ItemShop Service
        bazar = new BazarManager(this, "economy.json");
        // NPC Service
        npc = new NPCManager();
        // Cuboids Service
        cuboid = new CuboidsManager();
        cuboid.Start();
        // Market Service
        rynek = new RynekManager();
        Bukkit.getPluginManager().registerEvents(rynek, this);
        // TimeVoter Service
        timeVoter = new TimeVoterManager(this);
        timeVoter.Start(this);
        // Leaderboard Service
        new LeaderboardManager(this);
        // CustomFishing Service
        Bukkit.getPluginManager().registerEvents(new FishingListener(), this);
        // Task Service
        task = new TaskManager();
        task.Start(this);

        Logger.logPluginMessage(MessageFormat.format("Wczytano w &a{0}ms", System.currentTimeMillis() - start));
    }

    @Override
    public void onDisable() {
        if (task != null) task.Disable(this);
        if (timeVoter != null) timeVoter.Disable(this);
        try {
            rynek.save();
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        try {
            mySQL.disable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        SaveDataYML();
        Logger.logPluginMessage("Wyłączono plugin");
    }

    private void LoadPluginConfig() {
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        getConfig().options().parseComments(true);
        saveConfig();
    }

    private void LoadDataConfig() {
        if (!dataFile.exists()) saveResource("data.yml", false);
        dataFile = new File(getDataFolder(), "data.yml");
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
    }

    private void SaveDataYML() {
        try {
            dataConfig.save(dataFile);
        } catch (IOException e) {
            Logger.logAdminLog("Wystąpił błąd podczas zapisywania data.yml");
        }
    }
}
