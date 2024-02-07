package de.frinshhd.anturnialobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LobbyItemTypes {

    @JsonProperty
    private String type = null;

    @JsonProperty
    private Item item = null;
}
