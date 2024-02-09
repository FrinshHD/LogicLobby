package de.frinshhd.anturnialobby.menusystem.library;

import de.frinshhd.anturnialobby.utils.FillerType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class Menu implements InventoryHolder {

    protected Player player;
    protected Inventory inventory;
    protected String title;

    public Menu(Player player) {
        this.player = player;
        setTitle("MenuTitle");
    }

    public abstract int getSlots();

    public String getTitle() {
        return title;
    }

    ;

    public void setTitle(String title) {
        this.title = title;
    }

    ;

    public abstract void setItems();

    public void open() {
        open(FillerType.NONE);
    }

    public void open(FillerType fillerType) {
        open(fillerType, getFillerItem());
    }

    public void open(FillerType fillerType, ItemStack filler) {
        createInventory();

        if (fillerType.equals(FillerType.OUTLINE)) {
            fillBorders(filler);
        } else if (fillerType.equals(FillerType.FULL)) {
            fillInventory(filler);
        }

        setItems();

        player.openInventory(inventory);
    }

    public void fillInventory(ItemStack filler) {
        for (int i = 0; i < inventory.getSize(); i++) {
            if (inventory.getItem(i) != null) {
                continue;
            }

            inventory.setItem(i, filler);
        }
    }

    public void createInventory() {
        this.inventory = Bukkit.createInventory(this, getSlots(), getTitle());
    }

    public void fillBorders(ItemStack item) {
        for (int i = 0; i < inventory.getSize(); i++) {

            if (inventory.getItem(i) != null) {
                continue;
            }

            if (i < 9) {
                inventory.setItem(i, item);
            } else if (i % 9 == 0) {
                inventory.setItem(i, item);
                inventory.setItem(i - 1, item);
            } else if (i > inventory.getSize() - 9) {
                inventory.setItem(i, item);
            }
        }
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }

    public abstract void handle(InventoryClickEvent event);

    public ItemStack getFillerItem() {
        return new ItemStack(Material.GRASS_BLOCK);
    }
}
