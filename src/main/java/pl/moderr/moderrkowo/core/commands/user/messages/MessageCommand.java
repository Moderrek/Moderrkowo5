package pl.moderr.moderrkowo.core.commands.user.messages;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.text.MessageFormat;
import java.util.HashMap;

public class MessageCommand implements CommandExecutor {
    public final static HashMap<Player, Player> lastMessageSender = new HashMap<>();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (sender instanceof Player) {
            Player messageSender = (Player) sender;
            if (args.length < 2) {
                final TextComponent componentUsageHint = Component.text("Użycie: /msg <nick> <treść>")
                        .color(NamedTextColor.RED);
                messageSender.sendMessage(componentUsageHint);
                messageSender.playSound(messageSender.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
            Player messageReceiver = Bukkit.getPlayer(args[0]);
            if (messageReceiver != null) {

                StringBuilder message = new StringBuilder();
                for (int i = 1; i != args.length; i++) {
                    message.append(" ").append(args[i]);
                }

                final String senderName = messageSender.getName();
                final String receiverName = messageReceiver.getName();

                final TextComponent senderComponent = Component.text(MessageFormat.format("Ja -> {0}", receiverName))
                        .color(NamedTextColor.GOLD)
                        .append(
                                Component.text(':')
                                        .color(NamedTextColor.DARK_GRAY))
                        .append(Component.text(message.toString())
                                .color(NamedTextColor.YELLOW));
                messageSender.sendMessage(senderComponent);
                messageSender.playSound(messageSender.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                setLastMessageSender(messageSender, messageReceiver);

                final TextComponent receiverComponent = Component.text(MessageFormat.format("{0} -> Ja", senderName))
                        .color(NamedTextColor.GOLD)
                        .append(
                                Component.text(':')
                                        .color(NamedTextColor.DARK_GRAY))
                        .append(Component.text(message.toString())
                                .color(NamedTextColor.YELLOW))
                        .hoverEvent(HoverEvent.showText(Component.text("Kliknij aby odpowiedzieć").color(NamedTextColor.GREEN)))
                        .clickEvent(ClickEvent.suggestCommand(MessageFormat.format("/msg {0} ", senderName)));
                messageReceiver.sendMessage(receiverComponent);
                messageReceiver.playSound(messageReceiver.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1, 1);
                setLastMessageSender(messageReceiver, messageSender);
            } else {
                final TextComponent componentOffline = Component.text("Gracz jest offline!")
                        .color(NamedTextColor.RED);
                messageSender.sendMessage(componentOffline);
                messageSender.playSound(messageSender.getLocation(), Sound.ENTITY_VILLAGER_NO, 1, 1);
                return false;
            }
        } else {
            sender.sendMessage("Nie jesteś graczem!");
        }
        return false;
    }

    public void setLastMessageSender(Player p1, Player p2) {
        lastMessageSender.remove(p1);
        lastMessageSender.put(p1, p2);
    }
}
