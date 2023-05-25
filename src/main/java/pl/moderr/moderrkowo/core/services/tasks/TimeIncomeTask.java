package pl.moderr.moderrkowo.core.services.tasks;

import lombok.val;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.entity.Player;
import pl.moderr.moderrkowo.core.api.util.ChatUtil;
import pl.moderr.moderrkowo.core.api.util.RandomUtil;
import pl.moderr.moderrkowo.core.services.mysql.UserManager;

import java.text.MessageFormat;

public class TimeIncomeTask implements Runnable {
    @Override
    public void run() {
        for (val u : UserManager.getUsers()) {
            final Player player = u.getPlayer();
            final double randomIncome = RandomUtil.getRandomInt(2, 6) * u.getLevel().playerLevel() * u.getRank().getIncomeMultiplierTime();
            u.addMoney(randomIncome);
            player.sendMessage(Component.text(MessageFormat.format("Otrzymano {0} za aktywność na serwerze", ChatUtil.formatMoney(randomIncome))).color(NamedTextColor.GREEN));
        }
    }
}
