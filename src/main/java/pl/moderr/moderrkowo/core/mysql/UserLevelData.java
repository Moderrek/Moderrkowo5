package pl.moderr.moderrkowo.core.mysql;

import com.google.gson.annotations.Expose;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

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

    public void addExp(double expadd) {
        double max = 0;
        switch (UserManager.getUser(owner).getRank()) {
            case None:
                max = 1d;
                break;
            case Zelazo:
            case Zloto:
                max = 1.5d;
                break;
            case Diament:
                max = 2d;
                break;
            case Emerald:
                max = 3d;
                break;
        }
        Player p = Bukkit.getPlayer(owner);
        if (bukkitTask != null) {
            bossBar.removePlayer(p);
            bukkitTask.cancel();
            bukkitTask = null;
        }
        double exp = expadd * max;
        this.exp += exp;
        if (this.exp >= expNeededToNextLevel(this.level)) {
            this.exp -= expNeededToNextLevel(this.level);
            addLevel(1);
            p.sendMessage(ColorUtils.color("  "));
            p.sendMessage(ColorUtils.color("  "));
            p.sendMessage(ColorUtils.color("  &fGratulacje odblokowałeś &a" + getLevel() + " poziom &f" + category.toString() + "!"));
            p.sendMessage(ColorUtils.color("  "));
            p.sendMessage(ColorUtils.color("  "));
            p.sendTitle(ColorUtils.color(UserLevel.levelCategoryColorString(category) + "&l" + category.toString().toUpperCase()), ColorUtils.color("&7[" + UserLevel.levelCategoryColorString(category) + (this.level - 1) + "&7] → [" + UserLevel.levelCategoryColorString(category) + this.level + "&7]"));
            p.spawnParticle(Particle.TOTEM, p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), 20, 1, 1, 1, 0.1f);
            p.playSound(p.getLocation(), Sound.ENTITY_WITHER_SHOOT, 1, 2);
        }
        if (bossBar == null) {
            bossBar = Bukkit.createBossBar("TITLE", UserLevel.levelCategoryColor(category), BarStyle.SOLID);
        }
        try {
            bossBar.setTitle(ColorUtils.color(UserLevel.levelCategoryColorString(category) + "+" + ChatUtil.getNumber(exp) + " " + category.toString() + " &f(" + ChatUtil.getNumber(this.exp) + "/" + ChatUtil.getNumber(expNeededToNextLevel(this.level)) + ")"));
            bossBar.setProgress(this.exp / expNeededToNextLevel(this.level));
            bossBar.addPlayer(p);
            bukkitTask = Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                bossBar.removePlayer(p);
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
            double temp = +this.exp;
            this.exp = expNeededToNextLevel(this.level) - temp;
        }
    }

    public double expNeededToNextLevel(int level) {
        double expToLevel = 60;
        if (level > 10) {
            if (level > 20) {
                if (level >= 40) {
                    if (level >= 50) {
                        if (level >= 75) {
                            return 40000;
                        }
                        return expToLevel * (level / 2) * 2.5d;
                    }
                    return expToLevel * (level / 2) * 2d;
                }
                return expToLevel * (level / 2) * 1.5d;
            }
            return expToLevel * (level / 2) * 1.2d;
        } else {
            return expToLevel * (level / 2);
        }
    }

    public LevelCategory getCategory() {
        return category;
    }
}
