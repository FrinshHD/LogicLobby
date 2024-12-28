package de.frinshhd.logiclobby.menusystem;

import de.frinshhd.logiclobby.model.ConfigServer;
import org.bukkit.inventory.ItemStack;

public class SavedItem {
    private int slot;
    private ItemStack itemStack;

    private ConfigServer configServer;
    private String lore;

    public SavedItem(int slot, ItemStack itemStack, ConfigServer configServer, String lore) {
        this.slot = slot;
        this.itemStack = itemStack;
        this.configServer = configServer;
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
        return this.configServer;
    }

    public String getLore() {
        return this.lore;
    }

    public void updateLore(String lore) {
        this.lore = lore;
    }
}
