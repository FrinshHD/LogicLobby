package de.frinshhd.anturnialobby;

import de.frinshhd.anturnialobby.utils.DynamicCommands;
import de.frinshhd.anturnialobby.utils.DynamicListeners;
import de.frinshhd.anturnialobby.utils.Metrics;
import de.frinshhd.anturnialobby.utils.SpigotTranslator;
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

public final class Main extends JavaPlugin {
    public static String version;
    private static JavaPlugin instance;
    private static Manager manager;

    public static JavaPlugin getInstance() {
        return instance;
    }

    public static Manager getManager() {
        return manager;
    }

    @Override
    public void onEnable() {
        instance = this;

        //create files
        new File("plugins/AnturniaLobby").mkdir();

        List<String> files = new ArrayList<>();
        files.addAll(List.of("config.yml", "messages.properties"));

        for (String fileRaw : files) {
            File file = new File("plugins/AnturniaLobby/" + fileRaw);
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

        manager = new Manager();

        //Bstats
        int pluginId = 20932;
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
            SpigotTranslator.register("plugins/AnturniaLobby/messages.properties");
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
