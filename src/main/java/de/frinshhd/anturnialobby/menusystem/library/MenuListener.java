package de.frinshhd.anturnialobby.menusystem.library;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;

public class MenuListener implements Listener {

    @EventHandler
    public void onMenuClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            return;
        }

        InventoryHolder holder = event.getClickedInventory().getHolder();

        if (event.getCurrentItem() == null) {
            return;
        }

        if (holder instanceof Menu menu) {
            event.setCancelled(true);

            menu.handle(event);
        }
    }
}
