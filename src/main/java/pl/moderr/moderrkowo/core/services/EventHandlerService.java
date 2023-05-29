package pl.moderr.moderrkowo.core.services;

import org.bukkit.event.Listener;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.ServerService;
import pl.moderr.moderrkowo.core.commands.user.WithdrawCommand;
import pl.moderr.moderrkowo.core.commands.user.weather.PogodaCommand;
import pl.moderr.moderrkowo.core.events.server.ChatListener;
import pl.moderr.moderrkowo.core.events.server.PlayerJoinQuitListener;
import pl.moderr.moderrkowo.core.events.server.MotdListener;
import pl.moderr.moderrkowo.core.events.server.TNTListener;
import pl.moderr.moderrkowo.core.events.user.CropBreakListener;
import pl.moderr.moderrkowo.core.events.user.MiningListener;
import pl.moderr.moderrkowo.core.events.user.PlayerDeathListener;
import pl.moderr.moderrkowo.core.events.user.PlayerMoveListener;
import pl.moderr.moderrkowo.core.events.user.quest.AnimalBreedingListener;
import pl.moderr.moderrkowo.core.services.customitems.CustomItemsManager;

public class EventHandlerService implements ServerService {

    private ModerrkowoPlugin plugin;

    private void registerListener(Listener listener) {
        plugin.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    @Override
    public void Start(ModerrkowoPlugin plugin) {
        this.plugin = plugin;
        registerListener(new PlayerDeathListener());
        registerListener(new ChatListener());
        registerListener(new MotdListener());
        registerListener(new PlayerJoinQuitListener());
        registerListener(new CropBreakListener());
        registerListener(new PogodaCommand());
        registerListener(new TNTListener());
        registerListener(new WithdrawCommand());
//        registerListener(new PortalListener());
        registerListener(new MiningListener());
        registerListener(new PlayerMoveListener());
        registerListener(new AnimalBreedingListener(plugin));
        registerListener(new CustomItemsManager());
    }

    @Override
    public void Disable(ModerrkowoPlugin plugin) {

    }
}
