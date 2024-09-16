package de.frinshhd.logiclobby.itemsystem.items;

import de.frinshhd.logiclobby.Main;
import de.frinshhd.logiclobby.model.ClickItem;
import de.frinshhd.logiclobby.utils.ChatManager;
import de.frinshhd.logiclobby.utils.PlayerHashMap;
import de.frinshhd.logiclobby.utils.SpigotTranslator;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class PlayerHider {

    private static final PlayerHider playerHider = new PlayerHider();
    //hidden = false; shown = true
    public PlayerHashMap<UUID, Boolean> playersPlayerHider = new PlayerHashMap<>();

    public static PlayerHider getPlayerHider() {
        return playerHider;
    }

    public boolean toggle(Player player) {
        ClickItem playerHider = Main.getManager().getConfig().getClickItem("playerhider");

        if (hasPlayersHidden(player)) {
            save(player.getUniqueId(), true);

            //set item
            setItemPlayer(player);

            //show all players
            Bukkit.getOnlinePlayers().forEach(players -> {
                if (players.equals(player)) {
                    return;
                }

                player.showPlayer(Main.getInstance(), players);
            });

            ChatManager.sendMessage(player, SpigotTranslator.build("playerHider.show"));

            return false;
        } else {
            save(player.getUniqueId(), false);

            //set item
            setItemPlayer(player);

            //hide all players
            Bukkit.getOnlinePlayers().forEach(players -> {
                if (players.equals(player)) {
                    return;
                }

                player.hidePlayer(Main.getInstance(), players);
            });

            ChatManager.sendMessage(player, SpigotTranslator.build("playerHider.hide"));

            return true;
        }
    }

    public void setItemPlayer(Player player) {
        ClickItem playerHider = Main.getManager().getConfig().getClickItem("playerhider");

        if (playerHider == null) {
            return;
        }

        if (hasPlayersHidden(player)) {
            player.getInventory().setItem(playerHider.getSlot(), playerHider.getItem(true));
        } else {
            player.getInventory().setItem(playerHider.getSlot(), playerHider.getItem(false));
        }

    }

    public void save(UUID playerUUID, boolean hidden) {
        playersPlayerHider.put(playerUUID, hidden);
    }

    public boolean hasPlayersHidden(Player player) {
        if (playersPlayerHider.get(player.getUniqueId()) == null) {
            return false;
        }

        return !playersPlayerHider.get(player.getUniqueId());
    }

    public void onPlayerJoin(Player player) {
        PlayerHider.getPlayerHider().setItemPlayer(player);

        if (hasPlayersHidden(player)) {
            Bukkit.getOnlinePlayers().forEach(players -> {
                if (players.equals(player)) {
                    return;
                }

                player.hidePlayer(Main.getInstance(), players);
            });
        } else {
            Bukkit.getOnlinePlayers().forEach(players -> {
                if (players.equals(player)) {
                    return;
                }

                player.showPlayer(Main.getInstance(), players);
            });
        }
    }
}
