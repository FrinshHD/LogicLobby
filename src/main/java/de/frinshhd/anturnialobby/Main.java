package de.frinshhd.anturnialobby;

import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {


    public static void main(String[] args) {
        Manager.load();
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        Manager.load();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
