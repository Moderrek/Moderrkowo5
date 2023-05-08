package pl.moderr.moderrkowo.core.commands.user;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.moderr.moderrkowo.core.Main;
import pl.moderr.moderrkowo.core.npc.data.npc.NPCData;
import pl.moderr.moderrkowo.core.utils.ColorUtils;

public class ListaNPC implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        for (NPCData npc : Main.getInstance().NPCManager.npcs.values()) {
            sender.sendMessage(ColorUtils.color(" &6> &9" + npc.getId() + "\n   &7Posiada zadania: " + czyPosiadaZadania(npc) + "\n" + "   &7Posiada sklep: " + czyPosiadaSklep(npc)));
        }
        return false;
    }

    private String czyPosiadaZadania(NPCData npc) {
        if (npc.getQuests() == null || npc.getQuests().size() == 0) {
            return ColorUtils.color("&8Nie");
        } else {
            return ColorUtils.color("&cTak " + npc.getQuests().size() + " zadań");
        }
    }

    private String czyPosiadaSklep(NPCData npc) {
        if (npc.getShopItems() == null || npc.getShopItems().size() == 0) {
            return ColorUtils.color("&cNie");
        } else {
            return ColorUtils.color("&aTak &7[&b" + npc.getShopItems().size() + " przedmiotów&7]");
        }
    }
}
