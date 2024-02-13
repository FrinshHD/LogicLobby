package de.frinshhd.logiclobby.itemsystem;

import de.frinshhd.logiclobby.model.ClickItem;

import java.util.HashMap;

public class ItemsManager {

    private HashMap<String, ClickItem> items = new HashMap<>();

    public ItemsManager() {
    }

    public void addItem(ClickItem item) {
        items.put(item.getId(), item);
    }

    public HashMap<String, ClickItem> getItems() {
        return this.items;
    }

}
