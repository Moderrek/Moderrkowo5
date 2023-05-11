package pl.moderr.moderrkowo.core.commands.user;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.ranks.Rank;
import pl.moderr.moderrkowo.core.ranks.RankManager;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.ItemStackUtils;

import java.sql.SQLException;
import java.util.ArrayList;

public class SklepCommand implements CommandExecutor, Listener {

    private final String name = ColorUtils.color("&cZakup rangi");

    public SklepCommand() {
        Bukkit.getPluginManager().registerEvents(this, ModerrkowoPlugin.getInstance());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
//            p.openInventory(getInventory(UserManager.getUser(p.getUniqueId())));
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
//            p.sendMessage(ColorUtils.color("&aOtworzono " + name));
            p.sendMessage(Component.text("Sklep: ").color(NamedTextColor.GRAY).append(Component.text("sklep.moderrkowo.pl").color(NamedTextColor.GREEN).clickEvent(ClickEvent.openUrl("https://sklep.moderrkowo.pl/category/survival-ekonomia")).hoverEvent(HoverEvent.showText(Component.text("Przejdź do sklepu").color(NamedTextColor.GRAY)))));
        }
        return false;
    }

    public Inventory getInventory(User u) {
        Inventory inv = Bukkit.createInventory(null, 54, name);
        for (int i = 0; i != 9; i++) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
        }
        for (int i = 9; i != 45; i += 9) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
        }
        for (int i = 17; i != 44; i += 9) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
        }
        for (int i = 44; i != 54; i++) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
        }
        double wpln;
        try {
            wpln = ModerrkowoPlugin.getMySQL().getQuery().getWPLN(u.getName());
        } catch (SQLException exception) {
            u.getPlayer().closeInventory();
            exception.printStackTrace();
            return inv;
        }
        inv.setItem(4, ItemStackUtils.changeName(getItemOfRank(u.getRank(), u, false, wpln), "&aPosiadasz aktualnie"));
        inv.setItem(20, getItemOfRank(Rank.Zelazo, u, true, wpln));
        inv.setItem(22, getItemOfRank(Rank.Zloto, u, true, wpln));
        inv.setItem(24, getItemOfRank(Rank.Diament, u, true, wpln));
        inv.setItem(31, getItemOfRank(Rank.Emerald, u, true, wpln));
        inv.setItem(49, ItemStackUtils.createGuiItem(Material.GOLD_NUGGET, 1, ColorUtils.color("&7Posiadasz: &6" + ChatUtil.getWPLN(wpln))));

        return inv;
    }

    public ItemStack getItemOfRank(Rank rank, User u, boolean canBarrier, double wpln) {
        ItemStack itemStack = new ItemStack(RankManager.getMaterial(rank), 1);
        ItemMeta im = itemStack.getItemMeta();
        im.setDisplayName(" ");
        if (RankManager.getPriority(u.getRank()) == RankManager.getPriority(rank)) {
            itemStack.setType(RankManager.getMaterial(rank));
            im.setDisplayName(ColorUtils.color("&aJuż posiadasz"));
        } else {
            if (u.hasRank(RankManager.getPriority(rank) - 1) && RankManager.getPriority(rank) < RankManager.getPriority(u.getRank()) && canBarrier) {
                itemStack.setType(Material.BARRIER);
                im.setDisplayName(ColorUtils.color("&cPoziom niżej"));
            }
        }
        itemStack.setItemMeta(im);
        ArrayList<String> lore = new ArrayList<>();
        lore.add(ColorUtils.color(" &6> &7Ranga &r" + RankManager.getRankName(rank, false)));
        lore.add(ColorUtils.color("   &7Prefix &7\"" + RankManager.getRankNameShort(rank, false) + "&7\""));
        lore.add(ColorUtils.color("   &f" + RankManager.getChat(rank, u.getStuffRank()) + u.getName()));
        lore.add(ColorUtils.color(" "));
        lore.add(ColorUtils.color(" &6> &7Korzyści:"));
        RankManager.getBonus(rank).forEach(s -> {
            lore.add(ColorUtils.color("   " + RankManager.getRankColor(rank) + "+ &7" + s));
        });
        lore.add(ColorUtils.color(" "));
        lore.add(ColorUtils.color(" &6> &7Okres rangi"));
        lore.add(ColorUtils.color("   &ado końca sezonu"));
        lore.add(ColorUtils.color(" "));
        if (wpln >= RankManager.getCost(rank)) {
            lore.add(ColorUtils.color(" &6> &7Cena: &a" + ChatUtil.getWPLN(RankManager.getCost(rank)) + ""));
            if (!u.hasRank(rank)) {
                lore.add(ColorUtils.color("    &7Kliknij aby zakupić"));
            }
        } else {
            lore.add(ColorUtils.color(" &6> &7Cena: &c" + ChatUtil.getWPLN(RankManager.getCost(rank)) + ""));
        }
        lore.add(ColorUtils.color(" "));
        itemStack.setLore(lore);
        return itemStack;
    }


    public void buyRank(Player p, Rank rank) throws SQLException {
        User u = UserManager.getUser(p.getUniqueId());
        if (u.hasRank(rank)) {
            p.sendMessage(ColorUtils.color("&cJuż posiadasz tą range!"));
            return;
        }
        double wpln = ModerrkowoPlugin.getMySQL().getQuery().getWPLN(u.getName());
        double cost = RankManager.getCost(rank);
        if (wpln >= cost) {
            ModerrkowoPlugin.getMySQL().getQuery().setWPLN(u.getName(), wpln - cost);
            switch (rank) {
                case Zelazo:
                    u.addMoney(4000);
                    u.addSeasonOneCoins(150);
                    break;
                case Zloto:
                    u.addMoney(5000);
                    u.addSeasonOneCoins(275);
                    break;
                case Diament:
                    u.addMoney(25000);
                    u.addSeasonOneCoins(400);
                    break;
                case Emerald:
                    u.addMoney(50000);
                    u.addSeasonOneCoins(600);
                    break;
            }
            u.setRank(rank);
            ModerrkowoPlugin.getMySQL().getQuery().updateUser(u);
            p.closeInventory();
            p.sendTitle(ColorUtils.color("&aDziękujemy za wsparcie,"), ColorUtils.color(RankManager.getChat(u.getRank(), u.getStuffRank()) + p.getName()));
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(ColorUtils.color(" &6> &e" + p.getName() + " &7zakupił " + RankManager.getRankName(rank, false) + "\n   &7o wartości &6" + ChatUtil.getWPLN(RankManager.getCost(rank))));
            Bukkit.broadcastMessage(" ");
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1));
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1));
        } else {
            p.sendMessage(ColorUtils.color("&cNie stać cię na tą range!"));
            p.sendMessage(ColorUtils.color("&7Aby doładować wPLN wejdź na"));
            p.sendMessage(ColorUtils.color("&7https://tipo.live/p/moderrkowo"));
            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
    }

    @EventHandler
    public void onRynekClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            if (e.getView().getTitle().contains(name)) {
                e.setCancelled(true);
                return;
            }
            return;
        }
        if (e.getView().getTitle().contains(name)) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            try {
                switch (e.getSlot()) {
                    case 20:
                        buyRank(p, Rank.Zelazo);
                        break;
                    case 22:
                        buyRank(p, Rank.Zloto);
                        break;
                    case 24:
                        buyRank(p, Rank.Diament);
                        break;
                    case 31:
                        buyRank(p, Rank.Emerald);
                        break;
                    default:
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        break;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                p.sendMessage(ColorUtils.color("&cNie udało się zakupić rangi zgłoś się /helpop!"));
            }
        }
    }

}
