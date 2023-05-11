package pl.moderr.moderrkowo.core.commands.user;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.util.Objects;

public class PrzelejCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 1) {
                User u;
                try {
                    u = UserManager.getUser(p.getUniqueId());
                } catch (Exception userNotLoaded) {
                    userNotLoaded.printStackTrace();
                    p.sendMessage(ColorUtils.color("&cNie udało się wczytać danych o twoim portfelu!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
                int kwota;
                try {
                    kwota = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    p.sendMessage(ColorUtils.color("&cPodano nieprawidłową kwotę!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
                if (kwota < 0) {
                    p.sendMessage(ColorUtils.color("&cPodano nieprawidłową kwotę!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
                if (kwota > u.getMoney()) {
                    p.sendMessage(ColorUtils.color("&cNie posiadasz tyle pieniędzy!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
                if (Bukkit.getPlayer(args[0]) != null) {
                    if (Objects.requireNonNull(Bukkit.getPlayer(args[0])).getUniqueId().equals(p.getUniqueId())) {
                        p.sendMessage(ColorUtils.color("&cNie możesz robić przelewu do siebie!"));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return false;
                    } else {
                        u.subtractMoney(kwota);
                        p.sendMessage(ColorUtils.color("&aPomyślnie przelano"));
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        p.sendActionBar(ColorUtils.color("&c-" + ChatUtil.getMoney(kwota)));
                        User u2 = UserManager.getUser(Objects.requireNonNull(Bukkit.getPlayer(args[0])).getUniqueId());
                        u2.addMoney(kwota);
                        u2.getPlayer().sendMessage(ColorUtils.color("&8[!] &aOtrzymałeś przelew od &2" + p.getName() + " &ao kwocie &2" + ChatUtil.getMoney(kwota)));
                        u2.getPlayer().sendActionBar(ColorUtils.color("&a+" + ChatUtil.getMoney(kwota)));
                        u2.getPlayer().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    }
                } else {
                    p.sendMessage(ColorUtils.color("&cGracz jest offline!"));
                }
            } else {
                p.sendMessage(ColorUtils.color("&cżzyj: /przelej <nick> <kwota>"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
        }
        return false;
    }
}
