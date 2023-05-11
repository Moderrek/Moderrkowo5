package pl.moderr.moderrkowo.core.commands.user;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.mechanics.automessage.AutoMessageManager;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.ItemStackUtils;

import java.util.ArrayList;
import java.util.Objects;

public class PrzedmiotyCommand implements CommandExecutor, Listener {

    public final String PrzedmiotyGui_WithoutPage = ColorUtils.color("&6Przedmioty z ziemi &7- &eStrona ");
    public final String PrzedmiotyGUI_Name = ColorUtils.color("&6Przedmioty z ziemi &7- &eStrona %s");
    public boolean otwarte = false;
    public ArrayList<ItemStack> items = new ArrayList<>();

    public PrzedmiotyCommand(ModerrkowoPlugin moderrkowoPlugin) {
        Bukkit.getPluginManager().registerEvents(this, moderrkowoPlugin);
        Bukkit.getScheduler().scheduleSyncRepeatingTask(moderrkowoPlugin, () -> {
            items.clear();
            World w = Bukkit.getWorld("world");
            assert w != null;
            w.getEntitiesByClass(Item.class).forEach(item -> {
                items.add(item.getItemStack());
                item.remove();
            });
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_HAT, 1, 1));
            AutoMessageManager.SendServerMessage(ModerrkowoPlugin.getInstance(), "Zebrano przedmioty z ziemi.");
            Bukkit.broadcastMessage(ColorUtils.color("  &aAby odebrać darmowe przedmioty"));
            Bukkit.broadcastMessage(ColorUtils.color("  &fWpisz /przedmioty &f(30s)"));
            Bukkit.broadcastMessage(" ");
            otwarte = true;
            Bukkit.getScheduler().runTaskLater(moderrkowoPlugin, () -> {
                otwarte = false;
                AutoMessageManager.SendServerMessage(ModerrkowoPlugin.getInstance(), "Przedmioty zostały zamknięte.");
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getOpenInventory().getTitle().contains(PrzedmiotyGui_WithoutPage)) {
                        player.playSound(player.getLocation(), Sound.ENTITY_WITHER_AMBIENT, 0.5f, 0.5f);
                        player.closeInventory();
                        player.spawnParticle(Particle.CLOUD, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), 20, 1, 1, 1, 0.1f);
                    }
                }
            }, 20L * 30);
        }, 0, 20L * 60 * 20 + (20L * 30));
    }

    public int getPages() {
        int viewItems = 0;
        int pages = 0;
        while (items.size() > viewItems) {
            viewItems += 45;
            pages++;
        }
        return pages;
    }

    public ArrayList<ItemStack> getPage(int page) {
        int startItem = (page - 1) * 45;
        ArrayList<ItemStack> pageList = new ArrayList<>();
        for (int i = startItem; i != startItem + 45; i++) {
            try {
                pageList.add(items.get(i));
            } catch (Exception ignored) {

            }
        }
        return pageList;
    }

    public Inventory getPrzedmiotyInventory(int page) {
        int size = 54 - 1;
        ArrayList<ItemStack> pageItems = getPage(page);
        int availablePages = getPages();
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
            if (otwarte) {
                p.openInventory(getPrzedmiotyInventory(1));
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            } else {
                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                p.sendMessage(ColorUtils.color("&cPrzedmioty z ziemi są zamknięte!"));
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
                    p.openInventory(getPrzedmiotyInventory(page));
                }
                return;
            }
            if (e.getSlot() == 54 - 9) {
                if (Objects.requireNonNull(e.getClickedInventory().getItem(e.getSlot())).getType().equals(Material.ARROW)) {
                    page--;
                    p.openInventory(getPrzedmiotyInventory(page));
                }
                return;
            }
            if (e.getSlot() > 45) {
                return;
            }
            //</editor-fold> Page switcher

            //<editor-fold> Collect items
            ItemStack itemStack = e.getInventory().getItem(e.getSlot());
            if (itemStack == null) {
                return;
            }
            if (p.getInventory().firstEmpty() != -1) {
                p.getInventory().addItem(itemStack);
            } else {
                p.getWorld().dropItem(p.getLocation(), itemStack);
            }
            items.remove(itemStack);
            p.sendMessage(ColorUtils.color("&fPomyślnie zabrano przedmiot."));
            for (Player players : Bukkit.getOnlinePlayers()) {
                if (players.getOpenInventory().getTitle().contains(PrzedmiotyGui_WithoutPage)) {
                    players.openInventory(getPrzedmiotyInventory(Integer.parseInt(players.getOpenInventory().getTitle().replace(PrzedmiotyGui_WithoutPage, ""))));
                }
            }
            //</editor-fold> Buy items

        }
    }
}
