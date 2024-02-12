package de.frinshhd.logiclobby;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import de.frinshhd.logiclobby.model.Config;
import de.frinshhd.logiclobby.utils.SpigotTranslator;
import de.frinshhd.logiclobby.utils.TranslatorPlaceholder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Manager implements PluginMessageListener, Listener {
    private static Config config;
    private String serverName = "";

    public Manager(boolean notRegistered) {
        Main.getInstance().getServer().getMessenger().registerIncomingPluginChannel(Main.getInstance(), "BungeeCord", this);

        new BukkitRunnable() {
            @Override
            public void run() {
                ByteArrayDataOutput out = ByteStreams.newDataOutput();
                out.writeUTF("GetServer");

                Main.getInstance().getServer().sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
            }
        }.runTaskLater(Main.getInstance(), 8L);

        Main.getInstance().getServer().getPluginManager().registerEvents(this, Main.getInstance());

        init();
    }

    public void init() {
        load();

        config.getItems().forEach(item -> {
            Main.getItemManager().addItem(item);
        });
    }

    public Config getConfig() {
        return config;
    }

    public void load() {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        try {
            config = mapper.readValue(Manager.class.getClassLoader().getResourceAsStream("config.yml"), Config.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPlayerToServer(Player player, String server) {
        if (getServerName().equals(server)) {
            return;
        }


        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(Main.getInstance(), "BungeeCord", b.toByteArray());
            b.close();
            out.close();
        } catch (Exception e) {
            player.sendMessage(ChatColor.RED + "Error when trying to connect to " + server);
        }
    }

    public String getServerName() {
        return this.serverName;
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

        if (subchannel.equals("GetServer")) {
            String serverName = in.readUTF();

            if (!this.serverName.equals(serverName)) {
                this.serverName = serverName;
            }
        }
    }

    public void checkForServerName() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF("GetServer");

        Main.getInstance().getServer().sendPluginMessage(Main.getInstance(), "BungeeCord", out.toByteArray());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (config.getDefaultHotbarSlot() > -1) {
            player.getInventory().setHeldItemSlot(config.getDefaultHotbarSlot());
        }

        event.setJoinMessage(SpigotTranslator.build("player.join", new TranslatorPlaceholder("player", player.getName())));

        player.getInventory().clear();
        player.updateInventory();

        config.getItems().forEach(item -> {
            if (item.getSlot() > -1) {
                player.getInventory().setItem(item.getSlot(), item.getItem());
            }
        });

        player.updateInventory();

        if (config.getSpawn().isTeleportOnJoin()) {
            player.teleport(config.getSpawn().getLocation());
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        event.setQuitMessage(SpigotTranslator.build("player.leave", new TranslatorPlaceholder("player", player.getName())));
    }
}
