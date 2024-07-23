package de.frinshhd.logiclobby;

import de.frinshhd.logiclobby.itemsystem.ItemsManager;
import de.frinshhd.logiclobby.utils.*;
import org.bukkit.plugin.java.JavaPlugin;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

public final class Main extends JavaPlugin {
    public static String version;
    private static JavaPlugin instance;
    private static Manager manager;
    private static ItemsManager itemsManager;

    public static JavaPlugin getInstance() {
        return instance;
    }

    public static Manager getManager() {
        return manager;
    }

    public static ItemsManager getItemManager() {
        return itemsManager;
    }

    public static void reload() {
        getManager().load();

        // reload messages
        try {
            SpigotTranslator.register("plugins/LogicLobby/messages.properties");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void onEnable() {
        instance = this;
        Main.getInstance().getLogger().setLevel(Level.ALL);

        //create files
        new File("plugins/LogicLobby").mkdir();

        List<String> files = new ArrayList<>();
        files.addAll(List.of("config.yml", "messages.properties"));

        for (String fileRaw : files) {
            File file = new File("plugins/LogicLobby/" + fileRaw);
            if (file.exists()) {
                continue;
            }

            InputStream link = (Main.class.getClassLoader().getResourceAsStream(fileRaw));
            try {
                Files.copy(link, file.getAbsoluteFile().toPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        itemsManager = new ItemsManager(true);
        manager = new Manager(true);
        manager.connectToDB();

        SpigotMCCommunication.init();


        //Bstats
        int pluginId = 20990;
        Metrics metrics = new Metrics(this, pluginId);

        // Find plugin class names for dynamic loading
        String fullCanonicalName = Main.getInstance().getClass().getCanonicalName();
        String canonicalName = fullCanonicalName.substring(0, fullCanonicalName.lastIndexOf("."));

        Reflections reflections = new Reflections(canonicalName, new SubTypesScanner(false));
        Set<String> classNames = reflections.getAll(new SubTypesScanner(false));

        DynamicCommands.load(classNames, canonicalName);
        DynamicListeners.load(classNames, canonicalName);

        // register messages
        try {
            SpigotTranslator.register("plugins/LogicLobby/messages.properties");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Main.getInstance().getServer().getMessenger().registerOutgoingPluginChannel(Main.getInstance(), "BungeeCord");
    }

    @Override
    public void onDisable() {
        this.getServer().getMessenger().unregisterOutgoingPluginChannel(this);
        this.getServer().getMessenger().unregisterIncomingPluginChannel(this);
    }
}
