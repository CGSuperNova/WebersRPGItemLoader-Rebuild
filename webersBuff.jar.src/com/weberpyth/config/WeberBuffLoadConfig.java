/*     */ package com.weberpyth.config;
/*     */ 
/*     */ import com.weberpyth.weberBuffData.WeberBuffModel;
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
/*     */ 
/*     */ 
/*     */ public class WeberBuffLoadConfig
/*     */ {
/*  21 */   private FileConfiguration customConfig = null;
/*  22 */   private File customConfigFile = null;
/*     */   
/*     */   private String fileName;
/*     */   private JavaPlugin plugin;
/*     */   
/*     */   public void setFileName(String name) {
/*  28 */     this.fileName = name;
/*     */   }
/*     */   
/*     */   public void setFileName() {
/*  32 */     setFileName("buff.yml");
/*     */   }
/*     */   
/*     */   public void setPlugin(JavaPlugin plugin) {
/*  36 */     this.plugin = plugin;
/*     */   }
/*     */   
/*     */   public void reloadCustomConfig() {
/*  40 */     boolean firstTime = false;
/*  41 */     if (this.customConfigFile == null) {
/*  42 */       WeberBuffConfig.debugMsg("配置文件" + this.fileName + "不存在，载入默认文件");
/*  43 */       this.customConfigFile = new File(this.plugin.getDataFolder(), this.fileName);
/*  44 */       firstTime = true;
/*     */     } 
/*  46 */     this.customConfig = (FileConfiguration)YamlConfiguration.loadConfiguration(this.customConfigFile);
/*  47 */     Reader defConfigStream = null;
/*     */     
/*     */     try {
/*  50 */       defConfigStream = new InputStreamReader(this.plugin.getResource(this.fileName), "UTF8");
/*  51 */     } catch (Exception e) {
/*  52 */       WeberBuffConfig.debugMsg("无法载入" + this.fileName + "文件");
/*     */     } 
/*  54 */     if (defConfigStream != null) {
/*  55 */       YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
/*  56 */       this.customConfig.setDefaults((Configuration)defConfig);
/*     */     } 
/*  58 */     if (firstTime) {
/*  59 */       WeberBuffConfig.debugMsg("正在导出默认配置文件" + this.fileName);
/*  60 */       saveDefaultConfig();
/*  61 */       WeberBuffConfig.debugMsg("导出默认配置文件" + this.fileName + "完成，正在重载插件");
/*  62 */       reloadCustomConfig();
/*     */     } 
/*     */   }
/*     */   public FileConfiguration getCustomConfig() {
/*  66 */     if (this.customConfig == null) {
/*  67 */       WeberBuffConfig.debugMsg("首次加载，调用reloadCustomConfig()方法");
/*  68 */       reloadCustomConfig();
/*     */     } 
/*     */     
/*  71 */     return this.customConfig;
/*     */   }
/*     */   
/*     */   public void saveCustomConfig() {
/*  75 */     if (this.customConfig == null || this.customConfigFile == null) {
/*     */       return;
/*     */     }
/*     */     try {
/*  79 */       getCustomConfig().save(this.customConfigFile);
/*  80 */     } catch (IOException ex) {
/*  81 */       this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.customConfigFile, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saveDefaultConfig() {
/*  86 */     if (this.customConfigFile == null) {
/*  87 */       this.customConfigFile = new File(this.plugin.getDataFolder(), this.fileName);
/*     */     }
/*  89 */     if (!this.customConfigFile.exists()) {
/*  90 */       this.plugin.saveResource(this.fileName, false);
/*     */     }
/*     */   }
/*     */   
/*     */   public HashMap<String, WeberBuffModel> itemsParser() {
/*  95 */     HashMap<String, WeberBuffModel> items = new HashMap<>();
/*  96 */     Set<String> itemsName = null;
/*  97 */     ConfigurationSection itemsCS = this.customConfig.getConfigurationSection(this.customConfig.getCurrentPath());
/*     */     try {
/*  99 */       itemsName = itemsCS.getKeys(false);
/* 100 */     } catch (Exception e) {
/* 101 */       WeberBuffConfig.debugMsg("WRB载入失败，错误原因0");
/* 102 */       return null;
/*     */     } 
/*     */     try {
/* 105 */       weberConfig.debugMsg("等待载入WRB的Buff数量：" + itemsName.size());
/* 106 */       for (String s : itemsName) {
/* 107 */         WeberBuffModel weberBuffModel = new WeberBuffModel();
/* 108 */         weberBuffModel.loadParent(WeberItemConfig.loadFromConf(itemsCS, s, "Buff"));
/* 109 */         weberBuffModel.attributes.put("length", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("length")));
/* 110 */         weberBuffModel.name = itemsCS.getConfigurationSection(s).getString("name");
/* 111 */         items.put(s, weberBuffModel);
/*     */       } 
/*     */       
/* 114 */       WeberBuffConfig.debugMsg("WRB的Buff载入完成!");
/* 115 */     } catch (Exception e) {
/* 116 */       WeberBuffConfig.debugMsg("WRB的Buff载入失败,错误原因1");
/* 117 */       e.printStackTrace();
/*     */     } 
/* 119 */     return items;
/*     */   }
/*     */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\webersBuff.jar!\com\weberpyth\config\WeberBuffLoadConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */