package de.frinshhd.logiclobby;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

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
                 ENTITY_EXPLOSION:
                event.setCancelled(true);
        }
    }

}
