package pl.moderr.moderrkowo.core.mysql;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.npc.data.data.PlayerNPCSData;
import pl.moderr.moderrkowo.core.ranks.Rank;
import pl.moderr.moderrkowo.core.ranks.RankManager;
import pl.moderr.moderrkowo.core.ranks.StuffRank;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.sql.SQLException;
import java.util.*;

public class UserManager {

    public static Map<UUID, User> loadedUsers = new HashMap<>();

    @Contract(pure = true)
    public static boolean isUserLoaded(UUID uuid) {
        return loadedUsers.containsKey(uuid);
    }

    public static User getUser(UUID uuid) {
        if (!isUserLoaded(uuid)) {
            /*if (Bukkit.getPlayer(uuid) != null && Objects.requireNonNull(Bukkit.getPlayer(uuid)).isOnline()) {
                loadUser(Objects.requireNonNull(Bukkit.getPlayer(uuid)));
            }*/
            Logger.logPluginMessage("WYSTĄPIŁ BŁĄD UŻYTKOWNIK NIE JEST WCZYTANY, " + Bukkit.getOfflinePlayer(uuid).getName());
        }
        return loadedUsers.get(uuid);
    }

    public static User getDefaultUser(Player p) {
        return new User(p.getUniqueId(), p.getName(), 3000, 0, Rank.None, StuffRank.None, new UserLevel(), new PlayerNPCSData(), new java.sql.Date(Calendar.getInstance().getTime().getTime()), true, p.getStatistic(Statistic.PLAY_ONE_MINUTE), Main.getVersion());
    }

    public static void loadUser(Player p) {
        UUID uuid = p.getUniqueId();
        if (isUserLoaded(uuid)) {
            return;
        }
        User u = null;
        try {
            if (!Main.getMySQL().getQuery().userExists(uuid)) {
                u = getDefaultUser(p);
                Main.getMySQL().getQuery().insertUser(u);
                try{
                    p.teleport(Objects.requireNonNull(Main.getInstance().config.getLocation("spawn.location")));
                }catch (Exception e){
                    try{
                        p.teleport(Objects.requireNonNull(Bukkit.getWorld("void")).getSpawnLocation());
                    }catch (Exception ignored){}
                }

                p.getInventory().addItem(new ItemStack(Material.STONE_AXE));
                p.getInventory().addItem(new ItemStack(Material.OAK_LOG, 2));
                p.getInventory().addItem(new ItemStack(Material.BREAD, 16));
                p.sendMessage(ColorUtils.color("&aZostałeś pomyślnie zarejestrowany!"));
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                Bukkit.broadcastMessage(ColorUtils.color("  &fPowitajcie nowego gracza &a" + p.getName() + " &fna serwerze!"));
            } else {
                Main.getMySQL().getQuery().updateLastSeen(uuid);
                u = Main.getMySQL().getQuery().getUser(p.getUniqueId());
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        if (u != null) {
            p.setPlayerListName(RankManager.getChat(u.getRank(), u.getStuffRank()) + p.getName());
            if (u.hasRank(Rank.Zelazo)) {
                Bukkit.broadcastMessage(ColorUtils.color(RankManager.getChat(u.getRank(), u.getStuffRank()) + p.getName() + "&e dołączył"));
            }
            loadedUsers.put(uuid, u);
            if (!u.getVersion().equals(Main.getVersion())) {
                // TODO wyswielt książke
                //p.openBook(Main.changeLogItem());
                p.sendMessage(Component.text("Podczas twojej nieobecności serwer przeszedł aktualizację.").color(NamedTextColor.YELLOW));
            }
            u.LoadNotifications();
//            try {
//                Main.getInstance().sidebar.addViewer(p);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            Logger.logDatabaseMessage("Wczytano gracza");
        }
    }

    public static void saveUser(User user){
        try {
            Main.getMySQL().getQuery().updateUser(user);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    public static void unloadUser(UUID uuid) {
        if (isUserLoaded(uuid)) {
            User u = loadedUsers.get(uuid);
            try {
                Main.getMySQL().getQuery().updateUser(u);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
            if (u.hasRank(Rank.Zelazo)) {
                Bukkit.broadcastMessage(ColorUtils.color(RankManager.getChat(u.getRank(), u.getStuffRank()) + u.getName() + "&e opuścił"));
            }
            loadedUsers.remove(uuid);
            Logger.logDatabaseMessage("Odczytano gracza");
        } else {
            Logger.logDatabaseMessage("Gracz nie byłwczytany");
        }
//        try{
//            Main.getInstance().sidebar.removeViewer(Objects.requireNonNull(Bukkit.getPlayer(uuid)));
//        }catch (Exception ignored){}
    }

}
