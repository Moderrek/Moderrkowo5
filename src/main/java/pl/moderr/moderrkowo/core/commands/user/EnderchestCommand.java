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
import pl.moderr.moderrkowo.core.ranks.Rank;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

public class EnderchestCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = UserManager.getUser(p.getUniqueId());
            if (u.hasRank(Rank.Diament)) {
                if (Main.getInstance().instanceAntyLogout.inFight(p.getUniqueId())) {
                    p.sendMessage(ColorUtils.color("&cNie możesz używać podczas walki"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    Logger.logAdminLog(p.getName() + " chciał użyć enderchesta podczas walki");
                    return false;
                }
                p.openInventory(p.getEnderChest());
                p.sendMessage(ColorUtils.color("&aOtworzono skrzynie kresu"));
            } else {
                p.sendMessage(ColorUtils.color("&cNie posiadasz rangi &b&lDIAMENT &club &a&lEMERALD &caby otworzyć EnderChesta!"));
            }
        }
        return false;
    }
}
