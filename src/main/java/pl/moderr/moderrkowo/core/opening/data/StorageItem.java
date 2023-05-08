package pl.moderr.moderrkowo.core.opening.data;

public class StorageItem {
    private int amount;
    private final StorageItemType type;
    private final ModerrCaseEnum chestType;
    public StorageItem(int amount, StorageItemType itemType, ModerrCaseEnum chestType){
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
    public StorageItemType getType(){
        return type;
    }
    public ModerrCaseEnum getCase() {
        return chestType;
    }
}
