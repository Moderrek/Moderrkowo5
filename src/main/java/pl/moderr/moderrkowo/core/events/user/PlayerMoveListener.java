package pl.moderr.moderrkowo.core.events.user;

import org.bukkit.Sound;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import pl.moderr.moderrkowo.core.ModerrkowoPlugin;
import pl.moderr.moderrkowo.core.api.util.ChatUtil;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.services.mysql.UserManager;
import pl.moderr.moderrkowo.core.services.npc.data.data.PlayerNPCData;
import pl.moderr.moderrkowo.core.services.npc.data.npc.NPCData;
import pl.moderr.moderrkowo.core.services.npc.data.quest.Quest;
import pl.moderr.moderrkowo.core.services.npc.data.tasks.IQuestItem;
import pl.moderr.moderrkowo.core.services.npc.data.tasks.IQuestItemVisit;
import pl.moderr.moderrkowo.core.user.User;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void move(PlayerMoveEvent e) {
        if (e.isCancelled()) {
            return;
        }
        if (e.getTo().getWorld().getName().equals("spawn")) {
            return;
        }
        try {
            User u = UserManager.getUser(e.getPlayer().getUniqueId());
            PlayerNPCData data = null;
            for (PlayerNPCData villagers : u.getQuestData().getNPCSData().values()) {
                if (villagers.isActiveQuest()) {
                    data = villagers;
                    break;
                }
            }
            if (data == null) {
                return;
            }
            NPCData villager = ModerrkowoPlugin.getInstance().getNpc().npcs.get(data.getNpcId());
            Quest quest = villager.getQuests().get(data.getQuestIndex());
            for (IQuestItem item : quest.getQuestItems()) {
                if (item instanceof IQuestItemVisit) {
                    IQuestItemVisit craftItem = (IQuestItemVisit) item;
                    if (data.getQuestItemData().get(craftItem.getQuestItemDataId()) >= 1) {
                        return;
                    }
                    Biome biome = e.getTo().getWorld().getBiome(e.getTo().getBlockX(), e.getTo().getBlockY(), e.getTo().getBlockZ());
                    if (craftItem.getBiome().getKey().asString().equals(biome.getKey().asString())) {
                        data.getQuestItemData().replace(craftItem.getQuestItemDataId(), data.getQuestItemData().get(craftItem.getQuestItemDataId()), 1);
                        e.getPlayer().sendMessage(ColorUtil.color("&c&lQ &6» &aOdwiedzono &2" + ChatUtil.materialName(biome)));
                        e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.ENTITY_VILLAGER_CELEBRATE, 1, 1);
                        e.getPlayer().sendTitle(ColorUtil.color("&aOdwiedzono"), ColorUtil.color("&2" + ChatUtil.materialName(biome)));
                        u.UpdateScoreboard();
                    }
                }
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
        }
    }

}
