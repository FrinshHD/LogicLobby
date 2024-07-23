package de.frinshhd.logiclobby.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import de.frinshhd.logiclobby.itemsystem.items.PlayerHider;
import de.frinshhd.logiclobby.menusystem.LobbySwitcherMenu;
import de.frinshhd.logiclobby.menusystem.TeleporterMenu;
import de.frinshhd.logiclobby.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ClickItem extends Item {

    @JsonProperty
    private String id;

    @JsonProperty
    private String friendlyName = null;

    @JsonProperty
    private String description = null;

    @JsonProperty
    private ClickItemType type = null;

    @JsonProperty
    private String menu = null;

    @JsonProperty
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
        return MenuTypes.valueOf(this.menu);
    }

    public ClickItemType getType() {
        if (type == null) {
            return ClickItemType.MENU;
        }

        return type;
    }

    @JsonIgnore
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
                }
                break;
            case PLAYER_HIDER:
                PlayerHider.getPlayerHider().toggle(player);
                break;
            default:
        }
    }
}
