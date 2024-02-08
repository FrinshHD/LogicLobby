package de.frinshhd.anturnialobby.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.frinshhd.anturnialobby.Main;
import de.frinshhd.anturnialobby.utils.ItemTags;
import de.frinshhd.anturnialobby.utils.LoreBuilder;
import de.frinshhd.anturnialobby.utils.MessageFormat;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Server {

    @JsonProperty
    private String id;
    @JsonProperty
    private String name = "Lobby";

    @JsonProperty
    private String description = null;

    @JsonProperty
    private String task = null;

    @JsonProperty
    private String serverName = null;

    @JsonProperty
    private Item item = null;

    @JsonProperty
    private List<Double> location = null;

    @JsonProperty
    private String world = "world";

    @JsonProperty
    private Float yaw = null;

    @JsonProperty
    private Float pitch = null;

    @JsonIgnore
    public ItemStack getItem() {
        return getItem(null);
    }

    @JsonIgnore
    public String message = null;

    @JsonIgnore
    public ItemStack getItem(Material material) {
        ItemStack item = this.item.getItem();

        if (material != null) {
            item.setType(material);
        }

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(MessageFormat.build(name));

        itemMeta.setLore(LoreBuilder.build(getDescription(), ChatColor.GRAY));

        ItemTags.tagItemMeta(itemMeta, getId());

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
        //If player should just be teleported to a location
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
        return this.serverName;
    }

    public String getMessage() {
        return this.message;
    }
}
