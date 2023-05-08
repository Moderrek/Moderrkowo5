package pl.moderr.moderrkowo.core;

import me.catcoder.sidebar.ProtocolSidebar;
import me.catcoder.sidebar.Sidebar;
import me.catcoder.sidebar.text.TextIterators;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.antylogout.AntyLogoutManager;
import pl.moderr.moderrkowo.core.automessage.ModerrkowoAutoMessage;
import pl.moderr.moderrkowo.core.bazar.BazarManager;
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
import pl.moderr.moderrkowo.core.cuboids.CuboidsManager;
import pl.moderr.moderrkowo.core.customitems.CustomItemsManager;
import pl.moderr.moderrkowo.core.economy.PortfelCommand;
import pl.moderr.moderrkowo.core.economy.PrzelejCommand;
import pl.moderr.moderrkowo.core.economy.WithdrawCommand;
import pl.moderr.moderrkowo.core.events.EventManager;
import pl.moderr.moderrkowo.core.listeners.*;
import pl.moderr.moderrkowo.core.marketplace.RynekCommand;
import pl.moderr.moderrkowo.core.marketplace.RynekManager;
import pl.moderr.moderrkowo.core.mysql.MySQL;
import pl.moderr.moderrkowo.core.mysql.User;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.npc.NPCManager;
import pl.moderr.moderrkowo.core.opening.ModerrCaseManager;
import pl.moderr.moderrkowo.core.paid.SklepCommand;
import pl.moderr.moderrkowo.core.sklep.ASklepCommand;
import pl.moderr.moderrkowo.core.timevoter.TimeVoter;
import pl.moderr.moderrkowo.core.utils.*;
import pl.moderr.moderrkowo.core.worldmanager.TPWCommand;
import pl.moderr.moderrkowo.core.worldmanager.WorldManager;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Objects;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    public final java.util.logging.Logger logger = getLogger();
    public final FileConfiguration config = getConfig();

    // Instance
    private static Main instance;
    public CuboidsManager cuboidsManager;
    public AntyLogoutManager instanceAntyLogout;
    private static MySQL mySQL;
    //<editor-fold> Managers
    public ModerrkowoAutoMessage autoMessage;
    public RynekManager instanceRynekManager;
    public EventManager eventManager;
    public NPCManager NPCManager;
    public FileConfiguration dataConfig;
    //</editor-fold>
    public ModerrCaseManager caseManager;
    public Sidebar<Component> sidebar;
    //</editor-fold>
    //<editor-fold> Files
    public File dataFile = new File(getDataFolder(), "data.yml");
    // HashMaps
    public BazarManager bazarManager;

    @Contract(pure = true)
    public static Main getInstance() {
        return instance;
    }

    public static @NotNull String getServerName() {
        return ColorUtils.color(HexResolver.parseHexString("<gradient:#FD4F1D:#FCE045>Moderrkowo"));
    }

    // Methods
    @Contract(pure = true)
    public static @NotNull
    String getVersion() {
        return "v1.1.0";
    }

    @Contract(pure = true)
    public static MySQL getMySQL() {
        return mySQL;
    }

    @Override
    public void onEnable() {
        long start = System.currentTimeMillis();
        instance = this;
        Logger.logPluginMessage("Wczytywanie..");
        if (getServer().getPluginManager().getPlugin("Citizens") == null || !Objects.requireNonNull(getServer().getPluginManager().getPlugin("Citizens")).isEnabled()) {
            getLogger().log(Level.SEVERE, "Citizens 2.0 not found or not enabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }
        // Config
        LoadPluginConfig();
        LoadDataYML();
        // MySQL
        initializeMySQL();
        // Listeners
        initializeListeners();
        // AntyLogout
        initializeAntyLogout();
        // Commands
        initializeCommands();
        // Otwieranie skrzynek
        Bukkit.getPluginManager().registerEvents(new CustomItemsManager(), this);
        caseManager = new ModerrCaseManager();
        //
        bazarManager = new BazarManager(this);
        // Questy
        NPCManager = new NPCManager();
        Logger.logPluginMessage("Wczytano NPC");
        // Spawn
        WorldManager.TryLoadWorld(config.getString("spawn.world"));
        // Działki
        cuboidsManager = new CuboidsManager();
        cuboidsManager.Start();
        Logger.logPluginMessage("Wczytano działki");
        // Rynek
        instanceRynekManager = new RynekManager();
        Bukkit.getPluginManager().registerEvents(instanceRynekManager, this);
        // MiniEvents
        eventManager = new EventManager();
        // AutoMessage
        initializeAutoMessage();

        new TimeVoter(this);

        Bukkit.getPluginManager().registerEvents(new FishingListener(), this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            for (User u : UserManager.loadedUsers.values()) {
                double max = 0;
                switch (u.getRank()) {
                    case None:
                        max = 1;
                        break;
                    case Zelazo:
                    case Zloto:
                        max = 3;
                        break;
                    case Diament:
                        max = 5;
                        break;
                    case Emerald:
                        max = 8;
                        break;
                }
                double d = RandomUtils.getRandomInt(3, 7) * u.getUserLevel().playerLevel() * max;
                u.addMoney(d);
                u.getPlayer().sendMessage(ColorUtils.color("&aOtrzymano " + ChatUtil.getMoney(d)) + " za aktywność na serwerze");
            }
        }, 0, 20 * 60 * 10);
        Logger.logPluginMessage(MessageFormat.format("Wczytano w &a{0}ms", System.currentTimeMillis() - start));
    }

    private void initializeMySQL() {
        mySQL = new MySQL();
        mySQL.enable(config.getString("mysql.host"),
                config.getString("mysql.port"),
                config.getString("mysql.database"),
                config.getString("mysql.username"),
                config.getString("mysql.password"));
    }

    @Override
    public void onDisable() {
        try {
            instanceRynekManager.save();
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
        instanceAntyLogout = new AntyLogoutManager();
        Bukkit.getPluginManager().registerEvents(instanceAntyLogout, this);
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
        Objects.requireNonNull(getCommand("ranking")).setExecutor(new RankingCommand());
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
        Objects.requireNonNull(getCommand("enderchest")).setExecutor(new EnderchestCommand());
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
    private void LoadDataYML() {
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
        autoMessage = new ModerrkowoAutoMessage(this, 60 * 8, Arrays.asList(
                "Aby przenieść się do znajomego użyj /tpa <nick>",
                "Zgłoś błąd/problem na /helpop",
                "Zadania jak i sklepy znajdziesz na /spawn",
                "Dołącz do naszego DISCORD'a /discord",
                "Pieniądze można wypłacić za pomocą /wyplac <kwota>",
                "Skrzynie mozesz otwierać na spawnie",
                "Jest brzydka pogoda? Stwórz głosowanie! /pogoda"
        ));
        Logger.logPluginMessage("Wczytano AutoMessage");
    }
}
