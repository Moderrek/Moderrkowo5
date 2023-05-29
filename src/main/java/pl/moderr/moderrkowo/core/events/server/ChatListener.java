package pl.moderr.moderrkowo.core.events.server;

import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.util.ChatUtil;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.ComponentUtil;
import pl.moderr.moderrkowo.core.commands.admin.ChatCommand;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.user.ranks.RankManager;

import java.text.MessageFormat;
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
    public void chat(@NotNull AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        if (!(player.isOp() || ChatCommand.canChat)) {
            player.sendMessage(ComponentUtil.coloredText("Chat jest wyłączony!", NamedTextColor.RED));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            event.setCancelled(true);
            return;
        }
        final User user = User.Get(player.getUniqueId());
        if (user == null) {
            player.sendMessage(ComponentUtil.coloredText("Wystąpił problem ze strony serwera.", NamedTextColor.RED)
                    .hoverEvent(HoverEvent.showText(ComponentUtil.coloredText("Nie załadowano użytkownika", NamedTextColor.YELLOW))));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            event.setCancelled(true);
            return;
        }
        final Instant now = Instant.now();
        chatDelay.compute(player.getUniqueId(), (uuid, instant) -> {
            if (instant != null && now.isBefore(instant)) {
                player.sendActionBar(ComponentUtil.coloredText("Odczekaj chwilę między wysyłaniem wiadomości!", NamedTextColor.RED));
                event.setCancelled(true);
                return instant;
            }
            event.setCancelled(false);
            event.setMessage(filterMessage(event.getMessage().replace("%", "%%")));

            event.setFormat(ColorUtil.color(
                    MessageFormat.format("{0}{1} {2}",
                        RankManager.getChat(user.getRank(), user.getStuffRank()),
                        event.getPlayer().getName(),
                        RankManager.getMessageColor(user.getRank()))
            ) + event.getMessage());
            return now.plusMillis((long) (user.getRank().getChatSecondsDelay() * 1_000L));
        });
    }

    private @NotNull String fastScanReplace(String source, @NotNull String target, String replacement) {
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
        for (String badWord : ChatUtil.blockedWords) message = fastScanReplace(message, badWord, "*");
        return message;
    }

    @EventHandler
    public void preCommand(@NotNull PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        if (player.isOp()) return;
        event.setCancelled(true);
        final User user = User.Get(player);
        if (user == null) {
            player.sendMessage(ComponentUtil.coloredText("Wystąpił problem ze strony serwera.", NamedTextColor.RED)
                    .hoverEvent(HoverEvent.showText(ComponentUtil.coloredText("Nie załadowano użytkownika", NamedTextColor.YELLOW))));
            return;
        }
        final Instant now = Instant.now();
        commandDelay.compute(player.getUniqueId(), (uuid, instant) -> {
            if (instant != null && now.isBefore(instant)) {
                player.sendActionBar(ComponentUtil.coloredText("Odczekaj chwilę między wpisywaniem komend.", NamedTextColor.RED));
                return instant;
            }
            event.setCancelled(false);
            return now.plusMillis((long) (user.getRank().getCommandSecondsDelay() * 1_000L));
        });
    }

}
