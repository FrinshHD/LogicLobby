package de.frinshhd.logiclobby;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

public class Events implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        switch (event.getCause()) {
            case SUFFOCATION,
                 FALL,
                 FIRE,
                 ENTITY_ATTACK,
                 ENTITY_EXPLOSION,
                 PROJECTILE:
                event.setCancelled(true);
                break;

            case VOID:
                event.getEntity().teleport(Main.getManager().getConfig().getSpawn().getLocation());
                event.setDamage(0);
                event.getEntity().setFallDistance(0);
                break;
        }
    }

    @EventHandler
    public void onPlayerHunger(FoodLevelChangeEvent event){
        if (!(event.getEntity() instanceof Player)) {
            return;
        }

        event.setCancelled(true);
    }
}
