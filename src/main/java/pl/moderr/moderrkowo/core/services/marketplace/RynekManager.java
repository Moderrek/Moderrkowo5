package pl.moderr.moderrkowo.core.services.marketplace;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.util.ChatUtil;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.ItemStackUtil;
import pl.moderr.moderrkowo.core.api.util.Logger;
import pl.moderr.moderrkowo.core.services.mysql.UserManager;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.user.notification.MNotification;
import pl.moderr.moderrkowo.core.user.notification.NotificationType;
import pl.moderr.moderrkowo.core.user.notification.exceptions.MNotification_CannotPublish;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class RynekManager implements Listener {

    public final String RynekGui_WithoutPage = ColorUtil.color("&6Rynek graczy &7- &eStrona ");
    public final String RynekGUI_Name = ColorUtil.color("&6Rynek graczy &7- &eStrona %s");

    public final ArrayList<RynekItem> rynekItems = new ArrayList<>();


    public RynekManager() {
        load();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(ModerrkowoPlugin.getInstance(), () -> {
            try {
                save();
            } catch (SQLException exception) {
                exception.printStackTrace();
            }
        }, 0, 20 * 60 * 30);
    }

    public void load() {
        Logger.logAdminLog("Wczytywanie przedmiotów na rynku...");
        int total = 0;
        int i = 0;
        try {
            rynekItems.clear();
            String sqlGet = String.format("SELECT * FROM `%s`", ModerrkowoPlugin.getMySQL().rynekTable);
            Statement stmt = ModerrkowoPlugin.getMySQL().getConnection().createStatement();
            ResultSet rs = stmt.executeQuery(sqlGet);
            if (rs == null) {
                Logger.logAdminLog("Nie wczytano rynku, ponieważ nie ma żadnych przedmiotów");
                return;
            }
            while (rs.next()) {
                total++;
                try {
                    rynekItems.add(new RynekItem(UUID.fromString(rs.getString("OWNER")), ItemStackUtil.fromBase64(rs.getString("ITEM")), rs.getInt("COST"), LocalDateTime.parse(rs.getString("EXPIRE"))));
                    i++;
                } catch (Exception e) {
                    Logger.logAdminLog("Wystąpił problem z pobraniem przedmiotem na rynku");
                    e.printStackTrace();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            Logger.logAdminLog("Wystąpił problem podczas ładowania rynku");
        }
        Logger.logAdminLog("Wczytano przedmioty na rynku (" + i + "/" + total + ")");
    }

    public void save() throws SQLException {
        Logger.logAdminLog("Zapisywanie przedmiotów na rynku...");
        String sql = "TRUNCATE TABLE `" + ModerrkowoPlugin.getMySQL().rynekTable + "`";
        Statement stmt = ModerrkowoPlugin.getMySQL().getConnection().createStatement();
        stmt.execute(sql);
        int temp = 0;
        for (RynekItem i : rynekItems) {
            String SQL = String.format("INSERT INTO `%s` (`OWNER`,`ITEM`,`COST`,`EXPIRE`) VALUES (?,?,?,?)", ModerrkowoPlugin.getMySQL().rynekTable);
            PreparedStatement preparedStatement = ModerrkowoPlugin.getMySQL().getConnection().prepareStatement(SQL);
            preparedStatement.setString(1, i.getOwner().toString());
            preparedStatement.setString(2, ItemStackUtil.toBase64(i.getItem()));
            preparedStatement.setInt(3, i.getCost());
            preparedStatement.setString(4, i.getExpire().toString());
            preparedStatement.execute();
            temp++;
        }
        Logger.logAdminLog("Zapisano przedmioty na rynku (" + temp + ")");
    }


    public void buyItem(User buyer, RynekItem item) {
        // Seller earn
        if (LocalDateTime.now().isAfter(item.getExpire())) {
            buyer.getPlayer().sendMessage(ColorUtil.color("&cPrzedmiot wygasł"));
            buyer.getPlayer().playSound(buyer.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
            return;
        }
        Player seller = Bukkit.getPlayer(item.getOwner());
        if (seller != null) {
            User sellerU = UserManager.getUser(seller.getUniqueId());
            sellerU.addMoney(item.getCost());
            buyer.subtractMoney(item.getCost());

            if (Objects.requireNonNull(buyer.getPlayer()).getInventory().firstEmpty() != -1) {
                buyer.getPlayer().getInventory().addItem(item.getItem());
            } else {
                buyer.getPlayer().getLocation().getWorld().dropItem(buyer.getPlayer().getLocation(), item.getItem());
            }
            rynekItems.remove(item);
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (players.getOpenInventory().getTitle().contains(RynekGui_WithoutPage)) {
                    players.openInventory(getRynekInventory(Integer.parseInt(players.getOpenInventory().getTitle().replace(RynekGui_WithoutPage, "")), players));
                }
            }
            buyer.getPlayer().sendMessage(ColorUtil.color("&aPomyślnie zakupiono przedmiot z rynku od " + sellerU.getName()));
            buyer.getPlayer().sendMessage(ColorUtil.color("&c- " + ChatUtil.formatMoney(item.getCost())));
            buyer.getPlayer().playSound(buyer.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
            buyer.getPlayer().sendMessage(ColorUtil.color("&aPrzelew zakończony powodzeniem"));
            seller.sendMessage(ColorUtil.color("&9Rynek &8> &6" + buyer.getName() + " &7zakupił przedmiot od Ciebie &8[&9" + ChatUtil.materialName(item.getItem().getType()) + "&8]"));
            seller.sendMessage(ColorUtil.color("&9Rynek &8> &a+ " + ChatUtil.formatMoney(item.getCost())));
            Logger.logAdminLog(ColorUtil.color("&6" + buyer.getName() + " &7kupił &6" + ChatUtil.materialName(item.getItem().getType()) + " &7od &6" + sellerU.getName() + " &7za &6" + ChatUtil.formatMoney(item.getCost())));
        } else {
            OfflinePlayer offlineSeller = Bukkit.getOfflinePlayer(item.getOwner());
            try {
                double kwota = ModerrkowoPlugin.getMySQL().getQuery().getMoney(item.getOwner());
                kwota += item.getCost();
                ModerrkowoPlugin.getMySQL().getQuery().setMoney(item.getOwner(), kwota);
                buyer.subtractMoney(item.getCost());
                if (buyer.getPlayer().getInventory().firstEmpty() != -1) {
                    buyer.getPlayer().getInventory().addItem(item.getItem());
                } else {
                    buyer.getPlayer().getLocation().getWorld().dropItem(buyer.getPlayer().getLocation(), item.getItem());
                }
                rynekItems.remove(item);
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (players.getOpenInventory().getTitle().contains(RynekGui_WithoutPage)) {
                        players.openInventory(getRynekInventory(Integer.parseInt(players.getOpenInventory().getTitle().replace(RynekGui_WithoutPage, "")), players));
                    }
                }
                buyer.getPlayer().sendMessage(ColorUtil.color("&aPomyślnie zakupiono przedmiot z rynku od " + offlineSeller.getName()));
                buyer.getPlayer().sendMessage(ColorUtil.color("&c- " + ChatUtil.formatMoney(item.getCost())));
                buyer.getPlayer().playSound(buyer.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_YES, 1, 1);
                //buyer.getPlayer().sendMessage(ColorUtils.color("&aPrzelew zakończony powodzeniem"));
                Logger.logAdminLog(ColorUtil.color("&6" + buyer.getName() + " &7kupił &6" + ChatUtil.materialName(item.getItem().getType()) + " &7od &6" + Bukkit.getOfflinePlayer(item.getOwner()).getName() + " &7za &6" + ChatUtil.formatMoney(item.getCost())));
                try {
                    final String data = "&2" + buyer.getName() + " &azakupil przedmiot za &2" + ChatUtil.formatMoney(item.getCost());
                    new MNotification(UUID.randomUUID(), offlineSeller.getUniqueId(), NotificationType.RYNEK_SELL_ITEM_OFFLINE, false, data).publish();
                } catch (MNotification_CannotPublish mNotification_cannotPublish) {
                    mNotification_cannotPublish.printStackTrace();
                }
            } catch (SQLException throwables) {
                buyer.getPlayer().sendMessage(ColorUtil.color("&cPrzelew zakończony niepowodzeniem"));
                throwables.printStackTrace();
            }
        }
        // Seller earn
    }

    public void addItem(RynekItem item) {
        rynekItems.add(item);
    }

    public RynekItem getItem(int page, int slot) {
        ArrayList<RynekItem> pageItems = getPage(page);
        if (pageItems.size() < (slot + 1)) {
            return null;
        }
        return pageItems.get(slot);
    }

    public ItemStack getItemOfItemRynek(RynekItem item, Player player) {
        ItemStack i = item.getItem();
        ItemMeta meta = i.getItemMeta();
        assert meta != null;
        ArrayList<String> lore = (ArrayList<String>) meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }
        User u = UserManager.getUser(player.getUniqueId());
        if (item.getOwner().equals(player.getUniqueId())) {
            lore.add(" ");
            lore.add(ColorUtil.color("&7Cena: &a" + ChatUtil.formatMoney(item.getCost())));
            lore.add(ColorUtil.color("&7Wystawione przez: &c" + "Ciebie"));
            lore.add(ColorUtil.color("&7Wygasa: &c" + ChatUtil.formatCountingDown(item.getExpire())));
            lore.add(" ");
            lore.add(ColorUtil.color("&cKliknij, aby zwrócić ofertę"));
        } else {
            if (u.getMoney() >= item.getCost()) {
                lore.add(ColorUtil.color("&7Cena: &a" + ChatUtil.formatMoney(item.getCost())));
            } else {
                lore.add(ColorUtil.color("&7Cena: &c" + ChatUtil.formatMoney(item.getCost())));
            }
            lore.add(ColorUtil.color("&7Wystawione przez: &e" + Bukkit.getOfflinePlayer(item.getOwner()).getName()));
            lore.add(ColorUtil.color("&7Wygasa: &c" + ChatUtil.formatCountingDown(item.getExpire())));
            lore.add(" ");
            if (u.getMoney() >= item.getCost()) {
                lore.add(ColorUtil.color("&aKliknij, aby zakupić"));
            } else {
                lore.add(ColorUtil.color("&cKliknij, aby zakupić"));
            }
        }
        if (meta.hasDisplayName()) {
            meta.setDisplayName(meta.getDisplayName());
        } else {
            meta.displayName(Component.translatable(i.getType().translationKey(), NamedTextColor.YELLOW).decoration(TextDecoration.ITALIC, false));
        }
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }

    public int getPages() {
        int viewItems = 0;
        int pages = 0;
        while (rynekItems.size() > viewItems) {
            viewItems += 45;
            pages++;
        }
        return pages;
    }

    public int getItemsOfPlayer(UUID uuid) {
        clearExpiredItems();
        int i = 0;
        for (RynekItem item : rynekItems) {
            if (item.getOwner() == uuid) {
                i++;
            }
        }
        return i;
    }

    public void clearExpiredItems() {
        for (int i = 0; i < rynekItems.size(); i++) {
            RynekItem item = rynekItems.get(i);
            if (LocalDateTime.now().isAfter(item.getExpire())) {
                String SQL = "INSERT INTO `" + ModerrkowoPlugin.getMySQL().rewardTable + "`(`UUID`,`ITEM`) VALUES (?,?)";
                try {
                    PreparedStatement preparedStatement = ModerrkowoPlugin.getMySQL().getConnection().prepareStatement(SQL);
                    preparedStatement.setString(1, item.getOwner().toString());
                    preparedStatement.setString(2, ItemStackUtil.toBase64(item.getItem()));
                    preparedStatement.execute();
                    rynekItems.remove(i);
                    if (Bukkit.getPlayer(item.getOwner()) == null) {
                        try {
                            final String data = "&cPodczas twojej nieobecności twój przedmiot na rynku wygasł odbierz go za pomocą /odbierz";
                            new MNotification(UUID.randomUUID(), item.getOwner(), NotificationType.RYNEK_TIMEOUT_ITEM, false, data).publish();
                        } catch (MNotification_CannotPublish | SQLException mNotification_cannotPublish) {
                            mNotification_cannotPublish.printStackTrace();
                        }
                    } else {
                        Player p = Bukkit.getPlayer(item.getOwner());
                        assert p != null;
                        p.sendMessage(ColorUtil.color("&cTwój przedmiot na rynku wygasł odbierz go za pomocą /odbierz"));
                        p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                    }
                } catch (SQLException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public ArrayList<RynekItem> getPage(int page) {
        int startItem = (page - 1) * 45;
        clearExpiredItems();
        ArrayList<RynekItem> pageList = new ArrayList<>();
        for (int i = startItem; i != startItem + 45; i++) {
            try {
                pageList.add(rynekItems.get(i));
            } catch (Exception ignored) {

            }
        }
        return pageList;
    }

    public Inventory getRynekInventory(int page, Player p) {
        int size = 54 - 1;
        ArrayList<RynekItem> pageItems = getPage(page);
        int availablePages = getPages();
        Inventory inv = Bukkit.createInventory(null, 54, String.format(RynekGUI_Name, page + ""));
        for (int i = size - 8; i != size + 1; i++) {
            inv.setItem(i, ItemStackUtil.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
        }
        if (page > 1) {
            inv.setItem(size - 8, ItemStackUtil.createGuiItem(Material.ARROW, 1, ColorUtil.color("&aPoprzednia strona")));
        }
        if (page < availablePages) {
            inv.setItem(size, ItemStackUtil.createGuiItem(Material.ARROW, 1, ColorUtil.color("&aNastępna strona")));
        }
        //inv.setItem(size - 4, ItemStackUtils.createGuiItem(Material.DIAMOND, 1, ColorUtils.color("&aTwoje oferty")));
        if (pageItems.size() > 0) {
            for (int i = 0; i != pageItems.size(); i++) {
                inv.setItem(i, getItemOfItemRynek(pageItems.get(i), p));
            }
        }
        return inv;
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
            if (e.getView().getTitle().contains(RynekGui_WithoutPage)) {
                e.setCancelled(true);
                return;
            }
            return;
        }
        if (e.getView().getTitle().contains(RynekGui_WithoutPage)) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            User u;

            try {
                u = UserManager.getUser(p.getUniqueId());
            } catch (Exception userNotLoaded) {
                userNotLoaded.printStackTrace();
                p.sendMessage(ColorUtil.color("&cWystąpił błąd. Jeżeli się powtarza, wyjdź i wejdź na serwer."));
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }

            //<editor-fold> Page switcher
            String title = e.getView().getTitle().replace(RynekGui_WithoutPage, "");
            int page = Integer.parseInt(title);
            if (e.getSlot() == 54 - 1) {
                if (Objects.requireNonNull(e.getClickedInventory().getItem(e.getSlot())).getType().equals(Material.ARROW)) {
                    page++;
                    p.openInventory(getRynekInventory(page, p));
                }
            }
            if (e.getSlot() == 54 - 9) {
                if (Objects.requireNonNull(e.getClickedInventory().getItem(e.getSlot())).getType().equals(Material.ARROW)) {
                    page--;
                    p.openInventory(getRynekInventory(page, p));
                }
            }
            //</editor-fold> Page switcher

            //<editor-fold> Buy items
            RynekItem item = getItem(page, e.getSlot());
            if (item == null) {
                return;
            }
            if (item.getOwner().equals(p.getUniqueId())) {
                if (p.getInventory().firstEmpty() != -1) {
                    p.getInventory().addItem(item.getItem());
                } else {
                    p.getWorld().dropItem(p.getLocation(), item.getItem());
                }
                rynekItems.remove(item);
                p.sendMessage(ColorUtil.color("&fPomyślnie zwrócono przedmiot z rynku."));
                for (Player players : Bukkit.getOnlinePlayers()) {
                    if (players.getOpenInventory().getTitle().contains(RynekGui_WithoutPage)) {
                        players.openInventory(getRynekInventory(Integer.parseInt(players.getOpenInventory().getTitle().replace(RynekGui_WithoutPage, "")), players));
                    }
                }
            } else {
                if (u.getMoney() >= item.getCost()) {
                    buyItem(u, item);
                } else {
                    p.sendMessage(ColorUtil.color("&cNie posiadasz tyle pieniędzy!"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                }
            }
            //</editor-fold> Buy items

        }
    }

}
