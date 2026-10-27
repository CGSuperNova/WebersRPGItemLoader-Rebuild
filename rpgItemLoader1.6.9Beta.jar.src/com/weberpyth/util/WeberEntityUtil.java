/*    */ package com.weberpyth.util;
/*    */ 
/*    */ import org.bukkit.attribute.Attribute;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.entity.Projectile;
/*    */ 
/*    */ public class WeberEntityUtil {
/*    */   public static Double getMaxHealth(LivingEntity entity) {
/*    */     try {
/* 11 */       return Double.valueOf(entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
/* 12 */     } catch (NoClassDefFoundError t) {
/* 13 */       return Double.valueOf(entity.getMaxHealth());
/*    */     } 
/*    */   }
/*    */   public static Double getCurrentHealth(LivingEntity entity) {
/* 17 */     return Double.valueOf(entity.getHealth());
/*    */   }
/*    */   public static LivingEntity findShooter(Entity e) {
/* 20 */     if (e instanceof Projectile && (
/* 21 */       (Projectile)e).getShooter() instanceof LivingEntity) {
/* 22 */       return (LivingEntity)((Projectile)e).getShooter();
/*    */     }
/*    */ 
/*    */     
/* 26 */     return null;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyt\\util\WeberEntityUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */