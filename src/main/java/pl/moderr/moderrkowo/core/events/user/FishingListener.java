package pl.moderr.moderrkowo.core.events.user;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.util.*;
import pl.moderr.moderrkowo.core.commands.user.WithdrawCommand;
import pl.moderr.moderrkowo.core.services.customitems.CustomItemsManager;
import pl.moderr.moderrkowo.core.services.mysql.UserManager;
import pl.moderr.moderrkowo.core.services.npc.NPCManager;
import pl.moderr.moderrkowo.core.services.npc.data.data.PlayerNPCData;
import pl.moderr.moderrkowo.core.services.npc.data.npc.NPCData;
import pl.moderr.moderrkowo.core.services.npc.data.quest.Quest;
import pl.moderr.moderrkowo.core.services.npc.data.tasks.IQuestItem;
import pl.moderr.moderrkowo.core.services.npc.data.tasks.IQuestItemFish;
import pl.moderr.moderrkowo.core.services.npc.data.tasks.IQuestItemFishingRod;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.user.level.LevelCategory;

import java.util.Objects;
import java.util.Random;

interface FishDrop {
    ItemStack getItemStack();
}

interface FishDropItemStack extends FishDrop {
    Material getMaterial();

    @Override
    default ItemStack getItemStack() {
        return new ItemStack(getMaterial(), 1);
    }
}

interface FishDropShulkerBox extends FishDrop {
    @Override
    default ItemStack getItemStack() {
        Bukkit.broadcastMessage(ColorUtil.color("&8[!] &eKtoś wyłowił skrzynkę"));
//        return Main.shulkerDropBox.getRandomShulker();
        return null;
    }
}

interface FishDropBanknot extends FishDrop {
    RandomRange getRange();

    @Override
    default ItemStack getItemStack() {
        return WithdrawCommand.generateItemStatic(1, getRange().getRandom());
    }
}

interface FishDropRandomItemStack extends FishDrop {
    RandomRange getRange();

    Material getMaterial();

    @Override
    default ItemStack getItemStack() {
        return new ItemStack(getMaterial(), getRange().getMax());
    }
}

public class FishingListener implements Listener {

    private final WeightedList<FishDrop> randomDrop = new WeightedList<>();

    public FishingListener() {
        randomDrop.put((FishDropItemStack) () -> Material.CHARCOAL, 24);
        randomDrop.put((FishDropItemStack) () -> Material.GOLD_INGOT, 4);
        randomDrop.put((FishDropItemStack) () -> Material.BOWL, 8);
        randomDrop.put((FishDropItemStack) () -> Material.STRING, 8);
        randomDrop.put((FishDropItemStack) () -> Material.GUNPOWDER, 10);
        randomDrop.put((FishDropItemStack) () -> Material.EMERALD, 4);
        randomDrop.put((FishDropItemStack) () -> Material.SADDLE, 2);
        randomDrop.put((FishDropItemStack) () -> Material.LEATHER, 12);
        randomDrop.put((FishDropItemStack) () -> Material.CLAY_BALL, 12);
        randomDrop.put((FishDropItemStack) () -> Material.LILY_PAD, 12);
        randomDrop.put((FishDropItemStack) () -> Material.STICK, 12);
        randomDrop.put((FishDropItemStack) () -> Material.TRIPWIRE_HOOK, 12);
        randomDrop.put(CustomItemsManager::getZwyklaChest, 3);
        randomDrop.put((FishDropItemStack) () -> Material.BOOK, 4);
        randomDrop.put((FishDropItemStack) () -> Material.COD, 120);
        randomDrop.put((FishDropItemStack) () -> Material.SALMON, 48);
        randomDrop.put((FishDropItemStack) () -> Material.TROPICAL_FISH, 20);
        randomDrop.put((FishDropItemStack) () -> Material.PUFFERFISH, 14);
        randomDrop.put((FishDropItemStack) () -> Material.BONE, 6);
        randomDrop.put((FishDropItemStack) () -> Material.EXPERIENCE_BOTTLE, 3);
        randomDrop.put((FishDropItemStack) () -> Material.NAME_TAG, 2);
        randomDrop.put((FishDropItemStack) () -> Material.ENDER_PEARL, 2);
        randomDrop.put((FishDropItemStack) () -> Material.COD, 8);
//        randomDrop.put(new FishDropShulkerBox() {
//        }, 1);
        randomDrop.put((FishDropBanknot) () -> new RandomRange(1, 100), 10);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onFish(@NotNull PlayerFishEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getState().equals(PlayerFishEvent.State.CAUGHT_FISH)) {
            e.setExpToDrop((int) (e.getExpToDrop() * 1.5));
            try {
                ItemStack drop = randomDrop.get(new Random()).getItemStack();
                ((Item) Objects.requireNonNull(e.getCaught())).setItemStack(drop);
                UserManager.getUser(e.getPlayer().getUniqueId()).addExp(LevelCategory.Lowienie, getExp(drop, e.getPlayer().getInventory().getItemInMainHand()));
                double d = ((double) RandomUtil.getRandomInt(1, 9) / 15) * UserManager.getUser(e.getPlayer().getUniqueId()).getLevel().get(LevelCategory.Lowienie).getLevel();
                UserManager.getUser(e.getPlayer().getUniqueId()).addMoney(d);
                e.getPlayer().sendActionBar(Component.text("+" + ChatUtil.formatMoney(d)).color(NamedTextColor.GREEN));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            tryAddQuestData(e);
        }
    }

    private void tryAddQuestData(PlayerFishEvent e) {
        if (e.getCaught() == null) {
            return;
        }
        Item is = (Item) e.getCaught();
        User u = UserManager.getUser(e.getPlayer().getUniqueId());
        PlayerNPCData data = null;
        for (PlayerNPCData villagers : u.getQuestData().getNPCSData().values()) {
            if (villagers.isActiveQuest()) {
                data = villagers;
                break;
            }
        }
        if (data == null) {
            return;
        }
        final NPCManager npc = ModerrkowoPlugin.getInstance().getNpc();
        NPCData villager = npc.npcs.get(data.getNpcId());
        Quest quest = villager.getQuests().get(data.getQuestIndex());
        for (IQuestItem item : quest.getQuestItems()) {
            if (item instanceof IQuestItemFishingRod) {
                IQuestItemFishingRod craftItem = (IQuestItemFishingRod) item;
                int recipeAmount = 1;
                int items = data.getQuestItemData().get(craftItem.getQuestItemDataId());
                int temp = items;
                temp += recipeAmount;
                data.getQuestItemData().replace(craftItem.getQuestItemDataId(), items, temp);
                final TextComponent component = Component.text()
                        .color(NamedTextColor.GREEN)
                        .append(Component.text("Q").decoration(TextDecoration.BOLD, true).color(NamedTextColor.RED))
                        .appendSpace()
                        .append(Component.text("»").color(NamedTextColor.GOLD))
                        .appendSpace()
                        .append(Component.text("Zarzucono wędkę"))
                        .build();
                e.getPlayer().sendMessage(component);
                u.updateScoreboard();
            }
            if (item instanceof IQuestItemFish) {
                IQuestItemFish craftItem = (IQuestItemFish) item;
                if (craftItem.getMaterial().equals(is.getItemStack().getType())) {
                    int recipeAmount = 1;
                    int items = data.getQuestItemData().get(craftItem.getQuestItemDataId());
                    int temp = items;
                    temp += recipeAmount;
                    data.getQuestItemData().replace(craftItem.getQuestItemDataId(), items, temp);
                    final TextComponent component = Component.text()
                            .color(NamedTextColor.GREEN)
                            .append(Component.text("Q").decoration(TextDecoration.BOLD, true).color(NamedTextColor.RED))
                            .appendSpace()
                            .append(Component.text("»").color(NamedTextColor.GOLD))
                            .appendSpace()
                            .append(Component.text("Złowiono"))
                            .appendSpace()
                            .append(Component.translatable(is.getItemStack().getType().translationKey()))
                            .build();
                    e.getPlayer().sendMessage(component);
                    u.updateScoreboard();
                }
            }
        }
    }

    private double getExp(ItemStack e, ItemStack fishingRod) {
        if (e == null) {
            return 0;
        }
        int multiply = 1;
        if (fishingRod.getItemMeta().hasEnchant(Enchantment.LUCK)) {
            multiply = fishingRod.getEnchantmentLevel(Enchantment.LUCK);
        }
        double value = 1.5;
        switch (e.getType()) {
            case COD:
                value = 0.3;
                break;
            case SALMON:
                value = 0.7;
                break;
            case PUFFERFISH:
                value = 1.2;
                break;
            case TROPICAL_FISH:
                value = 0.5;
                break;
        }
        return value * multiply;
    }

}
