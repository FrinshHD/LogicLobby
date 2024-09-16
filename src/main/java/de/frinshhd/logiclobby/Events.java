package de.frinshhd.logiclobby;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPickupArrowEvent;
import org.bukkit.inventory.ItemStack;

import static de.frinshhd.logiclobby.Main.getManager;

public class Events implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (getManager().getConfig().getEvents().isNoDamage()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPlayerHunger(FoodLevelChangeEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        if (getManager().getConfig().getEvents().isNoHunger()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (getManager().getConfig().getEvents().isNoBlockBreak() && !event.getPlayer().hasPermission("logiclobby.admin.build")) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (getManager().getConfig().getEvents().isNoBlockPlace() && !event.getPlayer().hasPermission("logiclobby.admin.build")) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        if (getManager().getConfig().getEvents().isNoItemDrop() && !event.getPlayer().hasPermission("logiclobby.admin.build")) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (getManager().getConfig().getEvents().isNoItemPickup() && !player.hasPermission("logiclobby.admin.build")) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onItemCraft(PrepareItemCraftEvent event) {
        event.getViewers().forEach(player -> {
            if (getManager().getConfig().getEvents().isNoItemCraft() && !player.hasPermission("logiclobby.admin.build")) {
                event.getInventory().setResult(new ItemStack(Material.AIR));
            }
        });
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        if (getManager().getConfig().getEvents().isNoItemConsume() && !event.getPlayer().hasPermission("logiclobby.admin.build")) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (getManager().getConfig().getEvents().isNoEntityDamage()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if (getManager().getConfig().getEvents().isNoEntitySpawn()) {
            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onUnderLowestY(PlayerMoveEvent event) {
        if (getManager().getConfig().getEvents().getLowestY() != null && event.getPlayer().getLocation().getY() < getManager().getConfig().getEvents().getLowestY()) {
            event.getPlayer().setFallDistance(0);
            event.getPlayer().teleport(event.getPlayer().getWorld().getSpawnLocation());
            return;
        }
    }

    @EventHandler
    public void onArrowPickup(PlayerPickupArrowEvent event) {
        if (!event.getPlayer().hasPermission("logiclobby.admin.build")) {
            event.setCancelled(true);
            event.getArrow().remove();
            return;
        }
    }
}
