package de.frinshhd.logiclobby.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.checkerframework.checker.units.qual.C;

import java.util.ArrayList;
import java.util.List;

public class Config {

    @JsonProperty
    private LobbySwitcher lobbySwitcher = null;
    @JsonProperty
    private Teleporter teleporter = new Teleporter();

    @JsonProperty
    private List<ClickItem> items = new ArrayList<>();
    @JsonProperty
    private boolean enableCloudNetSupport = false;
    @JsonProperty
    private int defaultHotbarSlot = -1;

    @JsonProperty
    private Spawn spawn = null;

    @JsonIgnore
    public boolean hasCloudNetSupportEnabled() {
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

    public int getDefaultHotbarSlot() {
        return this.defaultHotbarSlot;
    }

    public Spawn getSpawn() {
        return this.spawn;
    }
}
