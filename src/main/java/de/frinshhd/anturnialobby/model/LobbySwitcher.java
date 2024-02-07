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
}
