package pl.moderr.moderrkowo.core.events.user;

import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.services.mysql.UserManager;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.user.level.LevelCategory;

public class MiningListener implements Listener {

    @EventHandler(priority = EventPriority.MONITOR)
    public void breakBlock(@NotNull BlockBreakEvent e) {
        if (e.isCancelled()) return;
        if (e.getPlayer().getInventory().getItemInMainHand().hasEnchant(Enchantment.SILK_TOUCH)) return;
        if (getExpValue(e.getBlock()) == 0) return;
        int dropAmount = e.getBlock().getDrops(e.getPlayer().getInventory().getItemInMainHand()).stream().mapToInt(ItemStack::getAmount).sum();
        expCollect(e.getPlayer(), e.getBlock(), dropAmount);
    }

    private void expCollect(Player player, Block block, int amount) {
        try {
            User u = UserManager.getUser(player.getUniqueId());
            u.addExp(LevelCategory.Kopanie, getExpValue(block) * amount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private double getExpValue(@NotNull Block block) {
        switch (block.getType()) {
            case COAL_ORE:
            case COPPER_ORE:
            case REDSTONE_ORE:
            case DEEPSLATE_LAPIS_ORE:
                return 0.2;
            case IRON_ORE:
            case DEEPSLATE_COAL_ORE:
            case DEEPSLATE_COPPER_ORE:
            case DEEPSLATE_REDSTONE_ORE:
                return 0.4;
            case NETHER_GOLD_ORE:
            case DEEPSLATE_IRON_ORE:
                return 0.7;
            case GOLD_ORE:
            case LAPIS_ORE:
                return 0.1;
            case NETHER_QUARTZ_ORE:
                return 0.5;
            case DIAMOND_ORE:
                return 1.2;
            case EMERALD_ORE:
                return 2.2;
            case DEEPSLATE_GOLD_ORE:
                return 0.9;
            case DEEPSLATE_DIAMOND_ORE:
                return 2;
            case DEEPSLATE_EMERALD_ORE:
                return 3.1;
            default:
                return 0;
        }
    }

}
