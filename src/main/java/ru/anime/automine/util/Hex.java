package ru.anime.automine.util;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Hex {
    private static final Pattern hexPattern = Pattern.compile("&(#[a-f0-9]{6})", Pattern.CASE_INSENSITIVE);
    public static @NotNull String hex(@NotNull String message) {
        Matcher m = hexPattern.matcher(message);
        while (m.find())
            message = message.replace(m.group(), ChatColor.of(m.group(1)).toString());
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static @NotNull String setPlaceholders(@Nullable OfflinePlayer player, @NotNull String string) {
        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            try {
                return PlaceholderAPI.setPlaceholders(player, string);
            } catch (Exception ignore) {
            }
        }
        return string;
    }
    public static String color(String string){
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', string);
    }
}
