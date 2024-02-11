package de.frinshhd.logiclobby.menusystem;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.frinshhd.logiclobby.Main;
import de.frinshhd.logiclobby.Manager;
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

public class TeleporterMenu extends Menu implements PluginMessageListener {

    private final Config config = Main.getManager().getConfig();

    private static HashMap<String, SavedItem> items = new HashMap<>();

    private Manager manager;

    public TeleporterMenu(Player player) {
        super(player);

        Main.getInstance().getServer().getMessenger().registerIncomingPluginChannel(Main.getInstance(), "BungeeCord", this);
    }

    @Override
    public String getTitle() {
        return SpigotTranslator.build("teleporter.title");
    }

    @Override
    public int getSlots() {
        return config.getTeleporter().getInventorySlots();
    }

    @Override
    public void setItems() {
        //check if CloudNet is used or not
        if (config.hasCloudNetSupportEnabled()) {
            //Todo: CloudNet integration

        } else {
            config.getTeleporter().getServers().forEach(server -> {
                if (server.getItemSlot() > -1) {
                    getCount(player, server.getServerName());

                    ItemStack item = server.getItem();
                    inventory.setItem(server.getItemSlot(), item);
                    items.put(server.getServerName(), new SavedItem(server.getItemSlot(), item, server, server.getDescription()));
                }
            });
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

        for (Server servers : config.getTeleporter().getServers()) {
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

        if (server == null) {
            return;
        }

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
        return config.getTeleporter().getFillerItem().getItem();
    }

    @Override
    public void open() {
        open(config.getTeleporter().getFillerItem().getFillerType());
    }
}
