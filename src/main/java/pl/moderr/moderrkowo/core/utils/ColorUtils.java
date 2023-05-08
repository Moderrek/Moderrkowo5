package pl.moderr.moderrkowo.core.utils;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColorUtils {

    private static final Pattern pattern = Pattern.compile("#[a-fA-f0-9]{6}");

    public static String color(String message) {
        Matcher matcher = pattern.matcher(message);

        while (matcher.find()) {
            String color = message.substring(matcher.start(), matcher.end());
            message = message.replace(color, "" + net.md_5.bungee.api.ChatColor.of(color));
            matcher = pattern.matcher(message);
        }

        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static final Color DARK_RED = new Color(170, 0, 0);
    public static final Color RED = new Color(255,85,85);
    public static final Color GOLD = new Color(255,170,0);
    public static final Color YELLOW = new Color(255,255,85);
    public static final Color DARK_GREEN = new Color(0,170,0);
    public static final Color GREEN = new Color(85,255,85);
    public static final Color BLUE = new Color(85,85,255);

}
