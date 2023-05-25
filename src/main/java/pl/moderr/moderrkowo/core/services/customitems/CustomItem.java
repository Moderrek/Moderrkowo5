package pl.moderr.moderrkowo.core.services.customitems;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public interface CustomItem {

    void onEat(Player player, ItemStack itemStack);

    void onClick(Player player, ItemStack itemStack);

}
