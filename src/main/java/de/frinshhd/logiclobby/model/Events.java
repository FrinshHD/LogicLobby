package de.frinshhd.logiclobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.GameMode;

public class Events {

    @JsonProperty
    private boolean noDamage = true;
    @JsonProperty
    private boolean noHunger = true;
    @JsonProperty
    private boolean noWeatherChange = true;
    @JsonProperty
    private boolean noTimeChange = true;
    @JsonProperty
    private boolean noBlockBreak = true;
    @JsonProperty
    private boolean noBlockPlace = true;
    @JsonProperty
    private boolean noItemDrop = true;
    @JsonProperty
    private boolean noItemPickup = true;
    @JsonProperty
    private boolean noItemCraft = true;
    @JsonProperty
    private boolean noItemConsume = true;
    @JsonProperty
    private boolean noEntityDamage = true;
    @JsonProperty
    private boolean noEntityInteract = true;
    @JsonProperty
    private boolean noEntitySpawn = true;
    @JsonProperty
    private Long lowestY = null;
    @JsonProperty
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
