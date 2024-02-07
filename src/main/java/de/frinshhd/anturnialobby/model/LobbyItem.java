package de.frinshhd.anturnialobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class LobbyItem {

    @JsonProperty
    private HashMap<String, Item> types = null;

}
