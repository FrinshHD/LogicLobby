package de.frinshhd.logiclobby.itemsystem;

import de.frinshhd.logiclobby.Main;
import de.frinshhd.logiclobby.utils.ItemTags;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class LogicItemListener implements Listener {

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerUse(PlayerInteractEvent event) {
        Player p = event.getPlayer();

        if (p.getInventory().getItemInMainHand().getItemMeta() == null) {
            return;
        }

        String itemId = ItemTags.extractItemId(p.getInventory().getItemInMainHand().getItemMeta());

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

}
