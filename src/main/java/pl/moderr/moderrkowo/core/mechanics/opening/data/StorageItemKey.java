package pl.moderr.moderrkowo.core.mechanics.opening.data;

public class StorageItemKey {
    private final StorageItemType itemType;
    private final ModerrCaseEnum chestType;

    public StorageItemKey(StorageItemType itemType, ModerrCaseEnum chestType) {
        this.itemType = itemType;
        this.chestType = chestType;
    }

    public StorageItemType getItemType() {
        return itemType;
    }

    public ModerrCaseEnum getChestType() {
        return chestType;
    }

    @Override
    public String toString() {
        return itemType.toString() + "|" + chestType.toString();
    }
}
