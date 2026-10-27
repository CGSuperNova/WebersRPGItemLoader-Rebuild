/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import com.weberpyth.util.WeberEntityUtil;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
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
/*    */ public class weberRegurgitationCurrentHealthDamageEvent
/*    */   extends weberAttributesEvent
/*    */ {
/*    */   double opd;
/*    */   
/*    */   public weberRegurgitationCurrentHealthDamageEvent(String name, int trigger) {
/* 23 */     this.name = name;
/* 24 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 29 */     this.value = (attrs.get("regurgitationCurrentHealthDamage") == null) ? new Double(0.0D) : attrs.get("regurgitationCurrentHealthDamage");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 34 */     return super.getValue();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 41 */     return modDamage;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 46 */     LivingEntity damager = null;
/* 47 */     if (entityDamageByEntityEvent.getDamager() instanceof org.bukkit.entity.Projectile) {
/* 48 */       damager = WeberEntityUtil.findShooter(entityDamageByEntityEvent.getDamager());
/* 49 */     } else if (entityDamageByEntityEvent.getDamager() instanceof LivingEntity) {
/* 50 */       damager = (LivingEntity)entityDamageByEntityEvent.getDamager();
/*    */     } else {
/* 52 */       damager = null;
/*    */     } 
/* 54 */     if (damager != null) {
/* 55 */       double currentHealth = damager.getHealth();
/* 56 */       this.opd = currentHealth * this.value.doubleValue();
/*    */     } else {
/* 58 */       this.opd = 0.0D;
/*    */     } 
/*    */     
/* 61 */     return Double.valueOf(modHealth.doubleValue() - this.opd);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 67 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 72 */     if (this.opd != 0.0D) {
/* 73 */       return msg + String.format("§4%s伤害反噬", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
/*    */     }
/* 75 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 80 */     if (this.opd != 0.0D) {
/* 81 */       return msg + String.format("§4%.2f伤害反噬", new Object[] { Double.valueOf(this.opd) });
/*    */     }
/* 83 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberRegurgitationCurrentHealthDamageEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */