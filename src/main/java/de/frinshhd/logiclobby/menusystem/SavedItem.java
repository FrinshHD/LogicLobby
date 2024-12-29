package de.frinshhd.logiclobby.menusystem;

import de.frinshhd.logiclobby.model.ConfigServer;
import org.bukkit.inventory.ItemStack;

public class SavedItem {
    private int slot;
    private ItemStack itemStack;

    private ConfigServer server;
    private String lore;

    public SavedItem(int slot, ItemStack itemStack, ConfigServer server, String lore) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.server = server;
        this.lore = lore;
    }

    public void updateItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }


    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public int getSlot() {
        return this.slot;
    }

    public ConfigServer getServer() {
        return this.server;
    }

    public String getLore() {
        return this.lore;
    }

    public void updateLore(String lore) {
        this.lore = lore;
    }
}
