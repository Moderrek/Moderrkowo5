package pl.moderr.moderrkowo.core.opening.data;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class ChestStorageDeserializer implements JsonDeserializer<StorageItemKey>, JsonSerializer<StorageItemKey> {

    @Override
    public StorageItemKey deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext ctx) throws JsonParseException
    {
        JsonObject obj = json.getAsJsonObject();
        Map.Entry<String, JsonElement> entry = obj.entrySet().iterator().next();
        if (entry == null) return null;
        StorageItemType type;
        type = StorageItemType.valueOf(entry.getKey());
        ModerrCaseEnum value = ModerrCaseEnum.valueOf(entry.getValue().getAsString());
        return new StorageItemKey(type, value);
    }

    @Override
    public JsonElement serialize(StorageItemKey src, Type typeOfSrc, JsonSerializationContext context) {

        return null;
    }
}
