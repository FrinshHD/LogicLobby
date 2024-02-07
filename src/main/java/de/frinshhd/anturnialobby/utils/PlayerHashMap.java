package de.frinshhd.anturnialobby.utils;

import de.frinshhd.anturnialobby.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;

public class PlayerHashMap<K, V> extends HashMap<K, V> implements Listener {


    public PlayerHashMap() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.remove(player.getUniqueId());
    }
}
