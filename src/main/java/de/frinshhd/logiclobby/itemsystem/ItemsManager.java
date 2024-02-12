package de.frinshhd.logiclobby.itemsystem;

import de.frinshhd.logiclobby.Main;
import de.frinshhd.logiclobby.model.ClickItem;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.http.WebSocket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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
