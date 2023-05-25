package pl.moderr.moderrkowo.core.services.timevoter.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.api.executor.PlayerCommand;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.services.timevoter.TimeVoterManager;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;


public class VoteCommand implements PlayerCommand, TabCompleter {
    private final TimeVoterManager timeVoter;

    public VoteCommand(TimeVoterManager timeVoter) {
        this.timeVoter = timeVoter;
    }

    @Override
    public boolean execute(@NotNull Player player, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!timeVoter.getUtil().isInMainWorld(player)) {
            player.sendMessage(Component.text("Aby głosować musisz być w normalnym świecie.").color(NamedTextColor.YELLOW));
            return true;
        }
        if (args.length == 1) {
            if (timeVoter.isVoteActive()) {
                if (timeVoter.getYesVote().contains(player.getUniqueId()) || timeVoter.getNoVote().contains(player.getUniqueId())) {
                    player.sendMessage(ColorUtil.color("&cJuż głosowałeś!"));
                    return true;
                }
                if (args[0].equalsIgnoreCase("tak")) {
                    timeVoter.getYesVote().add(player.getUniqueId());
                    Component votedYes = Component.text().content(MessageFormat.format("{0} zagłosował na tak.", player.getName())).color(NamedTextColor.GREEN).build();
                    timeVoter.getPlugin().getServer().broadcast(votedYes);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    return true;
                } else if (args[0].equalsIgnoreCase("nie")) {
                    timeVoter.getNoVote().add(player.getUniqueId());
                    Component votedNo = Component.text().content(MessageFormat.format("{0} zagłosował na nie.", player.getName())).color(NamedTextColor.RED).build();
                    timeVoter.getPlugin().getServer().broadcast(votedNo);
                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    return true;
                } else {
                    Component hintUsage = Component.text().content("Użyj: /timevote <tak/nie>").color(NamedTextColor.YELLOW).build();
                    player.sendMessage(hintUsage);
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
            }
        }
//        else {
//            if (!timeVoter.isVoteActive()) {
//                timeVoter.getVoteUtil().StartVote(player);
//            }
//        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return Arrays.asList("tak", "nie");
        }
        return null;
    }
}
