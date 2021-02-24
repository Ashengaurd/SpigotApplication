package me.ashenguard.api.versions;

import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MCVersion {
    V1_16("1.16"),
    V1_15("1.15"),
    V1_14("1.14"),
    V1_13("1.13"),
    V1_12("1.12"),
    V1_11("1.11"),
    V1_10("1.10"),
    V1_9("1.9"),
    V1_8("1.8");

    public final Version version;
    public final boolean newAPI;

    MCVersion(String version) {
        this.version = new Version(version);
        this.newAPI = this.version.minor >= 13;
    }

    public boolean isHigherThan(MCVersion version) {
        return version.version.minor < this.version.minor;
    }
    public boolean isLowerThan(MCVersion version) {
        return version.version.minor > this.version.minor;
    }

    public static MCVersion getMCVersion() {
        MCVersion mcVersion = values()[0];
        Matcher matcher = Pattern.compile("\\(MC:.+?\\)").matcher(Bukkit.getVersion());
        Version version = matcher.find() ? new Version(matcher.group()) : V1_16.version;
        for (MCVersion mcv: values()) if (mcv.version.major == version.major && mcv.version.minor == version.minor) mcVersion = mcv;
        return mcVersion;
    }
    public static boolean isLegacy() {
        return !getMCVersion().newAPI;
    }
}

