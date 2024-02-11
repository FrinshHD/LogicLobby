package de.frinshhd.logiclobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.frinshhd.logiclobby.utils.FillerType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class FillerItem {

    @JsonProperty
    private String type = null;
    @JsonProperty
    private Item item = null;

    public ItemStack getItem() {
        ItemStack item = this.item.getItem();
        ItemMeta itemMeta = item.getItemMeta();

        itemMeta.setDisplayName("");

        item.setItemMeta(itemMeta);

        return item;
    }

    public FillerType getFillerType() {
        return FillerType.valueOf(type);
    }
}
