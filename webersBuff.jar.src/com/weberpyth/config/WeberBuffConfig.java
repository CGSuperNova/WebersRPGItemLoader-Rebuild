/*    */ package com.weberpyth.config;
/*    */ 
/*    */ import com.weberpyth.weberBuffData.WeberBuffModel;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.Bukkit;
/*    */ import org.bukkit.configuration.file.FileConfiguration;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WeberBuffConfig
/*    */ {
/*    */   public static boolean isDebug;
/*    */   public static boolean publicEcho;
/*    */   public static HashMap<String, WeberBuffModel> models;
/*    */   
/*    */   public WeberBuffConfig(FileConfiguration fc) {
/* 29 */     isDebug = fc.getConfigurationSection(fc.getCurrentPath()).getBoolean("isDebug");
/* 30 */     publicEcho = fc.getConfigurationSection(fc.getCurrentPath()).getBoolean("publicEcho");
/* 31 */     showWeberBuffInfo();
/*    */   }
/*    */   public static void showWeberBuffInfo() {
/* 34 */     showPublicMsg(String.format("Debug模式: %s", new Object[] { isDebug ? "开启" : "关闭" }));
/* 35 */     showPublicMsg(String.format("全服广播回显模式: %s", new Object[] { publicEcho ? "开启" : "关闭" }));
/* 36 */     debugMsg("插件正处于DeBug模式！");
/*    */   }
/*    */   public static void debugMsg(String msg) {
/* 39 */     if (isDebug)
/* 40 */       showDEBUGMsg(msg); 
/*    */   }
/*    */   
/*    */   public static void setModels(HashMap<String, WeberBuffModel> m) {
/* 44 */     models = m;
/*    */   }
/*    */   private static void showDEBUGMsg(String msg) {
/* 47 */     Bukkit.getLogger().info("[Weber's Buff:DEBUG]" + msg);
/*    */   }
/*    */   public static void showPublicMsg(String msg) {
/* 50 */     Bukkit.getLogger().info("[Weber's Buff]" + msg);
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\webersBuff.jar!\com\weberpyth\config\WeberBuffConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */