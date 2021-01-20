package me.ashenguard.api.messenger;

import org.bukkit.ChatColor;
import org.bukkit.permissions.Permissible;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;

public enum MessageMode {
    Info(ChatColor.GREEN),
    Warning(ChatColor.RED),
    Debug(ChatColor.YELLOW),
    Personal(ChatColor.LIGHT_PURPLE, null, null),
    Operator(ChatColor.AQUA, "*"),
    BroadCast(ChatColor.DARK_RED, null, null);

    public final String permission;
    private final ChatColor color;
    private final String prefix;

    MessageMode(ChatColor color) {
        this(color, "AUTO", "NAME");
    }
    MessageMode(ChatColor color, String permission) {
        this(color, permission, "NAME");
    }
    MessageMode(ChatColor color, String permission, String prefix) {
        this.color = color;
        if (permission == null) this.permission = null;
        else if (permission.equals("AUTO")) this.permission = "Messages." + name();
        else this.permission = "Messages." + permission;
        if (prefix != null && prefix.equals("NAME")) this.prefix = name();
        else this.prefix = prefix;
    }

    public boolean hasPermission(JavaPlugin plugin, Permissible permissible) {
        return permission == null || (permission.equals("*") ? permissible.isOp() : permissible.hasPermission(plugin.getName() + "." + permission));
    }

    public String getPrefix(JavaPlugin plugin) {
        return String.format("§7[%s]§r %s", Messenger.getInstance(plugin).prefix, prefix == null ? "" : color + prefix + "§r ");
    }
    public String getSpace(JavaPlugin plugin) {
        String prefix = ChatColor.stripColor(getPrefix(plugin));
        return String.join("", Collections.nCopies(prefix.length(), " "));
    }
    public String getPrefix(JavaPlugin plugin, boolean first) {
        return first ? getPrefix(plugin) : getSpace(plugin);
    }
}
