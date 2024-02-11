package de.frinshhd.logiclobby.utils;

import de.frinshhd.logiclobby.Main;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;

public class PlayerArrayList<K> extends ArrayList<K> implements Listener {


    public PlayerArrayList() {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }


    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        this.remove(player.getUniqueId());
    }

    @Override
    public boolean add(K k) {
        return super.add(k);
    }
}
