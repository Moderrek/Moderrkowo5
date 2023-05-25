package pl.moderr.moderrkowo.core.services.opening.data;

public class StorageItem {
    private final StorageItemType type;
    private final ModerrCaseEnum chestType;
    private int amount;

    public StorageItem(int amount, StorageItemType itemType, ModerrCaseEnum chestType) {
        this.amount = amount;
        this.type = itemType;
        this.chestType = chestType;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public StorageItemType getType() {
        return type;
    }

    public ModerrCaseEnum getCase() {
        return chestType;
    }
}
