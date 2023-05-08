package pl.moderr.moderrkowo.core.worldmanager;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.WorldCreator;
import org.bukkit.entity.Player;
import pl.moderr.moderrkowo.core.utils.Logger;

public class WorldManager {

    public static void TryLoadWorld(String name) {
        if (Bukkit.getWorld(name) == null) {
            LoadWorld(name);
        }
    }

    public static World LoadWorld(String name) {
        Logger.logWorldManager(name, "Został wczytany");
        return new WorldCreator(name).environment(World.Environment.NORMAL).createWorld();
    }

    public static void TeleportWorld(String name, Player player) {
        World world = Bukkit.getWorld(name);
        if (world == null) {
            player.sendMessage(Component.text("Nie znaleziono świata " + name).color(NamedTextColor.YELLOW));
            player.sendMessage(Component.text("Tworzenie nowego świata..").color(NamedTextColor.YELLOW));
            world = LoadWorld(name);
            player.sendMessage(Component.text("Pomyślnie stworzono nowy świat.").color(NamedTextColor.GREEN));
            Logger.logWorldManager(name, "Utworzono nowy świat");
        }
        player.teleport(world.getSpawnLocation());
    }

}
