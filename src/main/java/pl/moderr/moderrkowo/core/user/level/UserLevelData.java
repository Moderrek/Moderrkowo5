package pl.moderr.moderrkowo.core.user.level;

import com.google.gson.annotations.Expose;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

import java.text.MessageFormat;
import java.util.UUID;

public class UserLevelData {

    @Expose(serialize = true, deserialize = true)
    private final UUID owner;
    @Expose(serialize = true, deserialize = true)
    private final LevelCategory category;
    @Expose(serialize = false, deserialize = false)
    BukkitTask bukkitTask = null;
    @Expose(serialize = false, deserialize = false)
    private BossBar bossBar;
    @Expose(serialize = true, deserialize = true)
    private double exp;
    @Expose(serialize = true, deserialize = true)
    private int level;

    public UserLevelData(UUID owner, int level, double exp, LevelCategory category) {
        this.owner = owner;
        this.level = level;
        this.exp = exp;
        this.category = category;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void addLevel(int level) {
        this.level += level;
    }

    public void subtractLevel(int level) {
        this.level -= level;
    }

    public boolean hasLevel(int level) {
        return this.level >= level;
    }

    public double getExp() {
        return exp;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public void addExp(double howManyExp) {
        final User user = UserManager.getUser(owner);
        final Player player = user.getPlayer();

        // Reset bukkit task
        if (bukkitTask != null) {
            bossBar.removePlayer(player);
            bukkitTask.cancel();
            bukkitTask = null;
        }
        final int level = this.level;
        final double multiplier = user.getRank().getPlayerLevelMultiplier();

        double exp = howManyExp * multiplier;
        this.exp += exp;

        boolean hasLeveledUp = false;
        while (this.exp >= expNeededToNextLevel(this.level)) {
            this.exp -= expNeededToNextLevel(this.level);
            hasLeveledUp = true;
            addLevel(1);
        }

        if (hasLeveledUp) {
            player.sendMessage(ColorUtils.color("  "));
            player.sendMessage(ColorUtils.color("  "));
            player.sendMessage(ColorUtils.color("  &fGratulacje odblokowałeś &a" + getLevel() + " poziom &f" + category.toString() + "!"));
            player.sendMessage(ColorUtils.color("  "));
            player.sendMessage(ColorUtils.color("  "));
            final String title = MessageFormat.format("{0}&l{1}",
                    UserLevel.levelCategoryColorString(category),
                    category.toString().toUpperCase());
            final String subtitle = MessageFormat.format("&7[{0}{1}&7] → [{2}{3}&7]",
                    UserLevel.levelCategoryColorString(category),
                    level,
                    UserLevel.levelCategoryColorString(category),
                    this.level);
            player.sendTitle(ColorUtils.color(title), ColorUtils.color(subtitle));
            player.spawnParticle(Particle.TOTEM, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 20, 1, 1, 1, 0.1f);
            player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1, 2);
        }
        if (bossBar == null) {
            bossBar = Bukkit.createBossBar(null, UserLevel.levelCategoryColor(category), BarStyle.SOLID);
        }
        try {
            final String title = MessageFormat.format("{0}+{1}pd {2} &f({3}/{4})", UserLevel.levelCategoryColorString(category), ChatUtil.getNumber(exp), category, ChatUtil.getNumber(this.exp), ChatUtil.getNumber(expNeededToNextLevel(this.level)));
            bossBar.setTitle(ColorUtils.color(title));
            bossBar.setProgress(this.exp / expNeededToNextLevel(this.level));
            bossBar.addPlayer(player);
            bossBar.setStyle(BarStyle.SEGMENTED_6);
            bukkitTask = Bukkit.getScheduler().runTaskLater(ModerrkowoPlugin.getInstance(), () -> {
                bossBar.removePlayer(player);
                bukkitTask = null;
            }, 20 * 3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void subtractExp(double exp) {
        this.exp -= exp;
        if (this.exp < 0) {
            subtractLevel(1);
            double temp = this.exp;
            this.exp = expNeededToNextLevel(this.level) - temp;
        }
    }

    public double expNeededToNextLevel(int level) {
        return Math.round(Math.pow(Math.pow(level, 3), 0.33) * 80);
    }

    public LevelCategory getCategory() {
        return category;
    }
}
