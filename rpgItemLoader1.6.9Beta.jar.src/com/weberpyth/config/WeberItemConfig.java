/*     */ package com.weberpyth.config;
/*     */ import com.weberpyth.item.WeberRPGItem;
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
/*     */ public class WeberItemConfig {
/*  17 */   private FileConfiguration customConfig = null;
/*  18 */   private File customConfigFile = null;
/*     */   
/*     */   private String fileName;
/*     */   private JavaPlugin plugin;
/*     */   
/*     */   public void setFileName(String name) {
/*  24 */     this.fileName = name;
/*     */   }
/*     */   
/*     */   public void setFileName() {
/*  28 */     setFileName("items.yml");
/*     */   }
/*     */   
/*     */   public void setPlugin(JavaPlugin plugin) {
/*  32 */     this.plugin = plugin;
/*     */   }
/*     */   
/*     */   public void reloadCustomConfig() {
/*  36 */     boolean firstTime = false;
/*  37 */     if (this.customConfigFile == null) {
/*  38 */       weberConfig.debugMsg("配置文件" + this.fileName + "不存在，载入默认文件");
/*  39 */       this.customConfigFile = new File(this.plugin.getDataFolder(), this.fileName);
/*  40 */       firstTime = true;
/*     */     } 
/*  42 */     this.customConfig = (FileConfiguration)YamlConfiguration.loadConfiguration(this.customConfigFile);
/*  43 */     Reader defConfigStream = null;
/*     */     
/*     */     try {
/*  46 */       defConfigStream = new InputStreamReader(this.plugin.getResource(this.fileName), "UTF8");
/*  47 */     } catch (Exception e) {
/*  48 */       weberConfig.debugMsg("无法载入" + this.fileName + "文件");
/*     */     } 
/*  50 */     if (defConfigStream != null) {
/*  51 */       YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
/*  52 */       this.customConfig.setDefaults((Configuration)defConfig);
/*     */     } 
/*  54 */     if (firstTime) {
/*  55 */       weberConfig.debugMsg("正在导出默认配置文件" + this.fileName);
/*  56 */       saveDefaultConfig();
/*  57 */       weberConfig.debugMsg("导出默认配置文件" + this.fileName + "完成，正在重载插件");
/*  58 */       reloadCustomConfig();
/*     */     } 
/*     */   }
/*     */   public FileConfiguration getCustomConfig() {
/*  62 */     if (this.customConfig == null) {
/*  63 */       weberConfig.debugMsg("首次加载，调用reloadCustomConfig()方法");
/*  64 */       reloadCustomConfig();
/*     */     } 
/*     */     
/*  67 */     return this.customConfig;
/*     */   }
/*     */   
/*     */   public void saveCustomConfig() {
/*  71 */     if (this.customConfig == null || this.customConfigFile == null) {
/*     */       return;
/*     */     }
/*     */     try {
/*  75 */       getCustomConfig().save(this.customConfigFile);
/*  76 */     } catch (IOException ex) {
/*  77 */       this.plugin.getLogger().log(Level.SEVERE, "Could not save config to " + this.customConfigFile, ex);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void saveDefaultConfig() {
/*  82 */     if (this.customConfigFile == null) {
/*  83 */       this.customConfigFile = new File(this.plugin.getDataFolder(), this.fileName);
/*     */     }
/*  85 */     if (!this.customConfigFile.exists()) {
/*  86 */       this.plugin.saveResource(this.fileName, false);
/*     */     }
/*     */   }
/*     */   
/*     */   public static WeberRPGItem loadFromConf(ConfigurationSection itemsCS, String s, String type) {
/*  91 */     HashMap<String, Double> attributes = new HashMap<>();
/*  92 */     WeberRPGItem wrl = new WeberRPGItem();
/*  93 */     wrl.attributes = attributes;
/*  94 */     weberConfig.debugMsg("设置" + type + "ID: " + s);
/*  95 */     wrl.setId(s);
/*  96 */     weberConfig.debugMsg("设置" + type + "内部名称 " + itemsCS.getConfigurationSection(s).getString("innerName"));
/*  97 */     wrl.setInnerName(itemsCS.getConfigurationSection(s).getString("innerName"));
/*     */     
/*  99 */     weberConfig.debugMsg("设置" + type + "伤害修正 " + itemsCS.getConfigurationSection(s).getDouble("damage"));
/* 100 */     wrl.attributes.put("damage", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("damage")));
/* 101 */     weberConfig.debugMsg("设置" + type + "输出增伤 " + itemsCS.getConfigurationSection(s).getDouble("outputDamage"));
/* 102 */     wrl.attributes.put("outputDamage", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("outputDamage")));
/* 103 */     weberConfig.debugMsg("设置" + type + "当前目标生命值百分比伤害 " + itemsCS.getConfigurationSection(s).getDouble("currentEntityHealthDamage"));
/* 104 */     wrl.attributes.put("currentEntityHealthDamage", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("currentEntityHealthDamage")));
/* 105 */     weberConfig.debugMsg("设置" + type + "攻击附伤 " + itemsCS.getConfigurationSection(s).getDouble("appendDamage"));
/* 106 */     wrl.attributes.put("appendDamage", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("appendDamage")));
/* 107 */     weberConfig.debugMsg("设置" + type + "目标最大生命值百分比伤害 " + itemsCS.getConfigurationSection(s).getDouble("maxEntityHealthDamage"));
/* 108 */     wrl.attributes.put("maxEntityHealthDamage", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("maxEntityHealthDamage")));
/* 109 */     weberConfig.debugMsg("设置" + type + "暴击几率 " + itemsCS.getConfigurationSection(s).getDouble("crit"));
/* 110 */     wrl.attributes.put("crit", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("crit")));
/* 111 */     weberConfig.debugMsg("设置" + type + "暴击倍率 " + itemsCS.getConfigurationSection(s).getDouble("critMul"));
/* 112 */     wrl.attributes.put("critMul", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("critMul")));
/* 113 */     weberConfig.debugMsg("设置" + type + "攻击治疗 " + itemsCS.getConfigurationSection(s).getDouble("attackHeal"));
/* 114 */     wrl.attributes.put("attackHeal", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("attackHeal")));
/* 115 */     weberConfig.debugMsg("设置" + type + "生命偷取 " + itemsCS.getConfigurationSection(s).getDouble("stealHealth"));
/* 116 */     wrl.attributes.put("stealHealth", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("stealHealth")));
/* 117 */     weberConfig.debugMsg("设置" + type + "格挡值 " + itemsCS.getConfigurationSection(s).getDouble("block"));
/* 118 */     wrl.attributes.put("block", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("block")));
/*     */     
/* 120 */     weberConfig.debugMsg("设置" + type + "狂怒伤害 " + itemsCS.getConfigurationSection(s).getDouble("angerDamage"));
/* 121 */     wrl.attributes.put("angerDamage", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("angerDamage")));
/*     */     
/* 123 */     weberConfig.debugMsg("设置" + type + "当前生命值百分比伤害反噬 " + itemsCS.getConfigurationSection(s).getDouble("regurgitationCurrentHealthDamage"));
/* 124 */     wrl.attributes.put("regurgitationCurrentHealthDamage", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("regurgitationCurrentHealthDamage")));
/*     */     
/* 126 */     weberConfig.debugMsg("设置" + type + "背刺伤害倍率 " + itemsCS.getConfigurationSection(s).getDouble("backstab"));
/* 127 */     wrl.attributes.put("backstab", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("backstab")));
/*     */     
/* 129 */     weberConfig.debugMsg("设置" + type + "斩杀伤害倍率 " + itemsCS.getConfigurationSection(s).getDouble("executeDamage"));
/* 130 */     wrl.attributes.put("executeDamage", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("executeDamage")));
/*     */     
/* 132 */     weberConfig.debugMsg("设置" + type + "击退强度 " + itemsCS.getConfigurationSection(s).getDouble("repel"));
/* 133 */     wrl.attributes.put("repel", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("repel")));
/*     */     
/* 135 */     weberConfig.debugMsg("设置" + type + "处决比例 " + itemsCS.getConfigurationSection(s).getDouble("annihilation"));
/* 136 */     wrl.attributes.put("annihilation", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("annihilation")));
/*     */     
/* 138 */     weberConfig.debugMsg("设置" + type + "玩家杀手 " + itemsCS.getConfigurationSection(s).getDouble("playerKiller"));
/* 139 */     wrl.attributes.put("playerKiller", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("playerKiller")));
/*     */     
/* 141 */     weberConfig.debugMsg("设置" + type + "坚忍不屈 " + itemsCS.getConfigurationSection(s).getDouble("tenacity"));
/* 142 */     wrl.attributes.put("tenacity", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("tenacity")));
/*     */     
/* 144 */     weberConfig.debugMsg("设置" + type + "地面攻击增益 " + itemsCS.getConfigurationSection(s).getDouble("increasesDamageOnTheGround"));
/* 145 */     wrl.attributes.put("increasesDamageOnTheGround", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("increasesDamageOnTheGround")));
/*     */     
/* 147 */     weberConfig.debugMsg("设置" + type + "地面防御增益 " + itemsCS.getConfigurationSection(s).getDouble("increaseDefenseOnTheGround"));
/* 148 */     wrl.attributes.put("increaseDefenseOnTheGround", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("increaseDefenseOnTheGround")));
/*     */     
/* 150 */     weberConfig.debugMsg("设置" + type + "击飞高度 " + itemsCS.getConfigurationSection(s).getDouble("blow"));
/* 151 */     wrl.attributes.put("blow", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("blow")));
/*     */     
/* 153 */     weberConfig.debugMsg("设置" + type + "护甲值(数值) " + itemsCS.getConfigurationSection(s).getDouble("armor"));
/* 154 */     wrl.attributes.put("armor", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("armor")));
/*     */     
/* 156 */     weberConfig.debugMsg("设置" + type + "护甲提升(百分比) " + itemsCS.getConfigurationSection(s).getDouble("armorIncrease"));
/* 157 */     wrl.attributes.put("armorIncrease", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("armorIncrease")));
/*     */     
/* 159 */     weberConfig.debugMsg("设置" + type + "护甲穿透(百分比) " + itemsCS.getConfigurationSection(s).getDouble("armorDecrease"));
/* 160 */     wrl.attributes.put("armorDecrease", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("armorDecrease")));
/*     */     
/* 162 */     weberConfig.debugMsg("设置" + type + "护甲穿透(固定数值) " + itemsCS.getConfigurationSection(s).getDouble("armorSub"));
/* 163 */     wrl.attributes.put("armorSub", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("armorSub")));
/*     */     
/* 165 */     weberConfig.debugMsg("设置" + type + "百分比减伤 " + itemsCS.getConfigurationSection(s).getDouble("injuryReduction"));
/* 166 */     wrl.attributes.put("injuryReduction", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("injuryReduction")));
/*     */     
/* 168 */     weberConfig.debugMsg("设置" + type + "巨人之力 " + itemsCS.getConfigurationSection(s).getDouble("giantDamage"));
/* 169 */     wrl.attributes.put("giantDamage", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("giantDamage")));
/*     */     
/* 171 */     weberConfig.debugMsg("设置" + type + "近战输出伤害 " + itemsCS.getConfigurationSection(s).getDouble("meleeDamage"));
/* 172 */     wrl.attributes.put("meleeDamage", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("meleeDamage")));
/*     */     
/* 174 */     weberConfig.debugMsg("设置" + type + "远程输出伤害 " + itemsCS.getConfigurationSection(s).getDouble("rangedDamage"));
/* 175 */     wrl.attributes.put("rangedDamage", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("rangedDamage")));
/*     */     
/* 177 */     weberConfig.debugMsg("设置" + type + "近战附伤 " + itemsCS.getConfigurationSection(s).getDouble("meleeAppendDamage"));
/* 178 */     wrl.attributes.put("meleeAppendDamage", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("meleeAppendDamage")));
/*     */     
/* 180 */     weberConfig.debugMsg("设置" + type + "近战附伤 " + itemsCS.getConfigurationSection(s).getDouble("rangedAppendDamage"));
/* 181 */     wrl.attributes.put("rangedAppendDamage", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("rangedAppendDamage")));
/*     */     
/* 183 */     weberConfig.debugMsg("设置" + type + "近战伤害 " + itemsCS.getConfigurationSection(s).getDouble("meleePower"));
/* 184 */     wrl.attributes.put("meleePower", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("meleePower")));
/*     */     
/* 186 */     weberConfig.debugMsg("设置" + type + "远程伤害 " + itemsCS.getConfigurationSection(s).getDouble("rangedPower"));
/* 187 */     wrl.attributes.put("rangedPower", Double.valueOf(itemsCS.getConfigurationSection(s).getDouble("rangedPower")));
/*     */ 
/*     */ 
/*     */     
/* 191 */     return wrl;
/*     */   }
/*     */   
/*     */   public HashMap<String, WeberRPGItem> itemsParser() {
/* 195 */     HashMap<String, WeberRPGItem> items = new HashMap<>();
/* 196 */     Set<String> itemsName = null;
/* 197 */     ConfigurationSection itemsCS = this.customConfig.getConfigurationSection(this.customConfig.getCurrentPath());
/*     */     try {
/* 199 */       itemsName = itemsCS.getKeys(false);
/* 200 */     } catch (Exception e) {
/* 201 */       weberConfig.debugMsg("WRL物品载入失败，错误原因0");
/* 202 */       return null;
/*     */     } 
/*     */     try {
/* 205 */       weberConfig.debugMsg("等待载入WRL物品数量：" + itemsName.size());
/* 206 */       for (String s : itemsName) {
/* 207 */         weberConfig.debugMsg("载入WRL物品" + s);
/*     */         
/* 209 */         weberConfig.debugMsg("实例化临时物品对象");
/* 210 */         HashMap<String, Double> attributes = new HashMap<>();
/* 211 */         WeberRPGItem wrl = new WeberRPGItem();
/* 212 */         wrl = loadFromConf(itemsCS, s, "物品");
/*     */ 
/*     */         
/* 215 */         weberConfig.debugMsg("将物品加入集合");
/* 216 */         items.put(s, wrl);
/*     */       } 
/*     */       
/* 219 */       weberConfig.debugMsg("WRL物品载入完成!");
/* 220 */     } catch (Exception e) {
/* 221 */       weberConfig.debugMsg("WRL物品载入失败,错误原因1");
/*     */     } 
/* 223 */     return items;
/*     */   }
/*     */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\config\WeberItemConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */