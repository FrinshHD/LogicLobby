package de.frinshhd.anturnialobby.menusystem;

import org.bukkit.inventory.ItemStack;

public class SavedItem {
    private int slot;
    private ItemStack itemStack;

    public SavedItem(int slot, ItemStack itemStack) {
        this.slot = slot;
        this.itemStack = itemStack;
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

}
