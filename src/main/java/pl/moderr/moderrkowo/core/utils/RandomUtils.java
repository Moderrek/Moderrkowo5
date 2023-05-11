package pl.moderr.moderrkowo.core.utils;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Random;

public class RandomUtils {
    public static Location getRandom(World world) {
        Random rand = new Random();
        int rangeMax = +(WorldUtils.getWorldBorder(world) / 2);
        int rangeMin = -(WorldUtils.getWorldBorder(world) / 2);
        int X = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
        int Z = rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
        int Y = world.getHighestBlockYAt(X, Z);
        Location location;
        if (world.getBlockAt(X, Y, Z).isLiquid() || !world.getBlockAt(X, Y, Z).isSolid()) {
            location = getRandom(world);
        } else {
            location = new Location(world, X, Y, Z).add(0.5, 1, 0.5);
        }
        return location;
    }

    public static int getRandomInt(int rangeMin, int rangeMax) {
        Random rand = new Random();
        return rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
    }
}
