package com.weberpyth.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Small helper around a user editable YAML file that lives in the plugin data folder and falls
 * back to the copy shipped inside the jar.
 *
 * <p>This replaces four near-identical copies of the same 70 lines. Those copies recursed through
 * {@code reloadCustomConfig()} on first start, leaked the default resource stream and used the
 * platform default charset for the jar resource.
 */
public abstract class SimpleYamlConfig {

    protected FileConfiguration customConfig;
    protected File customConfigFile;
    protected String fileName;
    protected JavaPlugin plugin;

    private final String defaultFileName;

    protected SimpleYamlConfig(String defaultFileName) {
        this.defaultFileName = defaultFileName;
    }

    public void setFileName(String name) {
        this.fileName = name;
    }

    public void setFileName() {
        setFileName(defaultFileName);
    }

    public void setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public String getFileName() {
        return fileName == null ? defaultFileName : fileName;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public File getCustomConfigFile() {
        if (customConfigFile == null && plugin != null) {
            customConfigFile = new File(plugin.getDataFolder(), getFileName());
        }
        return customConfigFile;
    }

    public void reloadCustomConfig() {
        if (plugin == null) {
            throw new IllegalStateException("setPlugin() must be called before loading " + getFileName());
        }
        File file = getCustomConfigFile();
        if (!file.exists()) {
            weberConfig.debugMsg("配置文件" + getFileName() + "不存在，导出默认文件");
            saveDefaultConfig();
        }
        customConfig = YamlConfiguration.loadConfiguration(file);
        try (InputStream resource = plugin.getResource(getFileName())) {
            if (resource != null) {
                YamlConfiguration defaults = YamlConfiguration.loadConfiguration(
                        new InputStreamReader(resource, StandardCharsets.UTF_8));
                customConfig.setDefaults(defaults);
            }
        } catch (IOException | IllegalArgumentException e) {
            weberConfig.debugMsg("无法载入默认配置 " + getFileName() + ": " + e.getMessage());
        }
    }

    public FileConfiguration getCustomConfig() {
        if (customConfig == null) {
            reloadCustomConfig();
        }
        return customConfig;
    }

    public void saveCustomConfig() {
        if (customConfig == null || getCustomConfigFile() == null) {
            return;
        }
        try {
            customConfig.save(getCustomConfigFile());
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + getCustomConfigFile(), ex);
        }
    }

    public void saveDefaultConfig() {
        if (plugin == null || getCustomConfigFile() == null) {
            return;
        }
        if (!getCustomConfigFile().exists()) {
            try {
                plugin.saveResource(getFileName(), false);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Jar 中缺少默认资源 " + getFileName());
            }
        }
    }
}
