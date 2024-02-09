package de.frinshhd.anturnialobby.menusystem;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.frinshhd.anturnialobby.Main;
import de.frinshhd.anturnialobby.menusystem.library.Menu;
import de.frinshhd.anturnialobby.model.Config;
import de.frinshhd.anturnialobby.model.Server;
import de.frinshhd.anturnialobby.utils.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.jetbrains.annotations.NotNull;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class LobbySwitcherMenu extends Menu implements PluginMessageListener {

    private static HashMap<String, SavedItem> items = new HashMap<>();
    private final Config config = Main.getManager().getConfig();

    public LobbySwitcherMenu(Player player) {
        super(player);

        Main.getInstance().getServer().getMessenger().registerIncomingPluginChannel(Main.getInstance(), "BungeeCord", this);
    }

    @Override
    public int getSlots() {
        return config.getLobbySwitcher().getInventorySlots();
    }

    @Override
    public String getTitle() {
        return SpigotTranslator.build("lobbySwitcher.title");
    }

    @Override
    public void setItems() {
        if (config.hasCloudNetSupportEnabled()) {
            //Todo: CloudNet implementation
        } else {

            int currentSlot = 10;

            for (Server lobbyServers : config.getLobbySwitcher().getLobbyServers()) {
                getCount(player, lobbyServers.getServerName());

                ItemStack item = lobbyServers.getItem(config.getLobbySwitcher().getLobbyItem().getMaterialState(LobbyStates.UNREACHABLE));
                inventory.setItem(currentSlot, item);
                items.put(lobbyServers.getServerName(), new SavedItem(10, item));
            }
        }
    }

    @Override
    public void handle(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        ItemStack item = event.getCurrentItem();

        if (item == null || item.getType().equals(Material.AIR)) {
            return;
        }

        String id = ItemTags.extractItemId(item.getItemMeta());

        if (id == null) {
            return;
        }

        Server server = null;

        for (Server servers : config.getLobbySwitcher().getLobbyServers()) {
            if (servers.getId().equals(id)) {
                server = servers;
                break;
            }
        }

        if (server == null) {
            return;
        }

        Sounds.itemClick(player);

        server.execute(player);
        if (server.getMessage() != null) {
            player.sendMessage(MessageFormat.build(server.getMessage()));
        }
    }
    public void getCount(Player player, String server) {

        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("PlayerCount");
        out.writeUTF(server);

        player.sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
    }

    @Override
    public void onPluginMessageReceived(@NotNull String channel, @NotNull Player player, @NotNull byte[] message) {
        if (!channel.equals("BungeeCord")) {
            return;
        }

        ByteArrayDataInput in = ByteStreams.newDataInput(message);

        // Checking available bytes for reading the subchannel
        if (message.length < 2) {
            return;
        }

        // Read the subchannel
        String subchannel = in.readUTF();

        if (message.length < 2 + 2 + subchannel.getBytes(StandardCharsets.UTF_8).length) {
            return;
        }

        if (subchannel.equals("PlayerCount")) {
            // Read the server name
            String server = in.readUTF();

            // Checking available bytes for reading the integer
            // The length of the subchannel, server name, and the length of the integer
            if (message.length < 2 + 2 + subchannel.getBytes(StandardCharsets.UTF_8).length + 4) {
                return;
            }

            int playerCount = in.readInt();

            if (!player.getUniqueId().equals(this.player.getUniqueId())) {
                return;
            }

            if (inventory == null) {
                return;
            }

            if (!items.containsKey(server)) {
                return;
            }

            SavedItem savedItem = items.get(server);
            ItemStack item = savedItem.getItemStack();

            LobbyStates lobbyState = LobbyStates.UNREACHABLE;

            if (playerCount > 0) {
                lobbyState = LobbyStates.NORMAL;
            } else if (playerCount == 0) {
                lobbyState = LobbyStates.EMPTY;
            }

            item.setType(config.getLobbySwitcher().getLobbyItem().getMaterialState(lobbyState));

            savedItem.updateItemStack(item);
            inventory.setItem(savedItem.getSlot(), item);
        }
    }


    @Override
    public ItemStack getFillerItem() {
        return config.getLobbySwitcher().getFillerItem().getItem();
    }

    @Override
    public void open() {
        open(config.getLobbySwitcher().getFillerItem().getFillerType());
    }
}