package pl.moderr.moderrkowo.core.services.npc.data.npc;

import org.jetbrains.annotations.Contract;
import pl.moderr.moderrkowo.core.services.npc.data.quest.Quest;

import java.util.ArrayList;

public class NPCData {

    private final String npcName;
    private final ArrayList<Quest> quests;
    private boolean shop = false;
    private ArrayList<NPCShopItem> shopItems;

    @Contract(pure = true)
    public NPCData(String npcName, ArrayList<Quest> quests) {
        this.npcName = npcName;
        this.quests = quests;
    }

    @Contract(pure = true)
    public NPCData(String npcName, ArrayList<Quest> quests, ArrayList<NPCShopItem> items) {
        this.npcName = npcName;
        this.quests = quests;
        this.shopItems = items;
        shop = true;
    }

    public ArrayList<Quest> getQuests() {
        return quests;
    }

    public String getId() {
        return npcName;
    }

    public ArrayList<NPCShopItem> getShopItems() {
        return shopItems;
    }

    public void setShopItems(ArrayList<NPCShopItem> shopItems) {
        this.shopItems = shopItems;
    }

    public boolean isShop() {
        return shop;
    }

    public void setShop(boolean shop) {
        this.shop = shop;
    }


}
