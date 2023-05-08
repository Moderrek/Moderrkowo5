package pl.moderr.moderrkowo.core.customitems;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.ItemStackUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;

public class CustomItemsManager implements Listener {

    public final static HashMap<ItemStack, CustomItem> customItems = new HashMap<>();
    private static ItemStack carrot = null;
    private static ItemStack owrong = null;
    private static ItemStack oneUseEnderchest = null;
    private static ItemStack zwyklaKey = null;
    private static ItemStack zwyklaChest = null;
    private static ItemStack wejsciowkaEnd = null;
    private static ItemStack fragmentEnd = null;

    public CustomItemsManager() {
        //Wejściówka do ENDU
        try{
            fragmentEnd = ItemStackUtils.createGuiItem(
                    Material.ENDER_PEARL,
                    1,
                    ColorUtils.color("&dFragment wejściówki"),
                    ColorUtils.color("&dZałamany"),
                    " ",
                    ColorUtils.color("&eZnajdź &b&lNAJLEPSZE RZEMIOSŁO"),
                    ColorUtils.color("&eaby wytworzyć teleport do kresu"));
            fragmentEnd.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            fragmentEnd.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            customItems.put(fragmentEnd, new CustomItem() {
                @Override
                public void onEat(Player player, ItemStack itemStack) {
                }

                @Override
                public void onClick(Player player, ItemStack itemStack) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        try{
            wejsciowkaEnd = ItemStackUtils.createGuiItem(
                    Material.BOOK,
                    1,
                    ColorUtils.color("&dWejściówka do KRESU"),
                    ColorUtils.color("&dZałamany"),
                    " ",
                    ColorUtils.color("&eKliknij PPM aby"),
                    ColorUtils.color("&eteleportować do kresu"));
            customItems.put(wejsciowkaEnd, new CustomItem() {
                @Override
                public void onEat(Player player, ItemStack itemStack) {
                }

                @Override
                public void onClick(Player player, ItemStack itemStack) {
                    if (Main.getInstance().instanceAntyLogout.inFight(player.getUniqueId())) {
                        player.sendMessage(ColorUtils.color("&cNie możesz używać podczas walki"));
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        Logger.logAdminLog(player.getName() + " chciał użyć teleportu do kresu podczas walki");
                        return;
                    }
                    ItemStackUtils.consumeItem(player, 1, itemStack);
                    // TELEPORT
                    player.sendMessage(ColorUtils.color("&aPrzeteleportowano do kresu"));
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,1,1);
                    player.teleport(Objects.requireNonNull(Bukkit.getWorld("world_the_end")).getSpawnLocation());
                    player.sendTitle(ColorUtils.color(" "), ColorUtils.color("&dWitaj w kresie!"));
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            carrot = ItemStackUtils.createGuiItem(
                    Material.CARROT,
                    1,
                    ColorUtils.color("&dZaklęta marchewka"),
                    ColorUtils.color("&dZałamany"),
                    " ",
                    ColorUtils.color("&eDodaje &c10 pkt &egłodu"),
                    ColorUtils.color("&eMoże powodować nudności"));
            carrot.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            carrot.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            customItems.put(carrot, new CustomItem() {
                @Override
                public void onEat(Player player, ItemStack itemStack) {
                    ItemStackUtils.consumeItem(player, 1, itemStack);
                    player.setFoodLevel(player.getFoodLevel() + 10);
                    Random rnd = new Random();
                    int i = rnd.nextInt(100);
                    if (i <= 35) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 20 * 10, 2));
                    } else {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20 * 3, 2));
                    }
                }

                @Override
                public void onClick(Player player, ItemStack itemStack) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            owrong = ItemStackUtils.createGuiItem(
                    Material.SWEET_BERRIES,
                    1,
                    ColorUtils.color("&e&lOWRONG"),
                    ColorUtils.color("&ePopularny owoc"),
                    " ",
                    ColorUtils.color("&eDodaje &c3 pkt &egłodu"));
            customItems.put(owrong, new CustomItem() {
                @Override
                public void onEat(Player player, ItemStack itemStack) {
                    ItemStackUtils.consumeItem(player, 1, itemStack);
                    player.setFoodLevel(player.getFoodLevel() + 3);
                }

                @Override
                public void onClick(Player player, ItemStack itemStack) {

                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            oneUseEnderchest = ItemStackUtils.createGuiItem(
                    Material.ENDER_EYE,
                    1,
                    ColorUtils.color("&dPrzenośna skrzynia kresu"),
                    ColorUtils.color("&eKliknij PPM aby otworzyć skrzynie kresu"),
                    ColorUtils.color("&eUżycie skutkuje zużyciem przedmiotu"));
            oneUseEnderchest.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            oneUseEnderchest.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            customItems.put(oneUseEnderchest, new CustomItem() {
                @Override
                public void onEat(Player player, ItemStack itemStack) {

                }

                @Override
                public void onClick(Player player, ItemStack itemStack) {
                    if (Main.getInstance().instanceAntyLogout.inFight(player.getUniqueId())) {
                        player.sendMessage(ColorUtils.color("&cNie możesz używać podczas walki"));
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        Logger.logAdminLog(player.getName() + " chciał użyć enderchesta podczas walki");
                        return;
                    }
                    ItemStackUtils.consumeItem(player, 1, itemStack);
                    player.openInventory(player.getEnderChest());
                    player.sendMessage(ColorUtils.color("&aOtworzono przenośna skrzynie kresu"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            zwyklaKey = ItemStackUtils.createGuiItem(
                    Material.TRIPWIRE_HOOK,
                    1,
                    ColorUtils.color("&eKlucz do skrzyni &a&lZWYKŁA"),
                    ColorUtils.color("&eAby otworzyć skrzynkę musisz,"),
                    ColorUtils.color("&ena spawnie znaleźć miejsce do otwierania skrzyń"));
            zwyklaKey.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            zwyklaKey.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            customItems.put(zwyklaKey, new CustomItem() {
                @Override
                public void onEat(Player player, ItemStack itemStack) {

                }

                @Override
                public void onClick(Player player, ItemStack itemStack) {
                    player.sendMessage(ColorUtils.color("&cAby otworzyć udaj się na spawna, z kluczem i skrzynią"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            zwyklaChest = ItemStackUtils.createGuiItem(
                    Material.CHEST,
                    1,
                    ColorUtils.color("&eSkrzynia &a&lZWYKŁA"),
                    ColorUtils.color("&eAby otworzyć skrzynkę musisz,"),
                    ColorUtils.color("&ena spawnie znaleźć miejsce do otwierania skrzyń"));
            customItems.put(zwyklaChest, new CustomItem() {
                @Override
                public void onEat(Player player, ItemStack itemStack) {

                }

                @Override
                public void onClick(Player player, ItemStack itemStack) {
                    player.sendMessage(ColorUtils.color("&cAby otworzyć udaj się na spawna, z kluczem i skrzynią"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ItemStack getCarrot() {
        return carrot.clone();
    }

    public static ItemStack getOwrong() {
        return owrong.clone();
    }

    public static ItemStack getOneUseEnderchest() {
        return oneUseEnderchest.clone();
    }

    public static ItemStack getZwyklaKey() {
        return zwyklaKey.clone();
    }

    public static ItemStack getZwyklaChest() {
        return zwyklaChest.clone();
    }

    public static ItemStack getWejsciowkaEnd() { return wejsciowkaEnd.clone(); }

    public static ItemStack getFragmentEnd() { return fragmentEnd.clone(); }

    @EventHandler(priority = EventPriority.MONITOR)
    public void preCraft(PrepareItemCraftEvent e) {
        for (ItemStack is : e.getInventory().getMatrix()) {
            customItems.keySet().forEach(itemStack -> {
                if (itemStack.isSimilar(is)) {
                    e.getInventory().setResult(new ItemStack(Material.AIR));
                }
            });
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        if (e.getItem() == null) {
            return;
        }
        if ((e.getAction() == Action.RIGHT_CLICK_BLOCK) || (e.getAction() == Action.RIGHT_CLICK_AIR) || (e.getAction() == Action.LEFT_CLICK_BLOCK) || (e.getAction() == Action.LEFT_CLICK_AIR)) {
            customItems.keySet().forEach(itemStack -> {
                if (itemStack.isSimilar(e.getItem())) {
                    e.setCancelled(true);
                    customItems.get(itemStack).onClick(e.getPlayer(), e.getItem());
                }
            });
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
     public void onEat(PlayerItemConsumeEvent e) {
        if (e.isCancelled()) {
            return;
        }
        customItems.keySet().forEach(itemStack -> {
            if (itemStack.isSimilar(e.getItem())) {
                e.setCancelled(true);
                customItems.get(itemStack).onEat(e.getPlayer(), e.getItem());
            }
        });
    }
}
