package de.frinshhd.logiclobby.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import de.frinshhd.logiclobby.menusystem.LobbySwitcherMenu;
import de.frinshhd.logiclobby.menusystem.TeleporterMenu;
import de.frinshhd.logiclobby.utils.*;
import org.bukkit.ChatColor;
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
    private String menu = null;

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

    @Override
    public ItemStack getItem() {
        ItemStack item = super.getItem();
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
        switch (getMenu()) {
            case TELEPORTER:
                new TeleporterMenu(player).open();
                break;
            case LOBBYSWITCHER:
                new LobbySwitcherMenu(player).open();
                break;
            default:
        }
    }
}
