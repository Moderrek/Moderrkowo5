package pl.moderr.moderrkowo.core.economy;

import com.destroystokyo.paper.Title;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.mysql.User;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.ItemStackUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

public class WithdrawCommand implements CommandExecutor, Listener {

    public static final String banknotPrefix = ColorUtils.color("&ePieniądz &2");
    public static final String banknotSuffix = ColorUtils.color(" zł");

    public static ItemStack generateItemStatic(int count, int money) {
        return ItemStackUtils.createGuiItem(Material.GOLD_NUGGET, count, banknotPrefix + money + banknotSuffix);
    }

    /**
     * Komenda po wywołaniu <b>/wyplac</b>
     **/
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            User user = UserManager.getUser(player.getUniqueId());
            if (args.length >= 1) {
                int money;
                try {
                    money = Integer.parseInt(args[0]);
                } catch (Exception e) {
                    player.sendMessage(ColorUtils.color("&cPodano niepoprawną kwotę!"));
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
                if (money < 100) {
                    player.sendMessage(ColorUtils.color("&cKwota nie może być mniejsza niż 100 zł!"));
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
                if (money > 100000) {
                    player.sendMessage(ColorUtils.color("&cKwota nie może być większa niż 100 tysięcy!"));
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
                if (!user.hasMoney(money)) {
                    player.sendMessage(ColorUtils.color("&cNie posiadasz tyle pieniędzy!"));
                    player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return false;
                }
                Logger.logAdminLog(ColorUtils.color("&6" + player.getName() + " &7wypłacił w banknocie &6" + ChatUtil.getMoney(money)));
                user.subtractMoney(money);
                player.sendMessage(ColorUtils.color("&9Wypłata &c- " + ChatUtil.getMoney(money)));
                ItemStackUtils.addItemStackToPlayer(player, generateItem(1, money));
                player.sendTitle(new Title("", ColorUtils.color("&aPomyślnie wypłacono")));
                player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                return true;
            } else {
                player.sendMessage(ColorUtils.color("&cUżycie: /wyplac <kwota>"));
                player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
        } else {
            sender.sendMessage(ColorUtils.color("&cTylko gracz może używać tej komendy!"));
            return false;
        }
    }

    /**
     * @param count Ilość
     * @param money Wartość banknotu
     * @return {@link ItemStack Banknot}
     */
    public ItemStack generateItem(int count, int money) {
        return ItemStackUtils.createGuiItem(Material.GOLD_NUGGET, count, banknotPrefix + money + banknotSuffix);
    }

    /**
     * Konwertowanie {@link ItemStack Banknot} na wartość banknota
     *
     * @param item {@link ItemStack Banknot}
     * @return {@link Integer wartośc banknota}
     */
    public int parseItem(ItemStack item) {
        if (item.hasItemMeta()) {
            if (item.getItemMeta().hasDisplayName()) {
                if (item.getItemMeta().getDisplayName().startsWith(banknotPrefix) && item.getItemMeta().getDisplayName().endsWith(banknotSuffix)) {
                    // jest banknotem
                    String itemName = item.getItemMeta().getDisplayName().replace(banknotPrefix, "").replace(banknotSuffix, "");
                    return Integer.parseInt(itemName);
                }
            }
        }
        return 0;
    }

    /**
     * Używanie banknota
     *
     * @param e {@link PlayerInteractEvent}
     */
    @EventHandler
    public void click(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if (e.getItem() != null) {
                if (e.getItem().hasItemMeta()) {
                    if (e.getItem().getItemMeta().hasDisplayName()) {
                        if (e.getItem().getItemMeta().getDisplayName().startsWith(banknotPrefix) && e.getItem().getItemMeta().getDisplayName().endsWith(banknotSuffix)) {
                            // ile zł posiada banknot
                            int moneyPerItem = parseItem(e.getItem());
                            // instancja gracza
                            Player player = e.getPlayer();
                            // ilosc banknota
                            int count;
                            if (player.isSneaking()) {
                                // używa wszystkie banknoty w stacku
                                count = e.getItem().getAmount();
                            } else {
                                // jeżeli nie, używa jeden banknot w stacku
                                count = 1;
                            }
                            // ile wpłaci
                            int money = moneyPerItem * count;
                            // instancja uzytkownika
                            User user = UserManager.getUser(player.getUniqueId());
                            // zabierania banknota
                            e.getItem().setAmount(e.getItem().getAmount() - count);
                            // dodanie pieniedzy
                            user.addMoney(money);
                            // wyswietlenie informacji
                            player.sendMessage(ColorUtils.color("&9Wpłata &a+ " + ChatUtil.getMoney(money)));
                            player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                        }
                    }
                }
            }
        }
    }

    /**
     * Blokowanie craftowania z {@link ItemStack Banknot}
     *
     * @param e {@link PrepareItemCraftEvent}
     */
    @EventHandler
    public void preCraft(PrepareItemCraftEvent e) {
        boolean hasItem = false;
        for (ItemStack item : e.getInventory().getMatrix()) {
            if (item == null) {
                continue;
            }
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasDisplayName()) {
                    if (item.getItemMeta().getDisplayName().startsWith(banknotPrefix)) {
                        hasItem = true;
                    }
                }
            }
        }
        if (hasItem) {
            e.getInventory().setResult(null);
        }
    }

    /**
     * Blokowanie używania kowadła z {@link ItemStack Banknot}'em
     *
     * @param e {@link PrepareAnvilEvent}
     */
    @EventHandler
    public void preAnvil(PrepareAnvilEvent e) {
        if (e.getInventory().getFirstItem() != null) {
            if (e.getInventory().getFirstItem().hasItemMeta()) {
                if (e.getInventory().getFirstItem().getItemMeta().hasDisplayName()) {
                    if (e.getInventory().getFirstItem().getItemMeta().getDisplayName().startsWith(banknotPrefix)) {
                        e.setResult(null);
                    }
                }
            }
        }
    }

}
