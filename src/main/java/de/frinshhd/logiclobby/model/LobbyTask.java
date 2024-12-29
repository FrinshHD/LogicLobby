package de.frinshhd.logiclobby.model;

import com.google.gson.annotations.SerializedName;
import de.frinshhd.logiclobby.utils.ItemTags;
import de.frinshhd.logiclobby.utils.LoreBuilder;
import de.frinshhd.logiclobby.utils.MessageFormat;
import de.frinshhd.logiclobby.utils.SpigotTranslator;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LobbyTask {

    @SerializedName("taskName")
    private String taskName = null;

    @SerializedName("item")
    private Item item = new Item();

    @SerializedName("description")
    private String description = "";

    public String getTaskName() {
        return this.taskName;
    }

    public ItemStack getItem(String serverName, Material material) {
        ItemStack item = this.item.getItem(material);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(MessageFormat.build(SpigotTranslator.build("items.standardColor") + serverName));

        itemMeta.setLore(LoreBuilder.build(getDescription(), ChatColor.getByChar(SpigotTranslator.build("items.standardDescriptionColor").substring(1))));

        ItemTags.tagItemMeta(itemMeta, serverName);

        item.setItemMeta(itemMeta);

        return item;
    }

    public String getDescription() {
        return this.description;
    }
}