package pl.moderr.moderrkowo.core.npc;

import pl.moderr.moderrkowo.core.cuboids.CuboidsManager;
import pl.moderr.moderrkowo.core.npc.data.npc.NPCData;
import pl.moderr.moderrkowo.core.npc.data.npc.NPCShopItem;

import java.util.ArrayList;
import java.util.Arrays;

public class DzialkaNPC extends NPCData {

    public DzialkaNPC() {
        super("Dzialka", new ArrayList<>(), (ArrayList<NPCShopItem>) Arrays.asList(new NPCShopItem(CuboidsManager.getCuboidItem(1),0,0,"",1000)));
    }
}
