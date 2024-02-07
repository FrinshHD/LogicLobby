package de.frinshhd.anturnialobby.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.frinshhd.anturnialobby.utils.MessageFormat;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Server {

    @JsonProperty
    private String name = "Lobby";

    @JsonProperty
    private String description = null;

    @JsonProperty
    private String host = null;

    @JsonProperty
    private Integer port = null;

    @JsonProperty
    private String task = null;

    @JsonProperty
    private String service = null;

    @JsonProperty
    private Item item = null;

    @JsonIgnore
    public ItemStack getItem() {
        return getItem(null);
    }

    @JsonIgnore
    public ItemStack getItem(Material material) {
        ItemStack item = this.item.getItem();

        if (material != null) {
            item.setType(material);
        }

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(MessageFormat.build(name));

        item.setItemMeta(itemMeta);

        return item;
    }

    public int getItemSlot() {
        return this.item.getSlot();
    }
}
