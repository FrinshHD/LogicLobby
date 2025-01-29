package de.frinshhd.logiclobby.model;

import com.google.gson.annotations.SerializedName;
import de.frinshhd.logiclobby.itemsystem.items.PlayerHider;
import de.frinshhd.logiclobby.menusystem.LobbySwitcherMenu;
import de.frinshhd.logiclobby.menusystem.TeleporterMenu;
import de.frinshhd.logiclobby.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Objects;

public class ClickItem extends Item {

    @SerializedName("id")
    private String id;

    @SerializedName("friendlyName")
    private String friendlyName = null;

    @SerializedName("description")
    private String description = null;

    @SerializedName("type")
    private ClickItemType type = null;

    @SerializedName("menu")
    private String menu = null;

    @SerializedName("command")
    private String command = null;

    @SerializedName("items")
    private ArrayList<ClickItem> items = new ArrayList<>();

    @SerializedName("toggledMaterial")
    private String toggledMaterial = null;

    public String getDescription() {
        return this.description;
    }

    public String getFriendlyName() {
        return this.friendlyName;
    }

    public String getId() {
        return this.id;
    }

    public MenuTypes getMenu() {
        if (menu == null) {
            return MenuTypes.NONE;
        }

        return MenuTypes.valueOf(this.menu);
    }

    public ClickItemType getType() {
        if (type == null) {
            return ClickItemType.MENU;
        }

        return type;
    }

    public Material getToggledMaterial() {
        return Material.getMaterial(toggledMaterial);
    }

    @Override
    public ItemStack getItem() {
        return getItem(false);
    }

    public ItemStack getItem(boolean toggled) {
        ItemStack item = super.getItem();

        if (toggled) {
            item.setType(getToggledMaterial());
        }

        ItemMeta itemMeta = item.getItemMeta();

        if (getFriendlyName() != null) {
            itemMeta.setDisplayName(MessageFormat.build(getFriendlyName()));
        }

        if (getDescription() != null) {
            itemMeta.setLore(LoreBuilder.build(getDescription(), ChatColor.getByChar(SpigotTranslator.build("items.standardDescriptionColor").substring(1))));
        }

        ItemTags.tagItemMeta(itemMeta, getId());

        item.setItemMeta(itemMeta);

        return item;
    }

    public void use(Player player) {
        // Moon: no menu type, do command
        if (getMenu() == MenuTypes.NONE && command != null && !command.isEmpty()) {
            player.performCommand(command);
            return;
        }

        switch (getType()) {
            case MENU:
                switch (getMenu()) {
                    case TELEPORTER:
                        new TeleporterMenu(player).open();
                        break;
                    case LOBBYSWITCHER:
                        new LobbySwitcherMenu(player).open();
                        break;
                    default:
                        break;
                }
                break;
            case PLAYER_HIDER:
                PlayerHider.getPlayerHider().toggle(player);
                break;
            default:
                break;
        }
    }

    public ArrayList<ClickItem> getItems() {
        return items;
    }
}