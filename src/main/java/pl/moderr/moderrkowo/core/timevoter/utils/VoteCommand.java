package pl.moderr.moderrkowo.core.timevoter.utils;

import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.timevoter.TimeVoter;
import pl.moderr.moderrkowo.core.utils.ColorUtils;


public class VoteCommand implements CommandExecutor {
    private final TimeVoter timeVoter;

    public VoteCommand(TimeVoter timeVoter) {
        this.timeVoter = timeVoter;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (!timeVoter.voteUtil.isOverworld(player)) {
                return false;
            }
            if (args.length == 1) {
                if (timeVoter.isVoteActive) {
                    if (timeVoter.getYesVote().contains(player.getUniqueId()) || timeVoter.getNoVote().contains(player.getUniqueId())) {
                        player.sendMessage(ColorUtils.color("&cJuż głosowałeś!"));
                        return false;
                    }
                    if (args[0].equalsIgnoreCase("tak")) {
                        //player.sendMessage(ColorUtils.color("&aZagłosowałeś na TAK"));
                        Main.getInstance().getServer().broadcastMessage(ColorUtils.color("&a" + player.getName() + " zagłosował na tak"));
                        timeVoter.getYesVote().add(player.getUniqueId());
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        return true;
                    } else if (args[0].equalsIgnoreCase("nie")) {
                        //player.sendMessage(ColorUtils.color("&cZagłosowałeś na NIE"));
                        Main.getInstance().getServer().broadcastMessage(ColorUtils.color("&c" + player.getName() + " zagłosował na nie"));
                        timeVoter.getNoVote().add(player.getUniqueId());
                        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        return true;
                    } else {
                        player.sendMessage(ColorUtils.color("&cUżyj: /timevote <tak,nie>"));
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return false;
                    }
                }
            } else {
                if (!timeVoter.isVoteActive) {
                    timeVoter.voteUtil.StartVote(player);
                }
            }
        } else {
            sender.sendMessage(ColorUtils.color("&cNie możesz głosować z powodu, iż nie jesteś graczem!"));
            return false;
        }
        return false;
    }
}
