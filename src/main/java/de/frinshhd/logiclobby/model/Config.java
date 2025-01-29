package de.frinshhd.logiclobby.model;

import com.google.gson.annotations.SerializedName;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class Config {

    @SerializedName("database")
    public Database database = new Database();
    @SerializedName("lobbySwitcher")
    private LobbySwitcher lobbySwitcher = null;
    @SerializedName("teleporter")
    private Teleporter teleporter = new Teleporter();
    @SerializedName("items")
    private List<ClickItem> items = new ArrayList<>();
    @SerializedName("enableCloudNetSupport")
    private boolean enableCloudNetSupport = false;
    @SerializedName("defaultHotbarSlot")
    private int defaultHotbarSlot = -1;
    @SerializedName("spawn")
    private Spawn spawn = null;
    @SerializedName("events")
    private Events events = new Events();

    public boolean hasCloudNetSupportEnabled() {
        if (!Bukkit.getPluginManager().isPluginEnabled("CloudNet-Bridge")) {
            return false;
        }

        return enableCloudNetSupport;
    }

    public LobbySwitcher getLobbySwitcher() {
        return this.lobbySwitcher;
    }

    public Teleporter getTeleporter() {
        return this.teleporter;
    }

    public List<ClickItem> getItems() {
        return this.items;
    }

    public ClickItem getClickItem(String id) {
        for (ClickItem item : getItems()) {
            if (item.getId().equalsIgnoreCase(id)) {
                return item;
            }
        }

        return null;
    }

    public int getDefaultHotbarSlot() {
        return this.defaultHotbarSlot;
    }

    public Spawn getSpawn() {
        return this.spawn;
    }

    public Events getEvents() {
        return this.events;
    }
}