package de.frinshhd.anturnialobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LobbyItem {

    @JsonProperty
    private HashMap<String, Item> types = null;

}
