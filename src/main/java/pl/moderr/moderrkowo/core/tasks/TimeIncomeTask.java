package pl.moderr.moderrkowo.core.tasks;

import lombok.val;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.RandomUtils;

import java.text.MessageFormat;

public class TimeIncomeTask implements Runnable {
    @Override
    public void run() {
        for (val u : UserManager.getUsers()) {
            final Player player = u.getPlayer();
            final double randomIncome = RandomUtils.getRandomInt(2, 6) * u.getUserLevel().playerLevel() * u.getRank().getIncomeMultiplierTime();
            u.addMoney(randomIncome);
            player.sendMessage(Component.text(MessageFormat.format("Otrzymano {0} za aktywność na serwerze", ChatUtil.getMoney(randomIncome))).color(NamedTextColor.GREEN));
        }
    }
}
