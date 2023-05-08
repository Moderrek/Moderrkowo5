package pl.moderr.moderrkowo.core.listeners;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Ageable;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.mysql.LevelCategory;
import pl.moderr.moderrkowo.core.mysql.User;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.npc.data.data.PlayerNPCData;
import pl.moderr.moderrkowo.core.npc.data.npc.NPCData;
import pl.moderr.moderrkowo.core.npc.data.quest.Quest;
import pl.moderr.moderrkowo.core.npc.data.tasks.IQuestItem;
import pl.moderr.moderrkowo.core.npc.data.tasks.IQuestItemCollect;
import pl.moderr.moderrkowo.core.utils.ChatUtil;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.ItemStackUtils;

import java.time.Instant;
import java.util.Collection;
import java.util.IdentityHashMap;
import java.util.Map;

public class CropBreakListener implements Listener {
    private final Map<Player, Instant> cropMessage = new IdentityHashMap<>();

    @EventHandler(priority = EventPriority.MONITOR)
    private void farmBreak(@NotNull BlockBreakEvent event) {
        if (event.isCancelled()) {
            return;
        }
        final Block block = event.getBlock();
        final Player player = event.getPlayer();
        if (player.isSneaking()) {
            return;
        }
        if (isNotCrop(block)) {
            return;
        }
        final Ageable ageable = (Ageable) block.getState().getBlockData();
        if (ageable.getAge() == ageable.getMaximumAge()) {
            int ExpOrbs = 1;
            World world = block.getWorld();
            world.spawn(block.getLocation(), ExperienceOrb.class).setExperience(ExpOrbs);
            Collection<ItemStack> drops = block.getDrops(player.getInventory().getItemInMainHand());
            for (ItemStack item : drops) {
                world.dropItemNaturally(block.getLocation(), item);
            }
            questCollect(player, block);
            int i = 0;
            for (ItemStack item : drops) {
                if (item.getType().equals(Material.POTATO) || item.getType().equals(Material.CARROT)) {
                    i += item.getAmount();
                    continue;
                }
                if (!item.getType().equals(getCropSeeds(event.getBlock()))) {
                    i += item.getAmount();
                }
            }
            expCollect(player, block, i);
            if (ItemStackUtils.consumeItem(player, 1, getCropSeeds(block))) {
                autoReplant(block);
            } else {
                block.setType(Material.AIR);
            }
            spawnParticles(block.getLocation());
        } else {
            final Instant now = Instant.now();
            cropMessage.compute(player, (uuid, instant) -> {
                if (instant != null && now.isBefore(instant)) {
                    return instant;
                }
                final Component component = Component.text("Kucnij, aby zniszczyć małe ").color(NamedTextColor.YELLOW).append(Component.translatable(getCropSeeds(block).translationKey()).append(Component.text(".")));
                player.sendMessage(component);
                player.sendActionBar(component);
                return now.plusSeconds(10);
            });
        }
        event.setCancelled(true);
    }

    private void expCollect(Player player, Block block, int count) {
        try {
            User u = UserManager.getUser(player.getUniqueId());
            u.getUserLevel().get(LevelCategory.Uprawa).addExp(getExpValue(block) * count);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double getExpValue(@NotNull Block block) {
        switch (block.getType()) {
            case WHEAT:
            case POTATOES:
                return 0.4;
            case CARROTS:
                return 0.6;
            case BEETROOTS:
                return 0.5;
            case MELON_STEM:
                return 1.8;
            case PUMPKIN_STEM:
                return 1.2;
            case NETHER_WART:
                return 1;
            default:
                return 0;
        }
    }

    private void questCollect(Player player, Block block) {
        try {
            User u = UserManager.getUser(player.getUniqueId());
            PlayerNPCData data = null;
            for (PlayerNPCData villagers : u.getNPCSData().getNPCSData().values()) {
                if (villagers.isActiveQuest()) {
                    data = villagers;
                    break;
                }
            }
            if (data == null) {
                return;
            }
            NPCData villager = Main.getInstance().NPCManager.npcs.get(data.getNpcId());
            Quest quest = villager.getQuests().get(data.getQuestIndex());
            for (IQuestItem item : quest.getQuestItems()) {
                if (item instanceof IQuestItemCollect) {
                    int items = data.getQuestItemData().get(item.getQuestItemDataId());
                    int temp = items;
                    temp += 1;
                    data.getQuestItemData().replace(item.getQuestItemDataId(), items, temp);
                    player.sendMessage(ColorUtils.color("&c&lQ &6» &aZebrano &2" + ChatUtil.materialName(block.getType())));
                    u.UpdateScoreboard();
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    @EventHandler
    private void cropTrample(@NotNull PlayerInteractEvent event) {
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }
        if (!event.hasBlock()) {
            return;
        }
        final Block farmland = event.getClickedBlock();
        if (farmland == null) return;
        final Block crop = farmland.getRelative(BlockFace.UP);
        if (isNotCrop(crop)) {
            return;
        }
        event.setCancelled(true);
    }
    private void spawnParticles(@NotNull Location location) {
        location.add(.5, .5, .5);
        location.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, location, 10, .5, .5, .5, 0);
    }
    private void autoReplant(@NotNull Block block) {
        block.setType(block.getType());
    }
    private @Nullable Material getCropSeeds(Block block) {
        if (isNotCrop(block)) {
            return null;
        }
        switch (block.getType()) {
            case WHEAT:
                return Material.WHEAT_SEEDS;
            case CARROTS:
                return Material.CARROT;
            case POTATOES:
                return Material.POTATO;
            case BEETROOTS:
                return Material.BEETROOT_SEEDS;
            case MELON_STEM:
                return Material.MELON_SEEDS;
            case PUMPKIN_STEM:
                return Material.PUMPKIN_SEEDS;
            case NETHER_WART:
                return Material.NETHER_WART;
        }
        return null;
    }
    private boolean isNotCrop(@NotNull Block block) {
        switch (block.getType()) {
            case WHEAT:
            case CARROTS:
            case POTATOES:
            case BEETROOTS:
            case MELON_STEM:
            case PUMPKIN_STEM:
            case NETHER_WART:
                return false;
        }
        return true;
    }
}
