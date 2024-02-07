package de.frinshhd.anturnialobby.utils;

import de.frinshhd.anturnialobby.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Objects;
import java.util.Set;

public class DynamicCommands {

    public static void load(Set<String> classNames, String canonicalName) {
        Iterator<String> classNamesIterator = classNames.iterator();

        while (classNamesIterator.hasNext()) {
            String className = classNamesIterator.next();

            if (className.contains(canonicalName)) {
                try {
                    Class<?> cls = Class.forName(className);

                    Class<CommandExecutor> commandExecutorClass = CommandExecutor.class;

                    if (commandExecutorClass.isAssignableFrom(cls)) {
                        Main.getInstance().getLogger().info("[DynamicCommands] Loading command in class " + className);

                        Constructor<?> constructor = cls.getConstructors()[0];
                        SpigotCommandExecutor commandExecutor = (SpigotCommandExecutor) constructor.newInstance();

                        Objects.requireNonNull(Bukkit.getPluginCommand(commandExecutor.getCommandName())).setExecutor(commandExecutor);

                        Main.getInstance().getLogger().info("[DynamicCommands] Finished loading command in class " + className);

                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         InvocationTargetException | IllegalArgumentException e) {

                    Main.getInstance().getLogger().warning("[DynamicCommands] Error loading command in class " + className + " " + e);
                }
            }
        }
    }
}

