package de.frinshhd.anturnialobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class LobbySwitcher {
    @JsonProperty
    private FillerItem fillerItem = null;

    @JsonProperty
    private LobbyItem lobbyItem = null;

    @JsonProperty
    private ArrayList<Server> lobbyServers = new ArrayList<>();

    @JsonProperty
    private int inventoryRows = 3;

    public int getInventorySlots() {
        return inventoryRows * 9;
    }

    public ArrayList<Server> getLobbyServers() {
        return this.lobbyServers;
    }

    public FillerItem getFillerItem() {
        return this.fillerItem;
    }

    public LobbyItem getLobbyItem() {
        return this.lobbyItem;
    }
}
