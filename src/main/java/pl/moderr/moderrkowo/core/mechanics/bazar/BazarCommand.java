package pl.moderr.moderrkowo.core.mechanics.bazar;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.executors.UserCommand;
import pl.moderr.moderrkowo.core.user.User;

import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BazarCommand implements UserCommand, TabCompleter {

    private final BazarManager manager;

    public BazarCommand(BazarManager manager) {
        this.manager = manager;
    }

    @Override
    public boolean execute(@NotNull User user, @NotNull Player player, @NotNull UUID uuid, @NotNull String name, @NotNull Command command, @NotNull String commandName, @NotNull String[] args) {
        if(player.hasPermission("moderrkowo.bazar.reload")){
            if(args.length == 1){
                if(args[0].equalsIgnoreCase("reload")){
                    try {
                        manager.reloadEconomy();
                        player.sendMessage("Pomyślnie przeładowano");
                    } catch (IOException e) {
                        player.sendMessage("Nie udało się przeładować!");
                    }
                }
            }
        }
        manager.openInventory(user, ItemCategory.MATERIALY);
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender.hasPermission("moderrkowo.bazar.reload") && args.length == 1){
            return List.of("reload");
        }
        return null;
    }
}
