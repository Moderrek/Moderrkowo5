package pl.moderr.moderrkowo.core.sklep;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class ASklepCommand implements CommandExecutor {

    // asklep thank MODERR &fŻelazo 5

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args[0].equalsIgnoreCase("thank")){
            Player p = Bukkit.getPlayer(args[1]);
            if(p != null){
                p.sendTitle(ColorUtils.color("&aDziękujemy za wsparcie"), "");
            }
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(ColorUtils.color(" &6> &e" + args[1] + " &7zakupił " + args[2] + "\n   &7o wartości &6" + ChatUtil.getWPLN(Double.parseDouble(args[3])*4.17)));
            Bukkit.broadcastMessage(" ");
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1));
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1));
        }
        if(args[0].equalsIgnoreCase("renew")){
            Player p = Bukkit.getPlayer(args[1]);
            if(p != null){
                p.sendTitle(ColorUtils.color("&aDziękujemy za wsparcie"), "");
            }
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(ColorUtils.color(" &6> &e" + args[1] + " &7odnowił " + args[2] + "\n   &7o wartości &6" + ChatUtil.getWPLN(Double.parseDouble(args[3])*4.17)));
            Bukkit.broadcastMessage(" ");
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1));
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1));
        }
        return false;
    }
}
