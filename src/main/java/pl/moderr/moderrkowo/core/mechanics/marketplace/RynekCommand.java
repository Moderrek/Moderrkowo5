package pl.moderr.moderrkowo.core.mechanics.marketplace;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.ranks.Rank;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class RynekCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length > 0) {
                if (args[0].equalsIgnoreCase("wystaw") || args[0].equalsIgnoreCase("sell")) {
                    User u = UserManager.getUser(p.getUniqueId());
                    int max = 0;
                    switch (u.getRank()) {
                        case None:
                            max = 15;
                            break;
                        case Zelazo:
                            max = 20;
                            break;
                        case Zloto:
                            max = 25;
                            break;
                        case Diament:
                            max = 30;
                            break;
                        case Emerald:
                            max = 50;
                            break;
                    }
                    if (ModerrkowoPlugin.getInstance().getRynek().getItemsOfPlayer(p.getUniqueId()) >= max) {
                        p.sendMessage(ColorUtils.color("&cNie możesz wystawić więcej przedmiotów niż " + max + "!"));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return false;
                    }
                    if (args.length > 1) {
                        int cost;
                        try {
                            cost = Integer.parseInt(args[1]);
                        } catch (Exception e) {
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            p.sendMessage(ColorUtils.color("&cPodano niepoprawną kwotę!"));
                            return false;
                        }
                        if (p.getInventory().getItemInMainHand().getType() == Material.AIR) {
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            p.sendMessage(ColorUtils.color("&cPrzedmiot, który chcesz wystawić musisz trzymać w ręku!"));
                            return false;
                        }
                        if (cost > 1000000 || cost < 0) {
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            p.sendMessage(ColorUtils.color("&cCena przedmiotu wychodzi poza wykres 0 zł-1,000,000 zł"));
                            return false;
                        }
                        if (!u.hasRank(Rank.Zelazo)) {
                            if (cost * 0.1 > u.getMoney()) {
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                p.sendMessage(ColorUtils.color("&7Nie można zapłacić &6" + ChatUtil.getMoney(cost * 0.1) + " &8(10% z kosztu)"));
                                return false;
                            } else {
                                u.subtractMoney(cost * 0.1);
                                p.sendMessage(ColorUtils.color("&aPobrano podatek z wystawienia &8[&910%&8]"));
                            }
                        }
                        ModerrkowoPlugin.getInstance().getRynek().addItem(new RynekItem(p.getUniqueId(), p.getInventory().getItemInMainHand().clone(), cost, LocalDateTime.now().plusDays(2)));
                        for (Player players : Bukkit.getOnlinePlayers()) {
                            if (players.getOpenInventory().getTitle().contains(ModerrkowoPlugin.getInstance().getRynek().RynekGui_WithoutPage)) {
                                players.openInventory(ModerrkowoPlugin.getInstance().getRynek().getRynekInventory(Integer.parseInt(players.getOpenInventory().getTitle().replace(ModerrkowoPlugin.getInstance().getRynek().RynekGui_WithoutPage, "")), p));
                            }
                        }
                        Logger.logAdminLog(ColorUtils.color("&6" + p.getName() + " &7wystawił &6" + p.getInventory().getItemInMainHand().getType() + " &7za &6" + ChatUtil.getMoney(cost)));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
//                        p.sendTitle(ColorUtils.color("&2Wystawiono"), ColorUtils.color("&a" + ChatUtil.materialName(p.getInventory().getItemInMainHand().getType())));
                        p.showTitle(Title.title(Component.text("Wystawiono").color(NamedTextColor.DARK_GREEN), Component.translatable(p.getInventory().getItemInMainHand().getType().translationKey()).color(NamedTextColor.GREEN)));
                        p.getInventory().getItemInMainHand().setAmount(0);
                        p.sendMessage(ColorUtils.color("&aPomyślnie wystawiono twój przedmiot na rynku!"));
                        return false;
                    }
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    p.sendMessage(ColorUtils.color("&cAby wystawić przedmiot musisz podać kwotę!"));
                    return false;
                }
                p.sendMessage(ColorUtils.color("&cUżyj: /rynek wystaw <kwota>"));
                return false;
            }
            p.openInventory(ModerrkowoPlugin.getInstance().getRynek().getRynekInventory(1, p));
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
            p.sendMessage(ColorUtils.color("&aOtworzono rynek"));
            return false;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String @NotNull [] args) {
        if (args.length == 1) {
            return Arrays.asList("sell", "wystaw");
        }
        return null;
    }
}
