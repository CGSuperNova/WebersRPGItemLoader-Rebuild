/*     */ package com.weberpyth.weberBuff;
/*     */ 
/*     */ import com.weberpyth.WeberBuffController.BuffCombatController;
/*     */ import com.weberpyth.WeberBuffController.CommandController;
/*     */ import com.weberpyth.config.WeberBuffCombatConfig;
/*     */ import com.weberpyth.config.WeberBuffConfig;
/*     */ import com.weberpyth.config.WeberBuffEchoConfig;
/*     */ import com.weberpyth.config.WeberBuffLoadConfig;
/*     */ import com.weberpyth.controller.CombatController;
/*     */ import com.weberpyth.nbtEditor.WeberNBTEditor;
/*     */ import com.weberpyth.weberBuffData.WeberBuffModel;
/*     */ import java.util.HashMap;
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.EventHandler;
/*     */ import org.bukkit.event.EventPriority;
/*     */ import org.bukkit.event.Listener;
/*     */ import org.bukkit.event.block.Action;
/*     */ import org.bukkit.event.player.PlayerInteractEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.plugin.java.JavaPlugin;
/*     */ 
/*     */ public class WeberBuff extends JavaPlugin implements Listener {
/*     */   public HashMap<String, WeberBuffModel> models;
/*     */   private WeberBuffLoadConfig weberBuffLoadConfig;
/*     */   private WeberBuffCombatConfig weberBuffCombatConfig;
/*     */   private CommandController commandController;
/*     */   public static WeberBuff plugin;
/*     */   
/*     */   public void onEnable() {
/*  34 */     plugin = this;
/*  35 */     Bukkit.getPluginManager().registerEvents(this, (Plugin)this);
/*  36 */     WeberBuffConfig weberBuffConfig = new WeberBuffConfig(getConfig());
/*  37 */     saveDefaultConfig();
/*     */     
/*  39 */     this.commandController = new CommandController(this);
/*     */     
/*  41 */     this.weberBuffLoadConfig = new WeberBuffLoadConfig();
/*  42 */     this.weberBuffLoadConfig.setFileName();
/*  43 */     this.weberBuffLoadConfig.setPlugin(this);
/*  44 */     this.weberBuffLoadConfig.getCustomConfig();
/*  45 */     this.models = this.weberBuffLoadConfig.itemsParser();
/*  46 */     this.weberBuffLoadConfig.saveDefaultConfig();
/*     */     
/*  48 */     this.weberBuffCombatConfig = new WeberBuffCombatConfig();
/*  49 */     this.weberBuffCombatConfig.setFileName();
/*  50 */     this.weberBuffCombatConfig.setPlugin(this);
/*  51 */     this.weberBuffCombatConfig.getCustomConfig();
/*     */     
/*  53 */     this.weberBuffCombatConfig.saveDefaultConfig();
/*     */     
/*  55 */     WeberBuffConfig.setModels(this.models);
/*     */     
/*  57 */     this.weberBuffCombatConfig.loadCombatInfo();
/*     */     
/*  59 */     WeberBuffConfig.debugMsg("注册Buff判定事件");
/*  60 */     CombatController.beforeCountModsLink.add(new BuffCombatController());
/*     */     
/*  62 */     WeberBuffEchoConfig weberBuffEchoConfig = new WeberBuffEchoConfig();
/*  63 */     weberBuffEchoConfig.setFileName();
/*  64 */     weberBuffEchoConfig.setPlugin(this);
/*  65 */     weberBuffEchoConfig.getCustomConfig();
/*  66 */     weberBuffEchoConfig.echoParser();
/*  67 */     weberBuffEchoConfig.saveDefaultConfig();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
/*  73 */     this.commandController.setCommand(sender, command, label, args, this);
/*     */     
/*     */     try {
/*  76 */       if (command.getName().equalsIgnoreCase("wrbReload")) {
/*  77 */         onDisable();
/*  78 */         WeberBuffConfig weberBuffConfig = new WeberBuffConfig(getConfig());
/*     */         
/*  80 */         this.commandController = new CommandController(this);
/*     */         
/*  82 */         this.weberBuffLoadConfig = new WeberBuffLoadConfig();
/*  83 */         this.weberBuffLoadConfig.setFileName();
/*  84 */         this.weberBuffLoadConfig.setPlugin(this);
/*  85 */         this.weberBuffLoadConfig.getCustomConfig();
/*  86 */         this.models = this.weberBuffLoadConfig.itemsParser();
/*  87 */         this.weberBuffLoadConfig.saveDefaultConfig();
/*     */         
/*  89 */         this.weberBuffCombatConfig = new WeberBuffCombatConfig();
/*  90 */         this.weberBuffCombatConfig.setFileName();
/*  91 */         this.weberBuffCombatConfig.setPlugin(this);
/*  92 */         this.weberBuffCombatConfig.getCustomConfig();
/*     */         
/*  94 */         this.weberBuffCombatConfig.saveDefaultConfig();
/*     */         
/*  96 */         WeberBuffConfig.setModels(this.models);
/*     */         
/*  98 */         this.weberBuffCombatConfig.loadCombatInfo();
/*     */ 
/*     */         
/* 101 */         WeberBuffEchoConfig weberBuffEchoConfig = new WeberBuffEchoConfig();
/* 102 */         weberBuffEchoConfig.setFileName();
/* 103 */         weberBuffEchoConfig.setPlugin(this);
/* 104 */         weberBuffEchoConfig.getCustomConfig();
/* 105 */         weberBuffEchoConfig.echoParser();
/* 106 */         weberBuffEchoConfig.saveDefaultConfig();
/* 107 */         sender.sendMessage("WRB重载完成");
/*     */         
/* 109 */         return true;
/*     */       } 
/* 111 */     } catch (Exception ex) {
/* 112 */       ex.printStackTrace();
/*     */     } 
/*     */     
/* 115 */     return this.commandController.response();
/*     */   }
/*     */ 
/*     */   
/*     */   public void onDisable() {
/* 120 */     this.weberBuffCombatConfig.saveCombatInfo();
/*     */     
/* 122 */     super.onDisable();
/*     */   }
/*     */   
/*     */   @EventHandler(priority = EventPriority.HIGHEST)
/*     */   public void onPlayerInteract(PlayerInteractEvent playerInteractEvent) {
/* 127 */     Player player = playerInteractEvent.getPlayer();
/* 128 */     if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR || playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK) {
/* 129 */       WeberBuffConfig.debugMsg("玩家右键事件");
/*     */     } else {
/*     */       return;
/*     */     } 
/* 133 */     String playerName = player.getDisplayName();
/* 134 */     if (playerName == null || playerName.equals("")) {
/*     */       return;
/*     */     }
/* 137 */     ItemStack im = playerInteractEvent.getItem();
/* 138 */     if (im == null) {
/*     */       return;
/*     */     }
/* 141 */     String buffID = WeberNBTEditor.getItemNBTStringValue(im, "weber_buff_id");
/* 142 */     if (buffID == null || buffID.equals("")) {
/*     */       return;
/*     */     }
/*     */     
/* 146 */     this.commandController.addBuff(playerName, buffID, false);
/*     */     
/* 148 */     im.setAmount(im.getAmount() - 1);
/* 149 */     if (WeberBuffConfig.publicEcho && 
/* 150 */       WeberBuffEchoConfig.echo.containsKey(buffID)) {
/* 151 */       String message = (String)WeberBuffEchoConfig.echo.get(buffID);
/* 152 */       if (message.contains("{player}")) {
/* 153 */         message = message.replace("{player}", playerName);
/*     */       }
/* 155 */       if (message.contains("{buffID}")) {
/* 156 */         message = message.replace("{buffID}", ((WeberBuffModel)WeberBuffConfig.models.get(buffID)).name);
/*     */       }
/* 158 */       echoMsg(message);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void echoMsg(String msg) {
/* 165 */     for (Player p : Bukkit.getServer().getOnlinePlayers())
/* 166 */       p.sendMessage(msg); 
/*     */   }
/*     */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\webersBuff.jar!\com\weberpyth\weberBuff\WeberBuff.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */