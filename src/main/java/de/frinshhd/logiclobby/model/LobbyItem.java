package de.frinshhd.logiclobby.model;

import com.google.gson.annotations.SerializedName;
import de.frinshhd.logiclobby.utils.LobbyState;
import org.bukkit.Material;

import java.util.HashMap;

public class LobbyItem {

    @SerializedName("types")
    private HashMap<String, Item> types = null;

    public HashMap<LobbyState, Item> getTypes() {
        HashMap<LobbyState, Item> map = new HashMap<>();

        types.forEach((s, item) -> {
            map.put(LobbyState.valueOf(s), item);
        });

        return map;
    }

    public Material getMaterialState(LobbyState lobbyState) {
        return getTypes().get(lobbyState).getMaterial();
    }
}