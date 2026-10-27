package com.weberpyth.config;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Global switches read from {@code config.yml}.
 *
 * <p>The old code reached the values through {@code fc.getConfigurationSection(fc.getCurrentPath())},
 * which relies on the empty-path special case and NPEs on a fresh install where the default config
 * has not been written yet. Reading the root keys directly is both simpler and safe.
 */
public class weberConfig {

    public static boolean isDebug;
    public static boolean playerOnly;
    public static String version;
    public static boolean battleEcho;
    public static boolean noOriginDamage;
    public static boolean lightBattleEcho;
    public static boolean noImmutableTick;
    public static boolean noNBTAPI;
    public static boolean loreCheck;

    public static boolean legacyDetect;
    public static int legacyDetectCooldown;
    public static String legacyMessage;

    public weberConfig(FileConfiguration fc) {
        isDebug = fc.getBoolean("isDebug", false);
        playerOnly = fc.getBoolean("playerOnly", true);
        battleEcho = fc.getBoolean("battleEcho", false);
        noOriginDamage = fc.getBoolean("noOriginDamage", false);
        lightBattleEcho = fc.getBoolean("lightBattleEcho", false);
        noImmutableTick = fc.getBoolean("noImmutableTick", false);
        noNBTAPI = fc.getBoolean("noNBTAPI", false);
        loreCheck = fc.getBoolean("loreCheck", false);
        legacyDetect = fc.getBoolean("legacyDetect", true);
        legacyDetectCooldown = fc.getInt("legacyDetectCooldown", 15);
        legacyMessage = fc.getString("legacyMessage", "&e检测到旧版 Weber 物品（&f%id%&e），请转换后再使用。");
        version = Bukkit.getBukkitVersion();
        debugInfo();
    }

    /** Kept for callers compiled against the old signature; the version argument is ignored. */
    @Deprecated
    public weberConfig(FileConfiguration fc, String ignoredVersion) {
        this(fc);
    }

    public static void debugInfo() {
        showDEBUGMsg("WRL插件Debug模式：" + (isDebug ? "开启" : "关闭"));
        showDEBUGMsg("WRL插件仅玩家模式：" + (playerOnly ? "开启" : "关闭"));
        showDEBUGMsg("WRL插件战斗回显状态：" + (battleEcho ? "开启" : "关闭"));
        showDEBUGMsg("WRL插件原版伤害：" + (!noOriginDamage ? "开启" : "关闭"));
        showDEBUGMsg("WRL插件旧版物品检测：" + (legacyDetect ? "开启" : "关闭"));
        showDEBUGMsg("WRL插件运行环境：" + version);
        if (isDebug) {
            showDEBUGMsg("[警告]WRL插件正处于Debug模式!");
        }
        if (playerOnly) {
            showDEBUGMsg("[警告]WRL插件正处于仅玩家模式!");
        }
    }

    /** Startup banner (the original ASCII art was replaced by a compact banner). */
    public static void HyperionDebugMSG() {
        Bukkit.getLogger().info("==================================================");
        Bukkit.getLogger().info("  Weber's RPG Item Loader - running on " + version);
        Bukkit.getLogger().info("==================================================");
    }

    public static void debugMsg(String msg) {
        if (isDebug) {
            showDEBUGMsg(msg);
        }
    }

    private static void showDEBUGMsg(String msg) {
        Bukkit.getLogger().info("[Weber's RPG Item Loader:DEBUG]" + msg);
    }

    public static void showPublicMsg(String msg) {
        Bukkit.getLogger().info("[Weber's RPG Item Loader]" + msg);
    }
}
