package pl.moderr.moderrkowo.core.services.tasks;

import org.bukkit.Bukkit;
import pl.moderr.moderrkowo.core.api.util.ChatUtil;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;

public class RankReminderTask implements Runnable {
    @Override
    public void run() {
        Bukkit.broadcastMessage(" ");
        Bukkit.broadcastMessage(ChatUtil.centerMotdLine("⛃ Moderrkowo ⛃").replace("⛃ Moderrkowo ⛃", ColorUtil.color("&e⛃ &6Moderrkowo &e⛃")));
        Bukkit.broadcastMessage(ChatUtil.centerMotdLine("▪ Tutaj zakupisz rangi i wesprzesz nasz serwer").replace("▪ Tutaj zakupisz rangi i wesprzesz nasz serwer", ColorUtil.color("&8▪ &7▪ Tutaj zakupisz rangi i wesprzesz nasz serwer")));
        Bukkit.broadcastMessage(ChatUtil.centerMotdLine("▪ Zobacz rangi pod /sklep").replace("▪ Zobacz rangi pod /sklep", ColorUtil.color("&8▪ &7Zobacz rangi pod &e/sklep")));
        Bukkit.broadcastMessage(" ");
    }
}
