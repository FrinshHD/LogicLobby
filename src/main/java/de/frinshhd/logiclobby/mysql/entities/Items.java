package de.frinshhd.logiclobby.mysql.entities;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@DatabaseTable(tableName = "Items")
public class Items {
    private static final Gson gson = new Gson();
    private static final Type type = new TypeToken<HashMap<String, Object>>() {}.getType();

    @DatabaseField(id = true)
    private UUID uuid;

    @DatabaseField
    private String items;

    public Items() {
    }

    public void create(UUID uuid) {
        this.uuid = uuid;
        items = hashMapToString(new HashMap<>());
    }

    public UUID getUUID() {
        return uuid;
    }

    public void putItems(String item, Object object) {
        HashMap<String, Object> items;
        if (this.items == null || this.items.isEmpty() || this.items.equals("{}")) {
            items = new HashMap<>();
        } else {
            items = stringToHashMap(this.items);
        }

        items.put(item, object);
        this.items = hashMapToString(items);
    }

    public HashMap<String, Object> getItems() {
        if (this.items == null || this.items.equals("{}") || this.items.isEmpty()) {
            return new HashMap<>();
        }

        return stringToHashMap(this.items);
    }

    public HashMap<String, Object> stringToHashMap(String jsonString) {
        return gson.fromJson(jsonString, type);
    }

    public String hashMapToString(Map<String, Object> map) {
        return gson.toJson(map);
    }
}