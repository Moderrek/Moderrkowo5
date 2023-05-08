package pl.moderr.moderrkowo.core.commands.admin;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;

public class SetSpawnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            Main.getInstance().config.set("spawn.location", p.getLocation());
            Main.getInstance().config.set("spawn.world", p.getLocation().getWorld().getName());
            Main.getInstance().saveConfig();
            p.showTitle(Title.title(Component.text(" "), Component.text("Pomyślnie ustawiono spawn").color(NamedTextColor.GREEN)));
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
        }
        return false;
    }
}
