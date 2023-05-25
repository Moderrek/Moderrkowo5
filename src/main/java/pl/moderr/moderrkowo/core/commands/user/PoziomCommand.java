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
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.util.ChatUtil;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.ItemStackUtil;
import pl.moderr.moderrkowo.core.services.mysql.UserManager;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.user.level.LevelCategory;
import pl.moderr.moderrkowo.core.user.level.UserLevelData;

import java.util.ArrayList;

public class PoziomCommand implements CommandExecutor, Listener {

    private final String title = ColorUtil.color("&aPoziom");

    public PoziomCommand() {
        Bukkit.getPluginManager().registerEvents(this, ModerrkowoPlugin.getInstance());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.openInventory(getInventory(UserManager.getUser(p.getUniqueId())));
            p.sendMessage(ColorUtil.color("&aOtworzono podgląd poziomów"));
            p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
        }
        return false;
    }

    public Inventory getInventory(User u) {
        Inventory inv = Bukkit.createInventory(null, 27, title);
        inv.setItem(4, ItemStackUtil.GetUUIDHead(ColorUtil.color("&cPoziom postaci"), 1, u.getPlayer(), new ArrayList<String>() {
            {
                add(ColorUtil.color("&ePosiadasz: &c" + u.getLevel().playerLevel() + " poziom"));
            }
        }));
        UserLevelData walka = u.getLevel().get(LevelCategory.Walka);
        inv.setItem(10, ItemStackUtil.createGuiItem(Material.IRON_SWORD, 1, ColorUtil.color("&cWalka " + walka.getLevel() + "lvl &f(" + ChatUtil.formatNumber(walka.getExp()) + "/" + ChatUtil.formatNumber(walka.expNeededToNextLevel(walka.getLevel())) + ")")));
        UserLevelData kopanie = u.getLevel().get(LevelCategory.Kopanie);
        inv.setItem(12, ItemStackUtil.createGuiItem(Material.IRON_PICKAXE, 1, ColorUtil.color("&cKopanie " + kopanie.getLevel() + "lvl &f(" + ChatUtil.formatNumber(kopanie.getExp()) + "/" + ChatUtil.formatNumber(kopanie.expNeededToNextLevel(kopanie.getLevel())) + ")")));
        UserLevelData uprawa = u.getLevel().get(LevelCategory.Uprawa);
        inv.setItem(14, ItemStackUtil.createGuiItem(Material.IRON_HOE, 1, ColorUtil.color("&aUprawa " + uprawa.getLevel() + "lvl &f(" + ChatUtil.formatNumber(uprawa.getExp()) + "/" + ChatUtil.formatNumber(uprawa.expNeededToNextLevel(uprawa.getLevel())) + ")")));
        UserLevelData lowienie = u.getLevel().get(LevelCategory.Lowienie);
        inv.setItem(16, ItemStackUtil.createGuiItem(Material.FISHING_ROD, 1, ColorUtil.color("&9Łowienie " + lowienie.getLevel() + "lvl &f(" + ChatUtil.formatNumber(lowienie.getExp()) + "/" + ChatUtil.formatNumber(lowienie.expNeededToNextLevel(lowienie.getLevel())) + ")")));
        return inv;
    }

    @EventHandler
    public void onClick(@NotNull InventoryClickEvent e) {
        if (e.getView().getTitle().equals(title)) {
            e.setCancelled(true);
        }
    }
}
