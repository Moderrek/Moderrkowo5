package pl.moderr.moderrkowo.core.commands.user.teleportation;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.Logger;
import pl.moderr.moderrkowo.core.services.mysql.UserManager;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;

public class SetHomeCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (ModerrkowoPlugin.getInstance().getAntyLogoutService().isFighting(p.getUniqueId())) {
                p.sendMessage(ColorUtil.color("&cNie możesz uciec podczas walki"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                Logger.logAdminLog(p.getName() + " chciał uciec podczas walki [sethome]");
                return false;
            }
            if (args.length > 0) {
                int temp = ModerrkowoPlugin.getInstance().dataConfig.getInt("homescount." + p.getUniqueId());
                int max = 0;
                switch (UserManager.getUser(p.getUniqueId()).getRank()) {
                    case None:
                        max = 1;
                        break;
                    case Zelazo:
                        max = 2;
                    case Zloto:
                        max = 3;
                        break;
                    case Diament:
                        max = 5;
                        break;
                    case Emerald:
                        max = 6;
                        break;
                }
                if (temp >= max) {
                    p.sendMessage(ColorUtil.color("&cPosiadasz już limit domów!"));
                    return false;
                }
                boolean isReplace = ModerrkowoPlugin.getInstance().dataConfig.isSet("homes." + p.getUniqueId() + "." + args[0]);
                ModerrkowoPlugin.getInstance().dataConfig.set("homes." + p.getUniqueId() + "." + args[0], p.getLocation());
                if (!isReplace) {
                    ModerrkowoPlugin.getInstance().dataConfig.set("homescount." + p.getUniqueId(), temp + 1);
                    List<String> homes = ModerrkowoPlugin.getInstance().dataConfig.getStringList(MessageFormat.format("homeslist.{0}", p.getUniqueId()));
                    homes.add(args[0]);
                    ModerrkowoPlugin.getInstance().dataConfig.set(MessageFormat.format("homeslist.{0}", p.getUniqueId()), homes);
                }
                try {
                    ModerrkowoPlugin.getInstance().dataConfig.save(ModerrkowoPlugin.getInstance().dataFile);
                } catch (IOException e) {
                    p.sendMessage(Component.text("Nie udało sie zapisać domu!").color(NamedTextColor.RED));
                }
                p.sendMessage(ColorUtil.color("&8[!] &aUstawiono nowe miejsce domu"));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
            } else {
                p.sendMessage(ColorUtil.color("&e/sethome <nazwa>"));
            }
        }
        return false;
    }
}
