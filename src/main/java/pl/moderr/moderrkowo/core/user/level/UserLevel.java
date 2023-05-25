package pl.moderr.moderrkowo.core.user.level;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import org.bukkit.boss.BarColor;
import org.jetbrains.annotations.Contract;
import pl.moderr.moderrkowo.core.api.util.ColorUtil;
import pl.moderr.moderrkowo.core.user.User;

import java.util.HashMap;

public class UserLevel {

    @Expose(serialize = false, deserialize = false)
    private User owner = null;
    @Expose(serialize = true, deserialize = true)
    private HashMap<LevelCategory, UserLevelData> levelData;

    @Contract(pure = true)
    public UserLevel() {
        this.levelData = new HashMap<>();
    }

    @Contract(pure = true)
    public UserLevel(User owner, HashMap<LevelCategory, UserLevelData> levelData) {
        this.owner = owner;
        this.levelData = levelData;
    }

    public UserLevel(String json) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        levelData = gson.fromJson(json, UserLevel.class).getLevelData();
    }

    public static BarColor levelCategoryColor(LevelCategory category) {
        switch (category) {
            case Walka:
            case Kopanie:
                return BarColor.RED;
            case Uprawa:
                return BarColor.GREEN;
            case Lowienie:
                return BarColor.BLUE;
            default:
                return BarColor.WHITE;
        }
    }

    public static String levelCategoryColorString(LevelCategory category) {
        switch (category) {
            case Walka:
            case Kopanie:
                return ColorUtil.color("&c");
            case Uprawa:
                return ColorUtil.color("&a");
            case Lowienie:
                return ColorUtil.color("&9");
            default:
                return ColorUtil.color("&f");
        }
    }

    public UserLevelData get(LevelCategory category) {
        if (levelData.containsKey(category)) {
            return levelData.get(category);
        } else {
            UserLevelData data = new UserLevelData(owner.getUniqueId(), 1, 0, category);
            levelData.put(category, data);
            return data;
        }
    }

    public void setAllCategories() {
        for (LevelCategory category : LevelCategory.values()) {
            get(category);
        }
    }

    public int playerLevel() {
        int poziom = 0;
        setAllCategories();
        for (UserLevelData levelData : getLevelData().values()) {
            /*for(UserLevelData levelData2 : getLevelData().values()){
                if(levelData.getCategory().equals(levelData2.getCategory())){
                    continue;
                }
                if(levelData.getLevel()-levelData2.getLevel() >= 0){
                    i++;
                }
            }
            if(i == LevelCategory.values().length){
                poziom = i;
                break;
            }*/
            poziom += levelData.getLevel();
        }
        return poziom / LevelCategory.values().length;
    }

    public HashMap<LevelCategory, UserLevelData> getLevelData() {
        return levelData;
    }

    public void setLevelData(HashMap<LevelCategory, UserLevelData> levelData) {
        this.levelData = levelData;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    @Override
    public String toString() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        return gson.toJson(this);
    }
}
