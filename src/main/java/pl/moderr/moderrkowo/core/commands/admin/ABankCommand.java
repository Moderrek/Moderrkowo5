package pl.moderr.moderrkowo.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.api.util.ChatUtil;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.services.mysql.UserManager;
import pl.moderr.moderrkowo.core.user.User;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class ABankCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        CommandSender p = sender;
        User u = UserManager.getUser(Objects.requireNonNull(Bukkit.getPlayer(args[1])).getUniqueId());
        if (args[0].equalsIgnoreCase("odejmij")) {
            u.subtractMoney(Double.parseDouble(args[2]));
            p.sendMessage(ColorUtil.color(MessageFormat.format("&8[!] &2{0} &aposiada {1}", u.getName(), ChatUtil.formatMoney(u.getMoney()))));
        }
        if (args[0].equalsIgnoreCase("dodaj")) {
            u.addMoney(Double.parseDouble(args[2]));
            p.sendMessage(ColorUtil.color(MessageFormat.format("&8[!] &2{0} &aposiada {1}", u.getName(), ChatUtil.formatMoney(u.getMoney()))));
        }
        if (args[0].equalsIgnoreCase("ustaw")) {
            u.setMoney(Double.parseDouble(args[2]));
            p.sendMessage(ColorUtil.color(MessageFormat.format("&8[!] &2{0} &aposiada {1}", u.getName(), ChatUtil.formatMoney(u.getMoney()))));
        }
        if (args[0].equalsIgnoreCase("sprawdz")) {
            p.sendMessage(ColorUtil.color(MessageFormat.format("&8[!] &2{0} &aposiada {1}", u.getName(), ChatUtil.formatMoney(u.getMoney()))));
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return Arrays.asList("odejmij", "dodaj", "ustaw", "sprawdz");
        }
        return null;
    }
}
