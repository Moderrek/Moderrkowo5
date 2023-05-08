package pl.moderr.moderrkowo.core.marketplace;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;

import java.time.LocalDateTime;
import java.util.UUID;

public class RynekItem {

    private final UUID owner;
    private final ItemStack item;
    private final int cost;
    private final LocalDateTime expire;

    @Contract(pure = true)
    public RynekItem(UUID owner, ItemStack item, int cost, LocalDateTime expire) {
        this.owner = owner;
        this.item = item;
        this.cost = cost;
        this.expire = expire;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public int getCost() {
        return cost;
    }

    public UUID getOwner() {
        return owner;
    }

    public LocalDateTime getExpire() {
        return expire;
    }
}
