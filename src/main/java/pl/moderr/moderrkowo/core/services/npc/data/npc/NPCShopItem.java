package pl.moderr.moderrkowo.core.services.npc.data.npc;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Contract;

public class NPCShopItem {

    private final int requiredQuestLevel;
    private final int requiredPlayerLevel;
    private final ItemStack item;
    private final String description;
    private final double cost;
    private boolean canSell;
    private double sellCost;
    private boolean boostedSell;
    private boolean custom;

    @Contract(pure = true)
    public NPCShopItem(ItemStack item, int requiredQuestLevel, int requiredPlayerLevel, String description, double cost) {
        this.item = item;
        this.requiredQuestLevel = requiredQuestLevel;
        this.requiredPlayerLevel = requiredPlayerLevel;
        this.description = description;
        this.cost = cost;
    }

    @Contract(pure = true)
    public NPCShopItem(ItemStack item, int requiredQuestLevel, int requiredPlayerLevel, String description, double cost, double sellCost, boolean boostedSell) {
        this.item = item;
        this.requiredQuestLevel = requiredQuestLevel;
        this.requiredPlayerLevel = requiredPlayerLevel;
        this.description = description;
        this.cost = cost;
        this.sellCost = sellCost;
        this.canSell = true;
        this.boostedSell = boostedSell;
    }

    public ItemStack getItem() {
        return item.clone();
    }

    public String getDescription() {
        return description;
    }

    public int getRequiredQuestLevel() {
        return requiredQuestLevel;
    }

    public double getCost() {
        return cost;
    }

    public boolean canSell() {
        return canSell;
    }

    public double getSellCost() {
        return sellCost;
    }

    public double getFinalCost() {
        if (!boostedSell) {
            return getSellCost();
        }
        double temp = getSellCost();
        temp = temp * 1.5d;
        if (temp > cost) {
            if (cost == 0) {
                return temp;
            } else {
                temp = cost;
            }
        }
        return temp;
    }

    public int getRequiredPlayerLevel() {
        return requiredPlayerLevel;
    }

    public boolean isBoostedSell() {
        return boostedSell;
    }

    public void setBoostedSell(boolean boostedSell) {
        this.boostedSell = boostedSell;
    }

    public void onBuy(Player p) {
        if (p.getInventory().firstEmpty() != -1) {
            p.getInventory().addItem(getItem().clone());
        } else {
            p.getWorld().dropItem(p.getLocation(), getItem().clone());
        }
    }

    public boolean buyByCoins() {
        return false;
    }

    public boolean isCustom() {
        return custom;
    }

    public void setCustom(boolean custom) {
        this.custom = custom;
    }
}
