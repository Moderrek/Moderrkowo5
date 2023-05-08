package pl.moderr.moderrkowo.core.opening.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.jetbrains.annotations.Contract;
import pl.moderr.moderrkowo.core.utils.Logger;

import javax.annotation.Nullable;
import java.util.HashMap;

public class UserChestStorage {
    public HashMap<String, StorageItem> userStorage;
    @Contract(pure = true)
    public UserChestStorage(HashMap<String, StorageItem> userStorage){
        this.userStorage = userStorage;
    }
    public UserChestStorage(String json) {
        Gson gson = new Gson();
        userStorage = gson.fromJson(json, new TypeToken<HashMap<String, StorageItem>>(){}.getType());
    }
    public HashMap<String, StorageItem> getUserStorage() {
        return userStorage;
    }
    public void setUserStorage(HashMap<String, StorageItem> userStorage) {
        this.userStorage = userStorage;
    }
    public void addItem(StorageItem item, boolean message){
        if(item.getType().equals(StorageItemType.Chest)){
            addChest(item, message);
            return;
        }
        if(item.getType().equals(StorageItemType.Key)){
            addKey(item, message);
            return;
        }
        Logger.logCaseMessage("Nie mozna dodac surowego przedmiotu");
    }
    public int getAmountOfItem(StorageItemKey key){
        if(hasItem(key, 1)){
            return getItem(key).getAmount();
        }else{
            return 0;
        }
    }
    public void addChest(StorageItem chest, boolean message){
        StorageItemKey key = new StorageItemKey(chest.getType(), chest.getCase());
        if(userStorage.containsKey(key.toString())){
            StorageItem keyItem = userStorage.get(key.toString());
            keyItem.setAmount(keyItem.getAmount()+chest.getAmount());
            userStorage.replace(key.toString(), keyItem);
        }else{
            userStorage.put(key.toString(), chest);
        }
    }
    public void subtractChest(StorageItemKey key, int amount){
        StorageItem item = getItem(key);
        if(item != null){
            item.setAmount(item.getAmount()-amount);
            userStorage.replace(key.toString(), item);
            if(item.getAmount() <= 0){
                userStorage.remove(key.toString(), item);
            }
        }
    }
    public boolean hasChest(StorageItemKey key, int amount){
        StorageItem item = getItem(key);
        if(item == null){
            return false;
        }
        return item.getAmount() >= amount;
    }
    public void addKey(StorageItem item, boolean message){
        StorageItemKey key = new StorageItemKey(item.getType(), item.getCase());
        if(userStorage.containsKey(key.toString())){
            StorageItem keyItem = userStorage.get(key.toString());
            keyItem.setAmount(keyItem.getAmount()+item.getAmount());
            userStorage.replace(key.toString(), keyItem);
        }else{
            userStorage.put(key.toString(), item);
        }
    }
    public void subtractKey(StorageItemKey key, int amount){
        StorageItem item = getItem(key);
        if(item != null){
            item.setAmount(item.getAmount()-amount);
            userStorage.replace(key.toString(), item);
            if(item.getAmount() <= 0){
                userStorage.remove(key.toString(), item);
            }
        }
    }
    public boolean hasKey(StorageItemKey key, int amount){
        StorageItem item = getItem(key);
        if(item == null){
            return false;
        }
        return item.getAmount() >= amount;
    }
    public boolean hasItem(StorageItemKey key, int amount){
        StorageItem item = getItem(key);
        if(item == null){
            return false;
        }
        return item.getAmount() >= amount;
    }
    @Nullable
    public StorageItem getItem(StorageItemType itemType, ModerrCaseEnum chestType){
        return userStorage.get(new StorageItemKey(itemType, chestType).toString());
    }
    @Nullable
    public StorageItem getItem(StorageItemKey key){
        return userStorage.get(key.toString());
    }
    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(userStorage);
    }
}
