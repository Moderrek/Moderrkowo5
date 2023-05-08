package pl.moderr.moderrkowo.core;

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
import pl.moderr.moderrkowo.core.commands.user.information.CraftingDzialkaCommand;
import pl.moderr.moderrkowo.core.commands.user.information.DiscordCommand;
import pl.moderr.moderrkowo.core.commands.user.information.RegulaminCommand;
import pl.moderr.moderrkowo.core.commands.user.messages.HelpopCommand;
import pl.moderr.moderrkowo.core.commands.user.messages.MessageCommand;
import pl.moderr.moderrkowo.core.commands.user.messages.ReplyCommand;
import pl.moderr.moderrkowo.core.commands.user.teleportation.*;
import pl.moderr.moderrkowo.core.commands.user.weather.PogodaCommand;
import pl.moderr.moderrkowo.core.cuboids.CuboidsManager;
import pl.moderr.moderrkowo.core.customitems.CustomItemsManager;
import pl.moderr.moderrkowo.core.discord.DiscordManager;
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
import java.util.ArrayList;
import java.util.Objects;
import java.util.logging.Level;

public final class Main extends JavaPlugin {

    public final static boolean IS_DEBUG = true;

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
    public DiscordManager discordManager;
    public NPCManager NPCManager;
    public FileConfiguration dataConfig;
    //</editor-fold>
    public ModerrCaseManager caseManager;
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
        return "v1.0.5";
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
        if(getServer().getPluginManager().getPlugin("Citizens") == null || getServer().getPluginManager().getPlugin("Citizens").isEnabled() == false) {
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

        new TimeVoter();

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
        // Discord
//        discordManager = new DiscordManager();
//        try {
//            discordManager.StartBot();
//        } catch (LoginException e) {
//            e.printStackTrace();
//            discordManager = null;
//        }
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
        if(discordManager != null){
            try {
                discordManager.EndBot();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
        Objects.requireNonNull(getCommand("adminchat")).setExecutor(new AdminChatCommand());
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
        Objects.requireNonNull(getCommand("craftingdzialka")).setExecutor(new CraftingDzialkaCommand());
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
//        Objects.requireNonNull(getCommand("zetony")).setExecutor(new ZetonyCommand());
        Objects.requireNonNull(getCommand("przedmioty")).setExecutor(new PrzedmiotyCommand(this));
        Objects.requireNonNull(getCommand("sidebar")).setExecutor(new SidebarCommand());
        Objects.requireNonNull(getCommand("poziom")).setExecutor(new PoziomCommand());
        Objects.requireNonNull(getCommand("poradnik")).setExecutor(new PoradnikCommand());
        Objects.requireNonNull(getCommand("listanpc")).setExecutor(new ListaNPC());
//        Objects.requireNonNull(getCommand("wpln")).setExecutor(new WPLNCommand());
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
            Bukkit.broadcastMessage(ChatUtil.centerText("▪ Tu kupisz rangi i doładujesz wirtualny portfel").replace("▪ Tu kupisz rangi i doładujesz wirtualny portfel", ColorUtils.color("&8▪ &7Tu kupisz rangi i doładujesz wirtualny portfel")));
//            Bukkit.broadcastMessage(ChatUtil.centerText("https://tipo.live/p/moderrkowo").replace("https://tipo.live/p/moderrkowo", ColorUtils.color("&ehttps://tipo.live/p/moderrkowo")));
            Bukkit.broadcastMessage(ChatUtil.centerText("▪ Zobacz rangi pod /sklep").replace("▪ Zobacz rangi pod /sklep", ColorUtils.color("&8▪ &7Zobacz rangi pod &e/sklep")));
            Bukkit.broadcastMessage(" ");
        }, 0, 20 * 60 * 30);
        autoMessage = new ModerrkowoAutoMessage(this, 60 * 8, new ArrayList<>() {
            {
                add("&fTymczasowo w pierwsze dni będą testy ekonomii, będzie prowadzana powoli");
                add("&fNa tym serwerze umożliwiamy prostą prośbę o teleportacje! &a/tpa <nick>");
                add("&fJeżeli zobaczyć jakiś błąd zgłoś go odrazu! &c/helpop");
                add("&fZadania jak i sklepy znajdziesz na &9/spawn");
                add("&fDołącz do naszego &9&lDISCORD&f'a &9/discord");
                add("&fPieniądze można wypłacić za pomocą &a/wyplac <kwota>");
                add("&fGrając na serwerze akceptujesz aktualne zasady &c/regulamin");
                add("&fNie wiesz jak grać? Wpisz &c/poradnik");
                add("&fJest &9brzydka pogoda&f? &aStwórz głosowanie! /pogoda");
                add("&fZdajemy sobie sprawę że samemu jest nudno, ale czemu by nie zaprosić znajomych?");
                add("&fGdy łowisz mogą ci wypaść legendarne skrzynie!");
            }
        });
        Logger.logPluginMessage("Wczytano AutoMessage");
    }
}
