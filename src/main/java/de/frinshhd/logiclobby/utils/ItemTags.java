package de.frinshhd.logiclobby.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import de.frinshhd.logiclobby.Main;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;


public class ItemTags {
    public static void tagItemMeta(ItemMeta meta, String id) {

        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(Main.getInstance(), "itemTag");
        container.set(key, PersistentDataType.STRING, id);
    }

    public static void tagItem(ItemStack itemStack, String id) {
        ItemMeta meta = itemStack.getItemMeta();
        ItemTags.tagItemMeta(meta, id);
        itemStack.setItemMeta(meta);
    }

    public static String extractItemId(ItemMeta itemMeta) {
        //if itemMeta doesn't exist
        if (itemMeta == null) {
            return null;
        }

        PersistentDataContainer container = itemMeta.getPersistentDataContainer();

        if (container.getKeys().isEmpty()) {
            return null;
        }

        String tag = null;

        //get all keys
        for (NamespacedKey key : container.getKeys()) {
            if (!key.getNamespace().equalsIgnoreCase(Main.getInstance().getName())) {
                continue;
            }

            if (!key.getKey().equalsIgnoreCase("itemTag")) {
                continue;
            }

            tag = container.get(key, PersistentDataType.STRING);
        }

        return tag;
    }
}
