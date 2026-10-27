/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class weberMeleeDamageEvent
/*    */   extends weberAttributesEvent
/*    */ {
/* 12 */   double opd = 0.0D;
/*    */   public weberMeleeDamageEvent(String name, int trigger) {
/* 14 */     this.name = name;
/* 15 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 21 */     this.value = (attrs.get("meleeDamage") == null) ? new Double(0.0D) : attrs.get("meleeDamage");
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
/* 33 */       this.opd = 0.0D;
/*    */     } else {
/* 35 */       this.opd = (entityDamageByEntityEvent.getDamage() + modDamage.doubleValue()) * this.value.doubleValue();
/*    */     } 
/* 37 */     return Double.valueOf(modDamage.doubleValue() + this.opd);
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 42 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 47 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 52 */     if (this.opd != 0.0D) {
/* 53 */       return msg + String.format("§e%s近战输出伤害", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
/*    */     }
/* 55 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 60 */     if (this.opd != 0.0D) {
/* 61 */       return msg + String.format("§e%.2f近战输出伤害", new Object[] { Double.valueOf(this.opd) });
/*    */     }
/* 63 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberMeleeDamageEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */