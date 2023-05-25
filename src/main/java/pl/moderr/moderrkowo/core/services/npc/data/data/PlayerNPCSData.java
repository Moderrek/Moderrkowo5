package pl.moderr.moderrkowo.core.services.npc.data.data;

import com.google.gson.Gson;
import org.jetbrains.annotations.Contract;

import java.util.HashMap;

public class PlayerNPCSData {

    private HashMap<String, PlayerNPCData> npcsData;

    @Contract(pure = true)
    public PlayerNPCSData() {
        this.npcsData = new HashMap<>();
    }

    @Contract(pure = true)
    public PlayerNPCSData(HashMap<String, PlayerNPCData> villagersData) {
        this.npcsData = villagersData;
    }

    public PlayerNPCSData(String json) {
        Gson gson = new Gson();
        npcsData = gson.fromJson(json, PlayerNPCSData.class).getNPCSData();
    }

    public HashMap<String, PlayerNPCData> getNPCSData() {
        return npcsData;
    }

    public void setNPCSData(HashMap<String, PlayerNPCData> npcsData) {
        this.npcsData = npcsData;
    }

    @Override
    public String toString() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }

    public String ToJson() {
        Gson gson = new Gson();
        return gson.toJson(this);
    }
}
