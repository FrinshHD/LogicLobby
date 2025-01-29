package de.frinshhd.logiclobby.model;

import com.google.gson.annotations.SerializedName;
import de.frinshhd.logiclobby.Main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;

import static de.frinshhd.logiclobby.Main.getManager;

public class Spawn {

    @SerializedName("teleportOnJoin")
    private boolean teleportOnJoin = true;

    @SerializedName("location")
    private ArrayList<Double> location;

    @SerializedName("world")
    private String world = "world";

    @SerializedName("yaw")
    private Float yaw = null;

    @SerializedName("pitch")
    private Float pitch = null;

    // Moon; helper function.
    public boolean isInSpawnWorld(Player player) {
        Spawn spawn = getManager().getConfig().getSpawn();
        return player.getWorld().equals(spawn.getWorld());
    }

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

    public void setLocation(Location location) {
        this.location = new ArrayList<>();
        this.location.add(location.getX());
        this.location.add(location.getY());
        this.location.add(location.getZ());

        this.world = location.getWorld().getName();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }
}