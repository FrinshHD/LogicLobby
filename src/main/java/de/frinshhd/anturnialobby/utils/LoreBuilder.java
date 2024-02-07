package de.frinshhd.anturnialobby.utils;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class LoreBuilder {

    public static List<String> build(String string, ChatColor color) {
        List<String> lines = new ArrayList<>();

        final int maxLenght = 30;

        int index = 0;
        int lastSpace = 0;
        int lastLineBreak = 0;

        while (string.length() > index) {
            char c = string.charAt(index);

            if (c == ' ') {
                lastSpace = index;
            }

            if (index - lastLineBreak > maxLenght) {
                if (color != null) {
                    lines.add(color + string.substring(lastLineBreak, lastSpace));
                } else {
                    lines.add(string.substring(lastLineBreak, lastSpace));
                }
                lastLineBreak = lastSpace + 1;
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
