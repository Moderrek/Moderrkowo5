package pl.moderr.moderrkowo.core.commands.user;

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
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.ItemStackUtils;
import pl.moderr.moderrkowo.core.utils.Logger;
import pl.moderr.moderrkowo.core.utils.ModerrkowoSerialization;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

public class OdbierzCommand implements CommandExecutor, Listener {

    public final String PrzedmiotyGui_WithoutPage = ColorUtils.color("&eOdbierz &7- &eStrona ");
    public final String PrzedmiotyGUI_Name = ColorUtils.color("&eOdbierz &7- &eStrona %s");

    public OdbierzCommand(ModerrkowoPlugin moderrkowoPlugin) {
        Bukkit.getPluginManager().registerEvents(this, moderrkowoPlugin);
    }

    public ArrayList<ItemStack> getItems(UUID uuid) throws SQLException {
        ArrayList<ItemStack> arrayList = new ArrayList<>();
        String sqlGet = String.format("SELECT * FROM `%s` WHERE `UUID`='%s'", ModerrkowoPlugin.getMySQL().rewardTable, uuid.toString());
        Statement stmt = ModerrkowoPlugin.getMySQL().getConnection().createStatement();
        ResultSet rs = stmt.executeQuery(sqlGet);
        if (rs == null) {
            return null;
        }
        while (rs.next()) {
            try {
                arrayList.add(ModerrkowoSerialization.fromBase64(rs.getString("ITEM")));
            } catch (Exception e) {
                Logger.logAdminLog("Wystąpił problem z pobraniem przedmiotem na nagrodach");
                e.printStackTrace();
            }
        }
        return arrayList;
    }

    private void itemsremove(UUID uniqueId) throws SQLException {
        Statement statement = ModerrkowoPlugin.getMySQL().getConnection().createStatement();
        statement.execute("DELETE FROM `rewards` WHERE `UUID`='" + uniqueId.toString());
    }

    public int getPages(UUID uuid) throws SQLException {
        int viewItems = 0;
        int pages = 0;
        while (getItems(uuid).size() > viewItems) {
            viewItems += 45;
            pages++;
        }
        return pages;
    }

    public ArrayList<ItemStack> getPage(int page, UUID uuid) {
        int startItem = (page - 1) * 45;
        ArrayList<ItemStack> pageList = new ArrayList<>();
        for (int i = startItem; i != startItem + 45; i++) {
            try {
                pageList.add(getItems(uuid).get(i));
            } catch (Exception ignored) {

            }
        }
        return pageList;
    }

    public Inventory getPrzedmiotyInventory(int page, UUID uuid) throws SQLException {
        int size = 54 - 1;
        ArrayList<ItemStack> pageItems = getPage(page, uuid);
        int availablePages = getPages(uuid);
        Inventory inv = Bukkit.createInventory(null, 54, String.format(PrzedmiotyGUI_Name, page + ""));
        for (int i = size - 8; i != size + 1; i++) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
        }
        if (page > 1) {
            inv.setItem(size - 8, ItemStackUtils.createGuiItem(Material.ARROW, 1, ColorUtils.color("&aPoprzednia strona")));
        }
        if (page < availablePages) {
            inv.setItem(size, ItemStackUtils.createGuiItem(Material.ARROW, 1, ColorUtils.color("&aNastępna strona")));
        }
        if (pageItems.size() > 0) {
            for (int i = 0; i != pageItems.size(); i++) {
                inv.setItem(i, pageItems.get(i));
            }
        }
        return inv;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            try {
                p.openInventory(getPrzedmiotyInventory(1, p.getUniqueId()));
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            } catch (SQLException exception) {
                exception.printStackTrace();
                p.sendMessage(ColorUtils.color("&cNie udało się otworzyć /odbierz"));
            }

        }
        return false;
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
            if (e.getView().getTitle().contains(PrzedmiotyGui_WithoutPage)) {
                e.setCancelled(true);
                return;
            }
            return;
        }
        if (e.getView().getTitle().contains(PrzedmiotyGui_WithoutPage)) {
            e.setCancelled(true);
            Player p = (Player) e.getWhoClicked();
            //<editor-fold> Page switcher
            String title = e.getView().getTitle().replace(PrzedmiotyGui_WithoutPage, "");
            int page = Integer.parseInt(title);
            if (e.getSlot() == 54 - 1) {
                if (Objects.requireNonNull(e.getClickedInventory().getItem(e.getSlot())).getType().equals(Material.ARROW)) {
                    page++;
                    try {
                        p.openInventory(getPrzedmiotyInventory(page, p.getUniqueId()));
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                        p.closeInventory();
                    }
                }
                return;
            }
            if (e.getSlot() == 54 - 9) {
                if (Objects.requireNonNull(e.getClickedInventory().getItem(e.getSlot())).getType().equals(Material.ARROW)) {
                    page--;
                    try {
                        p.openInventory(getPrzedmiotyInventory(page, p.getUniqueId()));
                    } catch (SQLException exception) {
                        exception.printStackTrace();
                        p.closeInventory();
                    }
                }
                return;
            }
            if (e.getSlot() > 45) {
                return;
            }
            //</editor-fold> Page switcher

            //<editor-fold> Collect items

            try {
                itemsremove(p.getUniqueId());
                for (ItemStack itemStack : getItems(p.getUniqueId())) {
                    if (p.getInventory().firstEmpty() != -1) {
                        p.getInventory().addItem(itemStack);
                    } else {
                        p.getWorld().dropItem(p.getLocation(), itemStack);
                    }
                    p.sendMessage(ColorUtils.color("&fPomyślnie zabrano przedmiot."));
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (players.getOpenInventory().getTitle().contains(PrzedmiotyGui_WithoutPage)) {
                            players.openInventory(getPrzedmiotyInventory(Integer.parseInt(players.getOpenInventory().getTitle().replace(PrzedmiotyGui_WithoutPage, "")), p.getUniqueId()));
                        }
                    }
                }
            } catch (SQLException exception) {
                exception.printStackTrace();
                p.sendMessage(ColorUtils.color("&cNie udało się zebrać przedmiotu!"));
            }
            //</editor-fold> Buy items

        }
    }


}
