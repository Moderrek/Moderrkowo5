package pl.moderr.moderrkowo.core.services.opening;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.Logger;
import pl.moderr.moderrkowo.core.api.util.WeightedList;
import pl.moderr.moderrkowo.core.services.opening.data.*;

import java.util.*;

public class ModerrCaseConstants {

    public static final Map<ModerrCaseEnum, ModerrCase> cases = new IdentityHashMap<>();

    static {
        addCase(ModerrCaseEnum.ZWYKLA,
                new ModerrCase() {
                    @Override
                    public String name() {
                        return ColorUtil.color("&a&lZWYKŁA");
                    }

                    @Override
                    public String guiName() {
                        return ColorUtil.color("&7Skrzynia " + name());
                    }

                    @Override
                    public String description() {
                        return "OPIS";
                    }

                    @Override
                    public WeightedList<ModerrCaseItem> randomList() {
                        WeightedList<ModerrCaseItem> items = new WeightedList<>();
                        itemList().forEach(item -> items.put(item, item.weight));
                        return items;
                    }

                    @Override
                    public ArrayList<ModerrCaseItem> itemList() {
                        return new ArrayList<ModerrCaseItem>() {
                            {
                                add(new ModerrCaseItem(new ItemStack(Material.ACACIA_SAPLING, 8), ModerrCaseItemRarity.POSPOLITE, 50));
                                add(new ModerrCaseItem(new ItemStack(Material.MANGROVE_LOG, 32), ModerrCaseItemRarity.POSPOLITE, 70));
                                add(new ModerrCaseItem(new ItemStack(Material.COOKED_BEEF, 16), ModerrCaseItemRarity.POSPOLITE, 20));
                                add(new ModerrCaseItem(new ItemStack(Material.COOKED_PORKCHOP, 16), ModerrCaseItemRarity.POSPOLITE, 20));
                                add(new ModerrCaseItem(new ItemStack(Material.COOKED_MUTTON, 16), ModerrCaseItemRarity.POSPOLITE, 20));
                                add(new ModerrCaseItem(new ItemStack(Material.IRON_INGOT, 32), ModerrCaseItemRarity.POSPOLITE, 70));
                                add(new ModerrCaseItem(new ItemStack(Material.SUGAR_CANE, 48), ModerrCaseItemRarity.POSPOLITE, 70));
                                add(new ModerrCaseItem(new ItemStack(Material.EMERALD, 20), ModerrCaseItemRarity.POSPOLITE, 40));
                                add(new ModerrCaseItem(new ItemStack(Material.ENDER_PEARL, 8), ModerrCaseItemRarity.RZADKIE, 25));
                                add(new ModerrCaseItem(new ItemStack(Material.DIAMOND, 2), ModerrCaseItemRarity.RZADKIE, 25));
                                add(new ModerrCaseItem(new ItemStack(Material.ENCHANTING_TABLE, 1), ModerrCaseItemRarity.RZADKIE, 25));
                                add(new ModerrCaseItem(new ItemStack(Material.BLAZE_ROD, 2), ModerrCaseItemRarity.RZADKIE, 25));
                                add(new ModerrCaseItem(new ItemStack(Material.HOPPER, 6), ModerrCaseItemRarity.RZADKIE, 15));
                                add(new ModerrCaseRandomEnchantment(ModerrCaseItemRarity.RZADKIE, 25));
                                add(new ModerrCaseItem(new ItemStack(Material.SLIME_BALL, 16), ModerrCaseItemRarity.RZADKIE, 20));
                                add(new ModerrCaseRandomDisc(ModerrCaseItemRarity.RZADKIE, 15));
                                add(new ModerrCaseRandomTool(ModerrCaseItemRarity.LEGENDARNE, 10));
                                add(new ModerrCaseItem(new ItemStack(Material.OBSERVER, 8), ModerrCaseItemRarity.LEGENDARNE, 10));
                                add(new ModerrCaseItem(new ItemStack(Material.GOLDEN_APPLE, 2), ModerrCaseItemRarity.LEGENDARNE, 10));
                                add(new ModerrCaseItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 1), ModerrCaseItemRarity.MITYCZNE, 5));
                                add(new ModerrCaseItem(new ItemStack(Material.TOTEM_OF_UNDYING, 1), ModerrCaseItemRarity.MITYCZNE, 5));
                            }
                        };
                    }
                });
        ItemStack is = new ItemStack(Material.DIAMOND_PICKAXE);
        is.addUnsafeEnchantment(Enchantment.DIG_SPEED, 6);
        addCase(ModerrCaseEnum.SZLACHECKA, new ModerrCase() {
            @Override
            public String name() {
                return ColorUtil.color("&e&lSzlachecka");
            }

            @Override
            public String guiName() {
                return ColorUtil.color("&7Skrzynia " + name());
            }

            @Override
            public String description() {
                return null;
            }

            @Override
            public WeightedList<ModerrCaseItem> randomList() {
                WeightedList<ModerrCaseItem> items = new WeightedList<>();
                itemList().forEach(item -> items.put(item, item.weight));
                return items;
            }


            @Contract(" -> new")
            @Override
            public @NotNull List<ModerrCaseItem> itemList() {
                return Arrays.asList(
                        new ModerrCaseRandomToolPerfect(ModerrCaseItemRarity.LEGENDARNE, 8),
                        new ModerrCaseRandomToolPerfect(ModerrCaseItemRarity.LEGENDARNE, 8),
                        new ModerrCaseRandomToolPerfect(ModerrCaseItemRarity.LEGENDARNE, 8),
                        new ModerrCaseRandomEnchantmentPerfect(ModerrCaseItemRarity.LEGENDARNE, 8),
                        new ModerrCaseRandomEnchantmentPerfect(ModerrCaseItemRarity.LEGENDARNE, 8),
                        new ModerrCaseRandomEnchantmentPerfect(ModerrCaseItemRarity.LEGENDARNE, 8),
                        new ModerrCaseItem(new ItemStack(Material.NETHERITE_INGOT, 2), ModerrCaseItemRarity.LEGENDARNE, 12),
                        new ModerrCaseItem(new ItemStack(Material.DIAMOND, 16), ModerrCaseItemRarity.RZADKIE, 40),
                        new ModerrCaseItem(new ItemStack(Material.GOLD_BLOCK, 32), ModerrCaseItemRarity.RZADKIE, 80),
                        new ModerrCaseItem(new ItemStack(Material.GOLDEN_CARROT, 32), ModerrCaseItemRarity.RZADKIE, 60),
                        new ModerrCaseItem(new ItemStack(Material.ENDER_PEARL, 16), ModerrCaseItemRarity.RZADKIE, 50),
                        new ModerrCaseItem(new ItemStack(Material.GOLDEN_APPLE, 16), ModerrCaseItemRarity.RZADKIE, 50),
                        new ModerrCaseItem(new ItemStack(Material.IRON_BLOCK, 48), ModerrCaseItemRarity.RZADKIE, 50),
                        new ModerrCaseItem(new ItemStack(Material.ZOMBIFIED_PIGLIN_SPAWN_EGG, 3), ModerrCaseItemRarity.MITYCZNE, 12),
                        new ModerrCaseItem(new ItemStack(Material.DOLPHIN_SPAWN_EGG, 8), ModerrCaseItemRarity.MITYCZNE, 8),
                        new ModerrCaseItem(new ItemStack(Material.IRON_GOLEM_SPAWN_EGG, 1), ModerrCaseItemRarity.MITYCZNE, 2),
                        new ModerrCaseItem(new ItemStack(Material.SPAWNER), ModerrCaseItemRarity.MITYCZNE, 2),
                        new ModerrCaseItem(is, ModerrCaseItemRarity.MITYCZNE, 1)
                );
            }
        });
    }

    public static ArrayList<ModerrCaseEnum> getChestTypes() {
        return new ArrayList<>(cases.keySet());
    }

    public static ArrayList<ModerrCase> getChests() {
        return new ArrayList<>(cases.values());
    }

    public static ArrayList<String> getGuiNames() {
        ArrayList<String> list = new ArrayList<>();
        for (ModerrCase chest : getChests()) {
            list.add(chest.guiName());
        }
        return list;
    }

    public static ModerrCase getCase(ModerrCaseEnum type) {
        return cases.get(type);
    }

    public static void addCase(ModerrCaseEnum type, ModerrCase moderrCase) {
        if (!cases.containsKey(type)) {
            cases.put(type, moderrCase);
            Logger.logCaseMessage("Zarejestrowano nową skrzynkę " + type.toString());
        } else {
            Logger.logCaseMessage("Skrzynka " + type.toString() + " już jest zarejestrowana!");
        }
    }

    public static ArrayList<String> getChestStringTypes() {
        ArrayList<String> list = new ArrayList<>();
        for (ModerrCaseEnum chest : getChestTypes()) {
            list.add(chest.toString());
        }
        return list;
    }
}
