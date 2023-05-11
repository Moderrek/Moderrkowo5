package pl.moderr.moderrkowo.core.listeners;

import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.commands.admin.ChatCommand;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.ranks.RankManager;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.HexResolver;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.time.Instant;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.UUID;

public class ChatListener implements Listener {

    private final ArrayList<String> blockedCmds = new ArrayList<>() {
        {
            add("minecraft:help");
            add("bukkit:help");
            add("plugins");
            add("bukkit:plugins");
            add("about");
            add("bukkit:about");
            add("pl");
            add("bukkit:pl");
            add("ver");
            add("bukkit:ver");
            add("version");
            add("bukkit:version");
            add("minecraft:msg");
            add("minecraft:help");
            add("seed");
            add("minecraft:seed");
            add("?");
            add("bukkit:?");
            add("icanhasbukkit");
        }
    };
    private final Map<UUID, Instant> chatDelay = new IdentityHashMap<>();

    private final Map<UUID, Instant> commandDelay = new IdentityHashMap<>();

    @EventHandler
    public void chat(@NotNull final AsyncPlayerChatEvent e) {
        if (!e.getPlayer().isOp() && !ChatCommand.canChat) {
            e.getPlayer().sendMessage(ColorUtils.color("&cChat jest wyłączony!"));
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            e.setCancelled(true);
            return;
        }
        User u = UserManager.getUser(e.getPlayer().getUniqueId());
        if (u == null) {
            e.getPlayer().sendMessage(ColorUtils.color("&cWystąpił problem podczas wysyłania wiadomości."));
            e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            e.setCancelled(true);
            return;
        }
        final Instant now = Instant.now();
        chatDelay.compute(e.getPlayer().getUniqueId(), (uuid, instant) -> {
            if (instant != null && now.isBefore(instant)) {
                e.getPlayer().sendActionBar(ColorUtils.color("&cOdczekaj chwilę miedzy wysyłaniem wiadomości"));
                e.setCancelled(true);
                return instant;
            }
            e.setCancelled(false);
            e.setMessage(e.getMessage().replace("%", "%%"));
            e.setMessage(filterMessage(e.getMessage()));
            double max = 0;
            switch (u.getRank()) {
                case None:
                    max = 1.5d;
                    e.setMessage(ColorUtils.color(e.getMessage()));
                    break;
                case Zelazo:
                    max = 0.7d;
                    e.setMessage(ColorUtils.color(e.getMessage()));
                    break;
                case Zloto:
                    max = 0.5d;
                    e.setMessage(ColorUtils.color(e.getMessage()));
                    break;
                case Diament:
                    max = 0.3d;
                    e.setMessage(ColorUtils.color(HexResolver.parseHexString(e.getMessage())));
                    break;
                case Emerald:
                    max = 0.2d;
                    e.setMessage(ColorUtils.color(HexResolver.parseHexString(e.getMessage())));
                    break;
            }
            e.setFormat(ColorUtils.color(RankManager.getChat(u.getRank(), u.getStuffRank()) + e.getPlayer().getName() + " " + RankManager.getMessageColor(u.getRank())) + e.getMessage());
            return now.plusMillis((long) (max * 1000));
        });
    }

    private String replace(String source, String target, String replacement) {
        StringBuilder sbSource = new StringBuilder(source);
        StringBuilder sbSourceLower = new StringBuilder(source.toLowerCase());
        String searchString = target.toLowerCase();

        int idx = 0;
        while ((idx = sbSourceLower.indexOf(searchString, idx)) != -1) {
            sbSource.replace(idx, idx + searchString.length(), replacement);
            sbSourceLower.replace(idx, idx + searchString.length(), replacement);
            idx += replacement.length();
        }
        sbSourceLower.setLength(0);
        sbSourceLower.trimToSize();
        sbSourceLower = null;

        return sbSource.toString();
    }

    private String filterMessage(String message) {
        for (String badWord : ChatUtil.bannableWords) {
            message = replace(message, badWord, "*");
        }
        return message;
    }

    @EventHandler
    public void preCommand(PlayerCommandPreprocessEvent e) {
        if (e.getPlayer().isOp()) {
            return;
        }
        boolean blocked = false;
        String command = e.getMessage().toLowerCase();
        for (String s : blockedCmds) {
            if (command.startsWith("/" + s)) {
                if (command.startsWith("/helpop")) {
                    e.setCancelled(false);
                    continue;
                }
                blocked = true;
                e.getPlayer().sendMessage(ColorUtils.color("&cNieznana komenda."));
                Logger.logAdminLog(e.getPlayer().getName() + " chciał wpisać nielegalną komendę. " + e.getMessage());
            }
        }
        e.setCancelled(true);
        if (blocked) {
            return;
        }
        User u = UserManager.getUser(e.getPlayer().getUniqueId());
        if (u == null) {
            e.getPlayer().sendMessage(ColorUtils.color("&cNie załadowano użytkownika!"));
            return;
        }
        final Instant now = Instant.now();
        commandDelay.compute(e.getPlayer().getUniqueId(), (uuid, instant) -> {
            if (instant != null && now.isBefore(instant)) {
                e.getPlayer().sendActionBar(ColorUtils.color("&cOdczekaj chwilę miedzy wpisywaniem komend"));
                return instant;
            }
            e.setCancelled(false);
            double max = 0;
            switch (u.getRank()) {
                case None:
                    max = 3d;
                    break;
                case Zelazo:
                case Zloto:
                    max = 2d;
                    break;
                case Diament:
                    max = 1d;
                    break;
                case Emerald:
                    max = 0.5d;
                    break;
            }
            return now.plusNanos((long) (max * 1000));
        });
    }

}
