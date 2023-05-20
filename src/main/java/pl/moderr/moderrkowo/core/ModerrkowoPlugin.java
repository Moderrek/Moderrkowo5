package pl.moderr.moderrkowo.core;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.commands.ModerrkowoCommand;
import pl.moderr.moderrkowo.core.commands.admin.*;
import pl.moderr.moderrkowo.core.commands.user.*;
import pl.moderr.moderrkowo.core.commands.user.information.DiscordCommand;
import pl.moderr.moderrkowo.core.commands.user.information.RegulaminCommand;
import pl.moderr.moderrkowo.core.commands.user.messages.HelpopCommand;
import pl.moderr.moderrkowo.core.commands.user.messages.MessageCommand;
import pl.moderr.moderrkowo.core.commands.user.messages.ReplyCommand;
import pl.moderr.moderrkowo.core.commands.user.teleportation.*;
import pl.moderr.moderrkowo.core.commands.user.weather.PogodaCommand;
import pl.moderr.moderrkowo.core.customitems.CustomItemsManager;
import pl.moderr.moderrkowo.core.events.EventManager;
import pl.moderr.moderrkowo.core.listeners.*;
import pl.moderr.moderrkowo.core.mechanics.antylogout.AntyLogoutManager;
import pl.moderr.moderrkowo.core.mechanics.bazar.BazarManager;
import pl.moderr.moderrkowo.core.mechanics.cuboids.CuboidsManager;
import pl.moderr.moderrkowo.core.mechanics.leaderboard.LeaderboardManager;
import pl.moderr.moderrkowo.core.mechanics.marketplace.RynekCommand;
import pl.moderr.moderrkowo.core.mechanics.marketplace.RynekManager;
import pl.moderr.moderrkowo.core.mechanics.npc.NPCManager;
import pl.moderr.moderrkowo.core.mechanics.opening.ModerrCaseManager;
import pl.moderr.moderrkowo.core.mechanics.timevoter.TimeVoterManager;
import pl.moderr.moderrkowo.core.mysql.MySQL;
import pl.moderr.moderrkowo.core.tasks.TaskManager;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.HexResolver;
import pl.moderr.moderrkowo.core.utils.Logger;
import pl.moderr.moderrkowo.core.worldmanager.TPWCommand;
import pl.moderr.moderrkowo.core.worldmanager.WorldManager;

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
    public final java.util.logging.Logger logger = getLogger();
    // Configs
    public final FileConfiguration config = getConfig();
    public FileConfiguration dataConfig;
    public File dataFile = new File(getDataFolder(), "data.yml");
    // Reworked
    @Getter
    private TimeVoterManager timeVoter;
    @Getter
    private AntyLogoutManager antyLogout;
    @Getter
    private TaskManager task;

    // Server Mechanics
    @Getter
    private BazarManager bazar;
    // TODO next update
    @Getter
    private EventManager event;
    // Kit
    // Warp
    // Asynchroniczny rynek

    // TODO rework (w kolejności)
    @Getter
    private RynekManager rynek;
    @Getter
    private ModerrCaseManager dropCase;
    @Getter
    private CuboidsManager cuboid;
    @Getter
    private NPCManager npc;

    public static @NotNull String getServerName() {
        return ColorUtils.color(HexResolver.parseHexString("<gradient:#FD4F1D:#FCE045>Moderrkowo"));
    }

    public static @NotNull String getVersion() {
        return "v1.2.2";
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        Logger.logPluginMessage("Wczytywanie ModerrkowoPlugin");
        if (getServer().getPluginManager().getPlugin("Citizens") == null || !Objects.requireNonNull(getServer().getPluginManager().getPlugin("Citizens")).isEnabled()) {
            Logger.logPluginMessage("Brak zainstalowanej biblioteki!");
            getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        // Config
        LoadPluginConfig();
        LoadDataConfig();
        // MySQL
        mySQL = new MySQL();
        mySQL.enable(config.getString("mysql.host"),
                config.getString("mysql.port"),
                config.getString("mysql.database"),
                config.getString("mysql.username"),
                config.getString("mysql.password"));
        // Listeners
        initializeListeners();
        // AntyLogout
        initializeAntyLogout();
        // Commands
        initializeCommands();
        // Otwieranie skrzynek
        Bukkit.getPluginManager().registerEvents(new CustomItemsManager(), this);
        dropCase = new ModerrCaseManager();
        //
        bazar = new BazarManager(this, "economy.json");
        // Questy
        npc = new NPCManager();
        Logger.logPluginMessage("Wczytano NPC");
        // Spawn
        WorldManager.TryLoadWorld(config.getString("spawn.world"));
        // Działki
        cuboid = new CuboidsManager();
        cuboid.Start();
        Logger.logPluginMessage("Wczytano działki");
        // Rynek
        rynek = new RynekManager();
        Bukkit.getPluginManager().registerEvents(rynek, this);
        // MiniEvents
//        eventManager = new EventManager();
        // AutoMessage
        initializeAutoMessage();

        timeVoter = new TimeVoterManager(this);
        timeVoter.Start(this);
        new LeaderboardManager(this);

        Bukkit.getPluginManager().registerEvents(new FishingListener(), this);

        task = new TaskManager();
        task.Start(this);
        Logger.logPluginMessage(MessageFormat.format("Wczytano w &a{0}ms", System.currentTimeMillis() - start));
    }

    @Override
    public void onDisable() {
        if(task != null){
            task.Disable(this);
        }
        if (timeVoter != null) {
            timeVoter.Disable(this);
        }
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

    private void initializeAntyLogout() {
        antyLogout = new AntyLogoutManager(this);
        antyLogout.Start(this);
        Logger.logPluginMessage("Wczytano AntyLogout");
    }

    private void LoadPluginConfig() {
        //<editor-fold> Config
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        getConfig().options().parseComments(true);
        saveConfig();
        Logger.logPluginMessage("Wczytano config");
        //</editor-fold> Config
    }

    public void initializeCommands() {
        Objects.requireNonNull(getCommand("asklep")).setExecutor(new ASklepCommand());
        Objects.requireNonNull(getCommand("moderrkowo")).setExecutor(new ModerrkowoCommand(this));
        Objects.requireNonNull(getCommand("playerid")).setExecutor(new PlayerIDCommand());
        Objects.requireNonNull(getCommand("ahelpop")).setExecutor(new AHelpopCommand());
        Objects.requireNonNull(getCommand("chat")).setExecutor(new ChatCommand());
        Objects.requireNonNull(getCommand("endersee")).setExecutor(new EnderseeCommand());
        Objects.requireNonNull(getCommand("fly")).setExecutor(new FlyCommand());
        Objects.requireNonNull(getCommand("gamemode")).setExecutor(new GameModeCommand());
        Objects.requireNonNull(getCommand("invsee")).setExecutor(new InvseeCommand());
        Objects.requireNonNull(getCommand("mban")).setExecutor(new MBanCommand());
        Objects.requireNonNull(getCommand("mkick")).setExecutor(new MKickCommand());
        Objects.requireNonNull(getCommand("nazwa")).setExecutor(new NazwaCommand());
        Objects.requireNonNull(getCommand("say")).setExecutor(new SayCommand());
        Objects.requireNonNull(getCommand("sendalert")).setExecutor(new SendAlertCommand());
        Objects.requireNonNull(getCommand("vanish")).setExecutor(new VanishCommand());
        Objects.requireNonNull(getCommand("villager")).setExecutor(new ANPCCommand());
        Objects.requireNonNull(getCommand("setspawn")).setExecutor(new SetSpawnCommand());
        Objects.requireNonNull(getCommand("saveusers")).setExecutor(new SaveUsersCommand());
        Objects.requireNonNull(getCommand("holo")).setExecutor(new HoloCommand());
        Objects.requireNonNull(getCommand("abank")).setExecutor(new ABankCommand());
        Objects.requireNonNull(getCommand("arank")).setExecutor(new ARankCommand());
        Objects.requireNonNull(getCommand("tpw")).setExecutor(new TPWCommand());
        Objects.requireNonNull(getCommand("discord")).setExecutor(new DiscordCommand(config.getString("discord-link")));
        Objects.requireNonNull(getCommand("helpop")).setExecutor(new HelpopCommand());
        Objects.requireNonNull(getCommand("message")).setExecutor(new MessageCommand());
        Objects.requireNonNull(getCommand("reply")).setExecutor(new ReplyCommand());
        Objects.requireNonNull(getCommand("tpr")).setExecutor(new TPACommand());
        Objects.requireNonNull(getCommand("tpaccept")).setExecutor(new TPAccept());
        Objects.requireNonNull(getCommand("tpdeny")).setExecutor(new TPDeny());
        Objects.requireNonNull(getCommand("pogoda")).setExecutor(new PogodaCommand());
        Objects.requireNonNull(getCommand("regulamin")).setExecutor(new RegulaminCommand());
        Objects.requireNonNull(getCommand("spawn")).setExecutor(new SpawnCommand());
        Objects.requireNonNull(getCommand("wyplac")).setExecutor(new WithdrawCommand());
        Objects.requireNonNull(getCommand("portfel")).setExecutor(new PortfelCommand());
        Objects.requireNonNull(getCommand("sethome")).setExecutor(new SetHomeCommand());
        Objects.requireNonNull(getCommand("home")).setExecutor(new HomeCommand());
        Objects.requireNonNull(getCommand("acheststorage")).setExecutor(new AChestStorageCommand());
        Objects.requireNonNull(getCommand("przelej")).setExecutor(new PrzelejCommand());
        Objects.requireNonNull(getCommand("rynek")).setExecutor(new RynekCommand());
        Objects.requireNonNull(getCommand("przedmioty")).setExecutor(new PrzedmiotyCommand(this));
        Objects.requireNonNull(getCommand("sidebar")).setExecutor(new SidebarCommand());
        Objects.requireNonNull(getCommand("poziom")).setExecutor(new PoziomCommand());
        Objects.requireNonNull(getCommand("poradnik")).setExecutor(new PoradnikCommand());
        Objects.requireNonNull(getCommand("odbierz")).setExecutor(new OdbierzCommand(this));
        Objects.requireNonNull(getCommand("sklep")).setExecutor(new SklepCommand());
        Objects.requireNonNull(getCommand("crafting")).setExecutor(new CraftingCommand());
        Objects.requireNonNull(getCommand("enderchest")).setExecutor(new EnderChestCommand());
        Objects.requireNonNull(getCommand("delhome")).setExecutor(new DelHomeCommand());
        Logger.logPluginMessage("Wczytano komendy");
    }

    public void initializeListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new ChatListener(), this);
        Bukkit.getPluginManager().registerEvents(new MotdListener(), this);
        Bukkit.getPluginManager().registerEvents(new JoinQuitListener(), this);
        Bukkit.getPluginManager().registerEvents(new CropBreakListener(), this);
        Bukkit.getPluginManager().registerEvents(new PogodaCommand(), this);
        Bukkit.getPluginManager().registerEvents(new TNTListener(), this);
        Bukkit.getPluginManager().registerEvents(new WithdrawCommand(), this);
        Bukkit.getPluginManager().registerEvents(new PortalListener(), this);
        Bukkit.getPluginManager().registerEvents(new KopanieListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getPluginManager().registerEvents(new SpawnerPlaceListener(), this);
        new AnimalBreedingListener(this);
        Logger.logPluginMessage("Wczytano listenery");
    }

    //<editor-fold> YML
    private void LoadDataConfig() {
        //<editor-fold> Data.yml
        if (!dataFile.exists()) {
            saveResource("data.yml", false);
        }
        dataFile = new File(getDataFolder(), "data.yml");
        dataConfig = YamlConfiguration.loadConfiguration(dataFile);
        Logger.logPluginMessage("Wczytano rekord graczy");
        //</editor-fold> Data.yml
    }

    private void SaveDataYML() {
        try {
            dataConfig.save(dataFile);
            Logger.logAdminLog("Zapisano data.yml");
        } catch (IOException e) {
            Logger.logAdminLog("Wystąpił błąd podczas zapisywania data.yml");
        }
    }

    //</editor-fold>
    public void initializeAutoMessage() {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(ChatUtil.centerText("⛃ Moderrkowo ⛃").replace("⛃ Moderrkowo ⛃", ColorUtils.color("&e⛃ &6Moderrkowo &e⛃")));
            Bukkit.broadcastMessage(ChatUtil.centerText("▪ Tutaj zakupisz rangi i wesprzesz nasz serwer").replace("▪ Tutaj zakupisz rangi i wesprzesz nasz serwer", ColorUtils.color("&8▪ &7▪ Tutaj zakupisz rangi i wesprzesz nasz serwer")));
            Bukkit.broadcastMessage(ChatUtil.centerText("▪ Zobacz rangi pod /sklep").replace("▪ Zobacz rangi pod /sklep", ColorUtils.color("&8▪ &7Zobacz rangi pod &e/sklep")));
            Bukkit.broadcastMessage(" ");
        }, 0, 20 * 60 * 30);
    }
}
