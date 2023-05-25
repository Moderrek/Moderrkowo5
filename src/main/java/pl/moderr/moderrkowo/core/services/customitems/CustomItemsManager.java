package pl.moderr.moderrkowo.core.services.customitems;

import lombok.Getter;
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
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.ItemStackUtil;
import pl.moderr.moderrkowo.core.api.util.Logger;

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
    @Getter
    private static ItemStack szlacheckaKey = null;
    @Getter
    private static ItemStack szlacheckaChest = null;
    private static ItemStack wejsciowkaEnd = null;
    private static ItemStack fragmentEnd = null;

    public CustomItemsManager() {
        //Wejściówka do ENDU
        try {
            fragmentEnd = ItemStackUtil.createGuiItem(
                    Material.ENDER_PEARL,
                    1,
                    ColorUtil.color("&dFragment wejściówki"),
                    ColorUtil.color("&dZałamany"),
                    " ",
                    ColorUtil.color("&eZnajdź &b&lNAJLEPSZE RZEMIOSŁO"),
                    ColorUtil.color("&eaby wytworzyć teleport do kresu"));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            wejsciowkaEnd = ItemStackUtil.createGuiItem(
                    Material.BOOK,
                    1,
                    ColorUtil.color("&dWejściówka do KRESU"),
                    ColorUtil.color("&dZałamany"),
                    " ",
                    ColorUtil.color("&eKliknij PPM aby"),
                    ColorUtil.color("&eteleportować do kresu"));
            customItems.put(wejsciowkaEnd, new CustomItem() {
                @Override
                public void onEat(Player player, ItemStack itemStack) {
                }

                @Override
                public void onClick(Player player, ItemStack itemStack) {
                    if (ModerrkowoPlugin.getInstance().getAntyLogoutService().isFighting(player.getUniqueId())) {
                        player.sendMessage(ColorUtil.color("&cNie możesz używać podczas walki"));
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        Logger.logAdminLog(player.getName() + " chciał użyć teleportu do kresu podczas walki");
                        return;
                    }
                    ItemStackUtil.consumeItem(player, 1, itemStack);
                    // TELEPORT
                    player.sendMessage(ColorUtil.color("&aPrzeteleportowano do kresu"));
                    player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                    player.teleport(Objects.requireNonNull(Bukkit.getWorld("world_the_end")).getSpawnLocation());
                    player.sendTitle(ColorUtil.color(" "), ColorUtil.color("&dWitaj w kresie!"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            carrot = ItemStackUtil.createGuiItem(
                    Material.CARROT,
                    1,
                    ColorUtil.color("&dZaklęta marchewka"),
                    ColorUtil.color("&dZałamany"),
                    " ",
                    ColorUtil.color("&eDodaje &c10 pkt &egłodu"),
                    ColorUtil.color("&eMoże powodować nudności"));
            carrot.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            carrot.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            customItems.put(carrot, new CustomItem() {
                @Override
                public void onEat(Player player, ItemStack itemStack) {
                    ItemStackUtil.consumeItem(player, 1, itemStack);
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
            owrong = ItemStackUtil.createGuiItem(
                    Material.SWEET_BERRIES,
                    1,
                    ColorUtil.color("&e&lOWRONG"),
                    ColorUtil.color("&ePopularny owoc"),
                    " ",
                    ColorUtil.color("&eDodaje &c3 pkt &egłodu"));
            customItems.put(owrong, new CustomItem() {
                @Override
                public void onEat(Player player, ItemStack itemStack) {
                    ItemStackUtil.consumeItem(player, 1, itemStack);
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
            oneUseEnderchest = ItemStackUtil.createGuiItem(
                    Material.ENDER_EYE,
                    1,
                    ColorUtil.color("&dPrzenośna skrzynia kresu"),
                    ColorUtil.color("&eKliknij PPM aby otworzyć skrzynie kresu"),
                    ColorUtil.color("&eUżycie skutkuje zużyciem przedmiotu"));
            oneUseEnderchest.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            oneUseEnderchest.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            customItems.put(oneUseEnderchest, new CustomItem() {
                @Override
                public void onEat(Player player, ItemStack itemStack) {

                }

                @Override
                public void onClick(Player player, ItemStack itemStack) {
                    if (ModerrkowoPlugin.getInstance().getAntyLogoutService().isFighting(player.getUniqueId())) {
                        player.sendMessage(ColorUtil.color("&cNie możesz używać podczas walki"));
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        Logger.logAdminLog(player.getName() + " chciał użyć enderchesta podczas walki");
                        return;
                    }
                    ItemStackUtil.consumeItem(player, 1, itemStack);
                    player.openInventory(player.getEnderChest());
                    player.sendMessage(ColorUtil.color("&aOtworzono przenośna skrzynie kresu"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            zwyklaKey = ItemStackUtil.createGuiItem(
                    Material.TRIPWIRE_HOOK,
                    1,
                    ColorUtil.color("&eKlucz do skrzyni &a&lZWYKŁA"),
                    ColorUtil.color("&eAby otworzyć skrzynkę musisz,"),
                    ColorUtil.color("&ena spawnie znaleźć miejsce do otwierania skrzyń"));
            zwyklaKey.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            zwyklaKey.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            customItems.put(zwyklaKey, new CustomItem() {
                @Override
                public void onEat(Player player, ItemStack itemStack) {

                }

                @Override
                public void onClick(Player player, ItemStack itemStack) {
                    player.sendMessage(ColorUtil.color("&cAby otworzyć udaj się na spawna, z kluczem i skrzynią"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            zwyklaChest = ItemStackUtil.createGuiItem(
                    Material.CHEST,
                    1,
                    ColorUtil.color("&eSkrzynia &a&lZWYKŁA"),
                    ColorUtil.color("&eAby otworzyć skrzynkę musisz,"),
                    ColorUtil.color("&ena spawnie znaleźć miejsce do otwierania skrzyń"));
            customItems.put(zwyklaChest, new CustomItem() {
                @Override
                public void onEat(Player player, ItemStack itemStack) {

                }

                @Override
                public void onClick(Player player, ItemStack itemStack) {
                    player.sendMessage(ColorUtil.color("&cAby otworzyć udaj się na spawna, z kluczem i skrzynią"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            szlacheckaKey = ItemStackUtil.createGuiItem(
                    Material.TRIPWIRE_HOOK,
                    1,
                    ColorUtil.color("&eKlucz do skrzyni &6&lSZLACHECKA"),
                    ColorUtil.color("&eAby otworzyć skrzynkę musisz,"),
                    ColorUtil.color("&ena spawnie znaleźć miejsce do otwierania skrzyń"));
            szlacheckaKey.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
            szlacheckaKey.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            customItems.put(szlacheckaKey, new CustomItem() {
                @Override
                public void onEat(Player player, ItemStack itemStack) {

                }

                @Override
                public void onClick(Player player, ItemStack itemStack) {
                    player.sendMessage(ColorUtil.color("&cAby otworzyć udaj się na spawna, z kluczem i skrzynią"));
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            szlacheckaChest = ItemStackUtil.createGuiItem(
                    Material.CHEST,
                    1,
                    ColorUtil.color("&eSkrzynia &6&lSZLACHECKA"),
                    ColorUtil.color("&eAby otworzyć skrzynkę musisz,"),
                    ColorUtil.color("&ena spawnie znaleźć miejsce do otwierania skrzyń"));
            customItems.put(szlacheckaChest, new CustomItem() {
                @Override
                public void onEat(Player player, ItemStack itemStack) {

                }

                @Override
                public void onClick(Player player, ItemStack itemStack) {
                    player.sendMessage(ColorUtil.color("&cAby otworzyć udaj się na spawna, z kluczem i skrzynią"));
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

    public static ItemStack getWejsciowkaEnd() {
        return wejsciowkaEnd.clone();
    }

    public static ItemStack getFragmentEnd() {
        return fragmentEnd.clone();
    }

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
