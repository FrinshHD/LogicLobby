package de.frinshhd.anturnialobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

public class LobbyItemTypes {

    @JsonProperty
    private String type = null;

    @JsonProperty
    private Item item = null;
}
