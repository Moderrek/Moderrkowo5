package pl.moderr.moderrkowo.core.api.util;

import io.papermc.lib.PaperLib;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;

public class WorldUtil {

    public static void TryLoadWorld(String name) {
        if (Bukkit.getWorld(name) != null) return;
        LoadWorld(name);
    }

    public static World LoadWorld(String name) {
        Logger.logWorldManager(name, "Został wczytany");
        return new WorldCreator(name).environment(World.Environment.NORMAL).createWorld();
    }

    public static void TeleportWorld(String name, Player player) {
        World world = Bukkit.getWorld(name);
        // world equals to null when world doesn't exist
        if (world == null) {
            player.sendMessage(ComponentUtil.coloredText("Nie znaleziono świata " + name, NamedTextColor.YELLOW));
            player.sendMessage(ComponentUtil.coloredText("Tworzenie nowego świata..", NamedTextColor.YELLOW));
            world = LoadWorld(name);
            player.sendMessage(ComponentUtil.coloredText("Pomyślnie stworzono nowy świat.", NamedTextColor.GREEN));
            Logger.logWorldManager(name, "Utworzono nowy świat");
        }
        final Location spawnLocation = world.getSpawnLocation();
        // async teleport. sync teleport can create server tick lag
        PaperLib.teleportAsync(player, spawnLocation).thenAccept(result -> {
            if (!result) {
                player.sendMessage(ComponentUtil.coloredText("Nie udało się przenieść świata!", NamedTextColor.RED));
            }
        });
    }

}
