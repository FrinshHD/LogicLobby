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
import eu.cloudnetservice.driver.inject.InjectionLayer;
import eu.cloudnetservice.driver.provider.CloudServiceProvider;
import eu.cloudnetservice.driver.registry.ServiceRegistry;
import eu.cloudnetservice.driver.service.ServiceInfoSnapshot;
import eu.cloudnetservice.modules.bridge.BridgeDocProperties;
import eu.cloudnetservice.modules.bridge.BridgeServiceHelper;
import eu.cloudnetservice.modules.bridge.player.CloudPlayer;
import eu.cloudnetservice.modules.bridge.player.PlayerManager;
import eu.cloudnetservice.modules.bridge.player.executor.PlayerExecutor;
import eu.cloudnetservice.modules.bridge.player.executor.ServerSelectorType;
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

public class TeleporterMenu extends Menu implements PluginMessageListener {

    private static HashMap<String, SavedItem> items = new HashMap<>();
    private final Config config = Main.getManager().getConfig();
    private Manager manager;

    public TeleporterMenu(Player player) {
        super(player);

        Main.getInstance().getServer().getMessenger().registerIncomingPluginChannel(Main.getInstance(), "BungeeCord", this);
    }

    public static boolean isService(String input) {
        // Check if the input string is not null and has at least 2 characters
        if (input != null && input.length() >= 2) {
            // Check if the last character is a hyphen
            if (input.charAt(input.length() - 1) == '-') {
                // Extract the substring excluding the hyphen
                String numberPart = input.substring(0, input.length() - 1);

                // Check if the remaining part is a positive number
                try {
                    int number = Integer.parseInt(numberPart);
                    // Check if the number is positive
                    if (number >= 0) {
                        return true;
                    }
                } catch (NumberFormatException e) {
                    // If parsing to integer fails, it means the remaining part is not a number
                    return false;
                }
            }
        }
        return false;
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
            //CloudNet v4 implementation
            config.getTeleporter().getServers().forEach(server -> {
                if (server.getItemSlot() > -1) {
                    String task = null;
                    String service = null;
                    int playercount = 0;
                    boolean status = false;

                    if (server.getTask() != null) {
                        task = server.getTask();
                    } else if (server.getServerName() != null) {
                        service = server.getServerName();
                    }

                    if (task != null) {
                        List<ServiceInfoSnapshot> services = InjectionLayer.ext().instance(CloudServiceProvider.class).servicesByTask(task).stream().toList();
                        for (ServiceInfoSnapshot serviceInfoSnapshot : services) {
                            Integer servicePlayerCount = serviceInfoSnapshot.readProperty(BridgeDocProperties.ONLINE_COUNT);
                            if (servicePlayerCount != null) {
                                if (servicePlayerCount >= 0) {
                                    status = true;
                                    playercount += servicePlayerCount;
                                }
                            }
                        }
                    } else if (service != null) {
                        ServiceInfoSnapshot serviceInfoSnapshot = InjectionLayer.ext().instance(CloudServiceProvider.class).serviceByName(service);

                        if (serviceInfoSnapshot != null) {

                            Integer servicePlayerCount = serviceInfoSnapshot.readProperty(BridgeDocProperties.ONLINE_COUNT);

                            if (servicePlayerCount != null) {
                                if (servicePlayerCount >= 0) {
                                    playercount += servicePlayerCount;
                                    status = true;
                                }
                            }
                        }
                    }

                    if (task == null && service == null) {
                        service = server.getServerName();
                    }

                    ItemStack item;

                    if (task != null) {
                        item = server.getItem(task);
                    } else {
                        item = server.getItem(service);
                    }

                    ItemMeta itemMeta = item.getItemMeta();

                    String lore;
                    if (status) {
                        lore = SpigotTranslator.replacePlaceholders(server.getDescription(), new TranslatorPlaceholder("playercount", String.valueOf(playercount)), new TranslatorPlaceholder("status", SpigotTranslator.build("status.online")));
                    } else {
                        lore = SpigotTranslator.replacePlaceholders(server.getDescription(), new TranslatorPlaceholder("playercount", String.valueOf(playercount)), new TranslatorPlaceholder("status", SpigotTranslator.build("status.offline")));
                    }

                    itemMeta.setLore(LoreBuilder.build(lore, ChatColor.getByChar(SpigotTranslator.build("items.standardDescriptionColor").substring(1))));

                    item.setItemMeta(itemMeta);

                    inventory.setItem(server.getItemSlot(), item);
                }
            });
        } else {
            config.getTeleporter().getServers().forEach(server -> {
                if (server.getItemSlot() > -1) {
                    getCount(player, server.getServerName());

                    ItemStack item = server.getItem();
                    ItemMeta itemMeta = item.getItemMeta();

                    String lore = SpigotTranslator.replacePlaceholders(server.getDescription(), new TranslatorPlaceholder("playercount", "0"), new TranslatorPlaceholder("status", SpigotTranslator.build("status.offline")));

                    itemMeta.setLore(LoreBuilder.build(lore, ChatColor.getByChar(SpigotTranslator.build("items.standardDescriptionColor").substring(1))));

                    item.setItemMeta(itemMeta);

                    inventory.setItem(server.getItemSlot(), item);
                    items.put(server.getServerName(), new SavedItem(server.getItemSlot(), item, server, lore));
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

        if (config.hasCloudNetSupportEnabled()) {
            if (isService(id)) {
                //if it is a service
                List<ServiceInfoSnapshot> services = InjectionLayer.ext().instance(CloudServiceProvider.class).services().stream().toList();
                ServiceInfoSnapshot service = null;

                for (ServiceInfoSnapshot ser : services) {
                    if (ser.name().equals(id)) {
                        service = ser;
                        break;
                    }
                }

                if (service != null) {

                    BridgeServiceHelper.ServiceInfoState state = BridgeServiceHelper.guessStateFromServiceInfoSnapshot(service);

                    if (state.equals(BridgeServiceHelper.ServiceInfoState.STOPPED)) {
                        return;
                    }

                    ServiceRegistry serviceRegistry = InjectionLayer.ext().instance(ServiceRegistry.class);
                    PlayerManager playerManager = serviceRegistry.firstProvider(PlayerManager.class);

                    CloudPlayer cloudPlayer = playerManager.onlinePlayer(player.getUniqueId());

                    PlayerExecutor playerExecutor = playerManager.playerExecutor(Objects.requireNonNull(cloudPlayer).uniqueId());


                    playerExecutor.connect(service.name());
                    return;
                }
            } else {
                //id it is a task
                int services = InjectionLayer.ext().instance(CloudServiceProvider.class).serviceCountByTask(id);

                if (services > 0) {

                    ServiceRegistry serviceRegistry = InjectionLayer.ext().instance(ServiceRegistry.class);
                    PlayerManager playerManager = serviceRegistry.firstProvider(PlayerManager.class);

                    CloudPlayer cloudPlayer = playerManager.onlinePlayer(player.getUniqueId());

                    PlayerExecutor playerExecutor = playerManager.playerExecutor(Objects.requireNonNull(cloudPlayer).uniqueId());


                    //Todo: let admin choose what ServerSelectorType he wants
                    playerExecutor.connectToTask(id, ServerSelectorType.RANDOM);
                    return;
                }
            }
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
        return config.getTeleporter().getFillerItem().getItem();
    }

    @Override
    public void open() {
        open(config.getTeleporter().getFillerItem().getFillerType());
    }
}
