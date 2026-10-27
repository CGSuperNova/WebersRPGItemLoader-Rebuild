/*     */ package com.weberpyth.config;
/*     */ 
/*     */ import com.weberpyth.item.WeberRPGItemInfo;
/*     */ import java.io.File;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStreamReader;
/*     */ import java.io.Reader;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.Set;
/*     */ import java.util.logging.Level;
/*     */ import org.bukkit.configuration.Configuration;
/*     */ import org.bukkit.configuration.ConfigurationSection;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ import org.bukkit.configuration.file.YamlConfiguration;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ 
/*     */ public class WeberInfoConfig
/*     */ {
/*  20 */   private FileConfiguration customConfig = null;
/*  21 */   private File customConfigFile = null;
/*     */   
/*     */   private String fileName;
/*     */   private JavaPlugin plugin;
/*     */   
/*     */   public void setFileName(String name) {
/*  27 */     this.fileName = name;
/*     */   }
/*     */   
/*     */   public void setFileName() {
/*  31 */     setFileName("info.yml");
/*     */   }
/*     */   
/*     */   public void setPlugin(JavaPlugin plugin) {
/*  35 */     this.plugin = plugin;
/*     */   }
/*     */   
/*     */   public void reloadCustomConfig() {
/*  39 */     boolean firstTime = false;
/*  40 */     if (this.customConfigFile == null) {
/*  41 */       weberConfig.debugMsg("配置文件" + this.fileName + "不存在，载入默认文件");
/*  42 */       this.customConfigFile = new File(this.plugin.getDataFolder(), this.fileName);
/*  43 */       firstTime = true;
/*     */     } 
/*  45 */     this.customConfig = (FileConfiguration)YamlConfiguration.loadConfiguration(this.customConfigFile);
/*  46 */     Reader defConfigStream = null;
/*     */     
/*     */     try {
/*  49 */       defConfigStream = new InputStreamReader(this.plugin.getResource(this.fileName), "UTF8");
/*  50 */     } catch (Exception e) {
/*  51 */       weberConfig.debugMsg("无法载入" + this.fileName + "文件");
/*     */     } 
/*  53 */     if (defConfigStream != null) {
/*  54 */       YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
/*  55 */       this.customConfig.setDefaults((Configuration)defConfig);
/*     */     } 
/*  57 */     if (firstTime) {
/*  58 */       weberConfig.debugMsg("正在导出默认配置文件" + this.fileName);
/*  59 */       saveDefaultConfig();
/*  60 */       weberConfig.debugMsg("导出默认配置文件" + this.fileName + "完成，正在重载插件");
/*  61 */       reloadCustomConfig();
/*     */     } 
/*     */   }
/*     */   public FileConfiguration getCustomConfig() {
/*  65 */     if (this.customConfig == null) {
/*  66 */       weberConfig.debugMsg("首次加载，调用reloadCustomConfig()方法");
/*  67 */       reloadCustomConfig();
/*     */     } 
/*     */     
/*  70 */     return this.customConfig;
/*     */   }
/*     */   
/*     */   public void saveCustomConfig() {
/*  74 */     if (this.customConfig == null || this.customConfigFile == null) {
/*     */       return;
/*     */     }
/*     */     try {
/*  78 */       getCustomConfig().save(this.customConfigFile);
/*  79 */     } catch (IOException ex) {
/*  80 */       this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.customConfigFile, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saveDefaultConfig() {
/*  85 */     if (this.customConfigFile == null) {
/*  86 */       this.customConfigFile = new File(this.plugin.getDataFolder(), this.fileName);
/*     */     }
/*  88 */     if (!this.customConfigFile.exists()) {
/*  89 */       this.plugin.saveResource(this.fileName, false);
/*     */     }
/*     */   }
/*     */   
/*     */   public HashMap<String, WeberRPGItemInfo> itemsParser() {
/*  94 */     HashMap<String, WeberRPGItemInfo> items = new HashMap<>();
/*  95 */     Set<String> itemsName = null;
/*  96 */     ConfigurationSection itemsCS = this.customConfig.getConfigurationSection(this.customConfig.getCurrentPath());
/*     */     try {
/*  98 */       itemsName = itemsCS.getKeys(false);
/*  99 */     } catch (Exception e) {
/* 100 */       weberConfig.debugMsg("WRL信息载入失败，错误原因0");
/* 101 */       return null;
/*     */     } 
/*     */     try {
/* 104 */       weberConfig.debugMsg("等待载入WRL信息数量：" + itemsName.size());
/* 105 */       for (String s : itemsName) {
/* 106 */         weberConfig.debugMsg("载入WRL信息" + s);
/*     */         
/* 108 */         weberConfig.debugMsg("实例化临时信息对象");
/* 109 */         WeberRPGItemInfo rli = new WeberRPGItemInfo();
/* 110 */         weberConfig.debugMsg("设置物品命名: " + itemsCS.getConfigurationSection(s).getString("name"));
/* 111 */         rli.setName(itemsCS.getConfigurationSection(s).getString("name"));
/*     */         
/* 113 */         rli.lores = new ArrayList();
/* 114 */         for (Object lore : itemsCS.getConfigurationSection(s).getList("lores")) {
/* 115 */           weberConfig.debugMsg("设置物品描述: " + (String)lore);
/* 116 */           rli.lores.add((String)lore);
/*     */         } 
/*     */         
/* 119 */         weberConfig.debugMsg("将信息加入集合");
/* 120 */         rli.setId(s);
/* 121 */         items.put(s, rli);
/*     */       } 
/*     */       
/* 124 */       weberConfig.debugMsg("WRL物品载入完成!");
/* 125 */     } catch (Exception e) {
/* 126 */       weberConfig.debugMsg("WRL物品载入失败,错误原因1");
/*     */     } 
/* 128 */     return items;
/*     */   }
/*     */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\config\WeberInfoConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */