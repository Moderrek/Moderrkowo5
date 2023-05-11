package pl.moderr.moderrkowo.core.mechanics.npc.data.tasks;

import org.bukkit.block.Biome;

public interface IQuestItemVisit extends IQuestItem {
    Biome getBiome();

    @Override
    default String getQuestItemPrefix() {
        return "Odwiedź";
    }
}
