package pl.moderr.moderrkowo.core.commands.user;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.util.ChatUtil;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.services.mysql.UserManager;
import pl.moderr.moderrkowo.core.user.User;

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
                    p.sendMessage(ColorUtil.color("&cNie udało się wczytać danych o twoim portfelu!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
                int kwota;
                try {
                    kwota = Integer.parseInt(args[1]);
                } catch (Exception e) {
                    p.sendMessage(ColorUtil.color("&cPodano nieprawidłową kwotę!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
                if (kwota < 0) {
                    p.sendMessage(ColorUtil.color("&cPodano nieprawidłową kwotę!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
                if (kwota > u.getMoney()) {
                    p.sendMessage(ColorUtil.color("&cNie posiadasz tyle pieniędzy!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
                if (Bukkit.getPlayer(args[0]) != null) {
                    if (Objects.requireNonNull(Bukkit.getPlayer(args[0])).getUniqueId().equals(p.getUniqueId())) {
                        p.sendMessage(ColorUtil.color("&cNie możesz robić przelewu do siebie!"));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return false;
                    } else {
                        u.subtractMoney(kwota);
                        p.sendMessage(ColorUtil.color("&aPomyślnie przelano"));
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        p.sendActionBar(ColorUtil.color("&c-" + ChatUtil.formatMoney(kwota)));
                        User u2 = UserManager.getUser(Objects.requireNonNull(Bukkit.getPlayer(args[0])).getUniqueId());
                        u2.addMoney(kwota);
                        u2.getPlayer().sendMessage(ColorUtil.color("&8[!] &aOtrzymałeś przelew od &2" + p.getName() + " &ao kwocie &2" + ChatUtil.formatMoney(kwota)));
                        u2.getPlayer().sendActionBar(ColorUtil.color("&a+" + ChatUtil.formatMoney(kwota)));
                        u2.getPlayer().playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    }
                } else {
                    p.sendMessage(ColorUtil.color("&cGracz jest offline!"));
                }
            } else {
                p.sendMessage(ColorUtil.color("&cżzyj: /przelej <nick> <kwota>"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
        }
        return false;
    }
}
