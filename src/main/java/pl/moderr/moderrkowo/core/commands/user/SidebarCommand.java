package pl.moderr.moderrkowo.core.commands.user;

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

public class SidebarCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = UserManager.getUser(p.getUniqueId());
            u.setSidebar(!u.isSidebar());
            if (u.isSidebar()) {
                p.sendTitle("", ColorUtils.color("&aWłączono Sidebar"));
                u.UpdateScoreboard();
            } else {
                p.sendTitle("", ColorUtils.color("&cWyłączono Sidebar"));
                p.setScoreboard(Main.getInstance().getServer().getScoreboardManager().getMainScoreboard());
            }
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
        return false;
    }
}
