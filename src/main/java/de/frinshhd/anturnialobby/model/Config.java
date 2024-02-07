package de.frinshhd.anturnialobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Config {

    @JsonProperty
    private boolean enableCloudNetSupport = false;

    @JsonProperty
    public LobbySwitcher lobbySwitcher = null;

    @JsonProperty
    public Teleporter teleporter = new Teleporter();
}
