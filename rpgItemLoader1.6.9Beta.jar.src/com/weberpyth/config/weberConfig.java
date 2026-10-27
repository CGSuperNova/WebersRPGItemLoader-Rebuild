/*     */ package com.weberpyth.config;
/*     */ 
/*     */ import org.bukkit.Bukkit;
/*     */ import org.bukkit.configuration.file.FileConfiguration;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class weberConfig
/*     */ {
/*     */   public static boolean isDebug;
/*     */   public static boolean playerOnly;
/*     */   public static String version;
/*     */   public static boolean battleEcho;
/*     */   public static boolean noOriginDamage;
/*     */   public static boolean lightBattleEcho;
/*     */   public static boolean noImmutableTick;
/*     */   public static boolean noNBTAPI;
/*     */   public static boolean loreCheck;
/*     */   
/*     */   public weberConfig(FileConfiguration fc, String v) {
/*  22 */     isDebug = fc.getConfigurationSection(fc.getCurrentPath()).getBoolean("isDebug");
/*  23 */     playerOnly = fc.getConfigurationSection(fc.getCurrentPath()).getBoolean("playerOnly");
/*  24 */     battleEcho = fc.getConfigurationSection(fc.getCurrentPath()).getBoolean("battleEcho");
/*  25 */     noOriginDamage = fc.getConfigurationSection(fc.getCurrentPath()).getBoolean("noOriginDamage");
/*  26 */     lightBattleEcho = fc.getConfigurationSection(fc.getCurrentPath()).getBoolean("lightBattleEcho");
/*  27 */     noImmutableTick = fc.getConfigurationSection(fc.getCurrentPath()).getBoolean("noImmutableTick");
/*  28 */     noNBTAPI = fc.getConfigurationSection(fc.getCurrentPath()).getBoolean("noNBTAPI");
/*  29 */     loreCheck = fc.getConfigurationSection(fc.getCurrentPath()).getBoolean("loreCheck");
/*     */     
/*  31 */     version = v;
/*  32 */     debugInfo();
/*     */   }
/*     */   
/*     */   public static void debugInfo() {
/*  36 */     showDEBUGMsg("WRL插件Debug模式：" + (isDebug ? "开启" : "关闭"));
/*  37 */     showDEBUGMsg("WRL插件仅玩家模式：" + (playerOnly ? "开启" : "关闭"));
/*  38 */     showDEBUGMsg("WRL插件战斗回显状态：" + (battleEcho ? "开启" : "关闭"));
/*  39 */     showDEBUGMsg("WRL插件原版伤害：" + (!noOriginDamage ? "开启" : "关闭"));
/*  40 */     if (isDebug) {
/*  41 */       showDEBUGMsg("[警告]WRL插件正处于Debug模式!");
/*     */     }
/*  43 */     if (playerOnly) {
/*  44 */       showDEBUGMsg("[警告]WRL插件正处于仅玩家模式!");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public static void HyperionDebugMSG() {
/*  50 */     Bukkit.getLogger().info("\n                                                             $$            \n  $$     $$     $$  $$$$$$$   $$$$$$     $$$$$$$  $$$$$$$    $b   $$$$$    \n  $$    $$$     $$  $$        $~   $$    $        $$   \"$~   $   $$   $    \n  $$    $ $    $$   $$        $    $$   $$        $$    $$  \"$  $$         \n  $$   $$ $    $~   $~       L$    $$   $$        $b    $       $$         \n  B$   $  $   $$    $        $$   $$    $8        $   $$$        $$$       \n   $  $$  $   $    $$$$$$8   $$$$$$$    $$$$$$   $$$$$$           $$$:     \n   $  $   $8 $$    $$        $     $$  ^$        $$   $$            $$     \n   $ $$   $$ $     $         $     $$  $$        $    $$             $     \n   $ $    $$$$     $        $$     $i  $$        $    B$            $$     \n   $$$    $q$     B$        $$   $$$   $        B$     $      b$$  $$$     \n   $$     $$~     $$$$$$$   $$$$$$     $$$$$$   $$     $~       $$$$       \n                                                                           \n                                                                           \n\n    $$$$$$     $$$$$$       $$$$$$    b$                                   \n    $   $$$    $   b$$    $$$    $$   $$    $                              \n   $$    $$   L$    $$   $$           $$    $                              \n   $$    $$   $$    b$  ^$~           $$  $$$$$$   $$$$$    $ $$$$  $$$$   \n   $1   $$    $$    $$  $$            $    $$     $$   $i  \"$$$  $8$$ $$   \n   $$$$$$I    $    $$   $    $$$$$   b$    $     $$    $B  $$$   $$    $:  \n  $$  $$o    ^$$$$$$   $$    $$$$$   $$    $     $   $$$   $$    $$    $   \n  $$   $$    $$        $$       $$   $$   $$    $$$$$%     $     $    $$   \n  $$    $    $$        $$       $    $    $$    $$        ^$    B$    $$   \n  $b    $$   $$         $$     b$   \"$    $$    $$        $$    $$    $8   \n  $     $$   $          M$$$  $$$   $$    $$ ]  $$$   $   $$    $$    $    \n $$     $$  $$            $$$$$     $$    '$$$    $$$$    $B    $    $$                                                                             \n                                                                           \n\n                                        $                   \n    $$                                 ^$                   \n    $$                                 $$                   \n    $$                                 $$                   \n    $        $$$$$      $$$$\"$    $$$$ $     $$$$$   $$ $$' \n   L$       $$   $$   $$8  $$$   $$  B$$   b$8  $$   $~$    \n   $$      $$     $~  $$    $$  $$    $$   $    .$   $$     \n   $$      $$     $~ b$     $   $$    $$  $$  $$$$   $i     \n   $i      $      $  $$    ^$   $     $$  $$$$$     B$      \n   $      *$     $$  $$    $$  :$     $   $8        $$      \n  $$      \"$     $~  $$   $$$  :$    $$   $$        $$      \n  $$ UUC$  $$  b$$   $$  $$$i   $$ /$ $   $$    $   $~      \n  $$$$$$$   $$$$      $$$$ $    $$$$ $$    $$$$$    $  ");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static void debugMsg(String msg) {
/*  95 */     if (isDebug)
/*  96 */       showDEBUGMsg(msg); 
/*     */   }
/*     */   
/*     */   private static void showDEBUGMsg(String msg) {
/* 100 */     Bukkit.getLogger().info("[Weber's RPG Item Loader:DEBUG]" + msg);
/*     */   }
/*     */   public static void showPublicMsg(String msg) {
/* 103 */     Bukkit.getLogger().info("[Weber's RPG Item Loader]" + msg);
/*     */   }
/*     */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\config\weberConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */