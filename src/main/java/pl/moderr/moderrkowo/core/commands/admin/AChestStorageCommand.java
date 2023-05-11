package pl.moderr.moderrkowo.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.mechanics.opening.ModerrCaseConstants;
import pl.moderr.moderrkowo.core.mechanics.opening.data.StorageItemType;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.ArrayList;
import java.util.List;

public class AChestStorageCommand implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Player p2 = Bukkit.getPlayer(args[0]);
            if (p2 == null) {
                p.sendMessage(ColorUtils.color("&8[!] &cPodany gracz jest offline"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
            User u = UserManager.getUser(p2.getUniqueId());
            if (args[1].equalsIgnoreCase("get")) {
                StorageItemType type = StorageItemType.Unknown;
                if (args[2].equalsIgnoreCase("key")) {
                    type = StorageItemType.Key;
                }
                if (args[2].equalsIgnoreCase("chest")) {
                    type = StorageItemType.Chest;
                }
                p.sendMessage(ColorUtils.color("&8[!] &aPobrano dane"));
                p.sendMessage(ColorUtils.color("&8[!] &eType: " + type + " | ChestId: " + args[3]));
                //p.sendMessage(ColorUtils.color("&8[!] " + u.getUserChestStorage().getAmountOfItem(new StorageItemKey(type, ModerrCaseEnum.valueOf(args[3])))));
            }
            if (args[1].equalsIgnoreCase("add")) {
                StorageItemType type = StorageItemType.Unknown;
                if (args[2].equalsIgnoreCase("key")) {
                    type = StorageItemType.Key;
                }
                if (args[2].equalsIgnoreCase("chest")) {
                    type = StorageItemType.Chest;
                }
                p.sendMessage(ColorUtils.color("&8[!] &aDodawanie danych"));
                p.sendMessage(ColorUtils.color("&8[!] &eType: " + type + " | ChestId: " + args[3]));
                if (args.length == 5) {
                    //u.getUserChestStorage().addItem(new StorageItem(Integer.parseInt(args[4]), type, ModerrCaseEnum.valueOf(args[3])), false);
                    p.sendMessage(ColorUtils.color("&8[!] &aDodano " + args[4] + "x " + type + " do " + args[3]));
                } else {
                    //u.getUserChestStorage().addItem(new StorageItem(1, type, ModerrCaseEnum.valueOf(args[3])), false);
                    p.sendMessage(ColorUtils.color("&8[!] &aDodano 1x " + type + " do " + args[3]));
                }
            }

        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 2) {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("get");
            arrayList.add("add");
            return arrayList;
        }
        if (args.length == 3) {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add("key");
            arrayList.add("chest");
            return arrayList;
        }
        if (args.length == 4) {
            return new ArrayList<>(ModerrCaseConstants.getChestStringTypes());
        }
        return null;
    }
}
