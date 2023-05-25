package pl.moderr.moderrkowo.core.api.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldBorder;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

public class RandomUtil {

    /**
     * @param world The world
     * @return The random location
     * @deprecated This method is deprecated because it ignores the center of the border, is slow and old.
     */
    @Deprecated
    public static Location getRandom(@NotNull World world) {
        Random rand = new Random();
        int rangeMax = +((int) (world.getWorldBorder().getSize()) / 2);
        int rangeMin = -((int) (world.getWorldBorder().getSize()) / 2);
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

    @Deprecated
    public static int getRandomInt(int rangeMin, int rangeMax) {
        Random rand = new Random();
        return rand.nextInt((rangeMax - rangeMin) + 1) + rangeMin;
    }

    public static @NotNull Location worldLocation(final @NotNull World world, final @NotNull Random random) {
        final WorldBorder worldBorder = world.getWorldBorder();
        final Location center = worldBorder.getCenter();
        final double size = worldBorder.getSize();
        final double radius = size / 2;

        final int minX = (int) center.getX();
        final int maxX = (int) (center.getX() + radius);

        final int minZ = (int) center.getZ();
        final int maxZ = (int) (center.getZ() + radius);

        final double x = inRange(minX, maxX, random) + 0.5;
        final double z = inRange(minZ, maxZ, random) + 0.5;
        final double y = world.getHighestBlockYAt((int) x, (int) z) + 1;

        return new Location(world, x, y, z);
    }

    public static @NotNull Location worldLocation(World world, long seed) {
        return worldLocation(world, new Random(seed));
    }

    public static @NotNull Location worldLocation(World world) {
        return worldLocation(world, new Random());
    }

    public static int inRange(final int min, final int max, final @NotNull Random random) {
        return random.nextInt(max - min + 1) + min;
    }

    public static int inRange(final int min, final int max, long seed) {
        return inRange(min, max, new Random(seed));
    }

    public static int inRange(final int min, final int max) {
        return inRange(min, max, new Random());
    }
}
