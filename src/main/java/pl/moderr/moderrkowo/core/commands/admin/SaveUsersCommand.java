package pl.moderr.moderrkowo.core.commands.admin;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.mysql.User;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.sql.SQLException;

public class SaveUsersCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            int count = UserManager.loadedUsers.values().size();
            Logger.logAdminLog("&eAutomatyczne zapisywanie użytkowników [" + count + "]");
            int i = 1;
            for (User u : UserManager.loadedUsers.values()) {
                try {
                    Main.getMySQL().getQuery().updateUser(u);
                    u.getPlayer().sendActionBar(ColorUtils.color("&a✔ Zapisano dane."));
                    Logger.logAdminLog("&a✔. Zapisano &2" + u.getName() + " &f[" + i + "/" + count + "]");
                    i++;
                } catch (SQLException exception) {
                    Logger.logAdminLog("&c✘. Nie udało się zapisać " + u.getName());
                    i++;
                    u.getPlayer().sendActionBar(ColorUtils.color("&c✘ Nie udało się zapisać danych."));
                    u.getPlayer().sendMessage(ColorUtils.color("&eAutomatyczny zapis\n&cWystąpił błąd podczas zapisywania danych zgłoś się do administracji"));
                    u.getPlayer().playSound(u.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_HURT, 2, 1);
                    exception.printStackTrace();
                }


            }
        }
        return false;
    }
}
