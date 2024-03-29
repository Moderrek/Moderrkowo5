package pl.moderr.moderrkowo.core.user.ranks;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.api.util.ChatUtil;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;

import java.util.ArrayList;

public class RankManager {
    public static ChatColor getRankColor(Rank rank) {
        switch (rank) {
            case Zelazo:
                return ChatColor.WHITE;
            case Zloto:
                return ChatColor.GOLD;
            case Diament:
                return ChatColor.AQUA;
            case Emerald:
                return ChatColor.GREEN;
            default:
                return ChatColor.RED;
        }
    }

    public static String getPreNick(Rank rank, StuffRank stuffRank, boolean space) {
        String s = "";
        switch (rank) {
            case None:
                return ColorUtil.color("&7");
            case Zelazo:
                s = ColorUtil.color("&7");
                break;
            case Zloto:
                s = ColorUtil.color("&e");
                break;
            case Diament:
                s = ColorUtil.color("&6");
                break;
            case Emerald:
                s = ColorUtil.color("&2");
                break;
        }
        if (stuffRank.equals(StuffRank.Administrator) || stuffRank.equals(StuffRank.Moderator)) {
            s = ColorUtil.color("&6");
        }
        if (space && !s.equals("")) {
            return s + " ";
        } else {
            return s;
        }
    }

    public static String getRankNameShort(Rank rank, boolean space) {
        String s = "";
        switch (rank) {
            case None:
                return "";
            case Zelazo:
                s = ColorUtil.color("&f&lŻ");
                break;
            case Zloto:
                s = ColorUtil.color("&6&lZ");
                break;
            case Diament:
                s = ColorUtil.color("&b&lD");
                break;
            case Emerald:
                s = ColorUtil.color("&a&lE");
                break;
        }
        if (space && !s.equals("")) {
            return s + " ";
        } else {
            return s;
        }
    }

    public static String getRankName(Rank rank, boolean space) {
        String s = "";
        switch (rank) {
            case None:
                return "";
            case Zelazo:
                s = ColorUtil.color("&f&lŻELAZO");
                break;
            case Zloto:
                s = ColorUtil.color("&6&lZŁOTO");
                break;
            case Diament:
                s = ColorUtil.color("&b&lDIAMENT");
                break;
            case Emerald:
                s = ColorUtil.color("&a&lEMERALD");
        }
        if (space && !s.equals("")) {
            return s + " ";
        } else {
            return s;
        }
    }

    public static String getStuffRankNameShort(StuffRank rank, boolean space) {
        String s = "";
        switch (rank) {
            case None:
                return "";
            case Tester:
                s = ColorUtil.color("&a&lT");
                break;
            case CommunityManager:
                s = ColorUtil.color("&9&lCM");
                break;
            case Moderator:
                s = ColorUtil.color("&2&lMOD");
                break;
            case Administrator:
                s = ColorUtil.color("&c&lADM");
                break;
            case YT:
                s = ColorUtil.color("&c&lYT");
                break;
        }
        if (space && !s.equals("")) {
            return s + " ";
        } else {
            return s;
        }
    }

    public static String getStuffRankName(StuffRank rank, boolean space) {
        String s = "";
        switch (rank) {
            case None:
                return "";
            case Tester:
                s = ColorUtil.color("&a&lTESTER");
                break;
            case CommunityManager:
                s = ColorUtil.color("&9&lCommunityManager");
                break;
            case Moderator:
                s = ColorUtil.color("&2&lMODERATOR");
                break;
            case Administrator:
                s = ColorUtil.color("&c&lADMIN");
                break;
            case YT:
                s = ColorUtil.color("&c&lYOUTUBER");
                break;
        }
        if (space && !s.equals("")) {
            return s + " ";
        } else {
            return s;
        }
    }

    public static String getChat(Rank rank, StuffRank stuffRank) {
        if (stuffRank.equals(StuffRank.None)) {
            return getRankNameShort(rank, true) + getPreNick(rank, stuffRank, false);
        } else {
            return getStuffRankNameShort(stuffRank, true) + getRankNameShort(rank, true) + getPreNick(rank, stuffRank, false);
        }
    }

    public static Material getMaterial(Rank rank) {
        switch (rank) {
            case Zelazo:
                return Material.IRON_BLOCK;
            case Zloto:
                return Material.GOLD_BLOCK;
            case Diament:
                return Material.DIAMOND_BLOCK;
            case Emerald:
                return Material.EMERALD_BLOCK;
            default:
                return Material.DIRT;
        }
    }

    @Contract(pure = true)
    public static @NotNull String getMessageColor(@NotNull Rank rank) {
        switch (rank) {
            case Zelazo:
            case Zloto:
            case Diament:
            case Emerald:
                return "&f";
            default:
                return "&7";
        }
    }

    @Contract(pure = true)
    public static int getPriority(@NotNull Rank rank) {
        switch (rank) {
            case Zelazo:
                return 1;
            case Zloto:
                return 2;
            case Diament:
                return 3;
            case Emerald:
                return 4;
            default:
                return 0;
        }
    }

    public static double getCost(Rank rank) {
        switch (rank) {
            case Zelazo:
                return 5;
            case Zloto:
                return 10;
            case Diament:
                return 25;
            case Emerald:
                return 50;
            default:
                return 0;
        }
    }

    public static ArrayList<String> getBonus(Rank rank) {
        switch (rank) {
            case Zelazo:
                return new ArrayList<>() {
                    {
                        add("Jedno razowe +" + ChatUtil.formatMoney(5000)); // done
                        add("Brak opłaty przy wystawianiu na rynku"); // done
                        add("Większy limit przedmiotów na rynku (15 -> 20)"); // done
                        add("Brak oczekiwania na RTP"); // done
                        add("Wyróżnienie nad głową, na chacie, na TAB"); // done
                        add("Komunikat przy wejściu i wyjściu na serwer"); // done
                        add("Szybsze pisanie na chacie 1.5s -> 0.7s"); // done
                        add("Szybsze używanie komend 3s -> 2s"); // done
                        add("Mniejszy delay na zadania 10m -> 6m"); // done
                        add("Mnożnik x1,5 otrzymanego doświadczenia"); // done
                        add("Możliwość posiadania 2 domów"); // done
                        add("2x większy dochód za czas gry"); // done
                        add("Brak opłaty za anulowania zadania"); // done
                        add("Możliwość używania ustawień działki"); // done
                    }
                };
            case Zloto:
                return new ArrayList<>() {
                    {
                        add("Jedno razowe +" + ChatUtil.formatMoney(10000)); // done
                        add("Brak opłaty przy wystawianiu na rynku"); // done
                        add("Większy limit przedmiotów na rynku (15 -> 25)"); // done
                        add("Brak oczekiwania na RTP"); // done
                        add("Wyróżnienie nad głową, na chacie, na TAB"); // done
                        add("Komunikat przy wejściu i wyjściu na serwer"); // done
                        add("Szybsze pisanie na chacie 1.5s -> 0.5s"); // done
                        add("Szybsze używanie komend 3s -> 2s"); // done
                        add("Mniejszy delay na zadania 10m -> 5m"); // done
                        add("Mnożnik x1,5 otrzymanego doświadczenia"); // done
                        add("Możliwość posiadania 2 domów"); // done
                        add("3x większy dochód za czas gry"); // done
                        add("Brak opłaty za anulowania zadania"); // done
                        add("Możliwość używania ustawień działki"); // done
                    }
                };
            case Diament:
                return new ArrayList<>() {
                    {
                        add("Jedno razowe +" + ChatUtil.formatMoney(50000)); // done
                        add("Możliwość posiadania 2 działek na świat"); // done
                        add("Brak opłaty przy wystawianiu na rynku"); // done
                        add("Większy limit przedmiotów na rynku (15 -> 30)"); // done
                        add("Brak oczekiwania na RTP"); // done
                        add("Wyróżnienie nad głową, na chacie, na TAB"); // done
                        add("Komunikat przy wejściu i wyjściu na serwer"); // done
                        add("Szybsze pisanie na chacie 1.5s -> 0.3s"); // done
                        add("Szybsze używanie komend 3s -> 1s"); // done
                        add("Mniejszy delay na zadania 10m -> 2m"); // done
                        add("Mnożnik x2 otrzymanego doświadczenia"); // done
                        add("Możliwość posiadania 4 domów"); // done
                        add("5x większy dochód za czas gry"); // done
                        add("Brak opłaty za anulowania zadania"); // done
                        add("Możliwość używania Enderchesta /enderchest");
                        add("Pisanie kolorami na chacie"); // done
                        add("Możliwość używania ustawień działki"); // done
                    }
                };
            case Emerald:
                return new ArrayList<>() {
                    {
                        add("Jedno razowe +" + ChatUtil.formatMoney(100000));
                        add("Możliwość posiadania 3 działek na świat");
                        add("Brak opłaty przy wystawianiu na rynku");
                        add("Większy limit przedmiotów na rynku (15 -> 50)");
                        add("Brak oczekiwania na RTP");
                        add("Wyróżnienie nad głową, na chacie, na TAB");
                        add("Komunikat przy wejściu i wyjściu na serwer");
                        add("Szybsze pisanie na chacie 1.5s -> 0.2s");
                        add("Szybsze używanie komend 3s -> 0.5s");
                        add("Mniejszy delay na zadania 10m -> brak");
                        add("Mnożnik x3 otrzymanego doświadczenia");
                        add("Możliwość posiadania 6 domów");
                        add("8x większy dochód za czas gry");
                        add("Brak opłaty za anulowania zadania");
                        add("Możliwość używania Enderchesta /enderchest");
                        add("Możliwość używania craftingu /crafting");
                        add("Pisanie kolorami R, G, B i gradientem na chacie");
                    }
                };
            default:
                return new ArrayList<>() {
                    {
                        add("Brak");
                    }
                };
        }
    }
}
