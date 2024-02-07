package de.frinshhd.anturnialobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.inventory.ItemStack;

public class FillerItem {

    @JsonProperty
    private String type = null;
    @JsonProperty
    private Item item = null;

    public ItemStack getItem() {
        ItemStack item = this.item.getItem();

        return item;
    }

    public FillerType getFillerType() {
        return FillerType.valueOf(type);
    }
}
