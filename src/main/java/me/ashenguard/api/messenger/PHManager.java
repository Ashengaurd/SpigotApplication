package me.ashenguard.api.messenger;

import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;

import java.util.List;
import java.util.stream.Collectors;

public class PHManager {
    public static final boolean enable = Bukkit.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;

    public static String translate(OfflinePlayer player, String string) {
        return enable ? PlaceholderAPI.setPlaceholders(player, string) : translate(string);
    }
    public static List<String> translate(OfflinePlayer player, List<String> stringList) {
        return enable ? PlaceholderAPI.setPlaceholders(player, stringList) : translate(stringList);
    }
    public static String translate(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    public static List<String> translate(List<String> stringList) {
        return stringList.stream().map(PHManager::translate).collect(Collectors.toList());
    }
}
