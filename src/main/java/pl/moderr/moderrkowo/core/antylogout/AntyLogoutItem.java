package pl.moderr.moderrkowo.core.antylogout;

import org.bukkit.boss.BossBar;
import org.jetbrains.annotations.Contract;

public class AntyLogoutItem {

    private final BossBar bossBar;
    private int ticks;

    @Contract(pure = true)
    public AntyLogoutItem(BossBar bossBar, int seconds) {
        this.ticks = seconds;
        this.bossBar = bossBar;
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public int getTicks() {
        return ticks;
    }

    public void setTicks(int ticks) {
        this.ticks = ticks;
    }
}
