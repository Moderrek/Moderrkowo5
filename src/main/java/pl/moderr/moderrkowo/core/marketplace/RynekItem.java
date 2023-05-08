package pl.moderr.moderrkowo.core.marketplace;

import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class RynekItem {

    private final UUID owner;
    private final ItemStack item;
    private final int cost;
    private final LocalDateTime expire;

    public ItemStack getItem() {
        return item.clone();
    }

}
