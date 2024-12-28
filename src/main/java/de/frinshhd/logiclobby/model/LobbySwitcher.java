package de.frinshhd.logiclobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;

public class LobbySwitcher {
    @JsonProperty
    private FillerItem fillerItem = null;

    @JsonProperty
    private LobbyItem lobbyItem = null;

    @JsonProperty
    private LobbyTask lobbyTask = null;

    @JsonProperty
    private ArrayList<ConfigServer> lobbyConfigServers = new ArrayList<>();

    @JsonProperty
    private int inventoryRows = 3;

    public int getInventorySlots() {
        return inventoryRows * 9;
    }

    public ArrayList<ConfigServer> getLobbyServers() {
        return this.lobbyConfigServers;
    }

    public FillerItem getFillerItem() {
        return this.fillerItem;
    }

    public LobbyItem getLobbyItem() {
        return this.lobbyItem;
    }

    public LobbyTask getLobbyTask() {
        return this.lobbyTask;
    }
}
