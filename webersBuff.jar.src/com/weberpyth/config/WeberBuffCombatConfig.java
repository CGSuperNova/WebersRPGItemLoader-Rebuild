/*     */ package com.weberpyth.config;
/*     */ 
/*     */ import com.weberpyth.weberBuffData.WeberBuffEntity;
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
/*     */ public class WeberBuffCombatConfig
/*     */ {
/*  21 */   private FileConfiguration customConfig = null;
/*  22 */   private File customConfigFile = null;
/*     */   
/*     */   private String fileName;
/*     */   
/*     */   private JavaPlugin plugin;
/*     */   
/*     */   public static HashMap<String, HashMap<String, WeberBuffEntity>> combatInfo;
/*     */   
/*     */   public void setFileName(String name) {
/*  31 */     this.fileName = name;
/*     */   }
/*     */   
/*     */   public void setFileName() {
/*  35 */     setFileName("combat.yml");
/*     */   }
/*     */   
/*     */   public void setPlugin(JavaPlugin plugin) {
/*  39 */     this.plugin = plugin;
/*     */   }
/*     */   
/*     */   public void reloadCustomConfig() {
/*  43 */     boolean firstTime = false;
/*  44 */     if (this.customConfigFile == null) {
/*  45 */       WeberBuffConfig.debugMsg("配置文件" + this.fileName + "不存在，载入默认文件");
/*  46 */       this.customConfigFile = new File(this.plugin.getDataFolder(), this.fileName);
/*  47 */       firstTime = true;
/*     */     } 
/*  49 */     this.customConfig = (FileConfiguration)YamlConfiguration.loadConfiguration(this.customConfigFile);
/*  50 */     Reader defConfigStream = null;
/*     */     
/*     */     try {
/*  53 */       defConfigStream = new InputStreamReader(this.plugin.getResource(this.fileName), "UTF8");
/*  54 */     } catch (Exception e) {
/*  55 */       WeberBuffConfig.debugMsg("无法载入" + this.fileName + "文件");
/*     */     } 
/*  57 */     if (defConfigStream != null) {
/*  58 */       YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
/*  59 */       this.customConfig.setDefaults((Configuration)defConfig);
/*     */     } 
/*  61 */     if (firstTime) {
/*  62 */       WeberBuffConfig.debugMsg("正在导出默认配置文件" + this.fileName);
/*  63 */       saveDefaultConfig();
/*  64 */       WeberBuffConfig.debugMsg("导出默认配置文件" + this.fileName + "完成，正在重载插件");
/*  65 */       reloadCustomConfig();
/*     */     } 
/*     */   }
/*     */   public FileConfiguration getCustomConfig() {
/*  69 */     if (this.customConfig == null) {
/*  70 */       WeberBuffConfig.debugMsg("首次加载，调用reloadCustomConfig()方法");
/*  71 */       reloadCustomConfig();
/*     */     } 
/*     */     
/*  74 */     return this.customConfig;
/*     */   }
/*     */   
/*     */   public void saveCustomConfig() {
/*  78 */     if (this.customConfig == null || this.customConfigFile == null) {
/*     */       return;
/*     */     }
/*     */     try {
/*  82 */       getCustomConfig().save(this.customConfigFile);
/*  83 */     } catch (IOException ex) {
/*  84 */       this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.customConfigFile, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saveDefaultConfig() {
/*  89 */     if (this.customConfigFile == null) {
/*  90 */       this.customConfigFile = new File(this.plugin.getDataFolder(), this.fileName);
/*     */     }
/*  92 */     if (!this.customConfigFile.exists()) {
/*  93 */       this.plugin.saveResource(this.fileName, false);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void saveCombatInfo() {
/*  99 */     for (String s : combatInfo.keySet()) {
/*     */       
/* 101 */       for (String k : ((HashMap)combatInfo.get(s)).keySet()) {
/* 102 */         String path = String.format("%s.buff.%s", new Object[] { s, k });
/* 103 */         getCustomConfig().set(path, ((WeberBuffEntity)((HashMap)combatInfo.get(s)).get(k)).timer);
/*     */       } 
/*     */     } 
/* 106 */     saveCustomConfig();
/*     */   }
/*     */   
/*     */   public HashMap<String, HashMap<String, WeberBuffEntity>> loadCombatInfo() {
/* 110 */     combatInfo = new HashMap<>();
/* 111 */     Set<String> itemsName = null;
/* 112 */     ConfigurationSection itemsCS = this.customConfig.getConfigurationSection(this.customConfig.getCurrentPath());
/*     */     try {
/* 114 */       itemsName = itemsCS.getKeys(false);
/* 115 */     } catch (Exception e) {
/* 116 */       WeberBuffConfig.debugMsg("WRB战斗数据载入失败，错误原因0");
/* 117 */       return null;
/*     */     } 
/*     */     try {
/* 120 */       weberConfig.debugMsg("等待载入WRB战斗数据数量：" + itemsName.size());
/* 121 */       for (String s : itemsName) {
/*     */         
/* 123 */         HashMap<String, WeberBuffEntity> wbeM = new HashMap<>();
/* 124 */         WeberBuffConfig.debugMsg(String.format("读取角色%s 存在buff列表", new Object[] { s }));
/* 125 */         ConfigurationSection buffList = itemsCS.getConfigurationSection(String.format("%s.buff", new Object[] { s }));
/* 126 */         for (String m : buffList.getKeys(false)) {
/* 127 */           WeberBuffEntity wbe = new WeberBuffEntity();
/* 128 */           WeberBuffConfig.debugMsg(String.format("载入角色%s buff信息%s", new Object[] { s, m }));
/* 129 */           wbe.weberBuffModel = WeberBuffConfig.models.get(m);
/* 130 */           wbe.timer = Long.valueOf(buffList.getLong(m));
/* 131 */           wbeM.put(m, wbe);
/*     */         } 
/* 133 */         combatInfo.put(s, wbeM);
/*     */       } 
/*     */       
/* 136 */       WeberBuffConfig.debugMsg("WRB战斗数据载入完成!");
/* 137 */     } catch (Exception e) {
/* 138 */       WeberBuffConfig.debugMsg("WRB战斗数据载入失败,错误原因1");
/* 139 */       e.printStackTrace();
/*     */     } 
/* 141 */     return combatInfo;
/*     */   }
/*     */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\webersBuff.jar!\com\weberpyth\config\WeberBuffCombatConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */