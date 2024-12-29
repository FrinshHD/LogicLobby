package de.frinshhd.logiclobby.model;

import com.google.gson.annotations.SerializedName;

public class LobbyItemTypes {

    @SerializedName("type")
    private String type = null;

    @SerializedName("item")
    private Item item = null;
}