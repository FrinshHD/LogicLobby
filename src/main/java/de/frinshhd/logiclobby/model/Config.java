package de.frinshhd.logiclobby.model;

import app.simplecloud.controller.api.ControllerApi;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.frinshhd.logiclobby.utils.SimpleCloudWrapper;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.List;

public class Config {

    @JsonProperty
    public Database database = new Database();
    @JsonProperty
    private LobbySwitcher lobbySwitcher = null;
    @JsonProperty
    private Teleporter teleporter = new Teleporter();
    @JsonProperty
    private List<ClickItem> items = new ArrayList<>();
    @JsonProperty
    private boolean enableCloudNetSupport = false;
    @JsonProperty
    private boolean enableSimpleCloudSupport = false;
    @JsonProperty
    private int defaultHotbarSlot = -1;
    @JsonProperty
    private Spawn spawn = null;

    @JsonProperty
    private Events events = new Events();

    @JsonIgnore
    public boolean hasCloudNetSupportEnabled() {
        if (!Bukkit.getPluginManager().isPluginEnabled("CloudNet-Bridge")) {
            return false;
        }

        return enableCloudNetSupport;
    }

    @JsonIgnore
    public boolean hasSimpleCloudSupportEnabled() {
        ControllerApi.Future controllerApi = SimpleCloudWrapper.getControllerApi();

        try {
            controllerApi.getServers().getAllServers();
        } catch (Exception e) {
            return false;
        }


        return enableSimpleCloudSupport;
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
