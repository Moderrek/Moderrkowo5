package pl.moderr.moderrkowo.core.services.antylogout;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.boss.BossBar;

@Data
@AllArgsConstructor
public class AntyLogoutData {
    private final BossBar bossBar;
    private long ticks;
}