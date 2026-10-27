/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ 
/*    */ public class weberMeleePowerEvent
/*    */   extends weberAttributesEvent
/*    */ {
/* 11 */   double opd = 0.0D;
/*    */   public weberMeleePowerEvent(String name, int trigger) {
/* 13 */     this.name = name;
/* 14 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 20 */     this.value = (attrs.get("meleePower") == null) ? new Double(0.0D) : attrs.get("meleePower");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 25 */     return super.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 30 */     LivingEntity damager = null;
/* 31 */     if (entityDamageByEntityEvent.getDamager() instanceof org.bukkit.entity.Projectile) {
/* 32 */       this.opd = 0.0D;
/*    */     } else {
/* 34 */       this.opd = this.value.doubleValue();
/*    */     } 
/* 36 */     return Double.valueOf(modDamage.doubleValue() + this.opd);
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 41 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 46 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 51 */     if (this.opd != 0.0D) {
/* 52 */       return msg + String.format("§e%s近战伤害", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
/*    */     }
/* 54 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 59 */     if (this.opd != 0.0D) {
/* 60 */       return msg + String.format("§e%.2f近战伤害", new Object[] { Double.valueOf(this.opd) });
/*    */     }
/* 62 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberMeleePowerEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */