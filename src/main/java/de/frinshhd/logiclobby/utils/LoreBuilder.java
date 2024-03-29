package de.frinshhd.logiclobby.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class LoreBuilder {

    public static List<String> build(String string, ChatColor color) {
        List<String> lines = new ArrayList<>();

        if (string == null) {
            return lines;
        }

        final int maxLenght = 30;

        int index = 0;
        int lastSpace = 0;
        int lastLineBreak = 0;

        while (string.length() > index) {
            char c = string.charAt(index);

            if (c == ' ' || c == '\n') {
                if (c == '\n') {
                    lastSpace = index;
                } else {
                    lastSpace = index;
                }
            }

            if (index - lastLineBreak > maxLenght || c == '\n') {
                if (color != null) {
                    lines.add(color + string.substring(lastLineBreak, lastSpace));
                } else {
                    lines.add(string.substring(lastLineBreak, lastSpace));
                }
                if (c == '\n') {
                    lastLineBreak = index + 1;
                } else {
                    lastLineBreak = lastSpace + 1;
                }
            }

            index++;
        }

        if (color != null) {
            lines.add(color + string.substring(lastLineBreak));
        } else {
            lines.add(string.substring(lastLineBreak));
        }

        return lines;
    }


    public static List<String> build(String string) {
        return build(string, null);
    }

}
