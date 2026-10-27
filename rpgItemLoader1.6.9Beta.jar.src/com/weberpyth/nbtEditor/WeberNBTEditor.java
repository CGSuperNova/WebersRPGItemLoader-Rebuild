/*    */ package com.weberpyth.nbtEditor;
/*    */ 
/*    */ import com.weberpyth.config.weberConfig;
/*    */ import com.weberpyth.item.WeberRPGItem;
/*    */ import de.tr7zw.nbtapi.NBTItem;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public class WeberNBTEditor {
/*    */   public static WeberNBTItem weberNBTItem;
/*    */   public static boolean hasNBTAPI = true;
/*    */   
/*    */   public static void initWeberNBTEditor() {
/*    */     try {
/* 14 */       weberNBTItem = new WeberNBTItem(null);
/* 15 */     } catch (Exception e) {
/* 16 */       weberConfig.showPublicMsg("警告!前置NBTAPI插件未正常工作！插件部分功能受限！");
/* 17 */       hasNBTAPI = false;
/*    */     } 
/*    */   }
/*    */   
/*    */   public static void setItemNBTDoubleValue(ItemStack im, String key, double value) {
/* 22 */     NBTItem nbtItem = new NBTItem(im);
/* 23 */     nbtItem.setDouble(key, Double.valueOf(value));
/* 24 */     nbtItem.applyNBT(im);
/* 25 */     nbtItem = null;
/*    */   }
/*    */   public static double getItemNBTDoubleValue(ItemStack im, String key) {
/* 28 */     weberNBTItem.reloadItem(im);
/* 29 */     return weberNBTItem.getDouble(key).doubleValue();
/*    */   }
/*    */   public static String getItemNBTStringValue(ItemStack im, String key) {
/* 32 */     weberNBTItem.reloadItem(im);
/* 33 */     return weberNBTItem.getString(key);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void setItemNBTStringValue(ItemStack im, String key, String value) {
/* 40 */     weberNBTItem.reloadItem(im);
/* 41 */     weberNBTItem.setString(key, value);
/* 42 */     weberNBTItem.applyNBT(im);
/*    */   }
/*    */   
/*    */   public static void setItemNBTFromWRPGItem(ItemStack im, WeberRPGItem weberRPGItem) {
/* 46 */     weberNBTItem.reloadItem(im);
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 52 */     weberNBTItem.setString("weber_id", weberRPGItem.id);
/* 53 */     weberNBTItem.applyNBT(im);
/*    */   }
/*    */   public static WeberRPGItem getWRPGItemFromNBT(ItemStack im) {
/* 56 */     weberNBTItem.reloadItem(im);
/* 57 */     WeberRPGItem weberRPGItem = new WeberRPGItem();
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 63 */     weberRPGItem.id = weberNBTItem.getString("weber_id");
/* 64 */     return weberRPGItem;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\nbtEditor\WeberNBTEditor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */