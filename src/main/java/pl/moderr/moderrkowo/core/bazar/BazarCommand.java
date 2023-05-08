package pl.moderr.moderrkowo.core.bazar;

import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.executers.UserCommand;
import pl.moderr.moderrkowo.core.mysql.User;

import java.util.UUID;

public class BazarCommand implements UserCommand {

    private final BazarManager manager;
    public BazarCommand(BazarManager manager){
        this.manager = manager;
    }

    @Override
    public boolean execute(@NotNull User user, @NotNull Player player, @NotNull UUID uuid, @NotNull String name, @NotNull Command command, @NotNull String commandName, @NotNull String[] args) {
        manager.openInventory(user, player, ItemCategory.Materialy);
        return false;
    }
}
