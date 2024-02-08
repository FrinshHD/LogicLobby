package de.frinshhd.anturnialobby;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import de.frinshhd.anturnialobby.model.Config;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Manager {
    private static Config config;

    public Manager() {
        init();
    }

    public void init() {
        load();
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
        try {
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            DataOutputStream out = new DataOutputStream(b);
            out.writeUTF("Connect");
            out.writeUTF(server);
            player.sendPluginMessage(Main.getInstance(), "BungeeCord", b.toByteArray());
            b.close();
            out.close();
        }
        catch (Exception e) {
            player.sendMessage(ChatColor.RED+"Error when trying to connect to "+server);
        }
    }
}
