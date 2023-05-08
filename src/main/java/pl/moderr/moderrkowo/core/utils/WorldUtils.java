package pl.moderr.moderrkowo.core.utils;

import org.bukkit.World;

public class WorldUtils {
    public static int getWorldBorder(World world) {
        return (int) world.getWorldBorder().getSize();
    }
}
