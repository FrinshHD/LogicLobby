package de.frinshhd.anturnialobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.frinshhd.anturnialobby.utils.LobbyStates;
import org.bukkit.Material;

import java.util.HashMap;

public class LobbyItem {

    @JsonProperty
    private HashMap<String, Item> types = null;


    public HashMap<LobbyStates, Item> getTypes() {
        HashMap<LobbyStates, Item> map = new HashMap<>();

        types.forEach((s, item) -> {
            map.put(LobbyStates.valueOf(s), item);
        });

        return map;
    }


    public Material getMaterialState(LobbyStates lobbyState) {
        return getTypes().get(lobbyState).getMaterial();
    }


}
