package pl.moderr.moderrkowo.core.utils;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


public class PowerUtils {
    public static double CHANCE_FORTUNE_I = 0.33;
    public static double CHANCE_FORTUNE_II = 0.25;
    public static double CHANCE_FORTUNE_III = 0.20;

    @Contract(pure = true)
    public PowerUtils() {
    }

    // This method checks if the item is a power tool

    // This method returns the total amount to be dropped based on fortune level and the normal drop amount
    public static int getAmountPerFortune(int level, int amount) {
        Random rand = new Random();

        if (level == 1 && rand.nextDouble() <= CHANCE_FORTUNE_I)
            return amount * 2;
        else if (level == 2) {
            if (rand.nextDouble() <= CHANCE_FORTUNE_II)
                return amount * 3;
            if (rand.nextDouble() <= CHANCE_FORTUNE_II)
                return amount * 2;
        } else if (level == 3) {
            if (rand.nextDouble() <= CHANCE_FORTUNE_III)
                return amount * 4;
            if (rand.nextDouble() <= CHANCE_FORTUNE_III)
                return amount * 3;
            if (rand.nextDouble() <= CHANCE_FORTUNE_III)
                return amount * 2;
        }

        return amount;
    }

    // This method calculates and returns the chance of flint dropping from breaking gravel based on fortune level
    public static double getFlintDropChance(int level) {
        double chance = 0.10;

        if (level == 1)
            chance = 0.14;
        else if (level == 2)
            chance = 0.25;
        else if (level == 3)
            chance = 1.0;

        return chance;
    }

    // This method returns if you can use silk-touch on the block
    public static boolean canSilkTouchMine(Material blockType) {
        return Reference.MINABLE_SILKTOUCH.contains(blockType);
    }

    public static boolean canSilkTouchDig(Material blockType) {
        return Reference.DIGGABLE_SILKTOUCH.contains(blockType);
    }

    // This method returns if you can use fortune on the block
    public static boolean canFortuneMine(Material blockType) {
        return Reference.MINABLE_FORTUNE.get(blockType) != null;
    }

    public static boolean canFortuneDig(Material blockType) {
        return Reference.DIGGABLE_FORTUNE.get(blockType) != null;
    }

    // This method returns if the block is mineable
    public static boolean isMineable(Material blockType) {
        return Reference.MINABLE.containsKey(blockType);
    }

    // This method returns if the block is digable
    public static boolean isDigable(Material blockType) {
        return Reference.DIGGABLE.contains(blockType);
    }

    // This method will process the enchantment information and apply to to create the appropriate drop
    public static ItemStack processEnchantsAndReturnItemStack(Enchantment enchant, int enchantLevel, Block block) {
        Material blockType = block.getType();
        ItemStack drop = null;

        if (enchant == Enchantment.SILK_TOUCH)
            drop = new ItemStack(blockType, 1);
        else if (enchant == Enchantment.LOOT_BONUS_BLOCKS) {
            int amount = 0;
            Random rand = new Random();

            if (Reference.MINABLE_FORTUNE.get(blockType) != null) {
                switch (blockType) {
                    case GLOWSTONE: // Glowstone drops 2-4 dust, up to 4 max
                        amount = Math.min((rand.nextInt(5) + 2) + enchantLevel, 4);

                        break;
                    case REDSTONE_ORE: // Redstone Ore drops 4-5 dust, up to 8 max
                        amount = Math.min((rand.nextInt(2) + 4) + enchantLevel, 8);

                        break;
                    case COAL_ORE: // All these ores drop only 1 item
                    case DIAMOND_ORE:
                    case EMERALD_ORE:
                    case NETHER_QUARTZ_ORE:
                        amount = getAmountPerFortune(enchantLevel, 1);
                        break;
                    case LAPIS_ORE: // Lapis Ore drops 4-8 lapis, up to 32 max
                        amount = Math.min(getAmountPerFortune(enchantLevel, (rand.nextInt(5) + 4)), 32);
                        break;
                    default:
                        break;
                }

                if (amount > 0) {
                    // Lapis needs to be special parsed since it's actually just a DYE with damage value of 4
                    if (blockType == Material.LAPIS_ORE)
                        drop = new ItemStack(Reference.MINABLE_FORTUNE.get(blockType), amount, (short) 4);
                    else
                        drop = new ItemStack(Reference.MINABLE_FORTUNE.get(blockType), amount);
                }
            } else if (Reference.DIGGABLE_FORTUNE.get(blockType) != null) {
                if (blockType == Material.GLOWSTONE) { // Glowstone drops 2-4 dust, up to 4 max
                    amount = Math.min((rand.nextInt(5) + 2) + enchantLevel, 4);

                    drop = new ItemStack(Reference.DIGGABLE_FORTUNE.get(blockType), amount);
                } else if (blockType == Material.GRAVEL) {
                    if (rand.nextDouble() <= getFlintDropChance(enchantLevel))
                        drop = new ItemStack(Reference.DIGGABLE_FORTUNE.get(blockType), 1);
                    else // If no flint is going to be dropped, drop gravel instead
                        drop = new ItemStack(blockType, 1);
                }
            }
        }

        return drop;
    }

    // This method returns a list of surrounding (3x3) blocks given a block face and target block
    public static ArrayList<Block> getSurroundingBlocks(BlockFace blockFace, Block targetBlock) {
        ArrayList<Block> blocks = new ArrayList<>();
        World world = targetBlock.getWorld();

        int x, y, z;
        x = targetBlock.getX();
        y = targetBlock.getY();
        z = targetBlock.getZ();

        // Check the block face from which the block is being broken in order to get the correct surrounding blocks
        switch (blockFace) {
            case UP:
            case DOWN:
                blocks.add(world.getBlockAt(x + 1, y, z));
                blocks.add(world.getBlockAt(x - 1, y, z));
                blocks.add(world.getBlockAt(x, y, z + 1));
                blocks.add(world.getBlockAt(x, y, z - 1));
                blocks.add(world.getBlockAt(x + 1, y, z + 1));
                blocks.add(world.getBlockAt(x - 1, y, z - 1));
                blocks.add(world.getBlockAt(x + 1, y, z - 1));
                blocks.add(world.getBlockAt(x - 1, y, z + 1));
                break;
            case EAST:
            case WEST:
                blocks.add(world.getBlockAt(x, y, z + 1));
                blocks.add(world.getBlockAt(x, y, z - 1));
                blocks.add(world.getBlockAt(x, y + 1, z));
                blocks.add(world.getBlockAt(x, y - 1, z));
                blocks.add(world.getBlockAt(x, y + 1, z + 1));
                blocks.add(world.getBlockAt(x, y - 1, z - 1));
                blocks.add(world.getBlockAt(x, y - 1, z + 1));
                blocks.add(world.getBlockAt(x, y + 1, z - 1));
                break;
            case NORTH:
            case SOUTH:
                blocks.add(world.getBlockAt(x + 1, y, z));
                blocks.add(world.getBlockAt(x - 1, y, z));
                blocks.add(world.getBlockAt(x, y + 1, z));
                blocks.add(world.getBlockAt(x, y - 1, z));
                blocks.add(world.getBlockAt(x + 1, y + 1, z));
                blocks.add(world.getBlockAt(x - 1, y - 1, z));
                blocks.add(world.getBlockAt(x + 1, y - 1, z));
                blocks.add(world.getBlockAt(x - 1, y + 1, z));
                break;
            default:
                break;
        }

        // Trim the nulls from the list
        blocks.removeAll(Collections.singleton(null));
        return blocks;
    }


    // Returns if the tool is a valid hammer against certain block
    public static boolean validateHammer(Material hammerType, Material blockType) {
        return (isMineable(blockType) && Reference.PICKAXES.contains(hammerType) &&
                (Reference.MINABLE.get(blockType) == null || Reference.MINABLE.get(blockType).contains(hammerType)));
    }

    // Returns if the tool is a valid excavator against certain block
    public static boolean validateExcavator(Material excavatorType, Material blockType) {
        return (isDigable(blockType) && Reference.SPADES.contains(excavatorType));
    }
}
