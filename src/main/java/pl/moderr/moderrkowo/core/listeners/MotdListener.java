package pl.moderr.moderrkowo.core.listeners;

import com.destroystokyo.paper.event.server.PaperServerListPingEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerPreLoginEvent;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.HexResolver;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class MotdListener implements Listener {

    public static final LocalDateTime SERVER_START_DATE = LocalDateTime.of(2023, Month.MAY, 5, 16, 0);
    public static final List<String> ALLOW_PRESTART_NICKNAMES = Arrays.asList("MODERR", "Sosna__", "Dorszu", "Tyrkkson_");
    public final int LENGTH_MOTD = 59;

    @EventHandler
    public void preLogin(@NotNull AsyncPlayerPreLoginEvent e) {
        if (ALLOW_PRESTART_NICKNAMES.contains(e.getName())) {
            return;
        }
        if (LocalDateTime.now().isBefore(SERVER_START_DATE)) {
            e.setKickMessage(ModerrkowoPlugin.getServerName() + ColorUtils.color("\n&cPoczekaj na start sezonu V!\n&fza " + ChatUtil.getTime(SERVER_START_DATE) + "\n\n&7Więcej informacji\n&9Discord: &f" + ModerrkowoPlugin.getInstance().config.getString("discord-link")));
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            e.setResult(PlayerPreLoginEvent.Result.KICK_OTHER);
//            Main.getInstance().discordManager.sendTryJoin(e.getName());
        }
    }

    @EventHandler
    public void ping(PaperServerListPingEvent e) {
        String line1 = ColorUtils.color("&c&k:: &6&lModerrkowo.PL &c&k:: &7[1.19+]");
        if (LocalDateTime.now().isAfter(SERVER_START_DATE)) {
            String line2 = ColorUtils.color(HexResolver.parseHexString(ModerrkowoPlugin.getInstance().getConfig().getString("motd-secound")));
            String line3 = ":: Moderrkowo.PL :: [1.19+]";
            String line4 = ModerrkowoPlugin.getInstance().getConfig().getString("motd-secound2");
            e.setMotd(centerText(line3).replace(line3, line1) + "\n" + centerText("Zadania - Działki - Ekonomia - Dodatki").replace("Zadania - Działki - Ekonomia - Dodatki", ColorUtils.color("&aZadania &9- &6Działki &9- &eEkonomia &9- &cDodatki")));
        } else {
            String top = "Moderrkowo Sezon V";
            String coloredTop = ModerrkowoPlugin.getServerName() + ColorUtils.color(" &fSezon V");
            e.setMotd(centerText(top).replace(top, coloredTop) + "\n" + centerText("za " + ChatUtil.getTime(SERVER_START_DATE)));
        }
    }

    String centerText(String text) {
        StringBuilder builder = new StringBuilder(text);
        char space = ' ';
        int distance = (LENGTH_MOTD - text.length()) / 2;
        for (int i = 0; i < distance; ++i) {
            builder.insert(0, space);
            builder.append(space);
        }
        return builder.toString();
    }

}
