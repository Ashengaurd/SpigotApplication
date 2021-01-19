package me.ashenguard.api.spigot;

import me.ashenguard.api.bstats.Metrics;
import me.ashenguard.api.messenger.Messenger;
import me.ashenguard.api.versions.MCVersion;
import me.ashenguard.api.versions.Version;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class SpigotPlugin extends JavaPlugin implements Listener {
    public abstract int getBStatsID();
    public abstract int getSpigotID();

    public boolean updateNotification = true;
    public final Messenger messenger = new Messenger(this);
    public final Metrics metrics = new Metrics(this, getBStatsID());

    public boolean isLegacy() {
        return MCVersion.isLegacy();
    }
    public Version getVersion() {
        return new Version(this.getDescription().getVersion());
    }

    @EventHandler
    public void onJoinUpdateNotification(PlayerJoinEvent event) {
        messenger.updateNotification(event.getPlayer(), updateNotification);
    }
}
