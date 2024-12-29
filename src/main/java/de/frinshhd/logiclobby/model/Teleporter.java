package de.frinshhd.logiclobby.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Teleporter {

    @SerializedName("inventoryRows")
    private int inventoryRows = 6;

    @SerializedName("fillerItem")
    private FillerItem fillerItem;

    @SerializedName("servers")
    private List<ConfigServer> servers = new ArrayList<>();

    public int getInventorySlots() {
        return inventoryRows * 9;
    }

    public List<ConfigServer> getServers() {
        return this.servers;
    }

    public FillerItem getFillerItem() {
        return this.fillerItem;
    }

}