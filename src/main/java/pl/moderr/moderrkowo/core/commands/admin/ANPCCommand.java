package pl.moderr.moderrkowo.core.commands.admin;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.mechanics.npc.data.npc.NPCData;
import pl.moderr.moderrkowo.core.utils.ColorUtils;
import pl.moderr.moderrkowo.core.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class ANPCCommand implements CommandExecutor, TabCompleter, Listener {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (sender instanceof Player) {
            Player p = (Player) sender;
            NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, Logger.getMessage(args, 0, true));
            npc.data().set(ModerrkowoPlugin.getInstance().getNpc().NpcIdKey, Logger.getMessage(args, 0, true));
            npc.spawn(p.getLocation());
            npc.setProtected(true);
            Logger.logNpcMessage(ColorUtils.color("&6" + p.getName() + " &7postawił nowego Villager'a &8(&f" + Logger.getMessage(args, 0, true) + "&8)"));
        }
        return false;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
        ArrayList<String> list = new ArrayList<>();
        list.add("Losowy teleport");
        list.add("Kowal");
        for (NPCData data : ModerrkowoPlugin.getInstance().getNpc().npcs.values()) {
            list.add(data.getId());
        }
        return list;
    }

}
