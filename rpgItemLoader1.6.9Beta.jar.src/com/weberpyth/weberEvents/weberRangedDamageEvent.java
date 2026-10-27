/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class weberRangedDamageEvent
/*    */   extends weberAttributesEvent
/*    */ {
/* 11 */   double opd = 0.0D;
/*    */   public weberRangedDamageEvent(String name, int trigger) {
/* 13 */     this.name = name;
/* 14 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 20 */     this.value = (attrs.get("rangedDamage") == null) ? new Double(0.0D) : attrs.get("rangedDamage");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 25 */     return super.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 30 */     if (entityDamageByEntityEvent.getDamager() instanceof org.bukkit.entity.Projectile) {
/* 31 */       this.opd = (entityDamageByEntityEvent.getDamage() + modDamage.doubleValue()) * this.value.doubleValue();
/*    */     } else {
/* 33 */       this.opd = 0.0D;
/*    */     } 
/* 35 */     return Double.valueOf(modDamage.doubleValue() + this.opd);
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 40 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 45 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 50 */     if (this.opd != 0.0D) {
/* 51 */       return msg + String.format("§e%s远程输出伤害", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
/*    */     }
/* 53 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 58 */     if (this.opd != 0.0D) {
/* 59 */       return msg + String.format("§e%.2f远程输出伤害", new Object[] { Double.valueOf(this.opd) });
/*    */     }
/* 61 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberRangedDamageEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */