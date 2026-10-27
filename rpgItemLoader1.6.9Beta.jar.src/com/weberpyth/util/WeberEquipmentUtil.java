/*    */ package com.weberpyth.util;
/*    */ 
/*    */ import org.bukkit.inventory.EntityEquipment;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ 
/*    */ public class WeberEquipmentUtil
/*    */ {
/*    */   public static ItemStack getItemInMainHand(EntityEquipment entityEquipment) {
/*    */     try {
/* 10 */       return entityEquipment.getItemInMainHand();
/* 11 */     } catch (Throwable t) {
/* 12 */       return entityEquipment.getItemInHand();
/*    */     } 
/*    */   }
/*    */   public static ItemStack getItemInOffHand(EntityEquipment entityEquipment) {
/*    */     try {
/* 17 */       return entityEquipment.getItemInOffHand();
/* 18 */     } catch (Throwable t) {
/* 19 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyt\\util\WeberEquipmentUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */