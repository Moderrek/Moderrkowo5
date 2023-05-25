package pl.moderr.moderrkowo.core.services.npc.data.npc;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pl.moderr.moderrkowo.core.services.mysql.UserManager;
import pl.moderr.moderrkowo.core.user.User;

public class NPCShopItemCustom extends NPCShopItem {

    private final NPCShopItemCustomType type;

    public NPCShopItemCustom(ItemStack item, int requiredQuestLevel, int requiredPlayerLevel, String description, double cost, NPCShopItemCustomType type) {
        super(item, requiredQuestLevel, requiredPlayerLevel, description, cost);
        this.type = type;
    }

    @Override
    public void onBuy(Player p) {
        User u = UserManager.getUser(p.getUniqueId());
        /*if(type.equals(NPCShopItemCustomType.KEYZWYKLA)){
            u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.ZWYKLA), false);
        }
        if(type.equals(NPCShopItemCustomType.KEYDNIADZIECKA)){
            u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Key, ModerrCaseEnum.SKAZENIA), false);
        }
        if(type.equals(NPCShopItemCustomType.CHESTDNIADZIECKA)){
            u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.SKAZENIA), false);
        }
        if(type.equals(NPCShopItemCustomType.CHESTZWYKLA)){
            u.getUserChestStorage().addItem(new StorageItem(1, StorageItemType.Chest, ModerrCaseEnum.ZWYKLA), false);
        }*/
        // TODO
    }
}
