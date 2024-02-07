package de.frinshhd.anturnialobby.utils;

import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Sounds {

    public static void itemClick(Player player) {
        player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 50, 1);
    }


}
