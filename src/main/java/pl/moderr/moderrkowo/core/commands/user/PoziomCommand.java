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
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.mysql.LevelCategory;
import pl.moderr.moderrkowo.core.mysql.User;
import pl.moderr.moderrkowo.core.mysql.UserLevelData;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.ItemStackUtils;

import java.util.ArrayList;

public class PoziomCommand implements CommandExecutor, Listener {

    private final String title = ColorUtils.color("&aPoziom");

    public PoziomCommand() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            p.openInventory(getInventory(UserManager.getUser(p.getUniqueId())));
            p.sendMessage(ColorUtils.color("&aOtworzono podgląd poziomów"));
            p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 1);
        }
        return false;
    }

    public Inventory getInventory(User u) {
        Inventory inv = Bukkit.createInventory(null, 27, title);
        inv.setItem(4, ItemStackUtils.GetUUIDHead(ColorUtils.color("&cPoziom postaci"), 1, u.getPlayer(), new ArrayList<String>() {
            {
                add(ColorUtils.color("&ePosiadasz: &c" + u.getUserLevel().playerLevel() + " poziom"));
            }
        }));
        UserLevelData walka = u.getUserLevel().get(LevelCategory.Walka);
        inv.setItem(10, ItemStackUtils.createGuiItem(Material.IRON_SWORD, 1, ColorUtils.color("&cWalka " + walka.getLevel() + "lvl &f(" + ChatUtil.getNumber(walka.getExp()) + "/" + ChatUtil.getNumber(walka.expNeededToNextLevel(walka.getLevel())) + ")")));
        UserLevelData kopanie = u.getUserLevel().get(LevelCategory.Kopanie);
        inv.setItem(12, ItemStackUtils.createGuiItem(Material.IRON_PICKAXE, 1, ColorUtils.color("&cKopanie " + kopanie.getLevel() + "lvl &f(" + ChatUtil.getNumber(kopanie.getExp()) + "/" + ChatUtil.getNumber(kopanie.expNeededToNextLevel(kopanie.getLevel())) + ")")));
        UserLevelData uprawa = u.getUserLevel().get(LevelCategory.Uprawa);
        inv.setItem(14, ItemStackUtils.createGuiItem(Material.IRON_HOE, 1, ColorUtils.color("&aUprawa " + uprawa.getLevel() + "lvl &f(" + ChatUtil.getNumber(uprawa.getExp()) + "/" + ChatUtil.getNumber(uprawa.expNeededToNextLevel(uprawa.getLevel())) + ")")));
        UserLevelData lowienie = u.getUserLevel().get(LevelCategory.Lowienie);
        inv.setItem(16, ItemStackUtils.createGuiItem(Material.FISHING_ROD, 1, ColorUtils.color("&9Łowienie " + lowienie.getLevel() + "lvl &f(" + ChatUtil.getNumber(lowienie.getExp()) + "/" + ChatUtil.getNumber(lowienie.expNeededToNextLevel(lowienie.getLevel())) + ")")));
        return inv;
    }

    @EventHandler
    public void onClick(@NotNull InventoryClickEvent e) {
        if (e.getView().getTitle().equals(title)) {
            e.setCancelled(true);
        }
    }
}
