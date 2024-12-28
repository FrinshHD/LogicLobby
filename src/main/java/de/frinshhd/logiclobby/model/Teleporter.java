package de.frinshhd.logiclobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Teleporter {

    @JsonProperty
    private int inventoryRows = 6;

    @JsonProperty
    private FillerItem fillerItem;

    @JsonProperty
    private List<ConfigServer> configServers;

    public int getInventorySlots() {
        return inventoryRows * 9;
    }

    public List<ConfigServer> getServers() {
        return this.configServers;
    }

    public FillerItem getFillerItem() {
        return this.fillerItem;
    }

}
