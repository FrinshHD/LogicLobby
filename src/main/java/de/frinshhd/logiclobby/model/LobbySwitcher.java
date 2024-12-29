package de.frinshhd.logiclobby.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class LobbySwitcher {
    @SerializedName("fillerItem")
    private FillerItem fillerItem = null;

    @SerializedName("lobbyItem")
    private LobbyItem lobbyItem = null;

    @SerializedName("lobbyTask")
    private LobbyTask lobbyTask = null;

    @SerializedName("lobbyServers")
    private ArrayList<ConfigServer> lobbyServers = new ArrayList<>();

    @SerializedName("inventoryRows")
    private int inventoryRows = 3;

    public int getInventorySlots() {
        return inventoryRows * 9;
    }

    public ArrayList<ConfigServer> getLobbyServers() {
        return this.lobbyServers;
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