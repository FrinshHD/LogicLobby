package de.frinshhd.logiclobby.utils;

import de.frinshhd.logiclobby.Main;
import org.bukkit.event.Listener;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Set;

public class DynamicListeners {

    public static void load(Set<String> classNames, String canonicalName) {
        Iterator<String> classNamesIterator = classNames.iterator();
        while (classNamesIterator.hasNext()) {
            String className = classNamesIterator.next();

            if (className.contains(canonicalName)) {
                try {
                    Class<?> cls = Class.forName(className);

                    Class<Listener> listenerClass = Listener.class;

                    if (listenerClass.isAssignableFrom(cls)) {
                        //Main.getInstance().getLogger().info("[DynamicListeners] Loading listener in class " + className);

                        Constructor<?> constructor = cls.getConstructors()[0];
                        Listener listener = (Listener) constructor.newInstance();


                        Main.getInstance().getServer().getPluginManager().registerEvents(listener, Main.getInstance());
                        //Main.getInstance().getLogger().info("[DynamicListeners] Finished loading listener in  class " + className);
                    }
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException |
                         InvocationTargetException | IllegalArgumentException e) {

                    Main.getInstance().getLogger().warning("[DynamicListeners] Error loading listener in class " + className + " " + e);
                }
            }
        }
    }
}

