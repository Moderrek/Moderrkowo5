package pl.moderr.moderrkowo.core.opening;

import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.customitems.CustomItemsManager;
import pl.moderr.moderrkowo.core.mysql.User;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.opening.data.*;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.ItemStackUtils;
import pl.moderr.moderrkowo.core.utils.WeightedList;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ModerrCaseManager implements Listener {

    private final String InventoryNameCheckChests = ColorUtils.color("&7Otwórz &6&lSKRZYNIE");
    private final String InventoryNameCheckPercent = ColorUtils.color("&6Podgląd skrzyni");

    public ModerrCaseManager() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }


    private Inventory getInvPercent(ModerrCase chest) {
        Inventory inv = Bukkit.createInventory(null, 54, InventoryNameCheckPercent);
        for (int i = 0; i != 53; i++) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
        }
        for (int i = 53 - 8; i != 53; i++) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
        }
        for(int i = 0; i != chest.itemList().size(); i++){
            ModerrCaseItem item = new ArrayList<>(chest.itemList()).get(i);
            String color = "";
            switch (item.rarity()){
                case POSPOLITE:
                    color = "&f&lPospolite";
                    break;
                case RZADKIE:
                    color = "&9&lRzadkie";
                    break;
                case LEGENDARNE:
                    color = "&d&lLegendarne";
                    break;
                case MITYCZNE:
                    color = "&c&lMityczne";
                    break;
            }
            ItemStack itemstack = item.item();
            ItemStackUtils.changeLore(itemstack, " ", ColorUtils.color("&eRzadkość: " + color), ColorUtils.color("&eSzansa: " + checkPercent(chest.randomList(), item.weight)), " ");
            inv.setItem(i, itemstack);
        }
        inv.setItem(53, ItemStackUtils.createGuiItem(Material.BARRIER, 1, ColorUtils.color("&cWyjdź")));
        return inv;
    }

    public String checkPercent(WeightedList<ModerrCaseItem> weightedList, double weight){
        double suma = weightedList.values().stream().mapToInt(integer -> integer).sum();
        DecimalFormat df = new DecimalFormat("##.##%");
        return df.format(weight /suma);
    }

    public Inventory getInv(User u){
        //22
        Inventory inv = Bukkit.createInventory(null, 54, InventoryNameCheckChests);
        for (int i = 0; i != 53; i++) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
        }
        for (int i = 53 - 8; i != 53; i++) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
        }
        inv.setItem(53, ItemStackUtils.createGuiItem(Material.BARRIER, 1, ColorUtils.color("&cWyjdź")));
        ModerrCaseEnum chestType = ModerrCaseEnum.ZWYKLA;
        ModerrCase chest = ModerrCaseConstants.getCase(chestType);
        inv.setItem(22, ItemStackUtils.createGuiItem(Material.CHEST, 1,
                ColorUtils.color("   &7Skrzynia " + chest.name() + "   "),
                ColorUtils.color(" "),
                ColorUtils.color("   &7Posiadasz: &6" + ItemStackUtils.getSameItems(u.getPlayer(), CustomItemsManager.getZwyklaChest()) + "   "),
                ColorUtils.color("   &7Klucze: &6" + ItemStackUtils.getSameItems(u.getPlayer(), CustomItemsManager.getZwyklaKey()) + "   "),
                ColorUtils.color(" "),
                ColorUtils.color("   &8Kliknij aby otworzyć" + "   "),
                ColorUtils.color("   &8PPM szczegóły"),
                ColorUtils.color(" ")
        ));
        /*ModerrCase chest2 = ModerrCaseConstants.getCase(ModerrCaseEnum.SKAZENIA);
        inv.setItem(31, ItemStackUtils.createGuiItem(Material.CRIMSON_NYLIUM, 1,
                ColorUtils.color("   &7Skrzynia " + chest2.name() + "   "),
                ColorUtils.color(" "),
                ColorUtils.color("   &7Posiadasz: &6" + u.getUserChestStorage().getAmountOfItem(new StorageItemKey(StorageItemType.Chest, ModerrCaseEnum.SKAZENIA)) + "   "),
                ColorUtils.color("   &7Klucze: &6" + u.getUserChestStorage().getAmountOfItem(new StorageItemKey(StorageItemType.Key, ModerrCaseEnum.SKAZENIA)) + "   "),
                ColorUtils.color(" "),
                ColorUtils.color("   &8Kliknij aby otworzyć" + "   "),
                ColorUtils.color("   &8PPM szczegóły"),
                ColorUtils.color(" ")
        ));*/
        return inv;
    }

    HashMap<UUID, OpeningData> openingCase = new HashMap<>();

    public void OpenCase(Player p, ModerrCase chest){
        Inventory inv = Bukkit.createInventory(null, 27, ColorUtils.color("&7Skrzynia " + chest.name()));
        for (int i = 0; i != 27; i++) {
            inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
        }
        inv.setItem(4, ItemStackUtils.createGuiItem(Material.LIME_STAINED_GLASS_PANE, 1, " "));
        inv.setItem(22, ItemStackUtils.createGuiItem(Material.LIME_STAINED_GLASS_PANE, 1, " "));
        ArrayList<ModerrCaseItemTemp> randomizedReward = new ArrayList<>();
        for(int i = 0; i != 33; i++){
            ModerrCaseItem item = chest.randomList().get(new Random());
            randomizedReward.add(new ModerrCaseItemTemp(item.item(), item.rarity()));
        }
        // showing anim
        int maxOffset = 21;
        int taskId;
        AtomicInteger offset = new AtomicInteger();
        taskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.getInstance(), () -> {
            offset.getAndIncrement();
            anim(p, inv, randomizedReward, offset.get());
            if (offset.get() == maxOffset) {
                OpenCaseReward(p.getUniqueId(), false);
                p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            } else {
                p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 2, 1);
            }
        },0,5L);
        openingCase.put(p.getUniqueId(), new OpeningData(new ModerrCaseItemTemp(randomizedReward.get(3).item(), randomizedReward.get(3).rarity()), chest.name(), inv, taskId));
        openingCase.get(p.getUniqueId()).setReward(randomizedReward.get(3+maxOffset));
        p.openInventory(inv);
    }

    public void OpenCaseReward(UUID playerId, boolean silent) {
        OpeningData data = openingCase.get(playerId);
        openingCase.remove(playerId);
        Bukkit.getScheduler().cancelTask(data.getTaskId());
        if (!silent) {
            Bukkit.broadcastMessage(ColorUtils.color("  &e&l" + Bukkit.getPlayer(playerId).getName() + " &7otwiera skrzynie &f" + data.name()));
        }
        String color = "";
        switch (data.getReward().rarity()) {
            case POSPOLITE:
                color = "&f&lP &e";
                break;
            case RZADKIE:
                color = "&9&lR &e";
                break;
            case LEGENDARNE:
                color = "&d&lL &e";
                break;
            case MITYCZNE:
                color = "&c&lM &e";
                break;
        }
        if (!silent) {
            Bukkit.broadcastMessage(ColorUtils.color("  &7znajduje " + color + ChatUtil.materialName(data.getReward().item().getType())));
        }
        Player p = Bukkit.getPlayer(playerId);
        assert p != null;
        if (p.getInventory().firstEmpty() != -1) {
            p.getInventory().addItem(data.getReward().item());
        } else {
            p.getWorld().dropItem(p.getLocation(), data.getReward().item());
        }
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> Objects.requireNonNull(Bukkit.getPlayer(playerId)).closeInventory(), 30);
        spawnFireworks(p.getLocation(), 1);

    }

    public static void spawnFireworks(Location location, int amount){
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());
        fw.setFireworkMeta(fwm);
        fw.detonate();
        for(int i = 0;i<amount; i++){
            Firework fw2 = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

    public void anim(Player p, Inventory inv, ArrayList<ModerrCaseItemTemp> rewards, int offset){
        for(int i = 10; i != 17; i++){
            String color = "";
            ModerrCaseItemTemp item = rewards.get((i-10)+offset);
            switch (item.rarity()){
                case POSPOLITE:
                    color = "&f&lP &e";
                    break;
                case RZADKIE:
                    color = "&9&lR &e";
                    break;
                case LEGENDARNE:
                    color = "&d&lL &e";
                    break;
                case MITYCZNE:
                    color = "&c&lM &e";
                    break;
                case ZALAMANY:
                    color = "&d&lZ &e";
                    break;
            }
            inv.setItem(i, ItemStackUtils.changeName(item.item(), color + ChatUtil.materialName(item.item().getType())));
        }
    }

    @EventHandler
    public void closeInventory(InventoryCloseEvent e){
        if(ModerrCaseConstants.getGuiNames().contains(e.getView().getTitle())){
            OpenCaseReward(e.getPlayer().getUniqueId(), true);
        }
    }


    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            if (e.getView().getTitle().equals(InventoryNameCheckChests) || e.getView().getTitle().equals(ColorUtils.color("&7Skrzynia &6&lTEST"))) {
                e.setCancelled(true);
                ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return;
            }
            return;
        }
        if (e.getView().getTitle().equals(InventoryNameCheckPercent)) {
            e.setCancelled(true);
            if (e.getSlot() == 53) {
                e.getWhoClicked().closeInventory();
            }
            return;
        }
        if (ModerrCaseConstants.getGuiNames().contains(e.getView().getTitle())) {
            e.setCancelled(true);
            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
        if (e.getView().getTitle().equals(InventoryNameCheckChests)) {
            e.setCancelled(true);
            User u = UserManager.getUser(e.getWhoClicked().getUniqueId());
            if (e.getSlot() == 22) {
                if (e.isRightClick()) {
                    e.getWhoClicked().openInventory(getInvPercent(ModerrCaseConstants.getCase(ModerrCaseEnum.ZWYKLA)));
                    return;
                }
                if (ItemStackUtils.getSameItems(u.getPlayer(), CustomItemsManager.getZwyklaChest()) >= 1 && ItemStackUtils.getSameItems(u.getPlayer(), CustomItemsManager.getZwyklaKey()) >= 1) {
                    if (ItemStackUtils.consumeItem(u.getPlayer(), 1, CustomItemsManager.getZwyklaChest()) &&
                            ItemStackUtils.consumeItem(u.getPlayer(), 1, CustomItemsManager.getZwyklaKey())) {
                        OpenCase((Player) e.getWhoClicked(), ModerrCaseConstants.getCase(ModerrCaseEnum.ZWYKLA));
                    } else {
                        e.getWhoClicked().sendMessage(ColorUtils.color("&cNie udało się zabrać skrzynki i klucza!"));
                        e.getWhoClicked().sendMessage(ColorUtils.color("&cZgłoś helpop albo rodziel itemy na pojedyńcze"));
                        ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        e.getWhoClicked().closeInventory();
                    }
                } else {
                    e.getWhoClicked().sendMessage(ColorUtils.color("&8[!] &cNie posiadasz potrzebnych przedmiotów"));
                    e.getWhoClicked().sendMessage(ColorUtils.color("&8[!] &eAby otworzyć skrzynke potrzebna jest skrzynia i klucz"));
                }
                /*if(u.getUserChestStorage().hasItem(new StorageItemKey(StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), 1) && u.getUserChestStorage().hasItem(new StorageItemKey(StorageItemType.Key, ModerrCaseEnum.ZWYKLA), 1)){
                    OpenCase((Player) e.getWhoClicked(), ModerrCaseConstants.getCase(ModerrCaseEnum.ZWYKLA));
                    u.getUserChestStorage().subtractChest(new StorageItemKey(StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), 1);
                    u.getUserChestStorage().subtractKey(new StorageItemKey(StorageItemType.Key, ModerrCaseEnum.ZWYKLA), 1);
                }else{
                    e.getWhoClicked().sendMessage(ColorUtils.color("&8[!] &cNie posiadasz potrzebnych przedmiotów"));
                    e.getWhoClicked().sendMessage(ColorUtils.color("&8[!] &eAby otworzyć skrzynke potrzebna jest skrzynia i klucz"));
                }*/
                return;
            }
            if (e.getSlot() == 53) {
                e.getWhoClicked().closeInventory();
                return;
            }
            ((Player) e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void interact(PlayerInteractEvent e) {
        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            //-60 80 -64
            Location loc1 = new Location(Bukkit.getWorld("void"), -60, 80, -64);
            Location loc3 = new Location(Bukkit.getWorld("void"), 444, 83, -415);
            Location loc2 = Objects.requireNonNull(e.getClickedBlock()).getLocation();
            if (!loc2.getWorld().getName().equals("void")) {
                return;
            }
            if (loc1.getBlockX() == loc2.getBlockX() &&
                    loc1.getBlockY() == loc2.getBlockY() &&
                    loc1.getBlockZ() == loc2.getBlockZ()) {
                e.setCancelled(true);
                Player p = e.getPlayer();
                p.playSound(p.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1, 0.7f);
                p.openInventory(getInv(UserManager.getUser(p.getUniqueId())));
                return;
            }
            if (loc3.getBlockX() == loc2.getBlockX() &&
                    loc3.getBlockY() == loc2.getBlockY() &&
                    loc3.getBlockZ() == loc2.getBlockZ()) {
                e.setCancelled(true);
                Player p = e.getPlayer();
                p.playSound(p.getLocation(), Sound.BLOCK_ENDER_CHEST_OPEN, 1, 0.7f);
                p.openInventory(getInv(UserManager.getUser(p.getUniqueId())));
            }
        }
    }
}
