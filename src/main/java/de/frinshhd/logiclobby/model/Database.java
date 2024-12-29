package de.frinshhd.logiclobby.model;

import com.google.gson.annotations.SerializedName;
import de.frinshhd.logiclobby.utils.DatabaseTypes;

public class Database {

    @SerializedName("username")
    public String username = null;

    @SerializedName("password")
    public String password = null;

    @SerializedName("database")
    public String database = "LogicLobby";

    @SerializedName("host")
    public String host = "127.0.0.1";

    @SerializedName("port")
    public int port = 3306;

    @SerializedName("type")
    private String type = "sqlite";

    public DatabaseTypes getType() {
        return DatabaseTypes.valueOf(type.toUpperCase());
    }

}