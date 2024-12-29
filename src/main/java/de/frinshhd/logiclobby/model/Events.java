package de.frinshhd.logiclobby.model;

import com.google.gson.annotations.SerializedName;
import org.bukkit.GameMode;

public class Events {

    @SerializedName("noDamage")
    private boolean noDamage = true;
    @SerializedName("noHunger")
    private boolean noHunger = true;
    @SerializedName("noWeatherChange")
    private boolean noWeatherChange = true;
    @SerializedName("noTimeChange")
    private boolean noTimeChange = true;
    @SerializedName("noBlockBreak")
    private boolean noBlockBreak = true;
    @SerializedName("noBlockPlace")
    private boolean noBlockPlace = true;
    @SerializedName("noItemDrop")
    private boolean noItemDrop = true;
    @SerializedName("noItemPickup")
    private boolean noItemPickup = true;
    @SerializedName("noItemCraft")
    private boolean noItemCraft = true;
    @SerializedName("noItemConsume")
    private boolean noItemConsume = true;
    @SerializedName("noEntityDamage")
    private boolean noEntityDamage = true;
    @SerializedName("noEntityInteract")
    private boolean noEntityInteract = true;
    @SerializedName("noEntitySpawn")
    private boolean noEntitySpawn = true;
    @SerializedName("lowestY")
    private Long lowestY = null;
    @SerializedName("joinGamemode")
    private String joinGamemode = null;

    public boolean isNoDamage() {
        return noDamage;
    }

    public boolean isNoHunger() {
        return noHunger;
    }

    public boolean isNoWeatherChange() {
        return noWeatherChange;
    }

    public boolean isNoTimeChange() {
        return noTimeChange;
    }

    public boolean isNoBlockBreak() {
        return noBlockBreak;
    }

    public boolean isNoBlockPlace() {
        return noBlockPlace;
    }

    public boolean isNoItemDrop() {
        return noItemDrop;
    }

    public boolean isNoItemPickup() {
        return noItemPickup;
    }

    public boolean isNoItemCraft() {
        return noItemCraft;
    }

    public boolean isNoItemConsume() {
        return noItemConsume;
    }

    public boolean isNoEntityDamage() {
        return noEntityDamage;
    }

    public boolean isNoEntityInteract() {
        return noEntityInteract;
    }

    public boolean isNoEntitySpawn() {
        return noEntitySpawn;
    }

    public Long getLowestY() {
        return lowestY;
    }

    public GameMode getJoinGamemode() {
        if (joinGamemode == null) {
            return null;
        }

        try {
            return GameMode.valueOf(joinGamemode);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}