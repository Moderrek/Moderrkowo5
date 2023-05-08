package pl.moderr.moderrkowo.core.timevoter.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.timevoter.TimeVoter;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.time.Duration;
import java.time.Instant;

public class VoteUtil {

    private final TimeVoter timeVoter;
    private BukkitTask timer;
    private BossBar bossBar;

    public VoteUtil(TimeVoter timeVoter) {
        this.timeVoter = timeVoter;
    }

    public void StartVote(Player player) {
        World world = player.getWorld();
        if (!isOverworld(player)) {
            return;
        }
        double timeElapsed = 0;
        if (timeVoter.lastVote != null) {
            timeElapsed = Duration.between(timeVoter.lastVote, Instant.now()).toMinutes();
        }
        if (timeVoter.lastVote == null || timeElapsed >= timeVoter.voteDelay) {
            timeVoter.isVoteActive = true;
            if (timeVoter.lastVote == null) {
                timeVoter.lastVote = Instant.now();
            }

            TextComponent yes = new TextComponent("Tak");
            yes.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tv tak"));
            yes.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ColorUtils.color("&aZagłosuj na TAK"))));
            yes.setColor(ChatColor.GREEN);

            TextComponent no = new TextComponent("Nie");
            no.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tv nie"));
            no.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ColorUtils.color("&cZagłosuj na NIE"))));
            no.setColor(ChatColor.RED);

            boolean day = world.getTime() >= 12600L;
            if (day) {
                Main.getInstance().getServer().broadcastMessage(
                        ColorUtils.color(
                                "&6" + player.getName() + " &7właśnie rozpoczął głosowanie o &e☀ dzień&7" +
                                        "\nMasz &6" + timeVoter.timeToVote + " &7sekund na głosowanie." +
                                        "\nKliknij TAK lub NIE albo połóż się spać aby zagłosować na TAK"
                        )
                );
            } else {
                Main.getInstance().getServer().broadcastMessage(
                        ColorUtils.color(
                                "&6" + player.getName() + " &7właśnie rozpoczął głosowanie o &9⭐ noc&7" +
                                        "\nMasz &6" + timeVoter.timeToVote + " &7sekund na głosowanie." +
                                        "\nKliknij TAK lub NIE albo połóż się spać aby zagłosować na TAK"
                        )
                );
            }
            Main.getInstance().getServer().broadcast(
                    new ComponentBuilder()
                            .append(yes)
                            .append(" / ").color(ChatColor.GOLD)
                            .append(no)
                            .create()
            );
            timeVoter.getYesVote().add(player.getUniqueId());
            player.sendMessage(ColorUtils.color("&aAutomatycznie zagłosowałeś na tak, ponieważ zacząłeś głosowanie"));

            createBossBar(day);

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                if (timeVoter.getYesVote().size() > timeVoter.getNoVote().size()) {
                    if (world.getTime() >= 12600L) {
                        world.setTime(0L);
                        Main.getInstance().getServer().broadcastMessage(
                                ColorUtils.color(
                                        "&aGłosowanie zostało zakończone sukcesem." +
                                                "\n&7Cykl nocy zostanie pominięty."
                                )
                        );
                    } else {
                        world.setTime(12600L);
                        Main.getInstance().getServer().broadcastMessage(
                                ColorUtils.color(
                                        "&aGłosowanie zostało zakończone sukcesem." +
                                                "\n&7Cykl dnia zostanie pominięty."
                                )
                        );
                    }
                } else {
                    Main.getInstance().getServer().broadcastMessage(
                            ColorUtils.color(
                                    "&cGłosowanie zostało zakończone niepowodzeniem." +
                                            "\n&7Czas upłynie w sposób naturalny."
                            )
                    );
                }
                timeVoter.isVoteActive = false;
                timeVoter.getYesVote().clear();
                timeVoter.getNoVote().clear();
                timer.cancel();
                bossBar.removeAll();
            }, timeVoter.timeToVote * 20L);
        } else {
            player.sendMessage(ColorUtils.color("&cZa wcześnie, aby rozpocząć głosowanie innym razem."));
        }
    }

    private void createBossBar(boolean day) {
        if (day) {
            bossBar = Bukkit.createBossBar(ColorUtils.color("&e☀ Dzień"), BarColor.YELLOW, BarStyle.SOLID);
        } else {
            bossBar = Bukkit.createBossBar(ColorUtils.color("&9⭐ Noc"), BarColor.BLUE, BarStyle.SOLID);
        }
        bossBar.setProgress(1);
        Bukkit.getOnlinePlayers().forEach(player -> {
            bossBar.addPlayer(player);
        });
        timer = Bukkit.getScheduler().runTaskTimer(Main.getInstance(), () -> {
            float increment = (float) 1 / timeVoter.timeToVote;
            double newProgress = bossBar.getProgress() - increment;
            if (newProgress <= 0) {
                bossBar.setProgress(0);
                return;
            }
            bossBar.setProgress(newProgress);
        }, 0, 20);
    }

    public boolean isOverworld(Player player) {
        if (player.getWorld().getEnvironment() != World.Environment.NORMAL) {
            player.sendMessage(ColorUtils.color("&cMożesz głosować tylko w normalnym świecie!"));
            return false;
        }
        return true;
    }
}
