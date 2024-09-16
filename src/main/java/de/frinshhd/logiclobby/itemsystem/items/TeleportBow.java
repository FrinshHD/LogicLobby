package de.frinshhd.logiclobby.itemsystem.items;

import de.frinshhd.logiclobby.Main;
import de.frinshhd.logiclobby.model.ClickItem;
import de.frinshhd.logiclobby.utils.PlayerHashMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Locale;
import java.util.Objects;
import java.util.UUID;

public class TeleportBow implements Listener {

    private static final TeleportBow teleportBow = new TeleportBow(true);

    public static TeleportBow getTeleportBow() {
        return teleportBow;
    }

    public TeleportBow(boolean notRegistered) {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }


    // player, arrow
    private PlayerHashMap<UUID, UUID> playerArrow = new PlayerHashMap<>();

    public void setItemPlayer(Player player) {
        if (!player.hasPermission("logiclobby.gadget.teleportBow")) {
            return;
        }

        ClickItem teleportBow = Main.getManager().getConfig().getClickItem("teleportbow");

        if (teleportBow == null) {
            return;
        }

        player.getInventory().setItem(teleportBow.getSlot(), teleportBow.getItem());
        setPlayerArrow(player);
    }

    public void setPlayerArrow(Player player) {
        ClickItem teleportBow = Main.getManager().getConfig().getClickItem("teleportbow");
        teleportBow.getItems().forEach(item -> {
            player.getInventory().setItem(item.getSlot(), item.getItem());
        });
    }

    public void onPlayerJoin(Player player) {
        TeleportBow.getTeleportBow().setItemPlayer(player);
    }

    public void onBowSpawn(EntitySpawnEvent event) {
        event.setCancelled(false);

        Arrow arrow = (Arrow) event.getEntity();

        if (arrow.getShooter() == null || !(arrow.getShooter() instanceof Player)) {
            return;
        }

        Player player = (Player) arrow.getShooter();

        playerArrow.put(player.getUniqueId(), arrow.getUniqueId());
        setPlayerArrow(player);

        ClickItem teleportBow = Main.getManager().getConfig().getClickItem("teleportbow");
        player.setCooldown(teleportBow.getItem().getType(), 40);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (!(event.getEntity() instanceof Arrow)) {
            return;
        }

        if (event.getEntity().getShooter() == null || !(event.getEntity().getShooter() instanceof Player)) {
            return;
        }

        Bukkit.getScheduler().runTask(Main.getInstance(), () -> {
            Arrow arrow = (Arrow) event.getEntity();
            Player player = (Player) arrow.getShooter();

            if (playerArrow.containsKey(player.getUniqueId()) && Objects.equals(playerArrow.get(player.getUniqueId()), arrow.getUniqueId())) {
                arrow.remove();

                Location location = arrow.getLocation().clone();
                location.setYaw(player.getLocation().getYaw());
                location.setPitch(player.getLocation().getPitch());

                player.teleport(location);
                playerArrow.remove(player.getUniqueId());
            }
        });
    }

}
