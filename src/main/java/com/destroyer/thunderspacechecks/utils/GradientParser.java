package com.destroyer.thunderspacechecks.utils;

import net.md_5.bungee.api.ChatColor;

import java.awt.Color;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GradientParser {

    private static final Pattern GRADIENT_PATTERN = Pattern.compile("&#([0-9a-fA-F]{6})");

    public static String parse(String input) {
        String result = ChatColor.translateAlternateColorCodes('&', input);

        Matcher matcher = GRADIENT_PATTERN.matcher(result);
        StringBuilder output = new StringBuilder();
        int lastIndex = 0;

        while (matcher.find()) {
            output.append(result, lastIndex, matcher.start());
            String hexColor = matcher.group(1);
            Color color = Color.decode("#" + hexColor);
            output.append(ChatColor.of(color));
            lastIndex = matcher.end();
        }

        output.append(result.substring(lastIndex));
        return output.toString();
    }
}