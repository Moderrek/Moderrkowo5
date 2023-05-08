package pl.moderr.moderrkowo.core.commands.admin;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class SetSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Main.getInstance().config.set("spawn.location", p.getLocation());
            Main.getInstance().config.set("spawn.world", p.getLocation().getWorld().getName().toString());
            Main.getInstance().saveConfig();
            p.sendTitle(" ", ColorUtils.color("&aPomyślnie ustawiono spawna"));
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
        return false;
    }
}
