package de.frinshhd.logiclobby.menusystem;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.frinshhd.logiclobby.Main;
import de.frinshhd.logiclobby.menusystem.library.Menu;
import de.frinshhd.logiclobby.model.Config;
import de.frinshhd.logiclobby.model.ConfigServer;
import de.frinshhd.logiclobby.utils.*;
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.driver.provider.CloudServiceProvider;
import eu.cloudnetservice.driver.registry.ServiceRegistry;
import eu.cloudnetservice.driver.service.ServiceInfoSnapshot;
import eu.cloudnetservice.modules.bridge.BridgeDocProperties;
import eu.cloudnetservice.modules.bridge.BridgeServiceHelper;
import eu.cloudnetservice.modules.bridge.BridgeServiceHelper.ServiceInfoState;
import eu.cloudnetservice.modules.bridge.player.CloudPlayer;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import eu.cloudnetservice.modules.bridge.player.executor.PlayerExecutor;
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
import java.util.List;
import java.util.Objects;

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
            //CloudNet v4 implementation
            List<ServiceInfoSnapshot> services = InjectionLayer.ext().instance(CloudServiceProvider.class).servicesByTask(config.getLobbySwitcher().getLobbyTask().getTaskName()).stream().toList();
            int currentSlot = 0;

            // Iterating through the inventory
            for (int i = 0; i < getSlots(); i++) {
                // Checking if the field is not in the left or right column
                if (i % 9 != 0 && i % 9 != 8) {
                    // Checking if the field is not in the first or last row
                    if (i >= 9 && i < getSlots() - 9) {
                        // Making sure we do not go beyond the bounds of the list
                        if (currentSlot < services.size()) {
                            // Getting the custom block from the list
                            ServiceInfoSnapshot lobbyServer = services.get(currentSlot);
                            ServiceInfoState state = BridgeServiceHelper.guessStateFromServiceInfoSnapshot(lobbyServer);

                            LobbyState lobbyState = LobbyState.UNREACHABLE;
                            int playerCount = 0;

                            if (state.equals(ServiceInfoState.ONLINE) ||
                                    state.equals(ServiceInfoState.STARTING) ||
                                    state.equals(ServiceInfoState.EMPTY_ONLINE) ||
                                    state.equals(ServiceInfoState.FULL_ONLINE)) {
                                playerCount = lobbyServer.readProperty(BridgeDocProperties.ONLINE_COUNT);

                                if (state.equals(ServiceInfoState.FULL_ONLINE)) {
                                    lobbyState = LobbyState.NORMAL;
                                } else if (state.equals(ServiceInfoState.STARTING)) {
                                    lobbyState = LobbyState.UNREACHABLE;
                                } else if (state.equals(ServiceInfoState.EMPTY_ONLINE)) {
                                    lobbyState = LobbyState.EMPTY;
                                }

                                if (Main.getManager().getCloudNetServiceName().equals(lobbyServer.name())) {
                                    lobbyState = LobbyState.CONNECTED;
                                }
                            }

                            boolean status = !lobbyState.equals(LobbyState.UNREACHABLE);

                            // Filling the field with the custom block
                            ItemStack item = config.getLobbySwitcher().getLobbyTask().getItem(lobbyServer.name(), config.getLobbySwitcher().getLobbyItem().getMaterialState(lobbyState));

                            ItemMeta itemMeta = item.getItemMeta();

                            String lore;
                            if (status) {
                                lore = SpigotTranslator.replacePlaceholders(config.getLobbySwitcher().getLobbyTask().getDescription(), new TranslatorPlaceholder("playercount", String.valueOf(playerCount)), new TranslatorPlaceholder("status", SpigotTranslator.build("status.online")));
                            } else {
                                lore = SpigotTranslator.replacePlaceholders(config.getLobbySwitcher().getLobbyTask().getDescription(), new TranslatorPlaceholder("playercount", String.valueOf(playerCount)), new TranslatorPlaceholder("status", SpigotTranslator.build("status.offline")));
                            }

                            itemMeta.setLore(LoreBuilder.build(lore, ChatColor.getByChar(SpigotTranslator.build("items.standardDescriptionColor").substring(1))));

                            item.setItemMeta(itemMeta);

                            inventory.setItem(i, item);
                            currentSlot++; // Move to the next custom block in the list
                        } else {
                            // If the list of custom blocks is exhausted, break
                            break;
                        }
                    }
                }
            }
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
                            ConfigServer lobbyserver = config.getLobbySwitcher().getLobbyServers().get(currentSlot);
                            // Filling the field with the custom block

                            int slot = i;

                            if (lobbyserver.getItemSlot() >= 0 && lobbyserver.getItemSlot() < getSlots()) {
                                slot = lobbyserver.getItemSlot();
                            }

                            getCount(player, lobbyserver.getServerName());

                            ItemStack item = lobbyserver.getItem(config.getLobbySwitcher().getLobbyItem().getMaterialState(LobbyState.UNREACHABLE));

                            ItemMeta itemMeta = item.getItemMeta();

                            String lore = SpigotTranslator.replacePlaceholders(lobbyserver.getDescription(), new TranslatorPlaceholder("playercount", "0"), new TranslatorPlaceholder("status", SpigotTranslator.build("status.offline")));

                            itemMeta.setLore(LoreBuilder.build(lore, ChatColor.getByChar(SpigotTranslator.build("items.standardDescriptionColor").substring(1))));

                            item.setItemMeta(itemMeta);

                            inventory.setItem(slot, item);
                            items.put(lobbyserver.getServerName(), new SavedItem(slot, item, lobbyserver, lobbyserver.getDescription()));

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

        if (config.hasCloudNetSupportEnabled()) {
            List<ServiceInfoSnapshot> services = InjectionLayer.ext().instance(CloudServiceProvider.class).services().stream().toList();
            ServiceInfoSnapshot service = null;

            for (ServiceInfoSnapshot ser : services) {
                if (ser.name().equals(id)) {
                    service = ser;
                    break;
                }
            }

            if (service == null) {
                return;
            }

            ServiceInfoState state = BridgeServiceHelper.guessStateFromServiceInfoSnapshot(service);

            if (state.equals(ServiceInfoState.STARTING) || state.equals(ServiceInfoState.STOPPED)) {
                return;
            }

            ServiceRegistry serviceRegistry = InjectionLayer.ext().instance(ServiceRegistry.class);
            PlayerManager playerManager = serviceRegistry.firstProvider(PlayerManager.class);

            CloudPlayer cloudPlayer = playerManager.onlinePlayer(player.getUniqueId());

            PlayerExecutor playerExecutor = playerManager.playerExecutor(Objects.requireNonNull(cloudPlayer).uniqueId());


            playerExecutor.connect(service.name());
            return;
        }

        ConfigServer server = null;

        for (ConfigServer servers : config.getLobbySwitcher().getLobbyServers()) {
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

            LobbyState lobbyState = LobbyState.UNREACHABLE;
            boolean status = false;

            if (playerCount > 0) {
                lobbyState = LobbyState.NORMAL;
                status = true;
            } else if (playerCount == 0) {
                lobbyState = LobbyState.EMPTY;
                status = true;
            }

            if (server.equals(Main.getManager().getServerName())) {
                lobbyState = LobbyState.CONNECTED;
                status = true;
            }

            item.setType(config.getLobbySwitcher().getLobbyItem().getMaterialState(lobbyState));

            String lore = SpigotTranslator.replacePlaceholders(savedItem.getServer().getDescription(), new TranslatorPlaceholder("playercount", String.valueOf(playerCount)), new TranslatorPlaceholder("status", SpigotTranslator.build("status.online")));

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
