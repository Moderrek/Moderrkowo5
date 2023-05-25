package pl.moderr.moderrkowo.core.commands.admin;

import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.Logger;
import pl.moderr.moderrkowo.core.events.server.JoinQuitListener;

import java.util.ArrayList;
import java.util.UUID;

public class VanishCommand implements CommandExecutor {


    public final static BossBar bossBar = Bukkit.createBossBar(ColorUtil.color("&9>>> JESTEŚ UKRYTY <<<"), BarColor.BLUE, BarStyle.SOLID);

    public final static ArrayList<UUID> hidden = new ArrayList<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (hidden.contains(p.getUniqueId())) {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (players.isOp()) {
                        continue;
                    }
                    players.showPlayer(ModerrkowoPlugin.getInstance(), p);
                }
                hidden.remove(p.getUniqueId());
                p.sendMessage(ColorUtil.color("&cZostałeś odkryty"));
                Bukkit.broadcastMessage(JoinQuitListener.getJoinMessage(p));
                Logger.logAdminLog(p.getName() + " został odkryty");
                bossBar.removePlayer(p);
            } else {
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (players.isOp()) {
                        continue;
                    }
                    players.hidePlayer(ModerrkowoPlugin.getInstance(), p);
                }
                hidden.add(p.getUniqueId());
                p.sendMessage(ColorUtil.color("&aZostałeś ukryty"));
                Bukkit.broadcastMessage(JoinQuitListener.getQuitMessage(p));
                Logger.logAdminLog(p.getName() + " został ukryty");
                bossBar.addPlayer(p);
            }
        }
        return false;
    }
}
