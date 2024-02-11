package de.frinshhd.logiclobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Teleporter {

    @JsonProperty
    private int inventoryRows = 6;

    @JsonProperty
    private FillerItem fillerItem;

    @JsonProperty
    private List<Server> servers;

    public int getInventorySlots() {
        return inventoryRows * 9;
    }

    public List<Server> getServers() {
        return this.servers;
    }

    public FillerItem getFillerItem() {
        return this.fillerItem;
    }

}
