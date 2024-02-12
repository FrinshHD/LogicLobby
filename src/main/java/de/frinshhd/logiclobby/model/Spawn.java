package de.frinshhd.logiclobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.frinshhd.logiclobby.Main;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;

public class Spawn {

    @JsonProperty
    private boolean teleportOnJoin = true;

    @JsonProperty
    private ArrayList<Double> location;

    @JsonProperty
    private String world = "world";
    @JsonProperty
    private Float yaw = null;
    @JsonProperty
    private Float pitch = null;

    public Location getLocation() {
        Location location = new Location(getWorld(), this.location.get(0), this.location.get(1), this.location.get(2));

        if (this.yaw != null) {
            location.setYaw(this.yaw);
        }

        if (this.pitch != null) {
            location.setPitch(this.pitch);
        }

        return location;
    }

    public World getWorld() {
        return Main.getInstance().getServer().getWorld(this.world);
    }

    public boolean isTeleportOnJoin() {
        return teleportOnJoin;
    }
}
