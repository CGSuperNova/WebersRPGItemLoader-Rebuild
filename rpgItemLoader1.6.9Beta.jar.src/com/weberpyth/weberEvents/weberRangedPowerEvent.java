/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ 
/*    */ public class weberRangedPowerEvent
/*    */   extends weberAttributesEvent
/*    */ {
/* 10 */   double opd = 0.0D;
/*    */   public weberRangedPowerEvent(String name, int trigger) {
/* 12 */     this.name = name;
/* 13 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 19 */     this.value = (attrs.get("rangedPower") == null) ? new Double(0.0D) : attrs.get("rangedPower");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 24 */     return super.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 29 */     if (entityDamageByEntityEvent.getDamager() instanceof org.bukkit.entity.Projectile) {
/* 30 */       this.opd = this.value.doubleValue();
/*    */     } else {
/* 32 */       this.opd = 0.0D;
/*    */     } 
/* 34 */     return Double.valueOf(modDamage.doubleValue() + this.opd);
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 39 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 44 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 49 */     if (this.opd != 0.0D) {
/* 50 */       return msg + String.format("§e%s远程伤害", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
/*    */     }
/* 52 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 57 */     if (this.opd != 0.0D) {
/* 58 */       return msg + String.format("§e%.2f远程伤害", new Object[] { Double.valueOf(this.opd) });
/*    */     }
/* 60 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberRangedPowerEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */