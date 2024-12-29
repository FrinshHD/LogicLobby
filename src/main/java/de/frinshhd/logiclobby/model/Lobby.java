package de.frinshhd.logiclobby.model;

import com.google.gson.annotations.SerializedName;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Lobby {

    @SerializedName("friendlyName")
    public String friendlyName;

    @SerializedName("tasks")
    public List<String> tasks = new ArrayList<>();
    @SerializedName("lobbyRadius")
    public Double lobbyRadius;
    @SerializedName("facing")
    public Float facing = 0F;
    @SerializedName("spawnLocation")
    private List<Double> spawnLocation;

    public Location getSpawnLocation() {
        return new Location(Bukkit.getWorld("world"), spawnLocation.get(0), spawnLocation.get(1), spawnLocation.get(2), facing, 0);
    }

    public boolean isGameLobby() {
        return !tasks.isEmpty();
    }
}