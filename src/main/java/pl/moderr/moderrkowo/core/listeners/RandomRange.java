package pl.moderr.moderrkowo.core.listeners;

import org.jetbrains.annotations.Contract;
import pl.moderr.moderrkowo.core.utils.RandomUtils;

public class RandomRange {
    private int min;
    private int max;

    @Contract(pure = true)
    public RandomRange(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getRandom() {
        return RandomUtils.getRandomInt(min, max);
    }
}
