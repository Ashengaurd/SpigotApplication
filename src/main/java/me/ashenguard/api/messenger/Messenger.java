package me.ashenguard.api.messenger;

import me.ashenguard.api.spigot.SpigotPlugin;
import me.ashenguard.api.spigot.SpigotResource;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.PrintStream;
import java.util.Collection;
import java.util.HashMap;

public class Messenger {
    private static final HashMap<JavaPlugin, Messenger> MessengerAPIMap = new HashMap<>();
    public static Messenger getInstance(JavaPlugin plugin) {
        return MessengerAPIMap.getOrDefault(plugin, null);
    }

    public final SpigotPlugin plugin;
    public final File exceptionFolder;

    public final boolean debugger;
    public final HashMap<MessageMode, Boolean> inGameMessaging;
    public final HashMap<String, Boolean> debugs;
    public final String prefix;

    public Messenger(SpigotPlugin plugin, HashMap<MessageMode, Boolean> inGameMessaging, HashMap<String, Boolean> debugs, String prefix, boolean debugger) {
        this.plugin = plugin;
        this.debugger = debugger;
        this.inGameMessaging = inGameMessaging;
        this.debugs = debugs;
        this.prefix = prefix;

        exceptionFolder = new File(plugin.getDataFolder(), "Exception");
        if (!exceptionFolder.exists() && exceptionFolder.mkdirs()) Debug("General", "Exception folder wasn't found, A new one created");

        MessengerAPIMap.put(plugin, this);
    }

    public Messenger(SpigotPlugin plugin) {
        FileConfiguration config = plugin.getConfig();
        this.plugin = plugin;

        inGameMessaging = new HashMap<>();
        inGameMessaging.put(MessageMode.Info, config == null || config.getBoolean("InGameMessages.Info", true));
        inGameMessaging.put(MessageMode.Warning, config == null || config.getBoolean("InGameMessages.Warning", true));
        inGameMessaging.put(MessageMode.Debug, config == null || config.getBoolean("InGameMessages.Debug", true));

        this.debugger = config != null && config.getBoolean("Debug.Enable", false);
        this.prefix = config == null ? plugin.getName() : config.getString("Prefix", plugin.getName());

        debugs = new HashMap<>();
        if (config != null) {
            ConfigurationSection section = config.getConfigurationSection("Debug");
            if (section != null) for (String key : section.getKeys(false)) if (!key.equals("Enable")) debugs.put(key, section.getBoolean(key, true));
        }

        exceptionFolder = new File(plugin.getDataFolder(), "Exception");
        if (!exceptionFolder.exists() && exceptionFolder.mkdirs()) Debug("General", "Exception folder wasn't found, A new one created");

        MessengerAPIMap.put(plugin, this);
    }

    public void Debug(String type, String... messages) {
        if (debugger && debugs.getOrDefault(type, true))
            sendMessage(MessageMode.Debug, Bukkit.getConsoleSender(), messages);
    }
    public void Warning(String... messages) {
        sendMessage(MessageMode.Warning, Bukkit.getConsoleSender(), messages);
    }
    public void Info(String... messages) {
        sendMessage(MessageMode.Info, Bukkit.getConsoleSender(), messages);
    }

    public void send(CommandSender sender, String... messages) {
        sendMessage(MessageMode.Personal, sender, messages);
    }

    public void sendMessage(MessageMode mode, CommandSender sender, String... messages) {
        if (messages == null) return;
        for (int i = 0; i < messages.length; i++) {
            String message = mode.getPrefix(plugin,i == 0) + messages[i];
            sender.sendMessage(message);
            if (sender instanceof Player) continue;
            sendAll(mode, message);
        }
    }
    public void sendAll(MessageMode mode, String message) {
        if (!inGameMessaging.getOrDefault(mode, true) && !(mode.equals(MessageMode.Operator) || mode.equals(MessageMode.Personal))) return;
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        for (Player player:players)
            if (mode.hasPermission(plugin, player))
                player.sendMessage(message);
    }

    public void updateNotification(CommandSender player) {
        updateNotification(player, true);
    }
    public void updateNotification(CommandSender player, boolean updates) {
        if (player == null || !player.isOp() || plugin == null) return;

        SpigotResource resource = new SpigotResource(plugin.getSpigotID());
        reminder(() -> {
            // Update Check
            if (updates && resource.version.isHigher(plugin.getVersion())) {
                send(player, "There is a §anew update§r available on SpigotMC");
                send(player, String.format("This version: §c%s§r", plugin.getVersion()));
                send(player, "SpigotMC version: §a%s§r", resource.version.toString(true));
            }
        });
    }

    public void reminder(Runnable runnable) {
        reminder(runnable, 100L);
    }
    public void reminder(Runnable runnable, long delay) {
        if (plugin == null) return;
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, runnable, delay);
    }

    public void handleException(Throwable exception) {
        handleException(exception, exceptionFolder);
    }
    public void handleException(Throwable exception, File exceptionFolder) {
        Warning("An error occurred");
        int count = 1;
        File file = new File(exceptionFolder, "Exception_1.warn");
        while (file.exists()) file = new File(exceptionFolder, String.format("Exception_%d.warn", ++count));
        try {
            PrintStream ps = new PrintStream(file); exception.printStackTrace(ps); ps.close();
            Warning("Exception saved as \"§cException_ " + count + ".warn§r\"");
        } catch (Exception ignored) {
            exception.printStackTrace();
        }
    }
}