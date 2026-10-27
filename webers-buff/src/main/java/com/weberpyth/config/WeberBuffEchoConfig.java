package com.weberpyth.config;

import java.util.HashMap;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Loads the broadcast messages from {@code echo.yml} ({@code <buffId>: <message>}). Placeholders
 * {@code {player}} and {@code {buffID}} are substituted before broadcasting.
 */
public class WeberBuffEchoConfig extends SimpleYamlConfig {

    public static HashMap<String, String> echo;

    public WeberBuffEchoConfig() {
        super("echo.yml");
    }

    public HashMap<String, String> echoParser() {
        echo = new HashMap<>();
        ConfigurationSection root = getCustomConfig().getRoot();
        if (root == null) {
            return echo;
        }
        Set<String> ids = root.getKeys(false);
        for (String id : ids) {
            String message = root.getString(id);
            if (message != null) {
                echo.put(id, message);
            }
        }
        WeberBuffConfig.debugMsg("WRB回显载入完成，共 " + echo.size() + " 项");
        return echo;
    }
}
