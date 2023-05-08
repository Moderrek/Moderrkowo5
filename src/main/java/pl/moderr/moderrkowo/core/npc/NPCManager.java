package pl.moderr.moderrkowo.core.npc;

import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.cuboids.CuboidsManager;
import pl.moderr.moderrkowo.core.customitems.CustomItemsManager;
import pl.moderr.moderrkowo.core.listeners.BreedingAnimal;
import pl.moderr.moderrkowo.core.mysql.LevelCategory;
import pl.moderr.moderrkowo.core.mysql.User;
import pl.moderr.moderrkowo.core.mysql.UserManager;
import pl.moderr.moderrkowo.core.npc.data.data.PlayerNPCData;
import pl.moderr.moderrkowo.core.npc.data.data.PlayerNPCSData;
import pl.moderr.moderrkowo.core.npc.data.npc.NPCData;
import pl.moderr.moderrkowo.core.npc.data.npc.NPCDelayData;
import pl.moderr.moderrkowo.core.npc.data.npc.NPCShopItem;
import pl.moderr.moderrkowo.core.npc.data.npc.NPCShopItemSeasonOneCoins;
import pl.moderr.moderrkowo.core.npc.data.quest.Quest;
import pl.moderr.moderrkowo.core.npc.data.quest.QuestDifficulty;
import pl.moderr.moderrkowo.core.npc.data.rewards.*;
import pl.moderr.moderrkowo.core.npc.data.tasks.*;
import pl.moderr.moderrkowo.core.ranks.Rank;
import pl.moderr.moderrkowo.core.utils.*;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class NPCManager implements Listener {

    public static final IdentityHashMap<String, NPCDelayData> questDelay = new IdentityHashMap<>();
    public final Map<String, NPCData> npcs = new HashMap<>();
    public final String NpcIdKey = "npcquestkey";
    private final Map<UUID, Instant> commandDelay = new IdentityHashMap<>();

    public NPCManager() {
        AddVillager(new NPCData("Rzeznik", new ArrayList<>() {
            {
                add(new Quest("Wołowina", "Cześć.\nW moim sklepie zabrakło właśnie krowiego,\nczerwonego mięsa, a mój dostawca gdzieś zaginął\nz towarem. Potrzebuję pomocy bo klienci się\nniecierpliwią.",
                        QuestDifficulty.EASY, new ArrayList<>() {
                    {
                        add(new IQuestItemKill() {
                            @Override
                            public EntityType getEntityType() {
                                return EntityType.COW;
                            }

                            @Override
                            public int getCount() {
                                return 24;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Krowa";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.BEEF;
                            }

                            @Override
                            public int getCount() {
                                return 16;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Surowa wołowina";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 500);
                        add((IQuestRewardItemStack) () -> new ItemStack(Material.COOKED_BEEF, 16));
                    }
                }));
                add(new Quest("Skaczący temat", "Witaj.\nOd dłuższego czasu nie potrafię sobie poradzić\nz króliczym mięscem, klienci się go domagają, \na ja zwyczajnie go nie mam. Zechcesz mi z tym\npomóc? Nie mogę tak olewać mojej klienteli.",
                        QuestDifficulty.NORMAL, new ArrayList<>() {
                    {
                        add(new IQuestItemKill() {
                            @Override
                            public EntityType getEntityType() {
                                return EntityType.RABBIT;
                            }

                            @Override
                            public int getCount() {
                                return 32;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Królik";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.RABBIT;
                            }

                            @Override
                            public int getCount() {
                                return 32;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Surowe mięso z królika";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 750);
                    }
                }));
                add(new Quest("Głupi jak baran",
                        "Na wesele, Rybak wraz z przyszłą\n" +
                                "żoną zażyczyli sobie baraninę na\n" +
                                "uroczysty obiad. Problem w tym,\n" +
                                "że mój zapas jest zbyt mały aby\n" +
                                "obsłużyć tak huczną imprezę.\n" +
                                "Chcesz mi pomóc zdobyć potrzebne\n" +
                                "mięsiwo?",
                        QuestDifficulty.NORMAL, new ArrayList<>() {
                    {
                        add(new IQuestItemKill() {
                            @Override
                            public EntityType getEntityType() {
                                return EntityType.SHEEP;
                            }

                            @Override
                            public int getCount() {
                                return 128;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Owca";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.MUTTON;
                            }

                            @Override
                            public int getCount() {
                                return 128;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Surowa baranina";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 1050);
                    }
                }));
                //<editor-fold> Świńska robota
                add(new Quest(
                                "Świńska robota",
                                "Moja farma świń za szybko się\n" +
                                        "kurczy, zrobisz z tym coś? Bo ja\n" +
                                        "nie mam czasu aktualnie.",
                                QuestDifficulty.HARD,
                                new ArrayList<>() {
                                    {
                                        add(TaskBreed(BreedingAnimal.PIG, 64, "1", "Świnia"));
                                    }
                                },
                                new ArrayList<>() {
                                    {
                                        add(RewardMoney(getRewardMoneyDifficulty(QuestDifficulty.HARD)));
                                    }
                                }
                        )
                );
                //</editor-fold>
            }
        }));
        AddVillager(new NPCData("Kolorysta", new ArrayList<>() {
            {
                add(new Quest("Czerwony kapturek",
                        "Ostatnio przyszedł do mnie Czerwony kapturek i poprosił mnie o trochę\nczerwonego barwnika, rzekomo chce sobie pomalować pokój na swój ulubiony\nkolor, pomożesz mi go załatwić?",
                        QuestDifficulty.EASY,
                        new ArrayList<>() {
                            {
                                add(new IQuestItemBreak() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.POPPY;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 64;
                                    }

                                    @Override
                                    public boolean blockSilk() {
                                        return false;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "1";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Mak";
                                    }
                                });
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.RED_DYE;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 64;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "2";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Czerwony barwnik";
                                    }
                                });
                            }
                        }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 750);
                    }
                }));
                add(new Quest("Suknia ślubna",
                        "Potrzebuję trochę barwnika, aby wykonać nową wersję kolorystyczną na ślub\nRybaka z jego wybranką, zażyczyła sobie mocno kontrowersyjny kolor. No ale nasz\nklient - nasz Pan.",
                        QuestDifficulty.EASY,
                        new ArrayList<>() {
                            {
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.PINK_DYE;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 32;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "1";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Różowy barwnik";
                                    }
                                });
                            }
                        }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 500);
                    }
                }));
                add(new Quest("Nowa posiadłość",
                        "Hej!\nNie wiem czy słyszałeś, ale planujemy budowę nowego pomieszczenia\ngospodarczego, w którym będę urzędował. Ale potrzebujemy sporej ilości\nmateriału, aby wszystko wyglądało tak jak lubię, czyli KOLOROWO! Pomożesz?",
                        QuestDifficulty.HARD,
                        new ArrayList<>() {
                            {
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.ORANGE_DYE;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 16;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "1";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Pomarańczowy barwnik";
                                    }
                                }); // orange dye x16
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.BLACK_DYE;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 16;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "2";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Czarny barwnik";
                                    }
                                }); // black dye x16
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.YELLOW_DYE;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 16;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "3";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Żółty barwnik";
                                    }
                                }); // yellow dye x16
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.GREEN_DYE;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 16;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "4";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Zielony barwnik";
                                    }
                                }); // blue dye x16
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.BLUE_DYE;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 16;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "5";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Niebieski barwnik";
                                    }
                                });
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.LIGHT_GRAY_DYE;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 16;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "6";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Jasnoszary barwnik";
                                    }
                                });
                            }
                        }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 5000);
                    }
                }));
            }
        }));
        AddVillager(new NPCData("Rolnik", new ArrayList<>() {
            {
                add(new Quest("Towary Farmera", "Witaj na moim targu!\nCeny nie są zbyt duże.", QuestDifficulty.EASY, new ArrayList<>() {
                    {
                        add(new IQuestItemPay() {
                            @Override
                            public int getCount() {
                                return 50;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardItemStack) () -> new ItemStack(Material.BEETROOT, 64));
                        add((IQuestRewardItemStack) () -> new ItemStack(Material.BEETROOT_SEEDS, 2));
                        add((IQuestRewardItemStack) () -> new ItemStack(Material.WHEAT, 48));
                    }
                }));
                add(new Quest("Buraki cukrowe", "Mam dobre kontrakty,\nale potrzebuje pomocnika.\nPodzielimy się?", QuestDifficulty.NORMAL, new ArrayList<>() {
                    {
                        add(new IQuestItemCollect() {
                            @Override
                            public Material getMaterial() {
                                return Material.BEETROOT_SEEDS;
                            }

                            @Override
                            public int getCount() {
                                return 32;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Buraki";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 500);
                        add(new IQuestRewardExp() {
                            @Override
                            public double exp() {
                                return 1000;
                            }

                            @Override
                            public LevelCategory category() {
                                return LevelCategory.Uprawa;
                            }
                        });
                    }
                }));
                add(new Quest("Zbiór Marchwi", "Zlecam Ci to zadanie.", QuestDifficulty.EASY, new ArrayList<>() {
                    {
                        add(new IQuestItemCollect() {
                            @Override
                            public Material getMaterial() {
                                return Material.CARROT;
                            }

                            @Override
                            public int getCount() {
                                return 64;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Marchewki";
                            }
                        });
                        add(new IQuestItemCraft() {
                            @Override
                            public Material getMaterial() {
                                return Material.COMPOSTER;
                            }

                            @Override
                            public int getCount() {
                                return 8;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Kompostownik";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 1250);
                    }
                }));
                add(new Quest("Uuu, Afera", "Rzeźnik wybił wszystkie moje kury, które wolno sobie\nbiegały wokół mojego domu. Przynieś mi trochę\nkurzych jaj, może coś się z nich wykluje.", QuestDifficulty.NORMAL, new ArrayList<>() {
                    {
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.EGG;
                            }

                            @Override
                            public int getCount() {
                                return 128;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Jajo";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> getRewardMoneyDifficulty(QuestDifficulty.HARD));
                    }
                }));
                //<editor-fold> Soczki
                add(new Quest(
                                "Soczki",
                                "W ostatnich dniach, moja córka zaczęła wyciskać sok ze świeżej marchwi, strasznie nam to wszystkim zasmakowało, a moje pole opustoszało w mig. Chcesz mi pomóc w zbiorach?",
                                QuestDifficulty.EASY,
                                new ArrayList<>() {
                                    {
                                        add(TaskFarm(Material.CARROT, 32, "1", "Marchewki"));
                                        add(TaskGive(Material.CARROT, 96, "2", "Marchewki"));
                                    }
                                },
                                new ArrayList<>() {
                                    {
                                        add(RewardMoney(getRewardMoneyDifficulty(QuestDifficulty.EASY)));
                                    }
                                }
                        )
                );
                //</editor-fold>
            }
        }, new ArrayList<>() {
            {
                add(new NPCShopItem(new ItemStack(Material.WHEAT_SEEDS, 1), 0, 0, "", 2.50, 0.25, false));
                add(new NPCShopItem(new ItemStack(Material.WHEAT, 1), 0, 0, "", 5, 1, false));
                add(new NPCShopItem(new ItemStack(Material.BEETROOT_SEEDS, 1), 0, 0, "", 3, 0.40, false));
                add(new NPCShopItem(new ItemStack(Material.BEETROOT, 1), 0, 0, "", 4.2, 0.6, false));
                add(new NPCShopItem(new ItemStack(Material.CARROT, 1), 0, 0, "", 5, 0.7, false));
                add(new NPCShopItem(new ItemStack(Material.POTATO, 1), 0, 0, "", 5, 1, true));
                add(new NPCShopItem(new ItemStack(Material.HONEY_BOTTLE), 0, 0, "", 0, 12, false));
                add(new NPCShopItem(new ItemStack(Material.HONEYCOMB), 0, 0, "", 0, 4, false));
                add(new NPCShopItem(new ItemStack(Material.SUGAR_CANE, 1), 0, 0, "", 15, 1, false));
                add(new NPCShopItem(new ItemStack(Material.CACTUS, 1), 0, 0, "", 9.5, 1.1, true));
                add(new NPCShopItem(new ItemStack(Material.MELON_SLICE, 1), 0, 0, "", 7.3, 0.9, false));
                add(new NPCShopItem(new ItemStack(Material.PUMPKIN, 1), 0, 0, "", 30, 3.5, false));
                add(new NPCShopItem(new ItemStack(Material.NETHER_WART, 1), 0, 0, "", 0, 2, false));
                add(new NPCShopItem(new ItemStack(Material.EGG, 1), 0, 0, "", 0, 1.5, false));
            }
        }));
        AddVillager(new NPCData("Gornik", new ArrayList<>() {
            {
                //<editor-fold> Kamień
                add(new Quest("Kamień", "Na pewno nie masz porządnego sprzętu\nwięc zbierz materiały i podzielmy się", QuestDifficulty.EASY, new ArrayList<>() {
                    {
                        add(TaskGive(Material.COBBLESTONE, 15, "1", "Bruk"));
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardItemStack) () -> new ItemStack(Material.IRON_PICKAXE));
                        add(new IQuestRewardExp() {
                            @Override
                            public double exp() {
                                return 100;
                            }

                            @Override
                            public LevelCategory category() {
                                return LevelCategory.Kopanie;
                            }
                        });
                    }
                }));
                //</editor-fold>
                //<editor-fold> Kowadło
                add(new Quest(
                                "Kowadło",
                                "Hej!\n" +
                                        "Ostatnimi czasy zużyłem prawie\n" +
                                        "wszystkie swoje narzędzia w\n" +
                                        "pracy, dałbyś radę pomóc mi ze\n" +
                                        "zdobyciem materiałów na jego\n" +
                                        "naprawę! Oczywiście nic za darmo.",
                                QuestDifficulty.HARD,
                                new ArrayList<>() {
                                    {
                                        add(TaskCraft(Material.ANVIL, 1, "1", "Kowadło"));
                                        add(TaskGive(Material.ANVIL, 1, "2", "Kowadło"));

                                        add(TaskBreak(Material.IRON_ORE, 32, false, "3", "Ruda żelaza"));
                                        add(TaskBreak(Material.DIAMOND_ORE, 6, true, "4", "Ruda diamentów"));

                                        add(TaskGive(Material.IRON_INGOT, 24, "5", "Sztabka żelaza"));
                                        add(TaskGive(Material.DIAMOND, 3, "6", "Diament"));
                                    }
                                },
                                new ArrayList<>() {
                                    {
                                        add(RewardMoney(getRewardMoneyDifficulty(QuestDifficulty.HARD)));
                                    }
                                }
                        )
                );
                //</editor-fold>
                //<editor-fold> Niebieska euforia
                add(new Quest(
                                "Niebieska euforia",
                                "Ostatnio pomogłeś mi zebrać\n" +
                                        "materiały na naprawę mojego\n" +
                                        "sprzętu, przydałby mi się teraz\n" +
                                        "zestaw do ulepszenia tych\n" +
                                        "przedmiotów. Potrzebuję wsparcia,\n" +
                                        "bo od momentu wypadku mam\n" +
                                        "lęk i nie wchodzę na niskie poziomy\n" +
                                        "kopalni.",
                                QuestDifficulty.HARD,
                                new ArrayList<>() {
                                    {
                                        add(TaskBreak(Material.LAPIS_ORE, 96, false, "1", "Ruda lapisu"));

                                        add(TaskGive(Material.LAPIS_LAZULI, 64, "2", "Lapis lazuli"));
                                    }
                                },
                                new ArrayList<>() {
                                    {
                                        add(RewardMoney(getRewardMoneyDifficulty(QuestDifficulty.HARD)));
                                    }
                                }
                        )
                );
                //</editor-fold>
                //<editor-fold> Renowacja kopalni
                add(new Quest(
                                "Renowacja kopalni",
                                "Moja kopalnia ostatnimi czasy\n" +
                                        "jest rzadko uczęszczana,\n" +
                                        "to wszystko przez mój wypadek,\n" +
                                        "nie mam już tyle sił co kiedyś.\n" +
                                        "Potrzebuję materiałów budowlanych\n" +
                                        "na jej odbudowę, zapłacę za pomoc.",
                                QuestDifficulty.NORMAL,
                                new ArrayList<>() {
                                    {
                                        add(TaskBreak(Material.STONE, 64, false, "1", "Kamień"));
                                        add(TaskBreak(Material.ANDESITE, 64, false, "2", "Andezyt"));
                                        add(TaskBreak(Material.GRANITE, 64, false, "3", "Granit"));
                                        add(TaskBreak(Material.DIORITE, 64, false, "4", "Dioryt"));

                                        add(TaskGive(Material.STONE, 48, "5", "Kamień"));
                                        add(TaskGive(Material.ANDESITE, 48, "6", "Andezyt"));
                                        add(TaskGive(Material.GRANITE, 48, "7", "Granit"));
                                        add(TaskGive(Material.DIORITE, 48, "8", "Dioryt"));
                                    }
                                },
                                new ArrayList<>() {
                                    {
                                        add(RewardMoney(getRewardMoneyDifficulty(QuestDifficulty.NORMAL)));
                                    }
                                }
                        )
                );
                //</editor-fold>
            }
        }, new ArrayList<>() {
            {
                add(new NPCShopItem(new ItemStack(Material.DIRT, 2), 0, 0, "", 5, 0.1, false));
                add(new NPCShopItem(new ItemStack(Material.COBBLESTONE, 1), 0, 0, "", 3, 0.1, false));
                add(new NPCShopItem(new ItemStack(Material.STONE, 1), 0, 0, "", 10, 0.4, false));
                add(new NPCShopItem(new ItemStack(Material.STONE_BRICKS, 1), 0, 0, "", 45, 2, false));
                add(new NPCShopItem(new ItemStack(Material.GRANITE, 1), 0, 0, "", 10, 1, false));
                add(new NPCShopItem(new ItemStack(Material.ANDESITE, 1), 0, 0, "", 10, 1, false));
                add(new NPCShopItem(new ItemStack(Material.DIORITE, 1), 0, 0, "", 10, 1, false));
                add(new NPCShopItem(new ItemStack(Material.GRAVEL, 1), 0, 0, "", 12, 0.8, false));
                add(new NPCShopItem(new ItemStack(Material.FLINT, 1), 0, 0, "", 62, 3.5, false));
                add(new NPCShopItem(new ItemStack(Material.COAL, 1), 0, 0, "", 18, 3.5, false));
                add(new NPCShopItem(new ItemStack(Material.CHARCOAL, 1), 0, 0, "", 18, 3, false));
                add(new NPCShopItem(new ItemStack(Material.IRON_INGOT, 1), 0, 0, "", 25, 7.5, false));
                add(new NPCShopItem(new ItemStack(Material.COPPER_INGOT), 0, 0, "", 0, 6, false));
                add(new NPCShopItem(new ItemStack(Material.GOLD_INGOT, 1), 0, 0, "", 91.5, 10.2, false));
                add(new NPCShopItem(new ItemStack(Material.REDSTONE, 1), 0, 0, "", 15, 0.6, false));
                add(new NPCShopItem(new ItemStack(Material.LAPIS_LAZULI, 1), 0, 0, "", 32.5, 2.25, false));
                add(new NPCShopItem(new ItemStack(Material.AMETHYST_SHARD), 0, 0, "", 0, 9, false));
                add(new NPCShopItem(new ItemStack(Material.DIAMOND, 1), 0, 0, "", 0, 50, false));
                add(new NPCShopItem(new ItemStack(Material.QUARTZ, 1), 0, 0, "", 30, 2, false));
                add(new NPCShopItem(new ItemStack(Material.OBSIDIAN, 1), 0, 0, "", 300, 42, false));
                add(new NPCShopItem(new ItemStack(Material.LAVA_BUCKET, 1), 0, 0, "", 375, 42, false));
            }
        }));
        AddVillager(new NPCData("Drwal", new ArrayList<>() {
            {
                add(new Quest("W Lesie", "ARHH! ARG! O\nSkąd się tu znalazłeś?\nMasz zapał do pracy?", QuestDifficulty.EASY, new ArrayList<>() {
                    {
                        add(new IQuestItemPay() {
                            @Override
                            public int getCount() {
                                return 150;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardItemStack) () -> new ItemStack(Material.GOLDEN_AXE));
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Przyjęcie do pracy";
                            }

                            @Override
                            public void Action(Player p, User u) {

                            }
                        });
                    }
                }));
                add(new Quest("Spokojne drzewo", "Ufff..\nDobra! Zrób to i to i przyjdź", QuestDifficulty.NORMAL, new ArrayList<>() {
                    {
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.OAK_LOG;
                            }

                            @Override
                            public int getCount() {
                                return 24;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Dębowy pień";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.OAK_PLANKS;
                            }

                            @Override
                            public int getCount() {
                                return 64;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Dębowe deski";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardItemStack) () -> new ItemStack(Material.IRON_AXE));
                        add((IQuestRewardMoney) () -> 1500);
                    }
                }));
                add(new Quest("Domek letniskowy", "W ramach prezentu ślubnego dla Rybaka, pomyślałem\n" +
                        "że wybuduję domek letniskowy dla pary młodej, gdzieś\n" +
                        "w cichym miejscu. Ale potrzebuję na to sporo drewna,\n" +
                        "a ledwo wyrabiam z obecnymi zamówieniami, zechcesz mi pomóc?", QuestDifficulty.HARD,
                        new ArrayList<>() {
                            {
                                add(new IQuestItemBreak() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.OAK_LOG;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 64;
                                    }

                                    @Override
                                    public boolean blockSilk() {
                                        return false;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "1";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Dębowy pień";
                                    }
                                });
                                add(new IQuestItemBreak() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.DARK_OAK_LOG;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 64;
                                    }

                                    @Override
                                    public boolean blockSilk() {
                                        return false;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "2";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Ciemny dębowy pień";
                                    }
                                });
                                add(new IQuestItemBreak() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.BIRCH_LOG;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 64;
                                    }

                                    @Override
                                    public boolean blockSilk() {
                                        return false;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "3";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Brzozowy pień";
                                    }
                                });
                                add(new IQuestItemBreak() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.SPRUCE_LOG;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 64;
                                    }

                                    @Override
                                    public boolean blockSilk() {
                                        return false;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "4";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Świerkowy pień";
                                    }
                                });
                                add(new IQuestItemBreak() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.JUNGLE_LOG;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 64;
                                    }

                                    @Override
                                    public boolean blockSilk() {
                                        return false;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "5";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Dżunglowy pień";
                                    }
                                });
                                add(new IQuestItemBreak() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.ACACIA_LOG;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 64;
                                    }

                                    @Override
                                    public boolean blockSilk() {
                                        return false;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "6";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Akacjowy pień";
                                    }
                                });
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.OAK_LOG;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 64;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "7";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Dębowy pień";
                                    }
                                });
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.DARK_OAK_LOG;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 64;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "8";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Ciemny dębowy pień";
                                    }
                                });
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.BIRCH_LOG;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 64;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "9";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Brzozowy pień";
                                    }
                                });
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.SPRUCE_LOG;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 64;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "10";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Świerkowy pień";
                                    }
                                });
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.JUNGLE_LOG;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 64;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "11";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Dżunglowy pień";
                                    }
                                });
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.ACACIA_LOG;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 64;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "12";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Akacjowy pień";
                                    }
                                });
                            }
                        }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 5500);
                    }
                }));
                add(new Quest("Sprzęt", "Potrzebuję nowej siekiery, moja ostatnia złamała się\nna wycince lasu dżunglowego. Przynieś mi nową, a dam\nCi coś na pamiątkę.", QuestDifficulty.NORMAL, new ArrayList<>() {
                    {
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.DIAMOND_AXE;
                            }

                            @Override
                            public int getCount() {
                                return 1;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Diamentowa Siekiera";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add(new IQuestRewardMoney() {
                            @Override
                            public int money() {
                                return 1000;
                            }
                        });
                        add(new IQuestRewardItemStack() {
                            @Override
                            public ItemStack item() {
                                Material type = Material.IRON_AXE;
                                ItemStack item = new ItemStack(type);
                                item.addUnsafeEnchantment(Enchantment.DIG_SPEED, 5);
                                item.addUnsafeEnchantment(Enchantment.DURABILITY, 3);
                                return item;
                            }
                        });
                    }
                }));
            }
        }, new ArrayList<>() {
            {
                add(new NPCShopItem(new ItemStack(Material.OAK_LOG, 1), 0, 0, "", 0, 1.1, false));
                add(new NPCShopItem(new ItemStack(Material.BIRCH_LOG, 1), 0, 0, "", 0, 0.6, false));
                add(new NPCShopItem(new ItemStack(Material.SPRUCE_LOG, 1), 0, 0, "", 0, 1, false));
                add(new NPCShopItem(new ItemStack(Material.ACACIA_LOG, 1), 0, 0, "", 0, 6, false));
                add(new NPCShopItem(new ItemStack(Material.MANGROVE_LOG), 0, 0, "", 0, 5, false));
                add(new NPCShopItem(new ItemStack(Material.DARK_OAK_LOG, 1), 0, 0, "", 0, 2, false));
                add(new NPCShopItem(new ItemStack(Material.JUNGLE_LOG, 1), 0, 0, "", 0, 2.25, true));
                add(new NPCShopItem(new ItemStack(Material.WARPED_STEM, 1), 0, 0, "", 0, 4, false));
                add(new NPCShopItem(new ItemStack(Material.CRIMSON_STEM, 1), 0, 0, "", 0, 4, false));


                add(new NPCShopItem(new ItemStack(Material.STRIPPED_OAK_LOG, 1), 0, 0, "", 0, 2, false));
                add(new NPCShopItem(new ItemStack(Material.STRIPPED_BIRCH_LOG, 1), 0, 0, "", 0, 0.9, false));
                add(new NPCShopItem(new ItemStack(Material.STRIPPED_SPRUCE_LOG, 1), 0, 0, "", 0, 1.5, false));
                add(new NPCShopItem(new ItemStack(Material.STRIPPED_ACACIA_LOG, 1), 0, 0, "", 0, 9, false));
                add(new NPCShopItem(new ItemStack(Material.STRIPPED_MANGROVE_LOG), 0, 0, "", 0, 7, false));
                add(new NPCShopItem(new ItemStack(Material.STRIPPED_DARK_OAK_LOG, 1), 0, 0, "", 0, 3, false));
                add(new NPCShopItem(new ItemStack(Material.STRIPPED_JUNGLE_LOG, 1), 0, 0, "", 0, 3.50, true));
                add(new NPCShopItem(new ItemStack(Material.STRIPPED_WARPED_STEM, 1), 0, 0, "", 0, 6, false));
                add(new NPCShopItem(new ItemStack(Material.STRIPPED_CRIMSON_STEM, 1), 0, 0, "", 0, 6, false));
            }
        }));
        AddVillager(new NPCData("Lowca", new ArrayList<>() {
            {
                //<editor-fold> 1. Ekwipunek
                add(new Quest(
                        "Ekwipunek",
                        "Potrzebuje pilnie miecz.\nPomożesz mi?",
                        QuestDifficulty.EASY, new ArrayList<>() {
                    {
                        add(new IQuestItemCraft() {
                            @Override
                            public Material getMaterial() {
                                return Material.WOODEN_SWORD;
                            }

                            @Override
                            public int getCount() {
                                return 2;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Drewniany miecz";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.WOODEN_SWORD;
                            }

                            @Override
                            public int getCount() {
                                return 2;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Drewniany miecz";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardItemStack) () -> new ItemStack(Material.STONE_SWORD));
                        add((IQuestRewardMoney) () -> 300);
                        add(new IQuestRewardExp() {
                            @Override
                            public double exp() {
                                return 50;
                            }

                            @Override
                            public LevelCategory category() {
                                return LevelCategory.Walka;
                            }
                        });
                        add((IQuestRewardItemStack) () -> CustomItemsManager.getZwyklaChest());
                    }
                }
                ));
                //</editor-fold>
                //<editor-fold> 2. Potyczki 2
                add(new Quest(
                        "Potyczki",
                        "Masz już ekwipunek\nwięc możesz zabić parę potworów!",
                        QuestDifficulty.EASY, new ArrayList<>() {
                    {
                        add(new IQuestItemKill() {
                            @Override
                            public EntityType getEntityType() {
                                return EntityType.ZOMBIE;
                            }

                            @Override
                            public int getCount() {
                                return 10;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Zombie";
                            }
                        });
                        add(new IQuestItemKill() {
                            @Override
                            public EntityType getEntityType() {
                                return EntityType.SKELETON;
                            }

                            @Override
                            public int getCount() {
                                return 2;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Szkielet";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 265);
                        add(new IQuestRewardExp() {
                            @Override
                            public double exp() {
                                return 70;
                            }

                            @Override
                            public LevelCategory category() {
                                return LevelCategory.Walka;
                            }
                        });
                        add((IQuestRewardItemStack) () -> CustomItemsManager.getZwyklaChest());
                    }
                }));
                //</editor-fold>
                //<editor-fold> 3. Przygotowania do działki
                add(new Quest(
                        "Płomyk",
                        "Słyszałem że brakuje ci\nswojego własnego terenu.\nPomogę Ci!\nAle musisz coś dla mnie zrobić.\nZnam alchemika ale nie mam materiałow\naby zrobił kości płomyka\n/craftingdzialka",
                        QuestDifficulty.NORMAL, new ArrayList<>() {
                    {
                        add(new IQuestItemKill() {
                            @Override
                            public EntityType getEntityType() {
                                return EntityType.SKELETON;
                            }

                            @Override
                            public int getCount() {
                                return 5;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Szkielet";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.LAVA_BUCKET;
                            }

                            @Override
                            public int getCount() {
                                return 1;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Wiadro z lawą";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.BONE;
                            }

                            @Override
                            public int getCount() {
                                return 6;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "3";
                            }

                            @Override
                            public String materialName() {
                                return "Kość";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardItemStack) () -> new ItemStack(Material.BLAZE_ROD, 6));
                        add(new IQuestRewardExp() {
                            @Override
                            public double exp() {
                                return 200;
                            }

                            @Override
                            public LevelCategory category() {
                                return LevelCategory.Walka;
                            }
                        });
                        add((IQuestRewardItemStack) () -> CustomItemsManager.getZwyklaChest());
                    }
                }
                ));
                //</editor-fold>
                //<editor-fold> 4. Bagna
                add(new Quest(
                        "Bagna",
                        "Stary.. nikt nie chce się\n podjąć tej brudnej roboty\nwierze w Ciebie!\nOgarnij mi trochę szlamu",
                        QuestDifficulty.HARD, new ArrayList<>() {
                    {
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.SLIME_BALL;
                            }

                            @Override
                            public int getCount() {
                                return 15;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Kula szlamu";
                            }
                        });
                        add(new IQuestItemVisit() {
                            @Override
                            public Biome getBiome() {
                                return Biome.SWAMP;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Bagna";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 2500);
                        add(new IQuestRewardExp() {
                            @Override
                            public double exp() {
                                return 300;
                            }

                            @Override
                            public LevelCategory category() {
                                return LevelCategory.Walka;
                            }
                        });
                    }
                }
                ));
                //</editor-fold>
                //<editor-fold> 5. Pustynne potwory
                add(new Quest(
                        "Pustynne potwory",
                        "Widziałeś kiedyś Zombie\nposypanego piachem? hah!\nTo POSUCH! Zobaczę na co cię stać",
                        QuestDifficulty.NORMAL, new ArrayList<>() {
                    {
                        add(new IQuestItemKill() {
                            @Override
                            public EntityType getEntityType() {
                                return EntityType.HUSK;
                            }

                            @Override
                            public int getCount() {
                                return 12;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return null;
                            }

                            @Override
                            public String materialName() {
                                return "Posuch";
                            }
                        });
                        add(new IQuestItemVisit() {
                            @Override
                            public Biome getBiome() {
                                return Biome.DESERT;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Pustynia";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 3000);
                        add(new IQuestRewardExp() {
                            @Override
                            public double exp() {
                                return 500;
                            }

                            @Override
                            public LevelCategory category() {
                                return LevelCategory.Walka;
                            }
                        });
                    }
                }
                ));
                //</editor-fold>
                //</editor-fold>
                //<editor-fold> 7. Naiwny rybak
                add(new Quest(
                        "Naiwny rybak",
                        "Hhahah. Ten naiwny Rybak\n" +
                                "siedzi z tą wędka 24 na dobę zamiast\n" +
                                "pójść nad ocean i brać ryby rękoma!",
                        QuestDifficulty.NORMAL, new ArrayList<>() {
                    {
                        add(new IQuestItemKill() {
                            @Override
                            public EntityType getEntityType() {
                                return EntityType.COD;
                            }

                            @Override
                            public int getCount() {
                                return 15;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Dorsz rzeczny";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.COOKED_COD;
                            }

                            @Override
                            public int getCount() {
                                return 5;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Upieczony dorsz";
                            }
                        });
                        add(new IQuestItemKill() {
                            @Override
                            public EntityType getEntityType() {
                                return EntityType.SALMON;
                            }

                            @Override
                            public int getCount() {
                                return 15;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "3";
                            }

                            @Override
                            public String materialName() {
                                return "Łosoś rzeczny";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 1500);
                        add(new IQuestRewardExp() {
                            @Override
                            public double exp() {
                                return 2000;
                            }

                            @Override
                            public LevelCategory category() {
                                return LevelCategory.Walka;
                            }
                        });
                        add((IQuestRewardItemStack) () -> {
                            ItemStack itemStack = CustomItemsManager.getCarrot();
                            itemStack.setAmount(10);
                            return itemStack;
                        });
                    }
                }
                ));
                //</editor-fold>
                //<editor-fold> 8. Wybuchowa sprawa
                add(new Quest(
                        "Wybuchowa sprawa",
                        "Mamy w planach z drwalem pokrzyżować trochę zamiary Górnika. Potrzebujemy\nprochu do materiałów wybuchowych, które lekko wspomogą zawalenie się jego\nkopalni.",
                        QuestDifficulty.NORMAL, new ArrayList<>() {
                    {
                        add(new IQuestItemKill() {
                            @Override
                            public EntityType getEntityType() {
                                return EntityType.CREEPER;
                            }

                            @Override
                            public int getCount() {
                                return 32;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Creeper";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.GUNPOWDER;
                            }

                            @Override
                            public int getCount() {
                                return 32;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Proch strzelniczy";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 1500);
                        add((IQuestRewardItemStack) () -> {
                            ItemStack itemStack = CustomItemsManager.getOneUseEnderchest();
                            itemStack.setAmount(5);
                            return itemStack;
                        });
                    }
                }
                ));
                //</editor-fold>
                //<editor-fold> 9. Sieć
                add(new Quest(
                                "Sieć",
                                "Jak już wiesz, mam nie pieńku\n" +
                                        "trochę z Rybakiem. Chcę mu dopiec\n" +
                                        "jeszcze bardziej, chcę zbudować\n" +
                                        "łódź i zamocować do niej sieć,\n" +
                                        "która zbierze wszystkie ryby\n" +
                                        "w okolicznych rzekach i nie tylko,\n" +
                                        "aby ten dziad poszedł z torbami.",
                                QuestDifficulty.HARD,
                                new ArrayList<>() {
                                    {
                                        add(TaskBreak(Material.COBWEB, 128, false, "1", "Pajęcza sieć"));
                                        add(TaskGive(Material.STRING, 128, "2", "Nić"));
                                    }
                                },
                                new ArrayList<>() {
                                    {
                                        add(RewardMoney(getRewardMoneyDifficulty(QuestDifficulty.HARD)));
                                    }
                                }
                        )
                );
                //</editor-fold>
                //<editor-fold> 10. Strzał
                add(new Quest(
                                "Sieć",
                                "Mój syn zaczął praktykować\n" +
                                        "strzelanie z łuku, idzie mu to\n" +
                                        "coraz lepiej, ale co chwila gubi\n" +
                                        "strzały, a ja już więcej nie mam.\n" +
                                        "Załatwisz to dla mnie?",
                                QuestDifficulty.HARD,
                                new ArrayList<>() {
                                    {
                                        add(TaskKill(EntityType.SKELETON, 64, "1", "Szkielet"));
                                        add(TaskGive(Material.ARROW, 128, "2", "Strzała"));
                                    }
                                },
                                new ArrayList<>() {
                                    {
                                        add(RewardMoney(getRewardMoneyDifficulty(QuestDifficulty.HARD)));
//                                        add((IQuestRewardItemStack) () -> ItemStackUtils.addEnchantment(new ItemStack(Material.BOW, 1), Enchantment.ARROW_INFINITE, 1));
                                    }
                                }
                        )
                );
                //</editor-fold>
            }
        }));
        AddVillager(new NPCData("Handlarz", new ArrayList<>(), new ArrayList<>() {
            {
                add(new NPCShopItem(CuboidsManager.getCuboidItem(1), 0, 1, "Zakup swoją działkę!", 3000));
                add(new NPCShopItem(CustomItemsManager.getZwyklaKey(), 0, 1, "Kup klucz do otwierania skrzyń", 100));
                add(new NPCShopItem(CustomItemsManager.getZwyklaChest(), 0, 1, "Kup skrzynie", 50));
            }
        }));
        AddVillager(new NPCData("Przedmioty zalamania", new ArrayList<>(), new ArrayList<>() {
            {
                add(new NPCShopItemSeasonOneCoins(CuboidsManager.getCuboidItem(1), 0, 1, "Zakup swoją działkę!", 350));
                ItemStack carrot = CustomItemsManager.getCarrot();
                carrot.setAmount(3);
                add(new NPCShopItemSeasonOneCoins(carrot, 0, 1, "", 60));
                ItemStack owrong = CustomItemsManager.getOwrong();
                owrong.setAmount(6);
                add(new NPCShopItemSeasonOneCoins(owrong, 0, 1, "", 60));
                add(new NPCShopItemSeasonOneCoins(CustomItemsManager.getOneUseEnderchest(), 0, 1, "", 75));
            }
        }));
        AddVillager(new NPCData("Podroznik", new ArrayList<>() {
            {
                add(new Quest("Podstawowy biom", "Założę się o wszystko\nże wiesz co tam jest", QuestDifficulty.NORMAL, new ArrayList<>() {
                    {
                        add(new IQuestItemVisit() {
                            @Override
                            public Biome getBiome() {
                                return Biome.FOREST;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Las dębowy";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.OAK_LOG;
                            }

                            @Override
                            public int getCount() {
                                return 16;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Dębowy pień";
                            }
                        });
                        add(new IQuestItemKill() {
                            @Override
                            public EntityType getEntityType() {
                                return EntityType.PIG;
                            }

                            @Override
                            public int getCount() {
                                return 1;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "3";
                            }

                            @Override
                            public String materialName() {
                                return "Świnia";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 1000);
                    }
                }));
                add(new Quest("Pierwsza rzeka", "Skoro już tam bywasz\nbodaj kampracie sztukę łyb", QuestDifficulty.NORMAL, new ArrayList<>() {
                    {
                        add(new IQuestItemVisit() {
                            @Override
                            public Biome getBiome() {
                                return Biome.RIVER;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Rzeka";
                            }
                        });
                        add(new IQuestItemKill() {
                            @Override
                            public EntityType getEntityType() {
                                return EntityType.SALMON;
                            }

                            @Override
                            public int getCount() {
                                return 4;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Łosoś rzeczny";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.COOKED_SALMON;
                            }

                            @Override
                            public int getCount() {
                                return 4;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "3";
                            }

                            @Override
                            public String materialName() {
                                return "Upieczony dorsz";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 1250);
                    }
                }));
                add(new Quest("Taiga", "Teraz poślę cie na Taige!\nAle weź się chociaż jakoś ubierz.\nP.S tam są OWRONGI", QuestDifficulty.NORMAL, new ArrayList<>() {
                    {
                        add(new IQuestItemVisit() {
                            @Override
                            public Biome getBiome() {
                                return Biome.TAIGA;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Taiga";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.SWEET_BERRIES;
                            }

                            @Override
                            public int getCount() {
                                return 64;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Jagody";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 500);
                    }
                }));
            }
        }));
        AddVillager(new NPCData("Rybak", new ArrayList<>() {
            {
                //<editor-fold> 1. Narzędzia
                add(new Quest(
                        "Narzędzia",
                        "Jeżeli chcesz zacząć przygodę\nz łowieniem, wytwórz sprzęt",
                        QuestDifficulty.EASY, new ArrayList<>() {
                    {
                        add(new IQuestItemCraft() {
                            @Override
                            public Material getMaterial() {
                                return Material.FISHING_ROD;
                            }

                            @Override
                            public int getCount() {
                                return 1;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Wędka";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 300);
                        add(new IQuestRewardExp() {
                            @Override
                            public double exp() {
                                return 40;
                            }

                            @Override
                            public LevelCategory category() {
                                return LevelCategory.Lowienie;
                            }
                        });
                    }
                }
                ));
                //</editor-fold> 1. Narzędzia
                //<editor-fold> 2. Pierwszy rzut
                add(new Quest(
                        "Pierwszy rzut",
                        "Jak już posiadasz sprzęt\ntrzeba chyba od czegoś zacząć!\nZobaczę jak ci idzie..",
                        QuestDifficulty.EASY, new ArrayList<>() {
                    {
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.COD;
                            }

                            @Override
                            public int getCount() {
                                return 2;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Dorsz";
                            }
                        });
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.SALMON;
                            }

                            @Override
                            public int getCount() {
                                return 1;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Łosoś";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 150);
                    }
                }
                ));
                //</editor-fold> 2. Pierwszy rzut
                //<editor-fold> 3. Test żyłki
                add(new Quest(
                        "Test żyłki",
                        "Zobaczmy jakie ryby złowisz na tej żyłce!\nBardzo mnie to ciekawi wiesz?",
                        QuestDifficulty.NORMAL, new ArrayList<>() {
                    {
                        add(new IQuestItemFish() {
                            @Override
                            public Material getMaterial() {
                                return Material.SALMON;
                            }

                            @Override
                            public int getCount() {
                                return 5;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Łosoś";
                            }
                        });
                        add(new IQuestItemFish() {
                            @Override
                            public Material getMaterial() {
                                return Material.COD;
                            }

                            @Override
                            public int getCount() {
                                return 2;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Dorsz";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 500);
                        add(new IQuestRewardExp() {
                            @Override
                            public double exp() {
                                return 75;
                            }

                            @Override
                            public LevelCategory category() {
                                return LevelCategory.Lowienie;
                            }
                        });
                    }
                }
                ));
                //</editor-fold> 3. Test żyłki
                //<editor-fold> 4. Smażalnia
                add(new Quest(
                        "Smażalnia",
                        "Usmaż pare podstawowych rybek",
                        QuestDifficulty.EASY, new ArrayList<>() {
                    {
                        add(new IQuestItemGive() {
                            @Override
                            public Material getMaterial() {
                                return Material.COOKED_COD;
                            }

                            @Override
                            public int getCount() {
                                return 20;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Upieczony dorsz";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 500);
                        add(new IQuestRewardExp() {
                            @Override
                            public double exp() {
                                return 175;
                            }

                            @Override
                            public LevelCategory category() {
                                return LevelCategory.Lowienie;
                            }
                        });
                        add((IQuestRewardItemStack) () -> new ItemStack(Material.COOKED_COD, 9));
                    }
                }
                ));
                //</editor-fold> 4. Smażalnia
                //<editor-fold> 5. Śmieci w wodzie
                add(new Quest(
                        "Śmieci w wodzie",
                        "Sprawdź co ludzie wyrzucają do wody!",
                        QuestDifficulty.NORMAL, new ArrayList<>() {
                    {
                        add(new IQuestItemFish() {
                            @Override
                            public Material getMaterial() {
                                return Material.LILY_PAD;
                            }

                            @Override
                            public int getCount() {
                                return 2;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Lilia wodna";
                            }
                        });
                        add(new IQuestItemFish() {
                            @Override
                            public Material getMaterial() {
                                return Material.BOWL;
                            }

                            @Override
                            public int getCount() {
                                return 2;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Miska";
                            }
                        });
                        add(new IQuestItemFish() {
                            @Override
                            public Material getMaterial() {
                                return Material.ROTTEN_FLESH;
                            }

                            @Override
                            public int getCount() {
                                return 1;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return "Zgniłe mięso";
                            }
                        });
                        add(new IQuestItemFish() {
                            @Override
                            public Material getMaterial() {
                                return Material.COD;
                            }

                            @Override
                            public int getCount() {
                                return 15;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "2";
                            }

                            @Override
                            public String materialName() {
                                return "Dorsz";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add((IQuestRewardMoney) () -> 1500);
                        add(new IQuestRewardExp() {
                            @Override
                            public double exp() {
                                return 500;
                            }

                            @Override
                            public LevelCategory category() {
                                return LevelCategory.Lowienie;
                            }
                        });
                        add((IQuestRewardItemStack) () -> {
                            ItemStack is = CustomItemsManager.getOneUseEnderchest();
                            is.setAmount(4);
                            return is;
                        });
                    }
                }
                ));
                //</editor-fold> 5. Śmieci w wodzie
                //<editor-fold> 6. RybPro
                add(new Quest("RybPro", "WOW! Yhym jestem..\npod wrażeniem\nTo napewno ty łowiłeś?", QuestDifficulty.HARD, new ArrayList<>() {
                    {
                        add(new IQuestItemFishingRod() {
                            @Override
                            public int getCount() {
                                return 100;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }

                            @Override
                            public String materialName() {
                                return null;
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add(new IQuestRewardExp() {
                            @Override
                            public double exp() {
                                return 1500.5;
                            }

                            @Override
                            public LevelCategory category() {
                                return LevelCategory.Lowienie;
                            }
                        });
                    }
                }));
                //</editor-fold> 6. RybPro
                //<editor-fold> 7. Schemat
                add(new Quest("Schemat", "Dobra, to zróbmy tak\nZapłacisz mi ja dam ci moje schematy", QuestDifficulty.HARD, new ArrayList<>() {
                    {
                        add(new IQuestItemPay() {
                            @Override
                            public int getCount() {
                                return 10000;
                            }

                            @Override
                            public String getQuestItemDataId() {
                                return "1";
                            }
                        });
                    }
                }, new ArrayList<>() {
                    {
                        add(new IQuestRewardExp() {
                            @Override
                            public double exp() {
                                return 500;
                            }

                            @Override
                            public LevelCategory category() {
                                return LevelCategory.Lowienie;
                            }
                        });
                        add(new IQuestRewardCustom() {
                            @Override
                            public String label() {
                                return "Odblokowanie najlepszej wędki";
                            }

                            @Override
                            public void Action(Player p, User u) {

                            }
                        });
                    }
                }));
                //</editor-fold> 7. Schemat
                //<editor-fold> 8. Staw
                add(new Quest("Staw", "Ostatnio zrobiłem sobie małą sadzawkę za swoim\ndomem, przydałoby mi się troche lili wodnych do\nozdoby, dasz radę załatwić?", QuestDifficulty.EASY,
                        new ArrayList<>() {
                            {
                                add(new IQuestItemGive() {
                                    @Override
                                    public Material getMaterial() {
                                        return Material.LILY_PAD;
                                    }

                                    @Override
                                    public int getCount() {
                                        return 24;
                                    }

                                    @Override
                                    public String getQuestItemDataId() {
                                        return "1";
                                    }

                                    @Override
                                    public String materialName() {
                                        return "Lilia wodna";
                                    }
                                });
                            }
                        }, new ArrayList<>() {
                    {
                        add(new IQuestRewardMoney() {
                            @Override
                            public int money() {
                                return 850;
                            }
                        });
                    }
                }));
                //</editor-fold>
            }
        }, new ArrayList<>() {
            {
                add(new NPCShopItem(new ItemStack(Material.COD, 1), 0, 0, "Świeży filet z dorsza", 36, 6, false));
                add(new NPCShopItem(new ItemStack(Material.COOKED_COD, 1), 0, 0, "Pieczony dorsz", 60.5, 8.5, false));
                add(new NPCShopItem(new ItemStack(Material.SALMON, 1), 0, 0, "", 45, 7.7, false));
                add(new NPCShopItem(new ItemStack(Material.COOKED_SALMON, 1), 0, 0, "", 70.2, 10, false));
                add(new NPCShopItem(new ItemStack(Material.TROPICAL_FISH, 1), 0, 0, "Rzadki okaz ryby", 0, 8.5, false));
                add(new NPCShopItem(new ItemStack(Material.PUFFERFISH, 1), 0, 0, "Rzadki okaz ryby", 0, 12.4, false));
                add(new NPCShopItem(new ItemStack(Material.NAUTILUS_SHELL, 1), 0, 0, "Niespotykany przedmiot", 0, 250.25, false));
                add(new NPCShopItem(new ItemStack(Material.KELP, 1), 0, 0, "Bardzo pospolity przedmiot", 0, 0.4, false));
                add(new NPCShopItem(new ItemStack(Material.DRIED_KELP, 1), 0, 0, "Bardzo pospolity przedmiot", 0, 0.9, false));
                add(new NPCShopItem(new ItemStack(Material.DRIED_KELP_BLOCK, 1), 0, 0, "Bardzo pospolity przedmiot", 0, 9.1, true));
                add(new NPCShopItem(new ItemStack(Material.TURTLE_EGG, 1), 0, 0, "", 0, 25.55, false));
                add(new NPCShopItem(new ItemStack(Material.SCUTE, 1), 0, 0, "", 0, 18.25, false));
                add(new NPCShopItem(new ItemStack(Material.SEAGRASS, 1), 0, 0, "", 0, 2.2, false));
                add(new NPCShopItem(new ItemStack(Material.SEA_PICKLE, 1), 0, 0, "", 0, 4.1, true));
                ItemStack is = ItemStackUtils.createGuiItem(Material.FISHING_ROD, 1, ColorUtils.color("&bProfesjonalna Wędka Rybaka"));
                is.addUnsafeEnchantment(Enchantment.LURE, 5);
                is.addUnsafeEnchantment(Enchantment.LUCK, 5);
                is.addUnsafeEnchantment(Enchantment.DURABILITY, 5);
                add(new NPCShopItem(is, 8, 4, "Bardzo szybko łapie ryby", 45720));
            }
        }));
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    public static int fits(ItemStack stack, @NotNull Inventory inv) {
        ItemStack[] contents = inv.getContents();
        int result = 0;

        for (ItemStack is : contents)
            if (is == null)
                result += stack.getMaxStackSize();
            else if (is.isSimilar(stack))
                result += Math.max(stack.getMaxStackSize() - is.getAmount(), 0);

        return result;
    }

    public static int getMaxCraftAmount(@NotNull CraftingInventory inv) {
        if (inv.getResult() == null)
            return 0;

        int resultCount = inv.getResult().getAmount();
        int materialCount = Integer.MAX_VALUE;

        for (ItemStack is : inv.getMatrix())
            if (is != null && is.getAmount() < materialCount)
                materialCount = is.getAmount();

        return resultCount * materialCount;
    }

    // Fireworks effect
    public static void spawnFireworks(Location location, int amount) {
        Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();

        fwm.setPower(2);
        fwm.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).build());

        fw.setFireworkMeta(fwm);
        fw.detonate();

        for (int i = 0; i < amount; i++) {
            Firework fw2 = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
            fw2.setFireworkMeta(fwm);
        }
    }

    private void AddVillager(NPCData npcData) {
        RegisterNPC(npcData);
    }

    public int getRewardMoneyDifficulty(QuestDifficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return 750;
            case NORMAL:
                return 1050;
            case HARD:
                return 2500;
            case TRYHARD:
                return 5000;
            default:
                return 0;
        }
    }

    public IQuestItemBreak TaskBreak(Material material, int count, boolean blockSilk, String questData, String materialName) {
        return new IQuestItemBreak() {
            @Override
            public Material getMaterial() {
                return material;
            }

            @Override
            public int getCount() {
                return count;
            }

            @Override
            public boolean blockSilk() {
                return blockSilk;
            }

            @Override
            public String getQuestItemDataId() {
                return questData;
            }

            @Override
            public String materialName() {
                return materialName;
            }
        };
    }

    public IQuestItemGive TaskGive(Material material, int count, String questData, String materialName) {
        return new IQuestItemGive() {
            @Override
            public Material getMaterial() {
                return material;
            }

            @Override
            public int getCount() {
                return count;
            }

            @Override
            public String getQuestItemDataId() {
                return questData;
            }

            @Override
            public String materialName() {
                return materialName;
            }
        };
    }

    public IQuestItemCraft TaskCraft(Material material, int count, String questData, String materialName) {
        return new IQuestItemCraft() {
            @Override
            public Material getMaterial() {
                return material;
            }

            @Override
            public int getCount() {
                return count;
            }

            @Override
            public String getQuestItemDataId() {
                return questData;
            }

            @Override
            public String materialName() {
                return materialName;
            }
        };
    }

    public IQuestItemCollect TaskFarm(Material material, int count, String questData, String materialName) {
        return new IQuestItemCollect() {
            @Override
            public Material getMaterial() {
                return material;
            }

            @Override
            public int getCount() {
                return count;
            }

            @Override
            public String getQuestItemDataId() {
                return questData;
            }

            @Override
            public String materialName() {
                return materialName;
            }
        };
    }

    public IQuestItemKill TaskKill(EntityType entity, int count, String questData, String entityName) {
        return new IQuestItemKill() {
            @Override
            public EntityType getEntityType() {
                return entity;
            }

            @Override
            public int getCount() {
                return count;
            }

            @Override
            public String getQuestItemDataId() {
                return questData;
            }

            @Override
            public String materialName() {
                return entityName;
            }
        };
    }

    public IQuestItemBreed TaskBreed(BreedingAnimal breedingAnimal, int count, String questData, String entityName) {
        return new IQuestItemBreed() {

            @Override
            public BreedingAnimal getEntityType() {
                return breedingAnimal;
            }

            @Override
            public int getCount() {
                return count;
            }

            @Override
            public String getQuestItemDataId() {
                return questData;
            }

            @Override
            public String materialName() {
                return entityName;
            }
        };
    }

    public IQuestRewardMoney RewardMoney(int money) {
        return () -> money;
    }

    public IQuestRewardItemStack RewardItemStack(ItemStack item) {
        return () -> item;
    }

    public void RegisterNPC(NPCData shop) {
        npcs.put(shop.getId(), shop);
        Logger.logNpcMessage("Zarejestrowano nowy sklep " + shop.getId());
    }

    public ItemStack getItemOfShop(User u, NPCData NPCData, NPCShopItem item) {
        PlayerNPCSData data = u.getNPCSData();
        PlayerNPCData playerNPCData = data.getNPCSData().get(NPCData.getId());
        Material mat;
        String name;
        boolean unlocked = playerNPCData.getQuestIndex() + 1 >= item.getRequiredQuestLevel() && u.getUserLevel().playerLevel() >= item.getRequiredPlayerLevel();
        if (unlocked) {
            mat = item.getItem().getType();
            if (item.getItem().getItemMeta().hasDisplayName()) {
                name = item.getItem().getItemMeta().getDisplayName();
            } else {
                name = null;
            }
        } else {
            mat = Material.IRON_BARS;
            name = ColorUtils.color("&c&k" + ChatUtil.materialName(item.getItem().getType()));
        }
        ItemStack i = item.getItem().clone();
        i.setType(mat);
        if (mat.equals(Material.IRON_BARS)) {
            i.setAmount(1);
        }
        if (!unlocked) {
            for (Enchantment enchantments : i.getEnchantments().keySet()) {
                i.removeEnchantment(enchantments);
            }
        }
        ItemMeta meta = i.getItemMeta();
        if (name != null) {
            meta.setDisplayName(name);
        }
        ArrayList<String> lore = new ArrayList<>();
        if (unlocked) {
            if (item.isBoostedSell()) {
                lore.add(ColorUtils.color("&a⬆ &9Wzmocniona sprzedaż &a⬆"));
                meta.addEnchant(Enchantment.DURABILITY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }
            if (item.getItem().getItemMeta().hasLore()) {
                lore.addAll(item.getItem().getLore());
            } else {
                if (!item.getDescription().equals("")) {
                    lore.add(ColorUtils.color(ColorUtils.color("&8" + item.getDescription())));
                }
            }
            lore.add(" ");
            //✔✘
            if (item.canSell()) {
                if (item.getCost() != 0) {
                    if (item.buyByCoins()) {
                        if ((int) item.getCost() <= u.getSeasonOneCoins()) {
                            lore.add(ColorUtils.color("&6Cena kupna"));
                            lore.add(ColorUtils.color("&d" + ChatUtil.getSeasonCoins((int) item.getCost())));
                            lore.add(ColorUtils.color("&3Kliknij LPM: &8[&b▟&7▙&8] &3aby kupić"));
                        } else {
                            lore.add(ColorUtils.color("&6Cena kupna"));
                            lore.add(ColorUtils.color("&c" + ChatUtil.getSeasonCoins((int) item.getCost())));
                            lore.add(ColorUtils.color("&3Kliknij LPM: &8[&b▟&7▙&8] &3aby kupić"));
                        }
                    } else {
                        if (item.getCost() <= u.getMoney()) {
                            lore.add(ColorUtils.color("&6Cena kupna"));
                            lore.add(ColorUtils.color("&a" + ChatUtil.getMoney(item.getCost())));
                            lore.add(ColorUtils.color("&3Kliknij LPM: &8[&b▟&7▙&8] &3aby kupić"));
                        } else {
                            lore.add(ColorUtils.color("&6Cena kupna"));
                            lore.add(ColorUtils.color("&c" + ChatUtil.getMoney(item.getCost())));
                            lore.add(ColorUtils.color("&3Kliknij LPM: &8[&b▟&7▙&8] &3aby kupić"));
                        }
                    }
                    lore.add(" ");
                }
                if (ItemStackUtils.getCountOfMaterial(u.getPlayer(), item.getItem().getType()) >= item.getItem().getAmount()) {
                    if (item.isBoostedSell()) {
                        lore.add(ColorUtils.color("&6Cena sprzedaży"));
                        lore.add(ColorUtils.color("&8&n&m" + ChatUtil.getMoney(item.getSellCost()) + "&a " + ChatUtil.getMoney(item.getFinalCost()) + " &a⬆"));
                        lore.add(ColorUtils.color("&3Kliknij PPM: &8[&7▟&b▙&8] &3aby sprzedać"));
                    } else {
                        lore.add(ColorUtils.color("&6Cena sprzedaży"));
                        lore.add(ColorUtils.color("&a" + ChatUtil.getMoney(item.getFinalCost())));
                        lore.add(ColorUtils.color("&3Kliknij PPM: &8[&7▟&b▙&8] &3aby sprzedać"));
                    }
                } else {
                    if (item.isBoostedSell()) {
                        lore.add(ColorUtils.color("&6Cena sprzedaży"));
                        lore.add(ColorUtils.color("&8&n&m" + ChatUtil.getMoney(item.getSellCost()) + "&c " + ChatUtil.getMoney(item.getFinalCost()) + " &a⬆"));
                        lore.add(ColorUtils.color("&3Kliknij PPM: &8[&7▟&b▙&8] &3aby sprzedać"));
                    } else {
                        lore.add(ColorUtils.color("&6Cena sprzedaży"));
                        lore.add(ColorUtils.color("&c" + ChatUtil.getMoney(item.getFinalCost())));
                        lore.add(ColorUtils.color("&3Kliknij PPM: &8[&7▟&b▙&8] &3aby sprzedać"));
                    }
                }
                lore.add(ColorUtils.color("&7Przytrzymaj SHIFT aby,"));
                lore.add(ColorUtils.color("&7sprzedać cały stack"));
            } else {
                if (item.buyByCoins()) {
                    if ((int) item.getCost() <= u.getSeasonOneCoins()) {
                        lore.add(ColorUtils.color("&6Cena kupna"));
                        lore.add(ColorUtils.color("&d" + ChatUtil.getSeasonCoins((int) item.getCost())));
                        lore.add(ColorUtils.color("&3Kliknij LPM: &8[&b▟&7▙&8] &3aby zakupić"));
                    } else {
                        lore.add(ColorUtils.color("&6Cena kupna"));
                        lore.add(ColorUtils.color("&c" + ChatUtil.getSeasonCoins((int) item.getCost())));
                        lore.add(ColorUtils.color("&3Kliknij LPM: &8[&b▟&7▙&8] &3aby zakupić"));
                    }
                } else {
                    if (item.getCost() <= u.getMoney()) {
                        lore.add(ColorUtils.color("&6Cena kupna"));
                        lore.add(ColorUtils.color("&a" + ChatUtil.getMoney(item.getCost())));
                        lore.add(ColorUtils.color("&3Kliknij LPM: &8[&b▟&7▙&8] &3aby zakupić"));
                    } else {
                        lore.add(ColorUtils.color("&6Cena kupna"));
                        lore.add(ColorUtils.color("&c" + ChatUtil.getMoney(item.getCost())));
                        lore.add(ColorUtils.color("&3Kliknij LPM: &8[&b▟&7▙&8] &3aby zakupić"));
                    }
                }
            }
        } else {
            if (playerNPCData.getQuestIndex() + 1 < item.getRequiredQuestLevel()) {
                lore.add(ColorUtils.color("&c✘ &eZostanie odblokowane na " + item.getRequiredQuestLevel() + " poz. zadań"));
            } else {
                lore.add(ColorUtils.color("&a✔ Posiadasz wystarczający poziom zadań"));
            }
            if (u.getUserLevel().playerLevel() < item.getRequiredPlayerLevel()) {
                lore.add(ColorUtils.color("&c✘ &eZostanie odblokowane na " + item.getRequiredPlayerLevel() + " poz. postaci"));
            } else {
                lore.add(ColorUtils.color("&a✔ Posiadasz wystarczający poziom postaci"));
            }
        }
        meta.setLore(lore);
        i.setItemMeta(meta);
        return i;
    }

    public void loreCheck(ArrayList<String> lore, final int count, final boolean isActiveQuest, final int questProgress, final String itemPrefix, final String suffix) {
        if (count == 1) {
            int value;
            if (isActiveQuest) {
                value = count - questProgress;
            } else {
                lore.add(ColorUtils.color(" &8▪ &7" + itemPrefix + " " + suffix));
                return;
            }
            boolean hasItem = value <= 0;
            if (hasItem) {
                lore.add(ColorUtils.color(" &a✔ &m" + itemPrefix + " " + suffix));
            } else {
                lore.add(ColorUtils.color(" &c✘ " + itemPrefix + " " + value + " " + suffix));
            }
        } else {
            int value;
            if (isActiveQuest) {
                value = count - questProgress;
            } else {
                lore.add(ColorUtils.color(" &8▪ &7" + itemPrefix + " " + count + " " + suffix));
                return;
            }
            boolean hasItem = value <= 0;
            if (hasItem) {
                lore.add(ColorUtils.color(" &a✔ &m" + itemPrefix + " " + count + " " + suffix));
            } else {
                lore.add(ColorUtils.color(" &c✘ " + itemPrefix + " " + value + " " + suffix));
            }
        }
    }

    public void loreCheckMoney(ArrayList<String> lore, final int count, final boolean isActiveQuest, final int questProgress, final String itemPrefix) {
        int value;
        if (isActiveQuest) {
            value = count - questProgress;
        } else {
            lore.add(ColorUtils.color(" &8▪ &7" + itemPrefix + " " + ChatUtil.getMoney(count)));
            return;
        }
        boolean hasItem = value <= 0;
        if (hasItem) {
            lore.add(ColorUtils.color(" &a✔ &m" + itemPrefix + " " + ChatUtil.getMoney(count)));
        } else {
            lore.add(ColorUtils.color(" &c✘ " + itemPrefix + " " + ChatUtil.getMoney(value)));
        }
    }

    public ItemStack getItemOfQuest(Player p, NPCData NPCData, PlayerNPCData playerNPCData) {
        Material mat;
        String name;
        String details;
        if (NPCData.getQuests().size() == 0) {
            return ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " ");
        }
        if (playerNPCData.getQuestIndex() >= NPCData.getQuests().size()) {
            mat = Material.DIAMOND;
            name = ColorUtils.color("&aZakończono wszystkie zadania");
            details = ColorUtils.color("&8Gratulacje! Zakończyłeś już wszystkie zadania");
            ItemStack item = new ItemStack(mat, playerNPCData.getQuestIndex() + 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(name);
            ArrayList<String> lore = new ArrayList<>() {
                {
                    add(details);
                }
            };
            meta.setLore(lore);
            item.setItemMeta(meta);
            return item;
        }
        AtomicBoolean delay = new AtomicBoolean(false);
        final Instant now = Instant.now();
        questDelay.compute(p.getName(), (uuid, instant) -> {
            if (instant == null) {
                return new NPCDelayData(new IdentityHashMap<>());
            }
            if (instant.getTimers().get(NPCData.getId()) == null) {
                instant.getTimers().put(NPCData.getId(), now);
            } else {
                if (now.isBefore(instant.getTimers().get(NPCData.getId()))) {
                    p.sendActionBar(ColorUtils.color("&cOdczekaj chwilę miedzy zadaniami"));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    delay.set(true);
                    return instant;
                }
            }
            // no return
            return instant;
        });
        if (delay.get()) {
            ItemStack item = new ItemStack(Material.CLOCK, playerNPCData.getQuestIndex() + 1);
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(ColorUtils.color("&ePoczekaj na kolejne zlecenia"));
            ArrayList<String> lore = new ArrayList<>() {
                {
                    add(ColorUtils.color("&7Wykonałeś zadanie, poczekaj na kolejne"));
                }
            };
            meta.setLore(lore);
            item.setItemMeta(meta);
            return item;
        }
        Quest q = NPCData.getQuests().get(playerNPCData.getQuestIndex());
        ArrayList<String> lore = new ArrayList<>();
        if (playerNPCData.isActiveQuest()) {
            mat = Material.GOLD_INGOT;
            name = ColorUtils.color("&9✎ &a" + q.getName() + " &8[&aAktywne&8]");
            //&3Kliknij LPM: &8[&b▟&7▙&8] &3aby kupić
            //lore.add(ColorUtils.color("&7LPM &8[&b▟&7▙&8] &7aby oddać"));
            //lore.add(ColorUtils.color("&cPPM &8[&7▟&b▙&8] &caby anulować"));
            //
        } else {
            mat = Material.BOOK;
            name = ColorUtils.color("&9✎ &6" + q.getName());
            //lore.add(ColorUtils.color("&7Kliknij aby zaakceptować"));
        }
        int amount = playerNPCData.getQuestIndex();
        if (amount == 0) {
            amount = 1;
        }
        ItemStack item = new ItemStack(mat, amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        String[] lines = q.getDescription().split("\\n");
        for (String line : lines) {
            lore.add(ColorUtils.color("   &7" + line));
        }
        lore.add(" ");
        lore.add(ColorUtils.color("&6Zadanie&8: [" + q.getDifficulty() + "&8]"));
        for (IQuestItem qItem : q.getQuestItems()) {
            if (qItem instanceof IQuestItemFish) {
                IQuestItemFish questItem = (IQuestItemFish) qItem;
                int progress = 0;
                if (playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId()) != null) {
                    progress = playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId());
                }
                loreCheck(lore,
                        questItem.getCount(),
                        playerNPCData.isActiveQuest(),
                        progress,
                        questItem.getQuestItemPrefix(),
                        questItem.materialName()
                );
            }
            if (qItem instanceof IQuestItemCollect) {
                IQuestItemCollect questItem = (IQuestItemCollect) qItem;
                int progress = 0;
                if (playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId()) != null) {
                    progress = playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId());
                }
                loreCheck(lore,
                        questItem.getCount(),
                        playerNPCData.isActiveQuest(),
                        progress,
                        questItem.getQuestItemPrefix(),
                        questItem.materialName()
                );
            }
            if (qItem instanceof IQuestItemPay) {
                IQuestItemPay questItem = (IQuestItemPay) qItem;
                int progress = 0;
                if (playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId()) != null) {
                    progress = playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId());
                }
                loreCheckMoney(lore,
                        questItem.getCount(),
                        playerNPCData.isActiveQuest(),
                        progress,
                        questItem.getQuestItemPrefix()
                );
            }
            if (qItem instanceof IQuestItemGive) {
                IQuestItemGive questItem = (IQuestItemGive) qItem;
                int progress = 0;
                if (playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId()) != null) {
                    progress = playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId());
                }
                loreCheck(
                        lore,
                        questItem.getCount(),
                        playerNPCData.isActiveQuest(),
                        progress,
                        questItem.getQuestItemPrefix(),
                        questItem.materialName()
                );
            }
            if (qItem instanceof IQuestItemCraft) {
                IQuestItemCraft questItem = (IQuestItemCraft) qItem;
                int progress = 0;
                if (playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId()) != null) {
                    progress = playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId());
                }
                loreCheck(
                        lore,
                        questItem.getCount(),
                        playerNPCData.isActiveQuest(),
                        progress,
                        questItem.getQuestItemPrefix(),
                        questItem.materialName()
                );
            }
            if (qItem instanceof IQuestItemBreak) {
                IQuestItemBreak questItem = (IQuestItemBreak) qItem;
                int progress = 0;
                if (playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId()) != null) {
                    progress = playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId());
                }
                loreCheck(lore,
                        questItem.getCount(),
                        playerNPCData.isActiveQuest(),
                        progress,
                        questItem.getQuestItemPrefix(),
                        questItem.materialName()
                );
            }
            if (qItem instanceof IQuestItemKill) {
                IQuestItemKill questItem = (IQuestItemKill) qItem;
                int progress = 0;
                if (playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId()) != null) {
                    progress = playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId());
                }
                loreCheck(lore,
                        questItem.getCount(),
                        playerNPCData.isActiveQuest(),
                        progress,
                        questItem.getQuestItemPrefix(),
                        questItem.materialName()
                );
            }
            if (qItem instanceof IQuestItemBreed) {
                IQuestItemBreed questItem = (IQuestItemBreed) qItem;
                int progress = 0;
                if (playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId()) != null) {
                    progress = playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId());
                }
                loreCheck(lore,
                        questItem.getCount(),
                        playerNPCData.isActiveQuest(),
                        progress,
                        questItem.getQuestItemPrefix(),
                        questItem.materialName()
                );
            }
            if (qItem instanceof IQuestItemFishingRod) {
                IQuestItemFishingRod questItem = (IQuestItemFishingRod) qItem;
                int progress = 0;
                if (playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId()) != null) {
                    progress = playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId());
                }
                loreCheck(lore,
                        questItem.getCount(),
                        playerNPCData.isActiveQuest(),
                        progress,
                        questItem.getQuestItemPrefix(),
                        questItem.materialName()
                );
            }
            if (qItem instanceof IQuestItemVisit) {
                IQuestItemVisit questItem = (IQuestItemVisit) qItem;
                int progress = 0;
                if (playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId()) != null) {
                    progress = playerNPCData.getQuestItemData().get(questItem.getQuestItemDataId());
                }
                loreCheck(lore,
                        1,
                        playerNPCData.isActiveQuest(),
                        progress,
                        questItem.getQuestItemPrefix(),
                        questItem.materialName()
                );
            }
        }
        lore.add(" ");
        lore.add(ColorUtils.color("&6Nagroda&8:"));
        for (IQuestReward reward : q.getRewardItems()) {
            if (reward instanceof IQuestRewardItemStack) {
                ItemStack przedmiot = ((IQuestRewardItemStack) reward).item();
                int przedmiotAmount = przedmiot.getAmount();
                String przedmiotName = ChatUtil.materialName(przedmiot.getType());
                String prefix = "";
                if (przedmiot.getEnchantments().size() > 0) {
                    prefix = "Enchanted ";
                }
                if (przedmiot.hasItemMeta() && przedmiot.getItemMeta().hasDisplayName()) {
                    przedmiotName = przedmiot.getItemMeta().getDisplayName();
                }

                if (przedmiotAmount > 1) {
                    lore.add(ColorUtils.color(" &8▪ &7" + prefix + przedmiotName + " x" + przedmiotAmount));
                } else {
                    lore.add(ColorUtils.color(" &8▪ &7" + prefix + przedmiotName));
                }
            }
            if (reward instanceof IQuestRewardCustom) {
                lore.add(ColorUtils.color(" &8▪ &7" + ((IQuestRewardCustom) reward).label()));
            }
            if (reward instanceof IQuestRewardMoney) {
                lore.add(ColorUtils.color(" &8▪ &7" + ChatUtil.getMoney(((IQuestRewardMoney) reward).money())));
            }
            if (reward instanceof IQuestRewardExp) {
                lore.add(ColorUtils.color(" &8▪ &7" + ((IQuestRewardExp) reward).exp() + " pd."));
            }
        }
        if (playerNPCData.isActiveQuest()) {
            lore.add(" ");
            lore.add(ColorUtils.color("&7LPM &8[&b▟&7▙&8] &7aby oddać"));
            lore.add(ColorUtils.color("&7PPM &8[&7▟&b▙&8] &7aby anulować"));
            if (!UserManager.getUser(p.getUniqueId()).hasRank(Rank.Zelazo)) {
                {
                    lore.add(ColorUtils.color("&cAnulowanie skutkuje -200 zł"));
                }
            }
        } else {
            lore.add(" ");
            lore.add(ColorUtils.color("&7LPM &8[&b▟&7▙&8] &7aby zaakceptować"));
        }
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public Inventory getInventoryOfVillager(NPCData villager, Player player) {
        User u = UserManager.getUser(player.getUniqueId());
        try {
            if (!u.getNPCSData().getNPCSData().containsKey(villager.getId())) {
                u.getNPCSData().getNPCSData().put(villager.getId(), new PlayerNPCData(villager.getId(), 0, false, new HashMap<>()));
            }
        } catch (Exception e) {
            System.out.println("Exception on fixing");
            e.printStackTrace();
        }
        int size = 54 - 1;

        Inventory inv;
        if (villager.isShop()) {
            inv = Bukkit.createInventory(null, 54, villager.getId());
            for (int i = 0; i != size - 1; i++) {
                inv.setItem(i, ItemStackUtils.createGuiItem(Material.GRAY_STAINED_GLASS_PANE, 1, " "));
            }
            for (int i = size - 8; i != size + 1; i++) {
                inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
            }
            inv.setItem(size - 4, getItemOfQuest(player, villager, u.getNPCSData().getNPCSData().get(villager.getId())));
            if (villager.getShopItems().size() > 0) {
                for (int i = 0; i != villager.getShopItems().size(); i++) {
                    inv.setItem(i, getItemOfShop(u, villager, villager.getShopItems().get(i)));
                }
            }
        } else {
            size = 9;
            inv = Bukkit.createInventory(null, 9, villager.getId());
            for (int i = 0; i != size; i++) {
                inv.setItem(i, ItemStackUtils.createGuiItem(Material.BLACK_STAINED_GLASS_PANE, 1, " "));
            }
            inv.setItem(size - 5, getItemOfQuest(player, villager, u.getNPCSData().getNPCSData().get(villager.getId())));
        }
        return inv;
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }
        if (e.getClickedInventory() == null) {
            return;
        }
        if (e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
            if (npcs.containsKey(e.getView().getTitle())) {
                e.setCancelled(true);
                return;
            }
            return;
        }
        if (npcs.containsKey(e.getView().getTitle())) {
            e.setCancelled(true);
            if (!npcs.containsKey(e.getView().getTitle())) {
                System.out.println("Sklep nie istnieje!");
            }
            // Varibales
            NPCData NPCData = npcs.get(e.getView().getTitle());
            Player p = (Player) e.getWhoClicked();
            User u = UserManager.getUser(p.getUniqueId());
            // Shop
            PlayerNPCData data = u.getNPCSData().getNPCSData().get(NPCData.getId());
            int questSlot;
            if (NPCData.isShop()) {
                // SHOP
                if (e.getSlot() > -1 && e.getSlot() < 45) {
                    // SHOP
                    if (e.getSlot() > NPCData.getShopItems().size()) {
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        return;
                    }
                    NPCShopItem shopItem = NPCData.getShopItems().get(e.getSlot());
                    if (e.getAction() == InventoryAction.PICKUP_ALL) {
                        boolean unlocked = data.getQuestIndex() + 1 >= shopItem.getRequiredQuestLevel() && u.getUserLevel().playerLevel() >= shopItem.getRequiredPlayerLevel();
                        if (unlocked) {
                            if (shopItem.getCost() == 0) {
                                return;
                            }
                            if (shopItem.buyByCoins()) {
                                if (u.hasSeasonOneCoins((int) shopItem.getCost())) {
                                    u.subtractSeasonOneCoins((int) shopItem.getCost());
                                    shopItem.onBuy(p);
                                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                    String name;
                                    if (shopItem.getItem().getItemMeta().hasDisplayName()) {
                                        name = shopItem.getItem().getItemMeta().getDisplayName();
                                    } else {
                                        name = ChatUtil.materialName(shopItem.getItem().getType());
                                    }
                                    p.sendMessage(ColorUtils.color("&c&l$ &6» &7Zakupiłeś &6" + name + " x" + shopItem.getItem().getAmount() + " &7za &d" + ChatUtil.getSeasonCoins((int) shopItem.getCost())));
                                    Logger.logNpcMessage(ColorUtils.color("&6" + p.getName() + " &7zakupił &6" + name + " &7od &9" + NPCData.getId()));
                                } else {
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                    p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + " &6» &cNiestety nie dogadamy się. Nie posiadasz tyle pieniędzy."));
                                }
                            } else {
                                if (u.hasMoney(shopItem.getCost())) {
                                    u.subtractMoney(shopItem.getCost());
                                    shopItem.onBuy(p);
                                    p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                                    String name;
                                    if (shopItem.getItem().getItemMeta().hasDisplayName()) {
                                        name = shopItem.getItem().getItemMeta().getDisplayName();
                                    } else {
                                        name = ChatUtil.materialName(shopItem.getItem().getType());
                                    }
                                    p.sendMessage(ColorUtils.color("&c&l$ &6» &7Zakupiłeś &6" + name + " x" + shopItem.getItem().getAmount() + " &7za &6" + ChatUtil.getMoney(shopItem.getCost())));
                                    Logger.logNpcMessage(ColorUtils.color("&6" + p.getName() + " &7zakupił &6" + name + " &7od &9" + NPCData.getId()));
                                } else {
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                    p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + " &6» &cNiestety nie dogadamy się. Nie posiadasz tyle pieniędzy."));
                                }
                            }
                        } else {
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + " &6» &cNie, tego nie sprzedaję."));
                        }
                        return;
                    }
                    if (e.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
                        if (e.isShiftClick() && e.isRightClick()) {
                            if (shopItem.canSell()) {
                                boolean unlocked = data.getQuestIndex() + 1 >= shopItem.getRequiredQuestLevel() && u.getUserLevel().playerLevel() >= shopItem.getRequiredPlayerLevel();

                                // item 8
                                // 8 x 8 = 64

                                int max = 0;
                                int temp = 0;
                                for (int j = 0; j != 64; j++) {
                                    if (temp >= shopItem.getItem().getMaxStackSize()) {
                                        temp = shopItem.getItem().getMaxStackSize();
                                        max = temp / shopItem.getItem().getAmount();
                                        break;
                                    } else {
                                        temp += shopItem.getItem().getAmount();
                                        max += 1;
                                    }
                                }

                                if (unlocked) {
                                    if (ItemStackUtils.getCountOfMaterial(p, shopItem.getItem().getType()) >= shopItem.getItem().getAmount() * max) {
                                        ItemStackUtils.consumeItem(p, shopItem.getItem().getAmount() * max, shopItem.getItem().getType());
                                        u.addMoney(shopItem.getFinalCost() * max);
                                        String name;
                                        if (shopItem.getItem().getItemMeta().hasDisplayName()) {
                                            name = shopItem.getItem().getItemMeta().getDisplayName();
                                        } else {
                                            name = ChatUtil.materialName(shopItem.getItem().getType());
                                        }
                                        p.sendMessage(ColorUtils.color("&a&l$ &6»  &7Sprzedałeś &6" + name + " x" + shopItem.getItem().getAmount() * max + " &7za &6" + ChatUtil.getMoney(shopItem.getFinalCost() * max)));
                                        //p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + " &6» &c- " + ChatUtil.materialName(shopItem.getItem().getType()) + " x" + shopItem.getItem().getAmount() * max));
                                        //p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + " &6» &a+ " + ChatUtil.getMoney(shopItem.getFinalCost() * max)));
                                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1, 1);
                                        Logger.logNpcMessage(ColorUtils.color("&6" + p.getName() + " &7sprzedał &6" + ChatUtil.materialName(shopItem.getItem().getType()) + " &7do &9" + NPCData.getId()));
                                    } else {
                                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                        p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + " &6» &cChcesz sprzedać coś czego nie masz?"));
                                        return;
                                    }
                                } else {
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                    p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + " &6» &cNiestety, tego nie kupuję."));
                                    return;
                                }
                            }
                        }
                    }
                    if (e.getAction() == InventoryAction.PICKUP_HALF) {
                        if (shopItem.canSell()) {
                            boolean unlocked = data.getQuestIndex() + 1 >= shopItem.getRequiredQuestLevel() && u.getUserLevel().playerLevel() >= shopItem.getRequiredPlayerLevel();
                            if (unlocked) {
                                if (ItemStackUtils.getCountOfMaterial(p, shopItem.getItem().getType()) >= shopItem.getItem().getAmount()) {
                                    ItemStackUtils.consumeItem(p, shopItem.getItem().getAmount(), shopItem.getItem().getType());
                                    u.addMoney(shopItem.getFinalCost());
                                    String name;
                                    if (shopItem.getItem().getItemMeta().hasDisplayName()) {
                                        name = shopItem.getItem().getItemMeta().getDisplayName();
                                    } else {
                                        name = ChatUtil.materialName(shopItem.getItem().getType());
                                    }
                                    p.sendMessage(ColorUtils.color("&a&l$ &6» &7Sprzedałeś &6" + name + " x" + shopItem.getItem().getAmount() + " &7za &6" + ChatUtil.getMoney(shopItem.getFinalCost())));
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_TRADE, 1, 1);
                                    Logger.logNpcMessage(ColorUtils.color("&6" + p.getName() + " &7sprzedał &6" + ChatUtil.materialName(shopItem.getItem().getType()) + " &7do &9" + NPCData.getId()));
                                } else {
                                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                    p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + " &6» &cChcesz sprzedać coś czego nie masz?"));
                                    return;
                                }
                            } else {
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + " &6» &cNiestety, tego nie kupuję."));
                                return;
                            }
                        }
                    }
                }
                // QuestSlot
                questSlot = 49;
            } else {
                // QuestSlot
                questSlot = 4;
            }
            if (e.getSlot() == questSlot) {
                // IF MAX QUEST
                if (data.getQuestIndex() >= NPCData.getQuests().size()) {
                    p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + " &6» &7Niestety nie mam już nic dla Ciebie..."));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_AMBIENT, 1, 1);
                    p.openInventory(getInventoryOfVillager(NPCData, p));
                    return;
                }
                // IF HAS ACTIVE QUEST
                boolean hasQuest = false;
                String otherVillagerName = null;
                for (PlayerNPCData villagers : u.getNPCSData().getNPCSData().values()) {
                    if (villagers.getNpcId().equals(NPCData.getId())) {
                        continue;
                    }
                    if (villagers.isActiveQuest()) {
                        otherVillagerName = this.npcs.get(villagers.getNpcId()).getId();
                        hasQuest = true;
                    }
                }
                if (hasQuest) {
                    p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + " &6» &7Najpierw zakończ zadanie u " + otherVillagerName));
                    p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                    return;
                }
                Quest activeQuest = NPCData.getQuests().get(data.getQuestIndex());
                if (data.isActiveQuest()) {
                    // Jeżeli chce anulować quest
                    if (e.getAction() == InventoryAction.PICKUP_HALF) {
                        for (IQuestItem item : activeQuest.getQuestItems()) {
                            if (item instanceof IQuestItemGive) {
                                if (data.getQuestItemData().get(item.getQuestItemDataId()) > 0) {
                                    ItemStackUtils.addItemStackToPlayer(p, new ItemStack(((IQuestItemGive) item).getMaterial(), data.getQuestItemData().get(item.getQuestItemDataId())));
                                }
                            }
                            if (item instanceof IQuestItemPay) {
                                int money = data.getQuestItemData().get(item.getQuestItemDataId());
                                if (money > 0) {
                                    u.addMoney(money);
                                    p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + " &6» &aZwracam twoje " + ChatUtil.getMoney(money) + ", które wpłaciłeś"));
                                }
                            }
                        }
                        data.setActiveQuest(false);
                        if (data.getQuestItemData() == null) {
                            data.setQuestItemData(new HashMap<>());
                        } else {
                            data.getQuestItemData().clear();
                        }
                        u.UpdateScoreboard();
                        if (!u.hasRank(Rank.Zelazo)) {
                            u.subtractMoney(200);
                            p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + " &6» &c-" + ChatUtil.getMoney(200)));
                        }
                        p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + " &6» &7Szkoda, że się rozmyśliłeś"));
                        p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                        p.openInventory(getInventoryOfVillager(NPCData, p));
                        return;
                    }
                    // Jeżeli chce oddać itemki
                    if (e.getAction() == InventoryAction.PICKUP_ALL) {
                        int haveItem = 0;
                        for (IQuestItem item : activeQuest.getQuestItems()) {
                            if (item instanceof IQuestItemGive) {
                                IQuestItemGive itemGive = (IQuestItemGive) item;
                                int ileJuzWplacil = data.getQuestItemData().get(item.getQuestItemDataId());
                                int ileJestPotrzebne = itemGive.getCount();
                                int ileMa = ItemStackUtils.getCountOfMaterial(p, itemGive.getMaterial());
                                int ileBrakuje = ileJestPotrzebne - ileJuzWplacil;
                                if (ileJuzWplacil == ileJestPotrzebne) {
                                    // jezeli nic nie brakuje do zaplaty
                                    haveItem++;
                                } else {
                                    // jezeli cos brakuje do zaplaty
                                    // zabiera mu pieniadze tyle ile moze i sprawdza czy brakuje nadal
                                    if (ileBrakuje > ileMa) {
                                        ItemStackUtils.consumeItem(p, ileMa, itemGive.getMaterial());
                                        data.getQuestItemData().replace(item.getQuestItemDataId(), ileJuzWplacil, ileJuzWplacil + ileMa);
                                        p.sendMessage(ColorUtils.color("&c&lQ &6» &cPrzyniesiono " + ileMa + " ale brakuje jeszcze " + (ileJestPotrzebne - ileJuzWplacil - ileMa) + "x " + ChatUtil.materialName(itemGive.getMaterial())));
                                        // wplacil tyle ile moze
                                    } else {
                                        ItemStackUtils.consumeItem(p, ileBrakuje, itemGive.getMaterial());
                                        data.getQuestItemData().replace(item.getQuestItemDataId(), ileJuzWplacil, ileJuzWplacil + ileBrakuje);
                                        // wplacil calosc
                                        haveItem++;
                                    }
                                }
                                u.UpdateScoreboard();
                            }
                            if (item instanceof IQuestItemPay) {
                                IQuestItemPay itemGive = (IQuestItemPay) item;
                                int ileJuzWplacil = data.getQuestItemData().get(item.getQuestItemDataId());
                                int ileJestPotrzebne = itemGive.getCount();
                                // TODO DOUBLE
                                int ileMa = (int) u.getMoney();
                                int ileBrakuje = ileJestPotrzebne - ileJuzWplacil;
                                if (ileJuzWplacil == ileJestPotrzebne) {
                                    // jezeli nic nie brakuje do zaplaty
                                    haveItem++;
                                } else {
                                    // jezeli cos brakuje do zaplaty
                                    // zabiera mu pieniadze tyle ile moze i sprawdza czy brakuje nadal
                                    if (ileBrakuje > ileMa) {
                                        u.subtractMoney(ileMa);
                                        data.getQuestItemData().replace(item.getQuestItemDataId(), ileJuzWplacil, ileJuzWplacil + ileMa);
                                        p.sendMessage(ColorUtils.color("&c&lQ &6» &cZapłacono " + ChatUtil.getMoney(ileMa) + " ale brakuje jeszcze " + ChatUtil.getMoney(ileJestPotrzebne - ileJuzWplacil - ileMa)));
                                        // wplacil tyle ile moze
                                    } else {
                                        u.subtractMoney(ileBrakuje);
                                        data.getQuestItemData().replace(item.getQuestItemDataId(), ileJuzWplacil, ileJuzWplacil + ileBrakuje);
                                        // wplacil calosc
                                        haveItem++;
                                    }
                                }
                                u.UpdateScoreboard();
                            }
                            if (item instanceof IQuestItemCraft) {
                                IQuestItemCraft itemCraft = (IQuestItemCraft) item;
                                int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                                if (temp >= itemCraft.getCount()) {
                                    haveItem++;
                                } else {
                                    int count = itemCraft.getCount() - temp;
                                    p.sendMessage(ColorUtils.color("&cMusisz jeszcze wytworzyć " + count + " " + ChatUtil.materialName(itemCraft.getMaterial())));
                                }
                            }
                            if (item instanceof IQuestItemKill) {
                                IQuestItemKill itemKill = (IQuestItemKill) item;
                                int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                                if (temp >= itemKill.getCount()) {
                                    haveItem++;
                                } else {
                                    int count = itemKill.getCount() - temp;
                                    p.sendMessage(ColorUtils.color("&cMusisz jeszcze zabić " + count + " " + ChatUtil.materialName(itemKill.getEntityType())));
                                }
                            }
                            if (item instanceof IQuestItemBreed) {
                                IQuestItemBreed itemKill = (IQuestItemBreed) item;
                                int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                                if (temp >= itemKill.getCount()) {
                                    haveItem++;
                                } else {
                                    int count = itemKill.getCount() - temp;
                                    p.sendMessage(ColorUtils.color("&cMusisz jeszcze rozmnożyć " + count + " " + ChatUtil.materialName(Objects.requireNonNull(itemKill.getEntityType().toEntity()))));
                                }
                            }
                            if (item instanceof IQuestItemFish) {
                                IQuestItemFish itemFish = (IQuestItemFish) item;
                                int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                                if (temp >= itemFish.getCount()) {
                                    haveItem++;
                                } else {
                                    int count = itemFish.getCount() - temp;
                                    p.sendMessage(ColorUtils.color("&cMusisz jeszcze złowić " + count + " " + ChatUtil.materialName(itemFish.getMaterial())));
                                }
                            }
                            if (item instanceof IQuestItemCollect) {
                                IQuestItemCollect itemBreak = (IQuestItemCollect) item;
                                int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                                if (temp >= itemBreak.getCount()) {
                                    haveItem++;
                                } else {
                                    int count = itemBreak.getCount() - temp;
                                    p.sendMessage(ColorUtils.color("&cMusisz jeszcze zebrać " + count + " " + ChatUtil.materialName(itemBreak.getMaterial())));
                                }
                            }
                            if (item instanceof IQuestItemBreak) {
                                IQuestItemBreak itemBreak = (IQuestItemBreak) item;
                                int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                                if (temp >= itemBreak.getCount()) {
                                    haveItem++;
                                } else {
                                    int count = itemBreak.getCount() - temp;
                                    p.sendMessage(ColorUtils.color("&cMusisz jeszcze zniszczyć " + count + " " + ChatUtil.materialName(itemBreak.getMaterial())));
                                }
                            }
                            if (item instanceof IQuestItemFishingRod) {
                                IQuestItemFishingRod itemBreak = (IQuestItemFishingRod) item;
                                int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                                if (temp >= itemBreak.getCount()) {
                                    haveItem++;
                                } else {
                                    int count = itemBreak.getCount() - temp;
                                    p.sendMessage(ColorUtils.color("&cMusisz jeszcze " + count + " razy zarzucić wędkę"));
                                }
                            }
                            if (item instanceof IQuestItemVisit) {
                                IQuestItemVisit itemBreak = (IQuestItemVisit) item;
                                int temp = data.getQuestItemData().get(item.getQuestItemDataId());
                                if (temp >= 1) {
                                    haveItem++;
                                } else {
                                    p.sendMessage(ColorUtils.color("&cNie odwiedziłeś jeszcze " + ChatUtil.materialName(itemBreak.getBiome())));
                                }
                            }
                        }
                        boolean haveItems = haveItem == activeQuest.getQuestItems().size();
                        if (haveItems) {
                            p.sendMessage(" ");
                            p.sendMessage(" ");
                            p.sendMessage(ColorUtils.color("  &eGratulacje odblokowałeś &c" + (data.getQuestIndex() + 2) + " poziom&e " + e.getView().getTitle() + "!"));
                            p.sendMessage(" ");
                            p.sendMessage(" ");
                            final Instant now = Instant.now();
                            questDelay.compute(p.getName(), (uuid, instant) -> {
                                if (instant == null) {
                                    return new NPCDelayData(new IdentityHashMap<>());
                                }
                                int max = 0;
                                switch (u.getRank()) {
                                    case None:
                                        max = 10;
                                        break;
                                    case Zelazo:
                                        max = 6;
                                        break;
                                    case Zloto:
                                        max = 5;
                                        break;
                                    case Diament:
                                        max = 2;
                                        break;
                                    case Emerald:
                                        max = 0;
                                        break;
                                }
                                if (instant.getTimers().get(NPCData.getId()) == null) {
                                    instant.getTimers().put(NPCData.getId(), now.plusSeconds(60 * max));
                                } else {
                                    instant.getTimers().replace(NPCData.getId(), now.plusSeconds(60 * max));
                                }
                                if (max > 0) {
                                    p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + "&6» &7Poczekaj &6" + max + " minut &7 na kolejne zadanie"));
                                }
                                return instant;
                            });
                            spawnFireworks(p.getLocation(), 1);
                            Bukkit.broadcastMessage(ColorUtils.color("  &6> &6" + p.getName() + " &7zakończył zadanie"));
                            Bukkit.broadcastMessage(ColorUtils.color("    &e&l" + activeQuest.getName() + " &7od &9" + NPCData.getId()));
                            for (Player temp : Bukkit.getOnlinePlayers()) {
                                temp.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 2, 1);
                            }
                            p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 2, 1);
                            p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + " &6» &aInteresy z tobą to przyjemność!"));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
                            data.setActiveQuest(false);
                            if (data.getQuestItemData() == null) {
                                data.setQuestItemData(new HashMap<>());
                            } else {
                                data.getQuestItemData().clear();
                            }
                            data.setQuestIndex(data.getQuestIndex() + 1);
                            p.openInventory(getInventoryOfVillager(NPCData, p));
                            for (IQuestReward reward : activeQuest.getRewardItems()) {
                                if (reward instanceof IQuestRewardItemStack) {
                                    ItemStack item = ((IQuestRewardItemStack) reward).item();
                                    if (p.getInventory().firstEmpty() != -1) {
                                        p.getInventory().addItem(item);
                                    } else {
                                        p.getWorld().dropItem(p.getLocation(), item);
                                        p.sendMessage(ColorUtils.color("&cMasz pełny ekwipunek. Więc przedmiot wyleciał z Ciebie"));
                                    }
                                }
                                if (reward instanceof IQuestRewardCustom) {
                                    IQuestRewardCustom custom = (IQuestRewardCustom) reward;
                                    custom.Action(p, u);
                                }
                                if (reward instanceof IQuestRewardMoney) {
                                    u.addMoney(((IQuestRewardMoney) reward).money());
                                }
                                if (reward instanceof IQuestRewardExp) {
                                    //u.addExp(((IQuestRewardExp) reward).exp());
                                }
                            }
                            Logger.logNpcMessage(ColorUtils.color("&6" + p.getName() + " &7zakończył Questa &6" + activeQuest.getName() + " &7od &9" + NPCData.getId()));
                            u.UpdateScoreboard();
                        } else {
                            p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + " &6» &7Nie wykonałeś wszystkich zadań.."));
                            p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                            p.openInventory(getInventoryOfVillager(NPCData, p));
                        }
                        return;
                    }
                } else {
                    AtomicBoolean blocked = new AtomicBoolean(false);
                    final Instant now = Instant.now();
                    questDelay.compute(p.getName(), (uuid, instant) -> {
                        if (instant == null) {
                            return new NPCDelayData(new IdentityHashMap<>());
                        }
                        if (instant.getTimers().get(NPCData.getId()) == null) {
                            instant.getTimers().put(NPCData.getId(), now);
                        } else {
                            if (now.isBefore(instant.getTimers().get(NPCData.getId()))) {
                                p.sendActionBar(ColorUtils.color("&cOdczekaj chwilę miedzy zadaniami"));
                                p.playSound(p.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                                blocked.set(true);
                                return instant;
                            }
                        }
                        // no return
                        return instant;
                    });
                    if (blocked.get()) {
                        return;
                    }
                    // Przyjmuje zadanie
                    if (data.getQuestItemData() == null) {
                        data.setQuestItemData(new HashMap<>());
                    } else {
                        data.getQuestItemData().clear();
                    }
                    data.setActiveQuest(true);
                    for (IQuestItem item : activeQuest.getQuestItems()) {
                        data.getQuestItemData().put(item.getQuestItemDataId(), 0);
                    }
                    p.sendMessage(ColorUtils.color("&9" + NPCData.getId() + " &6» &aDzięki, że przyjąłeś zadanie."));
                    u.UpdateScoreboard();
                    p.openInventory(getInventoryOfVillager(NPCData, p));
                    p.playSound(p.getLocation(), Sound.ENTITY_ITEM_PICKUP, 2, 1);
                }
            }
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void villagerClick(@NotNull NPCRightClickEvent e) {
        Player p = e.getClicker();
        NPC npc = e.getNPC();
        if (Main.getInstance().instanceAntyLogout.inFight(p.getUniqueId())) {
            p.sendMessage(ColorUtils.color("&cPoczekaj aż nie będziesz podczas walki!"));
            return;
        }
        if (npc.getName().equals("Poziom")) {
            e.setCancelled(true);
            p.performCommand("poziom");
            return;
        }
        if (npc.getName().equals("Regulamin")) {
            e.setCancelled(true);
            p.performCommand("regulamin");
            return;
        }
        if (npc.getName().equals("Discord")) {
            e.setCancelled(true);
            p.performCommand("discord");
            return;
        }
        if (npc.getName().equals("Kowal")) {
            e.setCancelled(true);
            p.playSound(p.getLocation(), Sound.BLOCK_ANVIL_USE, 1, 1);
            p.openAnvil(p.getLocation(), true);
            Logger.logPluginMessage(p.getName() + " użył darmowego kowadła.");
            p.sendMessage(ColorUtils.color("&fUżyłeś darmowego kowadła"));
            return;
        }
        if (npc.getName().equals("Rynek")) {
            e.setCancelled(true);
            p.openInventory(Main.getInstance().instanceRynekManager.getRynekInventory(1, p));
            return;
        }
        if (npc.getName().equals("Poradnik")) {
            e.setCancelled(true);
            p.performCommand("poradnik");
            return;
        }
        if (npc.getName().equals("Losowy teleport")) {
            e.setCancelled(true);
            final Instant now = Instant.now();
            commandDelay.compute(p.getUniqueId(), (uuid, instant) -> {
                if (instant != null && now.isBefore(instant)) {
                    p.sendActionBar(ColorUtils.color("&cOdczekaj chwilę miedzy losowym teleportem"));
                    return instant;
                }
                p.playSound(p.getLocation(), Sound.BLOCK_CHEST_OPEN, 1, 0.5f);
                p.playSound(p.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                Location loc = RandomUtils.getRandom(Bukkit.getWorld("world"));
                p.teleport(loc);
                p.sendActionBar(ColorUtils.color("&6x " + loc.getBlockX() + " y " + loc.getBlockY() + " z " + loc.getBlockZ()));
                Logger.logPluginMessage(p.getName() + " użył losowego teleportu.");
                User u = UserManager.getUser(p.getUniqueId());
                int max = 0;
                if (u.getRank() == Rank.None) {
                    max = 90;
                    p.sendMessage(ColorUtils.color("&cNastępny teleport możesz użyć za 1,5 minuty!"));
                }
                return now.plusSeconds(max);
            });
            return;
        }
        if (npc.getName().equals("Spawn")) {
            e.setCancelled(true);
            p.teleport(Main.getInstance().dataConfig.getLocation("spawn"));
            p.sendTitle(ColorUtils.color("&6Wioska"), ColorUtils.color("&eModerrkowo"));
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
            return;
        }
        e.setCancelled(true);
        String key = npc.getName();
        if (npcs.containsKey(key)) {
            NPCData NPCData = npcs.get(key);
            p.openInventory(getInventoryOfVillager(NPCData, p));
            p.playSound(p.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 0.5f, 1);
        } else {
            Logger.logNpcMessage(p.getName() + " próbuje otworzyć " + key + " (NIE ISTNIEJE)");
        }
    }

    //IQuestItemKill
    @EventHandler(priority = EventPriority.MONITOR)
    public void kill(EntityDeathEvent e) {
        if (e.getEntity() instanceof Player) {
            return;
        }
        if (e.getEntity().getKiller() == null) {
            return;
        }
        if (e.isCancelled()) {
            return;
        }
//        if(e.getEntityType().equals(EntityType.ENDERMAN)){
//            Random r = new Random();
//            int game = r.nextInt(100);
//            int mnoznik = 1;//            if(e.getEntity().getKiller().getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.LOOT_BONUS_MOBS)){
//                mnoznik = e.getEntity().getKiller().getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS)+1;
//            }
//            if(game < 10*mnoznik){
//                e.getEntity().getWorld().dropItemNaturally(e.getEntity().getLocation(), CustomItemsManager.getFragmentEnd());
//                e.getEntity().getKiller().sendMessage(ColorUtils.color("&aWypadł rzadki przedmiot"));
//            }
//        }
        try {
            Player p = e.getEntity().getKiller();
            User u = UserManager.getUser(p.getUniqueId());
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
            NPCData villager = npcs.get(data.getNpcId());
            Quest quest = villager.getQuests().get(data.getQuestIndex());
            for (IQuestItem item : quest.getQuestItems()) {
                if (item instanceof IQuestItemKill) {
                    IQuestItemKill craftItem = (IQuestItemKill) item;
                    if (craftItem.getEntityType().equals(e.getEntityType())) {
                        int recipeAmount = 1;
                        int items = data.getQuestItemData().get(craftItem.getQuestItemDataId());
                        int temp = items;
                        temp += recipeAmount;
                        data.getQuestItemData().replace(craftItem.getQuestItemDataId(), items, temp);
                        p.sendMessage(ColorUtils.color("&c&lQ &6» &aZabito &2" + ChatUtil.materialName(e.getEntityType())));
                        u.UpdateScoreboard();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    //IQuestItemBreak
    @EventHandler(priority = EventPriority.MONITOR)
    public void breakBlock(BlockBreakEvent e) {
        if (e.isCancelled()) {
            return;
        }
        try {
            Player p = e.getPlayer();
            User u = UserManager.getUser(p.getUniqueId());
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
            NPCData villager = npcs.get(data.getNpcId());
            Quest quest = villager.getQuests().get(data.getQuestIndex());
            for (IQuestItem item : quest.getQuestItems()) {
                if (item instanceof IQuestItemBreak) {
                    IQuestItemBreak breakItem = (IQuestItemBreak) item;
                    if (e.getBlock().getType().equals(breakItem.getMaterial())) {
                        if (p.getInventory().getItemInMainHand().getItemMeta().hasEnchant(Enchantment.SILK_TOUCH) && breakItem.blockSilk()) {
                            return;
                        }
                        int recipeAmount = 1;
                        int items = data.getQuestItemData().get(breakItem.getQuestItemDataId());
                        int temp = items;
                        temp += recipeAmount;
                        data.getQuestItemData().replace(breakItem.getQuestItemDataId(), items, temp);
                        p.sendMessage(ColorUtils.color("&c&lQ &6» &aWydobyto &2" + ChatUtil.materialName(e.getBlock().getType())));
                        //p.sendMessage(ColorUtils.color("&9" + villager.getId() + " &6> &aWydobyto " + ChatUtil.materialName(e.getBlock().getType())));
                        u.UpdateScoreboard();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    //IQuestItemCraft
    @EventHandler(priority = EventPriority.MONITOR)
    public void craft(CraftItemEvent e) {
        if (e.getInventory().getResult() == null || e.getInventory().getResult().getType().equals(Material.AIR)) {
            return;
        }
        try {
            Player p = (Player) e.getWhoClicked();
            User u = UserManager.getUser(p.getUniqueId());
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
            NPCData villager = npcs.get(data.getNpcId());
            Quest quest = villager.getQuests().get(data.getQuestIndex());
            for (IQuestItem item : quest.getQuestItems()) {
                if (item instanceof IQuestItemCraft) {
                    IQuestItemCraft craftItem = (IQuestItemCraft) item;
                    if (craftItem.getMaterial().equals(e.getInventory().getResult().getType())) {
                        int recipeAmount = e.getInventory().getResult().getAmount();
                        ClickType click = e.getClick();
                        switch (click) {
                            case NUMBER_KEY:
                                // If hotbar slot selected is full, crafting fails (vanilla behavior, even when
                                // items match)
                                if (e.getWhoClicked().getInventory().getItem(e.getHotbarButton()) != null)
                                    recipeAmount = 0;
                                break;

                            case DROP:
                            case CONTROL_DROP:
                                // If we are holding items, craft-via-drop fails (vanilla behavior)
                                ItemStack cursor = e.getCursor();
                                // Apparently, rather than null, an empty cursor is AIR. I don't think that's
                                // intended.
                                if (cursor != null && cursor.getType().equals(Material.AIR)) recipeAmount = 0;
                                break;

                            case SHIFT_RIGHT:
                            case SHIFT_LEFT:
                                if (recipeAmount == 0) {
                                    break;
                                }
                                int maxCraftable = getMaxCraftAmount(e.getInventory());
                                int capacity = fits(e.getInventory().getResult(), e.getView().getBottomInventory());
                                if (capacity < maxCraftable) {
                                    maxCraftable = ((capacity + recipeAmount - 1) / recipeAmount) * recipeAmount;
                                }
                                recipeAmount = maxCraftable;
                                break;
                            default:
                        }
                        int items = data.getQuestItemData().get(craftItem.getQuestItemDataId());
                        int temp = items;
                        temp += recipeAmount;
                        data.getQuestItemData().replace(craftItem.getQuestItemDataId(), items, temp);
                        p.sendMessage(ColorUtils.color("&c&lQ &6» &aWytworzono &2" + recipeAmount + " " + ChatUtil.materialName(e.getInventory().getResult().getType())));
                        u.UpdateScoreboard();
                    }
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
