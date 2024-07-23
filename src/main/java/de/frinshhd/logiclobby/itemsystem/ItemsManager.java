package de.frinshhd.logiclobby.itemsystem;

import com.j256.ormlite.dao.Dao;
import de.frinshhd.logiclobby.Main;
import de.frinshhd.logiclobby.itemsystem.items.PlayerHider;
import de.frinshhd.logiclobby.model.ClickItem;
import de.frinshhd.logiclobby.mysql.MysqlManager;
import de.frinshhd.logiclobby.mysql.entities.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.sql.SQLException;
import java.util.HashMap;

public class ItemsManager implements Listener {

    private HashMap<String, ClickItem> items = new HashMap<>();

    public ItemsManager(boolean notGenerated) {
        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());
    }

    public void addItem(ClickItem item) {
        items.put(item.getId(), item);
    }

    public HashMap<String, ClickItem> getItems() {
        return this.items;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        //playerhider
        {
            boolean bool = true;

            if (MysqlManager.getItemsPlayer(player.getUniqueId()).getItems().get("playerhider") != null) {
                bool = (boolean) MysqlManager.getItemsPlayer(player.getUniqueId()).getItems().get("playerhider");
            }

            PlayerHider.getPlayerHider().playersPlayerHider.put(player.getUniqueId(), bool);

            PlayerHider.getPlayerHider().setItemPlayer(player);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();


        Dao<Items, Long> itemsDao;
        try {
            itemsDao = MysqlManager.getItemsDao();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Items items;
        try {
            items = MysqlManager.getItemsPlayer(player.getUniqueId());

            if (items == null) {
                //Todo: throw error message because player has no requirements object registered in db
                return;
            }

            HashMap<String, Object> object = items.getItems();

            items.putItems("playerhider", PlayerHider.getPlayerHider().playersPlayerHider.get(player.getUniqueId()));

            itemsDao.update(items);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
