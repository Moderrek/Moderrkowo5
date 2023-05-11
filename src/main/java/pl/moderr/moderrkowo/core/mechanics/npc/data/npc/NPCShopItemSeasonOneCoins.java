package pl.moderr.moderrkowo.core.mechanics.npc.data.npc;

import org.bukkit.inventory.ItemStack;

public class NPCShopItemSeasonOneCoins extends NPCShopItem {


    public NPCShopItemSeasonOneCoins(ItemStack item, int requiredQuestLevel, int requiredPlayerLevel, String description, int cost) {
        super(item, requiredQuestLevel, requiredPlayerLevel, description, cost);
    }

    @Override
    public boolean buyByCoins() {
        return true;
    }
}
