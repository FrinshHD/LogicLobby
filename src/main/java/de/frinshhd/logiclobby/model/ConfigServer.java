package de.frinshhd.logiclobby.model;

import com.google.gson.annotations.SerializedName;
import de.frinshhd.logiclobby.Main;
import de.frinshhd.logiclobby.utils.ItemTags;
import de.frinshhd.logiclobby.utils.LoreBuilder;
import de.frinshhd.logiclobby.utils.MessageFormat;
import de.frinshhd.logiclobby.utils.SpigotTranslator;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ConfigServer {

    public transient String message = null;
    @SerializedName("id")
    private String id;
    @SerializedName("name")
    private String name = "Lobby";
    @SerializedName("description")
    private String description = null;
    @SerializedName("serverName")
    private String serverName = null;
    @SerializedName("item")
    private Item item = new Item();
    @SerializedName("location")
    private List<Double> location = null;
    @SerializedName("world")
    private String world = "world";
    @SerializedName("yaw")
    private Float yaw = null;
    @SerializedName("pitch")
    private Float pitch = null;
    @SerializedName("task")
    private String task = null;

    public ItemStack getItem() {
        return getItem(getId(), null);
    }

    public ItemStack getItem(String tagID) {
        return getItem(tagID, null);
    }

    public ItemStack getItem(Material material) {
        return getItem(getId(), material);
    }

    public ItemStack getItem(String tagID, Material material) {
        if (this.item.getMaterial() != null) {
            material = this.item.getMaterial();
        }

        ItemStack item = this.item.getItem(material);

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(MessageFormat.build(SpigotTranslator.build("items.standardColor") + name));

        itemMeta.setLore(LoreBuilder.build(getDescription(), ChatColor.getByChar(SpigotTranslator.build("items.standardDescriptionColor").substring(1))));

        ItemTags.tagItemMeta(itemMeta, tagID);

        item.setItemMeta(itemMeta);

        return item;
    }

    public int getItemSlot() {
        return this.item.getSlot();
    }

    public String getDescription() {
        return this.description;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void execute(Player player) {
        if (location != null) {
            player.teleport(getLocation());
            return;
        }

        if (Main.getManager().getConfig().hasCloudNetSupportEnabled()) {
            //Todo: add CloudNet support
        } else {
            Main.getManager().sendPlayerToServer(player, getServerName());
        }
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

    public String getServerName() {
        if (this.serverName == null) {
            return getId();
        }

        return this.serverName;
    }

    public String getMessage() {
        return this.message;
    }

    public String getTask() {
        return this.task;
    }
}