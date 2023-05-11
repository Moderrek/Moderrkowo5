package pl.moderr.moderrkowo.core.utils;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.NumberFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ChatUtil {

    public static final int MINUTES_PER_HOUR = 60;
    public static final int SECONDS_PER_MINUTE = 60;
    public static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

    public static List<String> bannableWords = Arrays.asList("chuj", "chuja", "chujek", "chuju", "chujem", "chujnia",
            "chujowy", "chujowa", "chujowe", "cipa", "cipę", "cipe", "cipą",
            "cipie", "cipsko", "cipeczka", "cipunia", "dojebać", "dojebac", "dojebie", "dojebał", "dojebal",
            "dojebała", "dojebala", "dojebałem", "dojebalem", "dojebałam",
            "dojebalam", "dojebię", "dojebie", "dopieprzać", "dopieprzac",
            "dopierdalać", "dopierdalac", "dopierdala", "dopierdalał",
            "dopierdalal", "dopierdalała", "dopierdalala", "dopierdoli",
            "dopierdolił", "dopierdolil", "dopierdolę", "dopierdole", "dopierdoli",
            "dopierdalający", "dopierdalajacy", "dopierdolić", "dopierdolic", "huj",
            "hujek", "hujnia", "huja", "huje", "hujem", "huju", "jebać", "jebac",
            "jebał", "jebal", "jebie", "jebią", "jebia", "jebak", "jebaka", "jebal",
            "jebał", "jebany", "jebane", "jebanka", "jebanko", "jebankiem",
            "jebanymi", "jebana", "jebanym", "jebanej", "jebaną", "jebana",
            "jebani", "jebanych", "jebanymi", "jebcie", "jebiący", "jebiacy",
            "jebiąca", "jebiaca", "jebiącego", "jebiacego", "jebiącej", "jebiacej",
            "jebia", "jebią", "jebie", "jebię", "jebliwy", "jebnąć", "jebnac",
            "jebnąc", "jebnać", "jebnął", "jebnal", "jebną", "jebna", "jebnęła",
            "jebnela", "jebnie", "jebnij", "jebut", "koorwa", "kórwa", "kurestwo",
            "kurew", "kurewski", "kurewska", "kurewskiej", "kurewską", "kurewska",
            "kurewsko", "kurewstwo", "kurwa", "kurwaa", "kurwami", "kurwą", "kurwe",
            "kurwę", "kurwie", "kurwiska", "kurwo", "kurwy", "kurwach", "kurwami",
            "kurewski", "kurwiarz", "kurwiący", "kurwica", "kurwić", "kurwic",
            "kurwidołek", "kurwik", "kurwiki", "kurwiszcze", "kurwiszon",
            "kurwiszona", "kurwiszonem", "kurwiszony", "kutas", "kutasa", "kutasie",
            "kutasem", "kutasy", "kutasów", "kutasow", "kutasach", "kutasami",
            "matkojebca", "matkojebcy", "matkojebcą", "matkojebca", "matkojebcami",
            "matkojebcach", "nabarłożyć", "najebać", "najebac", "najebał",
            "najebal", "najebała", "najebala", "najebane", "najebany", "najebaną",
            "najebana", "najebie", "najebią", "najebia", "naopierdalać",
            "naopierdalac", "naopierdalał", "naopierdalal", "naopierdalała",
            "naopierdalala", "naopierdalała", "napierdalać", "napierdalac",
            "napierdalający", "napierdalajacy", "napierdolić", "napierdolic",
            "nawpierdalać", "nawpierdalac", "nawpierdalał", "nawpierdalal",
            "nawpierdalała", "nawpierdalala", "obsrywać", "obsrywac", "obsrywający",
            "obsrywajacy", "odpieprzać", "odpieprzac", "odpieprzy", "odpieprzył",
            "odpieprzyl", "odpieprzyła", "odpieprzyla", "odpierdalać",
            "odpierdalac", "odpierdol", "odpierdolił", "odpierdolil",
            "odpierdoliła", "odpierdolila", "odpierdoli", "odpierdalający",
            "odpierdalajacy", "odpierdalająca", "odpierdalajaca", "odpierdolić",
            "odpierdolic", "odpierdoli", "odpierdolił", "opieprzający",
            "opierdalać", "opierdalac", "opierdala", "opierdalający",
            "opierdalajacy", "opierdol", "opierdolić", "opierdolic", "opierdoli",
            "opierdolą", "opierdola", "piczka", "pieprznięty", "pieprzniety",
            "pieprzony", "pierdel", "pierdlu", "pierdolą", "pierdola", "pierdolący",
            "pierdolacy", "pierdoląca", "pierdolaca", "pierdol", "pierdole",
            "pierdolenie", "pierdoleniem", "pierdoleniu", "pierdolę", "pierdolec",
            "pierdola", "pierdolą", "pierdolić", "pierdolicie", "pierdolic",
            "pierdolił", "pierdolil", "pierdoliła", "pierdolila", "pierdoli",
            "pierdolnięty", "pierdolniety", "pierdolisz", "pierdolnąć",
            "pierdolnac", "pierdolnął", "pierdolnal", "pierdolnęła", "pierdolnela",
            "pierdolnie", "pierdolnięty", "pierdolnij", "pierdolnik", "pierdolona",
            "pierdolone", "pierdolony", "pierdołki", "pierdzący", "pierdzieć",
            "pierdziec", "pizda", "pizdą", "pizde", "pizdę", "piździe", "pizdzie",
            "pizdnąć", "pizdnac", "pizdu", "podpierdalać", "podpierdalac",
            "podpierdala", "podpierdalający", "podpierdalajacy", "podpierdolić",
            "podpierdolic", "podpierdoli", "pojeb", "pojeba", "pojebami",
            "pojebani", "pojebanego", "pojebanemu", "pojebani", "pojebany",
            "pojebanych", "pojebanym", "pojebanymi", "pojebem", "pojebać",
            "pojebac", "pojebalo", "popierdala", "popierdalac", "popierdalać",
            "popierdolić", "popierdolic", "popierdoli", "popierdolonego",
            "popierdolonemu", "popierdolonym", "popierdolone", "popierdoleni",
            "popierdolony", "porozpierdalać", "porozpierdala", "porozpierdalac",
            "poruchac", "poruchać", "przejebać", "przejebane", "przejebac",
            "przyjebali", "przepierdalać", "przepierdalac", "przepierdala",
            "przepierdalający", "przepierdalajacy", "przepierdalająca",
            "przepierdalajaca", "przepierdolić", "przepierdolic", "przyjebać",
            "przyjebac", "przyjebie", "przyjebała", "przyjebala", "przyjebał",
            "przyjebal", "przypieprzać", "przypieprzac", "przypieprzający",
            "przypieprzajacy", "przypieprzająca", "przypieprzajaca",
            "przypierdalać", "przypierdalac", "przypierdala", "przypierdoli",
            "przypierdalający", "przypierdalajacy", "przypierdolić",
            "przypierdolic", "qrwa", "rozjebać", "rozjebac", "rozjebie",
            "rozjebała", "rozjebią", "rozpierdalać", "rozpierdalac", "rozpierdala",
            "rozpierdolić", "rozpierdolic", "rozpierdole", "rozpierdoli",
            "rozpierducha", "skurwić", "skurwiel", "skurwiela", "skurwielem",
            "skurwielu", "skurwysyn", "skurwysynów", "skurwysynow", "skurwysyna",
            "skurwysynem", "skurwysynu", "skurwysyny", "skurwysyński",
            "skurwysynski", "skurwysyństwo", "skurwysynstwo", "spieprzać",
            "spieprzac", "spieprza", "spieprzaj", "spieprzajcie", "spieprzają",
            "spieprzaja", "spieprzający", "spieprzajacy", "spieprzająca",
            "spieprzajaca", "spierdalać", "spierdalac", "spierdala", "spierdalał",
            "spierdalała", "spierdalal", "spierdalalcie", "spierdalala",
            "spierdalający", "spierdalajacy", "spierdolić", "spierdolic",
            "spierdoli", "spierdoliła", "spierdoliło", "spierdolą", "spierdola",
            "srać", "srac", "srający", "srajacy", "srając", "srajac", "sraj",
            "sukinsyn", "sukinsyny", "sukinsynom", "sukinsynowi", "sukinsynów",
            "sukinsynow", "śmierdziel", "udupić", "ujebać", "ujebac", "ujebał",
            "ujebal", "ujebana", "ujebany", "ujebie", "ujebała", "ujebala",
            "upierdalać", "upierdalac", "upierdala", "upierdoli", "upierdolić",
            "upierdolic", "upierdoli", "upierdolą", "upierdola", "upierdoleni",
            "wjebać", "wjebac", "wjebie", "wjebią", "wjebia", "wjebiemy",
            "wjebiecie", "wkurwiać", "wkurwiac", "wkurwi", "wkurwia", "wkurwiał",
            "wkurwial", "wkurwiający", "wkurwiajacy", "wkurwiająca", "wkurwiajaca",
            "wkurwić", "wkurwic", "wkurwi", "wkurwiacie", "wkurwiają", "wkurwiali",
            "wkurwią", "wkurwia", "wkurwimy", "wkurwicie", "wkurwiacie", "wkurwić",
            "wkurwic", "wkurwia", "wpierdalać", "wpierdalac", "wpierdalający",
            "wpierdalajacy", "wpierdol", "wpierdolić", "wpierdolic", "wpizdu",
            "wyjebać", "wyjebac", "wyjebali", "wyjebał", "wyjebac", "wyjebała",
            "wyjebały", "wyjebie", "wyjebią", "wyjebia", "wyjebiesz", "wyjebie",
            "wyjebiecie", "wyjebiemy", "wypieprzać", "wypieprzac", "wypieprza",
            "wypieprzał", "wypieprzal", "wypieprzała", "wypieprzala", "wypieprzy",
            "wypieprzyła", "wypieprzyla", "wypieprzył", "wypieprzyl", "wypierdal",
            "wypierdalać", "wypierdalac", "wypierdala", "wypierdalaj",
            "wypierdalał", "wypierdalal", "wypierdalała", "wypierdalala",
            "wypierdalać", "wypierdolić", "wypierdolic", "wypierdoli",
            "wypierdolimy", "wypierdolicie", "wypierdolą", "wypierdola",
            "wypierdolili", "wypierdolił", "wypierdolil", "wypierdoliła",
            "wypierdolila", "zajebać", "zajebac", "zajebie", "zajebią", "zajebia",
            "zajebiał", "zajebial", "zajebała", "zajebiala", "zajebali", "zajebana",
            "zajebani", "zajebane", "zajebany", "zajebanych", "zajebanym",
            "zajebanymi", "zapieprzyć",
            "zapieprzyc", "zapieprzy", "zapieprzył", "zapieprzyl", "zapieprzyła",
            "zapieprzyla", "zapieprzą", "zapieprza", "zapieprzy", "zapieprzymy",
            "zapieprzycie", "zapieprzysz", "zapierdala", "zapierdalać",
            "zapierdalac", "zapierdalaja", "zapierdalał", "zapierdalaj",
            "zapierdalajcie", "zapierdalała", "zapierdalala", "zapierdalali",
            "zapierdalający", "zapierdalajacy", "zapierdolić", "zapierdolic",
            "zapierdoli", "zapierdolił", "zapierdolil", "zapierdoliła",
            "zapierdolila", "zapierdolą", "zapierdola", "zapierniczać",
            "zapierniczający", "zasrać", "zasranym", "zasrywać", "zasrywający",
            "zesrywać", "zesrywający", "zjebać", "zjebac", "zjebał", "zjebal",
            "zjebała", "zjebala", "zjebana", "zjebią", "zjebali", "zjeby", "zjeb", "zjebka", "wyjebka", "kurwił", "kurwil", "marihuaną", "marihuana", "mefedron", "koka", "kokaina", "jebancu", "jebańcu");

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

    public static @NotNull String getTicksToTime(int ticks) {
        if (ticks > 20 * 60) {
            if (ticks > 20 * 60 * 60) {
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
                nf.setMaximumFractionDigits(2);
                if (ticks > 20 * 60 * 60 * 24) {
                    return (nf.format((double) ticks / 20 / 60 / 60 / 24)) + "d";
                } else {
                    return (nf.format((double) ticks / 20 / 60 / 60)) + "h";
                }
            } else {
                return (ticks / 20 / 60) + "m";
            }
        } else {
            return (ticks / 20) + "s";
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
            return nf.parse(money.substring(0, 1)).doubleValue();
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
        NumberFormat nf;
        if (money > 1000) {
            nf = NumberFormat.getCompactNumberInstance(Locale.US, NumberFormat.Style.SHORT);
        } else {
            nf = NumberFormat.getNumberInstance(Locale.US);
        }
        nf.setMaximumFractionDigits(2);
        return nf.format(money) + "$";
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
