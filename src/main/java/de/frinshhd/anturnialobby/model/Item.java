package de.frinshhd.anturnialobby.model;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.destroystokyo.paper.profile.ProfileProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffectType;

import java.util.UUID;

public class Item {
    @JsonProperty
    private Boolean glowing = false;
    @JsonProperty
    private Integer slot = -1;
    @JsonProperty
    private String material = null;
    @JsonProperty
    private String potion = null;
    @JsonProperty
    private String texture = null;
    @JsonProperty
    private DyeColor leatherColor = null;
    @JsonProperty
    private int amount = 1;

    @JsonIgnore
    public Material getMaterial() {
        return Material.getMaterial(material);
    }

    @JsonIgnore
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
            skullMeta.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.randomUUID()));
            PlayerProfile playerProfile = skullMeta.getPlayerProfile();
            assert playerProfile != null;
            playerProfile.setProperty(new ProfileProperty("textures", texture));
            skullMeta.setPlayerProfile(playerProfile);

            item.setItemMeta(skullMeta);
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

    @JsonIgnore
    public ItemStack getItem() {
        return getItem(this.getMaterial());
    }

    public int getSlot() {
        return this.slot;
    }
}
