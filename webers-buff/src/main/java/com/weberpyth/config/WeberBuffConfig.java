package com.weberpyth.config;

import com.weberpyth.weberBuffData.WeberBuffModel;
import java.util.HashMap;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class WeberBuffConfig {

    public static boolean isDebug;
    public static boolean publicEcho;
    public static HashMap<String, WeberBuffModel> models;

    public WeberBuffConfig(FileConfiguration fc) {
        isDebug = fc.getBoolean("isDebug", false);
        publicEcho = fc.getBoolean("publicEcho", true);
        showWeberBuffInfo();
    }

    public static void showWeberBuffInfo() {
        showPublicMsg(String.format("Debug模式: %s", isDebug ? "开启" : "关闭"));
        showPublicMsg(String.format("全服广播回显模式: %s", publicEcho ? "开启" : "关闭"));
        debugMsg("插件正处于DeBug模式！");
    }

    public static void debugMsg(String msg) {
        if (isDebug) {
            showDEBUGMsg(msg);
        }
    }

    public static void setModels(HashMap<String, WeberBuffModel> models) {
        WeberBuffConfig.models = models;
    }

    private static void showDEBUGMsg(String msg) {
        Bukkit.getLogger().info("[Weber's Buff:DEBUG]" + msg);
    }

    public static void showPublicMsg(String msg) {
        Bukkit.getLogger().info("[Weber's Buff]" + msg);
    }
}
