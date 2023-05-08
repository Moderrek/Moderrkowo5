package pl.moderr.moderrkowo.core.events.drop;

import org.bukkit.Material;
import org.jetbrains.annotations.Contract;

public class DropItem {

    public int min;
    public int max;
    public int count;
    public final boolean isRandom;
    public final Material mat;

    @Contract(pure = true)
    public DropItem(final Material mat, final int min, final int max) {
        this.isRandom = true;
        this.mat = mat;
        this.min = min;
        this.max = max;
    }

    @Contract(pure = true)
    public DropItem(final Material mat, final int count) {
        this.mat = mat;
        this.count = count;
        this.isRandom = false;
    }

    @Contract(pure = true)
    public DropItem(final Material mat) {
        this.mat = mat;
        this.isRandom = false;
        this.count = 1;
    }

}
