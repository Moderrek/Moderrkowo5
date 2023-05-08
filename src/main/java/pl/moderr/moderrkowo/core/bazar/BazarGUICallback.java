package pl.moderr.moderrkowo.core.bazar;

import lombok.Data;
import org.bukkit.entity.Player;
import pl.moderr.moderrkowo.core.mysql.User;

@Data
public class BazarGUICallback implements GUICallback{

    private final BazarManager manager;
    private final User user;
    private final Player player;

    @Override
    public void onOpen() {

    }

    @Override
    public void onClose() {

    }

    @Override
    public void onLeftClick(int slot) {
        if(slot >= 0 && slot < ItemCategory.values().length){
            player.closeInventory();
            manager.openInventory(user, player, ItemCategory.values()[slot]);
        }
    }

    @Override
    public void onRightClick(int slot) {

    }
}
