package pl.moderr.moderrkowo.core.utils;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Locale;

public class ChatUtil {

    public static final int MINUTES_PER_HOUR = 60;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

    public static void clearChat(final Player player) {
        for (int i = 0; i < 100; i++) {
            player.sendMessage(" ");
        }
    }

    public static String centerText(final String text) {
        StringBuilder builder = new StringBuilder(text);
        char space = ' ';
        int distance = (53 - text.length()) / 2;
        for (int i = 0; i < distance; ++i) {
            builder.insert(0, space);
            builder.append(space);
        }
        return builder.toString();
    }

    public static String centerText(final String text, final int dlugosc) {
        StringBuilder builder = new StringBuilder(text);
        char space = ' ';
        int distance = (dlugosc - text.length()) / 2;
        for (int i = 0; i < distance; ++i) {
            builder.insert(0, space);
            builder.append(space);
        }
        return builder.toString();
    }

    public static String getTicksToTime(final int ticks) {
        if (ticks > 20 * 60) {
            if (ticks > 20 * 60 * 60) {
                DecimalFormat df2 = new DecimalFormat("#.##");
                if (ticks > 20 * 60 * 60 * 24) {
                    return (df2.format((double) ticks / 20 / 60 / 60 / 24)) + " dni";
                } else {
                    return (df2.format((double) ticks / 20 / 60 / 60)) + " godz.";
                }
            } else {
                return (ticks / 20 / 60) + " min";
            }
        } else {
            return (ticks / 20) + " sek.";
        }
    }

    public static String materialName(final Biome biome) {
        String materialName = biome.toString();
        materialName = materialName.replaceAll("_", " ");
        materialName = materialName.toLowerCase();
        return WordUtils.capitalizeFully(materialName);
    }

    public static String materialName(final String name) {
        String materialName = name;
        materialName = materialName.replaceAll("_", " ");
        materialName = materialName.toLowerCase();
        return WordUtils.capitalizeFully(materialName);
    }

    public static String materialName(final Material material) {
        String materialName = material.toString();
        materialName = materialName.replaceAll("_", " ");
        materialName = materialName.toLowerCase();
        return WordUtils.capitalizeFully(materialName);
    }

    public static String materialName(final EntityType material) {
        String materialName = material.toString();
        materialName = materialName.replaceAll("_", " ");
        materialName = materialName.toLowerCase();
        return WordUtils.capitalizeFully(materialName);
    }

    public static double parseMoney(final String money) throws Exception {
        try {
            NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pl-PL"));
            nf.setMaximumFractionDigits(2);
            return nf.parse(money.substring(0, 3)).doubleValue();
        } catch (Exception e) {
            throw new Exception("Nie można przeliczyć");
        }
    }

    public static String getNumber(final double money) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pl-PL"));
        nf.setMaximumFractionDigits(2);
        return nf.format(money);
    }

    public static String getMoney(final double money) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pl-PL"));
        nf.setMaximumFractionDigits(2);
        return nf.format(money) + " zł";
    }

    public static String getSeasonCoins(final int coins) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pl-PL"));
        nf.setMaximumFractionDigits(2);
        return nf.format(coins) + " ⛃";
    }

    public static String getTime(LocalDateTime expire) {
        LocalDateTime now = LocalDateTime.now();
        if (now.isAfter(expire)) {
            return "Już";
        } else {
            LocalDateTime tempDateTime = now;

            long years = tempDateTime.until(expire, ChronoUnit.YEARS);
            tempDateTime = tempDateTime.plusYears(years);

            long months = tempDateTime.until(expire, ChronoUnit.MONTHS);
            tempDateTime = tempDateTime.plusMonths(months);

            long days = tempDateTime.until(expire, ChronoUnit.DAYS);
            tempDateTime = tempDateTime.plusDays(days);


            long hours = tempDateTime.until(expire, ChronoUnit.HOURS);
            tempDateTime = tempDateTime.plusHours(hours);

            long minutes = tempDateTime.until(expire, ChronoUnit.MINUTES);
            tempDateTime = tempDateTime.plusMinutes(minutes);

            long seconds = tempDateTime.until(expire, ChronoUnit.SECONDS);

            return days + "d " + hours + "h " + minutes + "m " + seconds + "s";
        }
    }

    private static Period getPeriod(LocalDateTime dob, LocalDateTime now) {
        return Period.between(dob.toLocalDate(), now.toLocalDate());
    }

    private static long[] getTime(LocalDateTime dob, LocalDateTime now) {
        LocalDateTime today = LocalDateTime.of(now.getYear(),
                now.getMonthValue(), now.getDayOfMonth(), dob.getHour(), dob.getMinute(), dob.getSecond());
        Duration duration = Duration.between(today, now);

        long seconds = duration.getSeconds();

        long hours = seconds / SECONDS_PER_HOUR;
        long minutes = ((seconds % SECONDS_PER_HOUR) / SECONDS_PER_MINUTE);
        long secs = (seconds % SECONDS_PER_MINUTE);

        return new long[]{hours, minutes, secs};
    }

    public static String getWPLN(double cost) {
        NumberFormat nf = NumberFormat.getNumberInstance(new Locale("pl-PL"));
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        return nf.format(cost) + " PLN";
    }
}
