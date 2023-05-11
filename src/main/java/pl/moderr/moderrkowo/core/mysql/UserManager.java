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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.mechanics.npc.data.data.PlayerNPCSData;
import pl.moderr.moderrkowo.core.ranks.Rank;
import pl.moderr.moderrkowo.core.ranks.RankManager;
import pl.moderr.moderrkowo.core.ranks.StuffRank;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.user.level.UserLevel;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {


    private static final Map<UUID, User> loadedUsers = new ConcurrentHashMap<>();

    @Contract("_ -> new")
    public static @NotNull User getDefaultUser(@NotNull Player p) {
        return new User(p.getUniqueId(), p.getName(), 3000, 0, Rank.None, StuffRank.None, new UserLevel(), new PlayerNPCSData(), new java.sql.Date(Calendar.getInstance().getTime().getTime()), true, p.getStatistic(Statistic.PLAY_ONE_MINUTE), ModerrkowoPlugin.getVersion());
    }

    @Contract(pure = true)
    public static @NotNull Collection<User> getUsers() {
        return loadedUsers.values();
    }

    public static boolean isUserLoaded(UUID uuid) {
        return loadedUsers.containsKey(uuid);
    }

    public static User getUser(UUID uuid) {
        if (!isUserLoaded(uuid)) {
            final String name = Bukkit.getOfflinePlayer(uuid).getName();
            Logger.logDatabaseMessage(MessageFormat.format("{0} nie jest w pamięci podręcznej!", name));
        }
        return loadedUsers.get(uuid);
    }

    public static void loadUser(@NotNull Player p) {
        UUID uuid = p.getUniqueId();
        if (isUserLoaded(uuid)) return;
        User u = null;
        // Try load user
        try {
            if (!ModerrkowoPlugin.getMySQL().getQuery().userExists(uuid)) {
                // Register user
                u = getDefaultUser(p);
                ModerrkowoPlugin.getMySQL().getQuery().insertUser(u);
                // Teleport to spawn
                try {
                    p.teleport(Objects.requireNonNull(ModerrkowoPlugin.getInstance().config.getLocation("spawn.location")));
                } catch (Exception e) {
                    try {
                        p.teleport(Objects.requireNonNull(Bukkit.getWorld("void")).getSpawnLocation());
                    } catch (Exception ignored) {
                    }
                }
                // Starter kit
                p.getInventory().addItem(new ItemStack(Material.STONE_AXE));
                p.getInventory().addItem(new ItemStack(Material.OAK_LOG, 2));
                p.getInventory().addItem(new ItemStack(Material.BREAD, 16));
                p.playSound(p.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
                final Component greet = Component.text()
                        .content("  Powitajcie nowego gracza")
                        .color(NamedTextColor.WHITE)
                        .appendSpace()
                        .append(Component.text(p.getName()).color(NamedTextColor.GREEN))
                        .appendSpace()
                        .append(Component.text("na serwerze!").color(NamedTextColor.WHITE))
                        .build();
                ModerrkowoPlugin.getInstance().getServer().broadcast(greet);
            } else {
                // Load user
                ModerrkowoPlugin.getMySQL().getQuery().updateLastSeen(uuid);
                u = ModerrkowoPlugin.getMySQL().getQuery().getUser(p.getUniqueId());
            }
        } catch (SQLException throwable) {
            throwable.printStackTrace();
        }
        // After load
        if (u != null) {
            // TODO component
            p.setPlayerListName(RankManager.getChat(u.getRank(), u.getStuffRank()) + p.getName());
            if (u.hasRank(Rank.Zelazo)) {
                // TODO component
                Bukkit.broadcastMessage(ColorUtils.color(RankManager.getChat(u.getRank(), u.getStuffRank()) + p.getName() + "&e dołączył"));
            }
            loadedUsers.put(uuid, u);
            if (!u.getVersion().equals(ModerrkowoPlugin.getVersion())) {
                p.sendMessage(Component.text("Podczas twojej nieobecności serwer przeszedł aktualizację.").color(NamedTextColor.YELLOW));
            }
            u.tryUpdateScoreboard();
            u.tryLoadNotifications();
            Logger.logDatabaseMessage("Wczytano gracza");
        }
    }

    public static boolean saveUser(User user) {
        try {
            ModerrkowoPlugin.getMySQL().getQuery().updateUser(user);
            return true;
        } catch (SQLException errorUpdate) {
            errorUpdate.printStackTrace();
            return false;
        }
    }

    public static void unloadUser(@Nullable UUID uuid) {
        if (uuid == null) return;
        if (isUserLoaded(uuid)) {
            final User user = loadedUsers.get(uuid);
            saveUser(user);
            // Server leave message
            if (user.hasRank(Rank.Zelazo)) {
                // TODO component
                Bukkit.broadcastMessage(ColorUtils.color(RankManager.getChat(user.getRank(), user.getStuffRank()) + user.getName() + "&e opuścił"));
            }
            // Remove user from cache
            loadedUsers.remove(uuid);
            Logger.logDatabaseMessage("Odczytano gracza");
        }
    }

}
