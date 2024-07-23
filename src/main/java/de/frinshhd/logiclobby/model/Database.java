package de.frinshhd.logiclobby.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.frinshhd.logiclobby.utils.DatabaseTypes;

public class Database {

    @JsonProperty
    public String username = null;
    @JsonProperty
    public String password = null;
    @JsonProperty
    public String database = "LogicLobby";
    @JsonProperty
    public String ip = "127.0.0.1";
    @JsonProperty
    public int port = 3306;
    @JsonProperty
    private String type = "sqlite";

    @JsonIgnore
    public DatabaseTypes getType() {
        return DatabaseTypes.valueOf(type.toUpperCase());
    }

}
