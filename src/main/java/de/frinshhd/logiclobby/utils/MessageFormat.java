package de.frinshhd.logiclobby.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class MessageFormat {

    public static String build(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public static List<String> splitAtDash(String text) {
        List<String> result = new ArrayList<>();
        int startIndex = 0;
        int dashIndex = text.indexOf('-');

        while (dashIndex != -1) {
            if (dashIndex > startIndex) {
                result.add(text.substring(startIndex, dashIndex));
            }
            result.add(text.substring(dashIndex, dashIndex + 2));
            startIndex = dashIndex + 2;
            dashIndex = text.indexOf('-', startIndex);
        }

        if (startIndex < text.length()) {
            result.add(text.substring(startIndex));
        }

        return result;
    }

}

