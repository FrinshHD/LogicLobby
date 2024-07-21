package de.frinshhd.logiclobby.itemsystem;

import de.frinshhd.logiclobby.Main;
import de.frinshhd.logiclobby.utils.ItemTags;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ListIterator;

public class LogicItemListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if (event.getItem() == null) {
            return;
        }

        if (event.getItem().getItemMeta() == null) {
            return;
        }

        String itemId = ItemTags.extractItemId(event.getItem().getItemMeta());

        if (itemId == null) {
            return;
        }

        if (!Main.getItemManager().getItems().containsKey(itemId)) {
            return;
        }


        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK) || event.getAction().equals(Action.PHYSICAL)) {
            // If player pressed left click
            Main.getItemManager().getItems().get(itemId).use(event.getPlayer());
        } else if (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            // If player pressed right click
            Main.getItemManager().getItems().get(itemId).use(event.getPlayer());
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (event.getItemInHand().getItemMeta() == null) {
            return;
        }

        String itemId = ItemTags.extractItemId(event.getPlayer().getInventory().getItemInMainHand().getItemMeta());

        if (itemId == null) {
            return;
        }

        // Check if the item is a clickItem
        if (!Main.getItemManager().getItems().containsKey(itemId)) {
            return;
        }

        // Deny the player placing the block
        event.setBuild(false);
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (event.getItemDrop().getItemStack().getItemMeta() == null) {
            System.out.println("1");
            return;
        }

        String itemId = ItemTags.extractItemId(event.getItemDrop().getItemStack().getItemMeta());

        if (itemId == null) {
            System.out.println("2");
            return;
        }

        // Check if the item is a clickItem
        if (!Main.getItemManager().getItems().containsKey(itemId)) {
            System.out.println("3");
            return;
        }

        // Deny the player dropping the block
        System.out.println("4");
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryItemMove(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        if (event.getCurrentItem().getItemMeta() == null) {
            return;
        }

        String itemId = ItemTags.extractItemId(event.getCurrentItem().getItemMeta());

        if (itemId == null) {
            return;
        }

        // Check if the item is a clickItem
        if (!Main.getItemManager().getItems().containsKey(itemId)) {
            return;
        }

        // Deny the player dropping the block
        Bukkit.getPluginManager().callEvent(new PlayerInteractEvent((Player) event.getWhoClicked(), Action.RIGHT_CLICK_AIR, event.getCurrentItem(), null, null));
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        for (ItemStack item : event.getPlayer().getInventory()) {
            if (item == null) {
                continue;
            }

            if (item.getItemMeta() == null) {
                continue;
            }

            String itemId = ItemTags.extractItemId(item.getItemMeta());

            if (itemId == null) {
                continue;
            }

            // Check if the item is a clickItem
            if (!Main.getItemManager().getItems().containsKey(itemId)) {
                continue;
            }

            // Drop the item
            event.getItemsToKeep().add(item);
            event.getDrops().remove(item);
        }
    }

    @EventHandler
    public void onBlockDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (event.getCause().equals(EntityDamageEvent.DamageCause.SUFFOCATION)) {
            event.setCancelled(true);
        }
    }
}
