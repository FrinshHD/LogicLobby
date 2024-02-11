package de.frinshhd.logiclobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    @JsonProperty
    public String friendlyName;

    @JsonProperty
    public List<String> tasks = new ArrayList<>();
    @JsonProperty
    public Double lobbyRadius;
    @JsonProperty
    public Float facing = 0F;
    @JsonProperty
    private List<Double> spawnLocation;

    public Location getSpawnLocation() {
        return new Location(Bukkit.getWorld("world"), spawnLocation.get(0), spawnLocation.get(1), spawnLocation.get(2), facing, 0);
    }

    public boolean isGameLobby() {
        return !tasks.isEmpty();
    }
}
