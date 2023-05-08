package pl.moderr.moderrkowo.core.opening;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.core.economy.WithdrawCommand;
import pl.moderr.moderrkowo.core.opening.data.*;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;
import pl.moderr.moderrkowo.core.utils.WeightedList;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;

public class ModerrCaseConstants {

    public static final Map<ModerrCaseEnum, ModerrCase> cases = new IdentityHashMap<>();

    static {
        addCase(ModerrCaseEnum.ZWYKLA,
                new ModerrCase() {
                    @Override
                    public String name() {
                        return ColorUtils.color("&a&lZWYKŁA");
                    }

                    @Override
                    public String guiName() {
                        return ColorUtils.color("&7Skrzynia " + name());
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
                        return new ArrayList<ModerrCaseItem>(){
                            {
                                add(new ModerrCaseItem(new ItemStack(Material.ACACIA_SAPLING,8), ModerrCaseItemRarity.POSPOLITE,50));
                                add(new ModerrCaseItem(new ItemStack(Material.MANGROVE_LOG,32), ModerrCaseItemRarity.POSPOLITE,70));
                                add(new ModerrCaseItem(new ItemStack(Material.COOKED_BEEF,16), ModerrCaseItemRarity.POSPOLITE,20));
                                add(new ModerrCaseItem(new ItemStack(Material.COOKED_PORKCHOP,16), ModerrCaseItemRarity.POSPOLITE,20));
                                add(new ModerrCaseItem(new ItemStack(Material.COOKED_MUTTON,16), ModerrCaseItemRarity.POSPOLITE,20));
                                add(new ModerrCaseItem(new ItemStack(Material.IRON_INGOT,32), ModerrCaseItemRarity.POSPOLITE,70));
                                add(new ModerrCaseItem(new ItemStack(Material.SUGAR_CANE,48), ModerrCaseItemRarity.POSPOLITE,70));
                                add(new ModerrCaseItem(new ItemStack(Material.EMERALD,20), ModerrCaseItemRarity.POSPOLITE,40));
                                add(new ModerrCaseItem(new ItemStack(Material.ENDER_PEARL,8), ModerrCaseItemRarity.RZADKIE,25));
                                add(new ModerrCaseItem(new ItemStack(Material.DIAMOND,2), ModerrCaseItemRarity.RZADKIE,25));
                                add(new ModerrCaseItem(new ItemStack(Material.ENCHANTING_TABLE,1), ModerrCaseItemRarity.RZADKIE,25));
                                add(new ModerrCaseItem(new ItemStack(Material.BLAZE_ROD,2), ModerrCaseItemRarity.RZADKIE,25));
                                add(new ModerrCaseItem(new ItemStack(Material.HOPPER,6), ModerrCaseItemRarity.RZADKIE,15));
                                add(new ModerrCaseRandomEnchantment(ModerrCaseItemRarity.RZADKIE,25));
                                add(new ModerrCaseItem(new ItemStack(Material.SLIME_BALL,16), ModerrCaseItemRarity.RZADKIE,20));
                                add(new ModerrCaseRandomDisc(ModerrCaseItemRarity.RZADKIE,15));
                                add(new ModerrCaseRandomTool(ModerrCaseItemRarity.LEGENDARNE,10));
                                add(new ModerrCaseItem(new ItemStack(Material.OBSERVER,8), ModerrCaseItemRarity.LEGENDARNE,10));
                                add(new ModerrCaseItem(new ItemStack(Material.GOLDEN_APPLE,2), ModerrCaseItemRarity.LEGENDARNE,10));
                                add(new ModerrCaseItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE,1), ModerrCaseItemRarity.MITYCZNE,5));
                                add(new ModerrCaseItem(new ItemStack(Material.TOTEM_OF_UNDYING,1), ModerrCaseItemRarity.MITYCZNE,5));
                            }
                        };
                    }
                });
        addCase(ModerrCaseEnum.SKAZENIA, new ModerrCase() {
            @Override
            public String name() {
                return ColorUtils.color("&c&lSkażenia");
            }

            @Override
            public String guiName() {
                return ColorUtils.color("&7Skrzynia " + name());
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

            @Override
            public ArrayList<ModerrCaseItem> itemList() {
                return new ArrayList<ModerrCaseItem>(){
                    {
                        add(new ModerrCaseItem(new ItemStack(Material.BLAZE_ROD,16), ModerrCaseItemRarity.RZADKIE,160));
                        add(new ModerrCaseItem(new ItemStack(Material.GLOWSTONE_DUST,24), ModerrCaseItemRarity.RZADKIE,80));
                        add(new ModerrCaseItem(WithdrawCommand.generateItemStatic(1,20000), ModerrCaseItemRarity.POSPOLITE,120));
                        add(new ModerrCaseItem(new ItemStack(Material.GOLDEN_APPLE,8), ModerrCaseItemRarity.RZADKIE,120));
                        add(new ModerrCaseRandomToolPerfect(ModerrCaseItemRarity.LEGENDARNE,120));
                        add(new ModerrCaseItem(new ItemStack(Material.NETHER_QUARTZ_ORE,24), ModerrCaseItemRarity.LEGENDARNE,100));
                        add(new ModerrCaseRandomEnchantmentPerfect(ModerrCaseItemRarity.RZADKIE,100));
                        add(new ModerrCaseItem(new ItemStack(Material.FERMENTED_SPIDER_EYE,64), ModerrCaseItemRarity.LEGENDARNE,80));
                        add(new ModerrCaseItem(new ItemStack(Material.NETHER_BRICK,48), ModerrCaseItemRarity.LEGENDARNE,80));
                        add(new ModerrCaseItem(new ItemStack(Material.SLIME_BALL,64), ModerrCaseItemRarity.RZADKIE,50));
                        add(new ModerrCaseRandomPotion(ModerrCaseItemRarity.LEGENDARNE,40));
                        add(new ModerrCaseItem(WithdrawCommand.generateItemStatic(1,12500), ModerrCaseItemRarity.POSPOLITE,30));
                        add(new ModerrCaseItem(WithdrawCommand.generateItemStatic(1,9000), ModerrCaseItemRarity.POSPOLITE,30));
                        add(new ModerrCaseItem(WithdrawCommand.generateItemStatic(1,3000), ModerrCaseItemRarity.POSPOLITE,30));
                        add(new ModerrCaseItem(new ItemStack(Material.SOUL_SAND,1), ModerrCaseItemRarity.MITYCZNE,20));
                        add(new ModerrCaseItem(new ItemStack(Material.NETHER_WART,1), ModerrCaseItemRarity.MITYCZNE,14));
                        add(new ModerrCaseItem(new ItemStack(Material.WITHER_SKELETON_SKULL,1), ModerrCaseItemRarity.MITYCZNE,10));
                        add(new ModerrCaseItem(new ItemStack(Material.NETHERITE_SCRAP,1), ModerrCaseItemRarity.MITYCZNE,2));
                        add(new ModerrCaseItem(new ItemStack(Material.WEEPING_VINES,1), ModerrCaseItemRarity.MITYCZNE,2));
                        add(new ModerrCaseItem(new ItemStack(Material.TWISTING_VINES,1), ModerrCaseItemRarity.MITYCZNE,1));

                        add(new ModerrCaseItem(new ItemStack(Material.NETHERITE_HELMET,1), ModerrCaseItemRarity.MITYCZNE,3));
                        add(new ModerrCaseItem(new ItemStack(Material.NETHERITE_CHESTPLATE,1), ModerrCaseItemRarity.MITYCZNE,3));
                        add(new ModerrCaseItem(new ItemStack(Material.NETHERITE_LEGGINGS,1), ModerrCaseItemRarity.MITYCZNE,3));
                        add(new ModerrCaseItem(new ItemStack(Material.NETHERITE_BOOTS,1), ModerrCaseItemRarity.MITYCZNE,3));
                    }
                };
            }
        });
    }

    public static ArrayList<ModerrCaseEnum> getChestTypes(){
        return new ArrayList<>(cases.keySet());
    }
    public static ArrayList<ModerrCase> getChests(){
        return new ArrayList<>(cases.values());
    }
    public static ArrayList<String> getGuiNames(){
        ArrayList<String> list = new ArrayList<>();
        for(ModerrCase chest : getChests()){
            list.add(chest.guiName());
        }
        return list;
    }
    public static ModerrCase getCase(ModerrCaseEnum type){
        return cases.get(type);
    }
    public static void addCase(ModerrCaseEnum type, ModerrCase moderrCase){
        if (!cases.containsKey(type)) {
            cases.put(type, moderrCase);
            Logger.logCaseMessage("Zarejestrowano nową skrzynkę " + type.toString());
        } else {
            Logger.logCaseMessage("Skrzynka " + type.toString() + " już jest zarejestrowana!");
        }
    }

    public static ArrayList<String> getChestStringTypes() {
        ArrayList<String> list = new ArrayList<>();
        for(ModerrCaseEnum chest : getChestTypes()){
            list.add(chest.toString());
        }
        return list;
    }
}
