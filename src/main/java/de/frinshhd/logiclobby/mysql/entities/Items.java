package de.frinshhd.logiclobby.mysql.entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@DatabaseTable(tableName = "Items")
public class Items {
    private static final ObjectMapper objectMapper = new ObjectMapper();

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
            items = (HashMap<String, Object>) stringToHashMap(this.items);
        }

        if (items.containsKey(item)) {
            items.put(item, object);
            this.items = hashMapToString(items);
            return;
        }

        items.put(item, object);
        this.items = hashMapToString(items);
    }

    public HashMap<String, Object> getItems() {
        if (this.items == null || this.items.equals("{}") || this.items.isEmpty()) {
            return new HashMap<>();
        }

        return (HashMap<String, Object>) stringToHashMap(this.items);
    }

    public Map<String, Object> stringToHashMap(String jsonString) {
        Map<String, Object> resultMap = null;
        try {
            resultMap = objectMapper.readValue(jsonString, new TypeReference<HashMap<String, Object>>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return resultMap;
    }

    public String hashMapToString(Map<String, Object> map) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonString;
    }
}
