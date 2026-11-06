package it.unicam.cs.mpgc.rpg131056.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

public class ItemAdapter implements JsonSerializer<Item>, JsonDeserializer<Item> {

    @Override
    public JsonElement serialize(Item item, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject json = context.serialize(item, item.getClass()).getAsJsonObject();
        json.addProperty("type", item.getType().name());
        return json;
    }

    @Override
    public Item deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        ItemType type = ItemType.valueOf(json.getAsJsonObject().get("type").getAsString());
        Class<? extends Item> classe = switch (type) {
            case FOOD -> Food.class;
            case WATER -> Water.class;
            case MATERIAL -> Material.class;            
        }; 
        
        return context.deserialize(json, classe);
    }
}
