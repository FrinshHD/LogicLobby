package de.frinshhd.logiclobby.menusystem;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.frinshhd.logiclobby.Main;
import de.frinshhd.logiclobby.menusystem.library.Menu;
import de.frinshhd.logiclobby.model.Config;
import de.frinshhd.logiclobby.model.Server;
import de.frinshhd.logiclobby.utils.*;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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

            int currentSlot = 0;

            // Iterating through the inventory
            for (int i = 0; i < getSlots(); i++) {
                // Checking if the field is not in the left or right column
                if (i % 9 != 0 && i % 9 != 8) {
                    // Checking if the field is not in the first or last row
                    if (i >= 9 && i < getSlots() - 9) {
                        // Making sure we do not go beyond the bounds of the list
                        if (currentSlot < config.getLobbySwitcher().getLobbyServers().size()) {
                            // Getting the custom block from the list
                            Server lobbyServer = config.getLobbySwitcher().getLobbyServers().get(currentSlot);
                            // Filling the field with the custom block

                            getCount(player, lobbyServer.getServerName());

                            ItemStack item = lobbyServer.getItem(config.getLobbySwitcher().getLobbyItem().getMaterialState(LobbyStates.UNREACHABLE));
                            inventory.setItem(i, item);
                            items.put(lobbyServer.getServerName(), new SavedItem(i, item, lobbyServer, lobbyServer.getDescription()));

                            currentSlot++; // Move to the next custom block in the list
                        } else {
                            // If the list of custom blocks is exhausted, break
                            break;
                        }
                    }
                }
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
            ItemMeta itemMeta = item.getItemMeta();

            LobbyStates lobbyState = LobbyStates.UNREACHABLE;

            if (playerCount > 0) {
                lobbyState = LobbyStates.NORMAL;
            } else if (playerCount == 0) {
                lobbyState = LobbyStates.EMPTY;
            }

            if (server.equals(Main.getManager().getServerName())) {
                lobbyState = LobbyStates.CONNECTED;
            }

            item.setType(config.getLobbySwitcher().getLobbyItem().getMaterialState(lobbyState));

            String lore = SpigotTranslator.replacePlaceholders(savedItem.getLore(), new TranslatorPlaceholder("playercount", String.valueOf(playerCount)));

            itemMeta.setLore(LoreBuilder.build(lore, ChatColor.getByChar(SpigotTranslator.build("items.standardDescriptionColor").substring(1))));
            item.setItemMeta(itemMeta);


            savedItem.updateItemStack(item);
            savedItem.updateLore(lore);
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
