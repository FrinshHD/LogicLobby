package de.frinshhd.anturnialobby.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Config {

    @JsonProperty
    private LobbySwitcher lobbySwitcher = null;
    @JsonProperty
    private Teleporter teleporter = new Teleporter();
    @JsonProperty
    private boolean enableCloudNetSupport = false;

    @JsonIgnore
    public boolean hasCloudNetSupportEnabled() {
        return enableCloudNetSupport;
    }

    public LobbySwitcher getLobbySwitcher() {
        return this.lobbySwitcher;
    }

    public Teleporter getTeleporter() {
        return this.teleporter;
    }
}
