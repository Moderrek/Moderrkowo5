package pl.moderr.moderrkowo.core.mechanics.npc.data.quest;

import org.bukkit.Material;
import org.jetbrains.annotations.Contract;

public class QuestItems {

    private final int count;
    private final Material material;

    @Contract(pure = true)
    public QuestItems(Material material, int count) {
        this.material = material;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public Material getMaterial() {
        return material;
    }
}
