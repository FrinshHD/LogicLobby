package de.frinshhd.anturnialobby.utils;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.Iterator;
import java.util.UUID;


public class ItemTags {
    public static void tagItemMeta(ItemMeta meta, String id) {
        ListMultimap<Attribute, AttributeModifier> attributeModifiers = ArrayListMultimap.create();
        attributeModifiers.put(Attribute.GENERIC_LUCK,
                new AttributeModifier(UUID.fromString("4268f089-e59c-4d65-ab28-f364f965b87c"), id, 0,
                        AttributeModifier.Operation.ADD_NUMBER));

        meta.setAttributeModifiers(attributeModifiers);

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
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

        //if meta has no attribute modifiers
        if (!itemMeta.hasAttributeModifiers()) {
            return null;
        }

        Collection<AttributeModifier> modifiers = itemMeta.getAttributeModifiers(Attribute.GENERIC_LUCK);

        //if meta has no Luck modifiers
        if (modifiers == null) {
            return null;
        }

        Iterator<AttributeModifier> it = modifiers.iterator();

        String itemId = "";

        //search for modifier with right uuid
        while (it.hasNext()) {
            AttributeModifier modifier = it.next();

            if (modifier.getUniqueId().equals(UUID.fromString("4268f089-e59c-4d65-ab28-f364f965b87c"))) {
                itemId = modifier.getName();
                break;
            }
        }

        //check if it has content
        if (itemId.isEmpty()) {
            return null;
        }

        return itemId;
    }
}
