package pl.moderr.moderrkowo.core.services.mysql;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.api.util.Logger;
import pl.moderr.moderrkowo.core.user.User;
import pl.moderr.moderrkowo.core.user.ranks.Rank;
import pl.moderr.moderrkowo.core.user.ranks.RankManager;

import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {


    private static final Map<UUID, User> loadedUsers = new ConcurrentHashMap<>();

    public static @NotNull Collection<User> getUsers() {
        return loadedUsers.values();
    }

    public static boolean isUserLoaded(UUID uuid) {
        return loadedUsers.containsKey(uuid);
    }

    public static @Nullable User getUser(UUID uuid) {
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
                u = User.CreateDefault(p);
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
            p.setPlayerListName(RankManager.getChat(u.getRank(), u.getStuffRank()) + p.getName());
            if (u.hasRank(Rank.Zelazo)) {
                Bukkit.broadcastMessage(ColorUtil.color(RankManager.getChat(u.getRank(), u.getStuffRank()) + p.getName() + "&e dołączył"));
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

    public static void saveUser(User user) {
        try {
            ModerrkowoPlugin.getMySQL().getQuery().updateUser(user);
        } catch (SQLException errorUpdate) {
            errorUpdate.printStackTrace();
        }
    }

    public static void unloadUser(@Nullable UUID uuid) {
        if (uuid == null) return;
        if (isUserLoaded(uuid)) {
            final User user = loadedUsers.get(uuid);
            saveUser(user);
            // Server leave message
            if (user.hasRank(Rank.Zelazo)) {
                Bukkit.broadcastMessage(ColorUtil.color(RankManager.getChat(user.getRank(), user.getStuffRank()) + user.getName() + "&e opuścił"));
            }
            // Remove user from cache
            loadedUsers.remove(uuid);
            Logger.logDatabaseMessage("Odczytano gracza");
        }
    }

}
