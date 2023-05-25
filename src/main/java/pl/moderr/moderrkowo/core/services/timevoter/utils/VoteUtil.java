package pl.moderr.moderrkowo.core.services.timevoter.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.services.timevoter.TimeVoterManager;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;

public class VoteUtil {

    private final TimeVoterManager timeVoter;
    private BukkitTask timer;
    private BossBar bossBar;

    public VoteUtil(TimeVoterManager timeVoter) {
        this.timeVoter = timeVoter;
    }

    public void StartVote(@NotNull Player startingPlayer) {
        World world = startingPlayer.getWorld();
        if (!isInMainWorld(startingPlayer)) {
            return;
        }
        double timeElapsed = 0;
        if (timeVoter.getLastVote() != null) {
            timeElapsed = Duration.between(timeVoter.getLastVote(), Instant.now()).toMinutes();
        }
        if (timeVoter.getLastVote() == null || timeElapsed >= timeVoter.getVoteDelay()) {
            timeVoter.setVoteActive(true);
            if (timeVoter.getLastVote() == null) {
                timeVoter.setLastVote(Instant.now());
            }

            TextComponent yes = Component.text("Tak")
                    .color(NamedTextColor.GREEN)
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/tv tak"))
                    .hoverEvent(Component.text("Zagłosuj na TAK")
                            .color(NamedTextColor.GREEN)
                    );
            TextComponent no = Component.text("Nie")
                    .color(NamedTextColor.RED)
                    .clickEvent(ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, "/tv nie"))
                    .hoverEvent(Component.text("Zagłosuj na NIE")
                            .color(NamedTextColor.RED)
                    );

            boolean isNight = world.getTime() >= 12600L;
            TextComponent timeOfDay = isNight ? Component.text().content("☀ dzień").color(NamedTextColor.YELLOW).build() : Component.text().content("⭐ noc").color(NamedTextColor.BLUE).build();
            final TextComponent playerName = Component.text().content(startingPlayer.getName()).color(NamedTextColor.GOLD).build();
            final TextComponent header = Component.text().content("właśnie rozpoczął głosowanie o").color(NamedTextColor.GRAY).build();
            final TextComponent timeToVote = Component.text().content(MessageFormat.format("Masz {0}s na głosowanie.", timeVoter.getTimeToVote())).color(NamedTextColor.GRAY).build();
            final TextComponent howToVote = Component.text().content("KLIKNIJ! TAK lub NIE aby zagłosować!").color(NamedTextColor.GRAY).build();
            final TextComponent.Builder voteDay = Component.text()
                    .append(playerName).appendSpace().append(header).appendSpace().append(timeOfDay)
                    .appendNewline().append(timeToVote)
                    .appendNewline().append(howToVote);

            timeVoter.getPlugin().getServer().broadcast(voteDay.build());
            timeVoter.getPlugin().getServer().broadcast(Component.text().append(yes).append(Component.text(" / ").color(NamedTextColor.GOLD)).append(no).build());
            timeVoter.getYesVote().add(startingPlayer.getUniqueId());
            startingPlayer.sendMessage(ColorUtil.color("&aAutomatycznie zagłosowałeś na tak, ponieważ zacząłeś głosowanie"));

            createBossBar(isNight);

            Bukkit.getScheduler().runTaskLater(ModerrkowoPlugin.getInstance(), () -> {
                if (timeVoter.getYesVote().size() > timeVoter.getNoVote().size()) {
                    if (world.getTime() >= 12600L) {
                        world.setTime(0L);
                        timeVoter.getPlugin().getServer().broadcast(Component.text()
                                .append(Component.text("Głosowanie zostało zakończone sukcesem.")
                                        .color(NamedTextColor.GREEN))
                                .appendNewline()
                                .append(Component.text("Cykl nocy zostanie pominięty.").color(NamedTextColor.GRAY))
                                .build()
                        );
                    } else {
                        world.setTime(12600L);
                        timeVoter.getPlugin().getServer().broadcast(Component.text()
                                .append(Component.text("Głosowanie zostało zakończone sukcesem.")
                                        .color(NamedTextColor.GREEN))
                                .appendNewline()
                                .append(Component.text("Cykl dnia zostanie pominięty.").color(NamedTextColor.GRAY))
                                .build()
                        );
                    }
                } else {
                    timeVoter.getPlugin().getServer().broadcast(Component.text()
                            .append(Component.text("Głosowanie zostało zakończone niepowodzeniem.")
                                    .color(NamedTextColor.RED))
                            .appendNewline()
                            .append(Component.text("Czas upłynie w sposób naturalny.").color(NamedTextColor.GRAY))
                            .build()
                    );
                }
                timeVoter.setVoteActive(false);
                timeVoter.getYesVote().clear();
                timeVoter.getNoVote().clear();
                timer.cancel();
                bossBar.removeAll();
            }, timeVoter.getTimeToVote() * 20L);
        } else {
            startingPlayer.sendMessage(ColorUtil.color("&cZa wcześnie, aby rozpocząć głosowanie innym razem."));
        }
    }

    private void createBossBar(boolean day) {
        String bossBarContent;
        BarColor bossBarColor;
        if (day) {
            bossBarContent = "&e☀ Dzień";
            bossBarColor = BarColor.YELLOW;
        } else {
            bossBarContent = "&9⭐ Noc";
            bossBarColor = BarColor.BLUE;
        }
        bossBar = Bukkit.createBossBar(ColorUtil.color(bossBarContent), bossBarColor, BarStyle.SOLID);
        bossBar.setProgress(1);
        Bukkit.getOnlinePlayers().forEach(bossBar::addPlayer);
        timer = Bukkit.getScheduler().runTaskTimer(ModerrkowoPlugin.getInstance(), () -> {
            float increment = (float) 1 / timeVoter.getTimeToVote();
            double newProgress = bossBar.getProgress() - increment;
            if (newProgress <= 0) {
                bossBar.setProgress(0);
                return;
            }
            bossBar.setProgress(newProgress);
        }, 0, 20);
    }

    public boolean isInMainWorld(@NotNull Player player) {
        World mainWorld = timeVoter.getPlugin().getServer().getWorlds().get(0);
        return mainWorld.equals(player.getWorld());
    }
}
