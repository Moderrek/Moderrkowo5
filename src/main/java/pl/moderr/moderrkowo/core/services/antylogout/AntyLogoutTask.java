package pl.moderr.moderrkowo.core.services.antylogout;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class AntyLogoutTask implements Runnable {

    private final AntyLogoutService manager;

    public AntyLogoutTask(AntyLogoutService manager) {
        this.manager = manager;
    }

    @Override
    public void run() {
        for (UUID uuid : manager.getUUIDs()) {
            AntyLogoutData data = manager.getAntyLogout().get(uuid);
            if (data.getTicks() <= 0) {
                try {
                    Player player = Bukkit.getPlayer(uuid);
                    if (player != null) {
                        final TextComponent component = Component.text("Wyszedłeś z walki.")
                                .color(NamedTextColor.GREEN);
                        player.sendMessage(component);
                        data.getBossBar().removePlayer(player);
                    }
                    manager.getAntyLogout().remove(uuid);
                } catch (Exception ignored) {
                }
                continue;
            }
            data.getBossBar().setProgress((double) data.getTicks() / manager.getAntyLogoutDuration());
            data.setTicks(data.getTicks() - 1);
            manager.getAntyLogout().replace(uuid, manager.getAntyLogout().get(uuid), data);
        }
    }

}
