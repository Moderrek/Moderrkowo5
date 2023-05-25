package pl.moderr.moderrkowo.core.services;

import org.bukkit.command.CommandExecutor;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.ServerService;
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
import pl.moderr.moderrkowo.core.services.marketplace.RynekCommand;

public class CommandService implements ServerService {

    private ModerrkowoPlugin plugin;

    private void registerCommand(String name, CommandExecutor executor) {
        if (plugin == null) return;
        plugin.getCommand(name).setExecutor(executor);
    }

    @Override
    public void Start(ModerrkowoPlugin plugin) {
        this.plugin = plugin;
        registerCommand("asklep", new ASklepCommand());
        registerCommand("moderrkowo", new ModerrkowoCommand(plugin));
        registerCommand("playerid", new PlayerIDCommand());
        registerCommand("ahelpop", new AHelpopCommand());
        registerCommand("chat", new ChatCommand());
        registerCommand("endersee", new EnderseeCommand());
        registerCommand("fly", new FlyCommand());
        registerCommand("gamemode", new GameModeCommand());
        registerCommand("invsee", new InvseeCommand());
        registerCommand("mban", new MBanCommand());
        registerCommand("mkick", new MKickCommand());
        registerCommand("nazwa", new NazwaCommand());
        registerCommand("say", new SayCommand());
        registerCommand("sendalert", new SendAlertCommand());
        registerCommand("vanish", new VanishCommand());
        registerCommand("villager", new ANPCCommand());
        registerCommand("setspawn", new SetSpawnCommand());
        registerCommand("saveusers", new SaveUsersCommand());
        registerCommand("holo", new HoloCommand());
        registerCommand("abank", new ABankCommand());
        registerCommand("arank", new ARankCommand());
        registerCommand("tpw", new TPWCommand());
        registerCommand("discord", new DiscordCommand(plugin.getConfig().getString("discord-link")));
        registerCommand("helpop", new HelpopCommand());
        registerCommand("message", new MessageCommand());
        registerCommand("reply", new ReplyCommand());
        registerCommand("tpr", new TPACommand());
        registerCommand("tpaccept", new TPAccept());
        registerCommand("tpdeny", new TPDeny());
        registerCommand("pogoda", new PogodaCommand());
        registerCommand("regulamin", new RegulaminCommand());
        registerCommand("spawn", new SpawnCommand());
        registerCommand("wyplac", new WithdrawCommand());
        registerCommand("portfel", new PortfelCommand());
        registerCommand("sethome", new SetHomeCommand());
        registerCommand("home", new HomeCommand());
        registerCommand("acheststorage", new AChestStorageCommand());
        registerCommand("przelej", new PrzelejCommand());
        registerCommand("rynek", new RynekCommand());
        registerCommand("przedmioty", new PrzedmiotyCommand(plugin));
        registerCommand("sidebar", new SidebarCommand());
        registerCommand("poziom", new PoziomCommand());
        registerCommand("poradnik", new PoradnikCommand());
        registerCommand("odbierz", new OdbierzCommand(plugin));
        registerCommand("sklep", new SklepCommand());
        registerCommand("crafting", new CraftingCommand());
        registerCommand("enderchest", new EnderChestCommand());
        registerCommand("delhome", new DelHomeCommand());
    }

    @Override
    public void Disable(ModerrkowoPlugin plugin) {
    }
}
