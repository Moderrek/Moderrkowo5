package pl.moderr.moderrkowo.core.events.server;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.util.ChatUtil;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.commands.admin.ChatCommand;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.user.ranks.RankManager;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ChatListener implements Listener {

    private final Map<UUID, Instant> chatDelay;
    private final Map<UUID, Instant> commandDelay;

    public ChatListener() {
        chatDelay = new ConcurrentHashMap<>();
        commandDelay = new ConcurrentHashMap<>();
    }

    @EventHandler
    public void chat(@NotNull final AsyncPlayerChatEvent e) {
        if (!e.getPlayer().isOp() && !ChatCommand.canChat) {
            e.getPlayer().sendMessage(ColorUtil.color("&cChat jest wyłączony!"));
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            e.setCancelled(true);
            return;
        }
        final User user = User.Get(e.getPlayer().getUniqueId());
        if (user == null) {
            e.getPlayer().sendMessage(ColorUtil.color("&cWystąpił problem podczas wysyłania wiadomości."));
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            e.setCancelled(true);
            return;
        }
        final Instant now = Instant.now();
        chatDelay.compute(e.getPlayer().getUniqueId(), (uuid, instant) -> {
            if (instant != null && now.isBefore(instant)) {
                e.getPlayer().sendActionBar(ColorUtil.color("&cOdczekaj chwilę miedzy wysyłaniem wiadomości"));
                e.setCancelled(true);
                return instant;
            }
            e.setCancelled(false);
            e.setMessage(e.getMessage().replace("%", "%%"));
            e.setMessage(filterMessage(e.getMessage()));
            e.setFormat(ColorUtil.color(RankManager.getChat(user.getRank(), user.getStuffRank()) + e.getPlayer().getName() + " " + RankManager.getMessageColor(user.getRank())) + e.getMessage());
            return now.plusMillis((long) (user.getRank().getChatSecondsDelay() * 1_000L));
        });
    }

    private @NotNull String replace(String source, @NotNull String target, String replacement) {
        StringBuilder sbSource = new StringBuilder(source);
        StringBuilder sbSourceLower = new StringBuilder(source.toLowerCase());
        String searchString = target.toLowerCase();

        int index = 0;
        while ((index = sbSourceLower.indexOf(searchString, index)) != -1) {
            sbSource.replace(index, index + searchString.length(), replacement);
            sbSourceLower.replace(index, index + searchString.length(), replacement);
            index += replacement.length();
        }
        sbSourceLower.setLength(0);
        sbSourceLower.trimToSize();

        return sbSource.toString();
    }

    private String filterMessage(String message) {
        for (String badWord : ChatUtil.blockedWords) {
            message = replace(message, badWord, "*");
        }
        return message;
    }

    @EventHandler
    public void preCommand(@NotNull PlayerCommandPreprocessEvent e) {
        final Player player = e.getPlayer();
        if (player.isOp()) return;
        e.setCancelled(true);
        User user = User.Get(player);
        if (user == null) {
            e.getPlayer().sendMessage(ColorUtil.color("&cNie załadowano użytkownika!"));
            return;
        }
        final Instant now = Instant.now();
        commandDelay.compute(e.getPlayer().getUniqueId(), (uuid, instant) -> {
            if (instant != null && now.isBefore(instant)) {
                e.getPlayer().sendActionBar(ColorUtil.color("&cOdczekaj chwilę miedzy wpisywaniem komend"));
                return instant;
            }
            e.setCancelled(false);
            return now.plusNanos((long) (user.getRank().getCommandSecondsDelay() * 1_000));
        });
    }

}
