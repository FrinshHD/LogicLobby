package de.frinshhd.logiclobby.model;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.google.gson.annotations.SerializedName;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffectType;

public class Item {
    @SerializedName("glowing")
    private Boolean glowing = false;
    @SerializedName("slot")
    private Integer slot = -1;
    @SerializedName("material")
    private String material = null;
    @SerializedName("potion")
    private String potion = null;
    @SerializedName("texture")
    private String texture = null;
    @SerializedName("leatherColor")
    private DyeColor leatherColor = null;
    @SerializedName("amount")
    private int amount = 1;

    public Material getMaterial() {
        return Material.getMaterial(material);
    }

    public ItemStack getItem(Material material) {
        if (material == null) {
            material = this.getMaterial();
        }

        ItemStack item = new ItemStack(material, amount);

        if (potion != null) {
            PotionMeta potionMeta = (PotionMeta) item.getItemMeta();
            potionMeta.setColor(PotionEffectType.getByName(potion).getColor());
            potionMeta.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
            item.setItemMeta(potionMeta);
        }

        if (texture != null) {
            SkullMeta skullMeta = (SkullMeta) item.getItemMeta();
            if (skullMeta != null) {
                OfflinePlayer player = Bukkit.getOfflinePlayer("bbd0af50-1c26-4e4e-9171-615c844d4a87");
                skullMeta.setOwningPlayer(player);

                PlayerProfile playerProfile = Bukkit.createProfile(player.getUniqueId());
                playerProfile.setProperty(new ProfileProperty("textures", texture));
                skullMeta.setPlayerProfile(playerProfile);

                item.setItemMeta(skullMeta);
            }
        }

        if (leatherColor != null) {
            LeatherArmorMeta leatherArmorMeta = (LeatherArmorMeta) item.getItemMeta();
            leatherArmorMeta.setColor(leatherColor.getColor());
            leatherArmorMeta.addItemFlags(ItemFlag.HIDE_DYE);
            item.setItemMeta(leatherArmorMeta);
        }

        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(itemMeta);

        return item;
    }

    public ItemStack getItem() {
        return getItem(this.getMaterial());
    }

    public int getSlot() {
        return this.slot;
    }
}