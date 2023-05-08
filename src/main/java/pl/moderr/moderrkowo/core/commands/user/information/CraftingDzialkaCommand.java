package pl.moderr.moderrkowo.core.commands.user.information;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class CraftingDzialkaCommand implements CommandExecutor {


    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(ColorUtils.color("&7https://cdn.discordapp.com/attachments/844544932282761236/844562754622586930/SPOILER_dzialki.png"));
        return false;
    }
}
