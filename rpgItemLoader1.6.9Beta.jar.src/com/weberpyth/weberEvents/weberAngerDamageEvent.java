/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import com.weberpyth.util.WeberEntityUtil;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ 
/*    */ public class weberAngerDamageEvent
/*    */   extends weberAttributesEvent
/*    */ {
/*    */   double opd;
/*    */   
/*    */   public weberAngerDamageEvent(String name, int trigger) {
/* 15 */     this.name = name;
/* 16 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 21 */     this.value = (attrs.get("angerDamage") == null) ? new Double(0.0D) : attrs.get("angerDamage");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 26 */     return super.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 31 */     LivingEntity damager = null;
/* 32 */     if (entityDamageByEntityEvent.getDamager() instanceof org.bukkit.entity.Projectile) {
/* 33 */       damager = WeberEntityUtil.findShooter(entityDamageByEntityEvent.getDamager());
/* 34 */     } else if (entityDamageByEntityEvent.getDamager() instanceof LivingEntity) {
/* 35 */       damager = (LivingEntity)entityDamageByEntityEvent.getDamager();
/*    */     } else {
/* 37 */       damager = null;
/*    */     } 
/* 39 */     if (damager != null) {
/* 40 */       double maxHealth = WeberEntityUtil.getMaxHealth(damager).doubleValue();
/* 41 */       double currentHealth = damager.getHealth();
/* 42 */       double mul = 1.0D - currentHealth / maxHealth;
/* 43 */       double orginDamage = entityDamageByEntityEvent.getDamage() + modDamage.doubleValue();
/*    */       
/* 45 */       this.opd = (mul > 0.0D) ? (orginDamage * mul * 100.0D * this.value.doubleValue()) : 0.0D;
/*    */     } else {
/* 47 */       this.opd = 0.0D;
/*    */     } 
/*    */     
/* 50 */     return Double.valueOf(modDamage.doubleValue() + this.opd);
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 55 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 60 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 65 */     if (this.opd != 0.0D) {
/* 66 */       return msg + String.format("§5%s狂怒伤害", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
/*    */     }
/* 68 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 73 */     if (this.opd != 0.0D) {
/* 74 */       return msg + String.format("§5%.2f狂怒伤害", new Object[] { Double.valueOf(this.opd) });
/*    */     }
/* 76 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberAngerDamageEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */