package de.frinshhd.logiclobby.mysql;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.table.TableUtils;
import de.frinshhd.logiclobby.mysql.entities.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class MysqlManager implements Listener {

    public static JdbcConnectionSource connectionSource;

    public static Dao<Items, Long> getItemsDao() throws SQLException {
        return DaoManager.createDao(connectionSource, Items.class);
    }

    public static void connect(String url) {
        connect(url, null, null);
    }

    public static void connect(String url, String userName, String password) {
        if (userName == null && password == null) {
            try {
                connectionSource = new JdbcConnectionSource(url);
            } catch (SQLException e) {
                createNewDatabase();
                connect(url, userName, password);
            }
        } else {
            try {
                connectionSource = new JdbcConnectionSource(url, userName, password);
            } catch (SQLException e) {
                //Todo: logging
            }
        }

        try {
            TableUtils.createTableIfNotExists(connectionSource, Items.class);
        } catch (SQLException e) {
            //Todo logging
        }
    }

    public static Items getItemsPlayer(UUID uuid) {
        Dao<Items, Long> itemsDao;
        try {
            itemsDao = getItemsDao();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        List<Items> items;
        try {
            items = itemsDao.queryForEq("uuid", uuid);
        } catch (SQLException e) {
            return null;
        }

        if (items.isEmpty()) {
            return null;
        }

        return items.get(0);
    }

    public static void disconnect() throws Exception {
        connectionSource.close();
    }

    /*public static void checkConnection() throws Exception {
        if (connectionSource.isOpen("Quests")) {
            return;
        }

        disconnect();
        connect();
    } */

    public static void createNewDatabase() {

        String url = "jdbc:sqlite:plugins/LogicLobby/sqlite.db";

        try {
            Connection conn = DriverManager.getConnection(url);
            if (conn != null) {
                DatabaseMetaData meta = conn.getMetaData();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPLayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        //create items
        if (MysqlManager.getItemsPlayer(player.getUniqueId()) == null) {
            Dao<Items, Long> questDao;
            try {
                questDao = MysqlManager.getItemsDao();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            Items quest = new Items();
            quest.create(player.getUniqueId());
            try {
                questDao.create(quest);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
