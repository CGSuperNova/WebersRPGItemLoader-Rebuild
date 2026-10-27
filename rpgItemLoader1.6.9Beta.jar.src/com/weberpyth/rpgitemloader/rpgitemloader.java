/*     */ package com.weberpyth.rpgitemloader;
/*     */ 
/*     */ import com.weberpyth.config.WeberInfoConfig;
/*     */ import com.weberpyth.config.WeberItemConfig;
/*     */ import com.weberpyth.config.weberConfig;
/*     */ import com.weberpyth.controller.CombatController;
/*     */ import com.weberpyth.controller.CommandController;
/*     */ import com.weberpyth.controller.DotController;
/*     */ import com.weberpyth.controller.ProjectileController;
/*     */ import com.weberpyth.item.WeberRPGItem;
/*     */ import com.weberpyth.item.WeberRPGItemInfo;
/*     */ import com.weberpyth.nbtEditor.WeberNBTEditor;
/*     */ import java.util.HashMap;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Projectile;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*     */ import org.bukkit.event.entity.EntityDamageEvent;
/*     */ import org.bukkit.event.entity.EntityShootBowEvent;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ 
/*     */ public class rpgitemloader
/*     */   extends JavaPlugin
/*     */   implements Listener
/*     */ {
/*     */   private CommandController commandController;
/*     */   private CombatController combatController;
/*     */   private HashMap<String, WeberRPGItem> items;
/*     */   private HashMap<String, WeberRPGItemInfo> itemInfo;
/*     */   public static rpgitemloader plugin;
/*     */   private DotController dotController;
/*     */   
/*     */   public void onEnable() {
/*  41 */     plugin = this;
/*  42 */     Bukkit.getPluginManager().registerEvents(this, (Plugin)this);
/*  43 */     String version = Bukkit.getServer().getClass().getPackage().getName();
/*  44 */     Pattern r = Pattern.compile("v.*");
/*  45 */     Matcher m = r.matcher(version);
/*  46 */     m.find();
/*  47 */     String v = m.group();
/*  48 */     weberConfig wc = new weberConfig(getConfig(), v);
/*  49 */     weberConfig.showPublicMsg("WRL插件正在启动");
/*  50 */     saveDefaultConfig();
/*  51 */     weberConfig.showPublicMsg("WRL插件正在加载指令处理器");
/*  52 */     this.commandController = new CommandController(this);
/*  53 */     weberConfig.showPublicMsg("WRL插件正在加载战斗系统");
/*  54 */     this.combatController = new CombatController();
/*  55 */     weberConfig.showPublicMsg("WRL插件正在加载WRL物品模板数据");
/*  56 */     WeberItemConfig wic = new WeberItemConfig();
/*  57 */     wic.setPlugin(this);
/*  58 */     wic.setFileName();
/*  59 */     wic.getCustomConfig();
/*  60 */     this.items = wic.itemsParser();
/*  61 */     wic.saveDefaultConfig();
/*  62 */     this.combatController.setItems(this.items);
/*  63 */     this.commandController.setItems(this.items);
/*  64 */     CombatController.initCombatController();
/*  65 */     weberConfig.showPublicMsg("WRL插件正在载入显示信息模板数据");
/*  66 */     WeberInfoConfig weberInfoConfig = new WeberInfoConfig();
/*  67 */     weberInfoConfig.setPlugin(this);
/*  68 */     weberInfoConfig.setFileName();
/*  69 */     weberInfoConfig.getCustomConfig();
/*  70 */     this.itemInfo = weberInfoConfig.itemsParser();
/*  71 */     weberInfoConfig.saveDefaultConfig();
/*     */     try {
/*  73 */       WeberNBTEditor.initWeberNBTEditor();
/*  74 */     } catch (Throwable t) {
/*  75 */       weberConfig.showPublicMsg("[警告]前置API未正常工作");
/*  76 */       weberConfig.noNBTAPI = true;
/*     */     } 
/*  78 */     this.commandController.setItemInfo(this.itemInfo);
/*  79 */     weberConfig.showPublicMsg("WRL插件正在载入DOT系统");
/*  80 */     this.dotController = new DotController();
/*  81 */     weberConfig.showPublicMsg("WRL插件加载完成!");
/*  82 */     weberConfig.HyperionDebugMSG();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void onDisable() {
/*  92 */     this.combatController = null;
/*  93 */     this.commandController = null;
/*  94 */     this.itemInfo = null;
/*  95 */     this.items = null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
/* 102 */     if (command.getName().equalsIgnoreCase("wrlReloadConf")) {
/* 103 */       reloadConfig();
/* 104 */       String version = Bukkit.getServer().getClass().getPackage().getName();
/* 105 */       Pattern r = Pattern.compile("v.*");
/* 106 */       Matcher m = r.matcher(version);
/* 107 */       m.find();
/* 108 */       String v = m.group();
/* 109 */       new weberConfig(getConfig(), v);
/* 110 */       weberConfig.showPublicMsg("WRL插件正在加载WRL物品模板数据");
/* 111 */       WeberItemConfig wic = new WeberItemConfig();
/* 112 */       wic.setPlugin(this);
/* 113 */       wic.setFileName();
/* 114 */       wic.getCustomConfig();
/* 115 */       this.items = wic.itemsParser();
/* 116 */       wic.saveDefaultConfig();
/* 117 */       this.combatController.setItems(this.items);
/* 118 */       CombatController.initCombatController();
/* 119 */       weberConfig.showPublicMsg("WRL插件正在载入显示信息模板数据");
/* 120 */       WeberInfoConfig weberInfoConfig = new WeberInfoConfig();
/* 121 */       weberInfoConfig.setPlugin(this);
/* 122 */       weberInfoConfig.setFileName();
/* 123 */       weberInfoConfig.getCustomConfig();
/* 124 */       this.itemInfo = weberInfoConfig.itemsParser();
/* 125 */       weberInfoConfig.saveDefaultConfig();
/* 126 */       this.commandController.setItemInfo(this.itemInfo);
/*     */       try {
/* 128 */         WeberNBTEditor.initWeberNBTEditor();
/* 129 */       } catch (Throwable t) {
/* 130 */         weberConfig.showPublicMsg("[警告]前置API未正常工作");
/* 131 */         weberConfig.noNBTAPI = true;
/*     */       } 
/* 133 */       weberConfig.showPublicMsg("WRL插件加载完成!");
/* 134 */       weberConfig.HyperionDebugMSG();
/*     */       
/* 136 */       return true;
/*     */     } 
/* 138 */     this.commandController.setCommand(sender, command, label, args, this);
/* 139 */     return this.commandController.response();
/*     */   }
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onBattleDamage(EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 143 */     if (entityDamageByEntityEvent.isCancelled()) {
/*     */       return;
/*     */     }
/* 146 */     this.combatController.setEntityDamageByEntityEvent(entityDamageByEntityEvent);
/*     */     try {
/* 148 */       this.combatController.response();
/* 149 */     } catch (Exception e) {
/* 150 */       weberConfig.showPublicMsg("[警告]战斗处理系统异常");
/* 151 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onDotDamage(EntityDamageEvent entityDamageEvent) {
/* 158 */     if (entityDamageEvent.isCancelled()) {
/*     */       return;
/*     */     }
/* 161 */     this.dotController.setEntityDamageEvent(entityDamageEvent);
/*     */     try {
/* 163 */       this.dotController.response();
/* 164 */     } catch (Exception e) {
/* 165 */       weberConfig.showPublicMsg("[警告]DOT系统异常");
/* 166 */       e.printStackTrace();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onShoot(EntityShootBowEvent entityShootBowEvent) {
/* 173 */     if (entityShootBowEvent.isCancelled()) {
/*     */       return;
/*     */     }
/* 176 */     ProjectileController.debugProjectileInfo((Projectile)entityShootBowEvent.getProjectile(), entityShootBowEvent.getEntity());
/*     */   }
/*     */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\rpgitemloader\rpgitemloader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */