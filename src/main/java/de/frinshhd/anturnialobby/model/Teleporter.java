package de.frinshhd.anturnialobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Teleporter {

    @JsonProperty
    private FillerItem fillerItem;

    @JsonProperty
    private List<Server> servers;

}
