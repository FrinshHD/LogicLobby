package de.frinshhd.anturnialobby.model;

import com.fasterxml.jackson.annotation.JsonKey;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Server {

    @JsonProperty
    private String name = null;

    @JsonProperty
    private String description = null;

    @JsonProperty
    private String host = null;

    @JsonProperty
    private Integer port = null;

    @JsonProperty
    private Item item = null;

}
