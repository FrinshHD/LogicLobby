package de.frinshhd.logiclobby.itemsystem;

import de.frinshhd.logiclobby.Main;
import de.frinshhd.logiclobby.itemsystem.items.TeleportBow;
import de.frinshhd.logiclobby.utils.ItemTags;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;

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

        if (p.getCooldown(event.getItem().getType()) > 0) {
            return;
        }

        if (event.getAction().equals(Action.LEFT_CLICK_AIR) || event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
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
            return;
        }

        String itemId = ItemTags.extractItemId(event.getItemDrop().getItemStack().getItemMeta());

        if (itemId == null) {
            return;
        }

        // Check if the item is a clickItem
        if (!Main.getItemManager().getItems().containsKey(itemId)) {
            return;
        }

        // Deny the player dropping the block
        event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryItemMove(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) {
            return;
        }

        if (event.getCurrentItem() == null) {
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
    public void onItemOffHandSwitch(PlayerSwapHandItemsEvent event) {
        if (event.getMainHandItem() == null && event.getOffHandItem() == null) {
            return;
        }

        if (event.getMainHandItem().getItemMeta() == null && event.getOffHandItem().getItemMeta() == null) {
            return;
        }

        if (event.getMainHandItem() != null && event.getMainHandItem().getItemMeta() != null) {
            String itemIdMain = ItemTags.extractItemId(event.getMainHandItem().getItemMeta());

            if (itemIdMain == null) {
                return;
            }

            // Check if the item is a clickItem
            if (!Main.getItemManager().getItems().containsKey(itemIdMain)) {
                return;
            }

            event.setCancelled(true);
            return;
        }

        if (event.getOffHandItem() != null && event.getOffHandItem().getItemMeta() != null) {
            String itemIdOff = ItemTags.extractItemId(event.getOffHandItem().getItemMeta());

            if (itemIdOff == null) {
                return;
            }

            // Check if the item is a clickItem
            if (!Main.getItemManager().getItems().containsKey(itemIdOff)) {
                return;
            }

            event.setCancelled(true);
            return;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onEntitySpawn(EntitySpawnEvent event) {

        if (!(event.getEntity() instanceof Arrow arrow)) return;

        if (arrow.getShooter() == null) return;

        Player player = arrow.getShooter() instanceof Player ? (Player) arrow.getShooter() : null;

        if (player == null) return;

        if (player.getInventory().getItemInMainHand().getItemMeta() == null) return;

        String itemId = ItemTags.extractItemId(player.getInventory().getItemInMainHand().getItemMeta());

        if (itemId == null) return;

        if (itemId.equals("teleportbow")) TeleportBow.getTeleportBow().onBowSpawn(event);
    }
}
