package pl.moderr.moderrkowo.core.commands.user;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.ranks.Rank;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

public class CraftingCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            User u = UserManager.getUser(p.getUniqueId());
            if (u.hasRank(Rank.Diament)) {
                if (ModerrkowoPlugin.getInstance().getAntyLogout().isFighting(p.getUniqueId())) {
                    p.sendMessage(ColorUtils.color("&cNie możesz używać podczas walki"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    Logger.logAdminLog(p.getName() + " chciał użyć craftingu podczas walki");
                    return false;
                }
                p.openWorkbench(null, true);
                p.sendMessage(ColorUtils.color("&aOtworzono crafting"));
            } else {
                p.sendMessage(ColorUtils.color("&cNie posiadasz rangi &b&lDIAMENT &club &a&lEMERALD &caby otworzyć EnderChesta!"));
            }
        }
        return false;
    }
}
