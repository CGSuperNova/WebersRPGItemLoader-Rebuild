/*     */ package com.weberpyth.config;
/*     */ 
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.util.HashMap;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import org.bukkit.configuration.Configuration;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ 
/*     */ public class WeberBuffEchoConfig
/*     */ {
/*  18 */   private FileConfiguration customConfig = null;
/*  19 */   private File customConfigFile = null;
/*     */   
/*     */   private String fileName;
/*     */   private JavaPlugin plugin;
/*     */   public static HashMap<String, String> echo;
/*     */   
/*     */   public void setFileName(String name) {
/*  26 */     this.fileName = name;
/*     */   }
/*     */   
/*     */   public void setFileName() {
/*  30 */     setFileName("echo.yml");
/*     */   }
/*     */   
/*     */   public void setPlugin(JavaPlugin plugin) {
/*  34 */     this.plugin = plugin;
/*     */   }
/*     */   
/*     */   public void reloadCustomConfig() {
/*  38 */     boolean firstTime = false;
/*  39 */     if (this.customConfigFile == null) {
/*  40 */       WeberBuffConfig.debugMsg("配置文件" + this.fileName + "不存在，载入默认文件");
/*  41 */       this.customConfigFile = new File(this.plugin.getDataFolder(), this.fileName);
/*  42 */       firstTime = true;
/*     */     } 
/*  44 */     this.customConfig = (FileConfiguration)YamlConfiguration.loadConfiguration(this.customConfigFile);
/*  45 */     Reader defConfigStream = null;
/*     */     
/*     */     try {
/*  48 */       defConfigStream = new InputStreamReader(this.plugin.getResource(this.fileName), "UTF8");
/*  49 */     } catch (Exception e) {
/*  50 */       WeberBuffConfig.debugMsg("无法载入" + this.fileName + "文件");
/*     */     } 
/*  52 */     if (defConfigStream != null) {
/*  53 */       YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
/*  54 */       this.customConfig.setDefaults((Configuration)defConfig);
/*     */     } 
/*  56 */     if (firstTime) {
/*  57 */       WeberBuffConfig.debugMsg("正在导出默认配置文件" + this.fileName);
/*  58 */       saveDefaultConfig();
/*  59 */       WeberBuffConfig.debugMsg("导出默认配置文件" + this.fileName + "完成，正在重载插件");
/*  60 */       reloadCustomConfig();
/*     */     } 
/*     */   }
/*     */   public FileConfiguration getCustomConfig() {
/*  64 */     if (this.customConfig == null) {
/*  65 */       WeberBuffConfig.debugMsg("首次加载，调用reloadCustomConfig()方法");
/*  66 */       reloadCustomConfig();
/*     */     } 
/*     */     
/*  69 */     return this.customConfig;
/*     */   }
/*     */   
/*     */   public void saveCustomConfig() {
/*  73 */     if (this.customConfig == null || this.customConfigFile == null) {
/*     */       return;
/*     */     }
/*     */     try {
/*  77 */       getCustomConfig().save(this.customConfigFile);
/*  78 */     } catch (IOException ex) {
/*  79 */       this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.customConfigFile, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saveDefaultConfig() {
/*  84 */     if (this.customConfigFile == null) {
/*  85 */       this.customConfigFile = new File(this.plugin.getDataFolder(), this.fileName);
/*     */     }
/*  87 */     if (!this.customConfigFile.exists()) {
/*  88 */       this.plugin.saveResource(this.fileName, false);
/*     */     }
/*     */   }
/*     */   
/*     */   public HashMap<String, String> echoParser() {
/*  93 */     echo = new HashMap<>();
/*  94 */     Set<String> itemsName = null;
/*  95 */     ConfigurationSection itemsCS = this.customConfig.getConfigurationSection(this.customConfig.getCurrentPath());
/*     */     try {
/*  97 */       itemsName = itemsCS.getKeys(false);
/*  98 */     } catch (Exception e) {
/*  99 */       WeberBuffConfig.debugMsg("WRB载入失败，错误原因0");
/* 100 */       return null;
/*     */     } 
/*     */     try {
/* 103 */       weberConfig.debugMsg("等待载入WRB回显数量：" + itemsName.size());
/* 104 */       for (String s : itemsName) {
/* 105 */         String desc = "";
/* 106 */         desc = itemsCS.getString(s);
/* 107 */         echo.put(s, desc);
/*     */       } 
/*     */       
/* 110 */       WeberBuffConfig.debugMsg("WRB回显载入完成!");
/* 111 */     } catch (Exception e) {
/* 112 */       WeberBuffConfig.debugMsg("WRB回显载入失败,错误原因1");
/* 113 */       e.printStackTrace();
/*     */     } 
/* 115 */     return echo;
/*     */   }
/*     */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\webersBuff.jar!\com\weberpyth\config\WeberBuffEchoConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */