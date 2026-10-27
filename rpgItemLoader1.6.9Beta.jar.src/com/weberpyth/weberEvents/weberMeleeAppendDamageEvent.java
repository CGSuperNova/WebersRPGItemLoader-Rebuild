/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ 
/*    */ public class weberMeleeAppendDamageEvent
/*    */   extends weberAttributesEvent
/*    */ {
/* 10 */   double opd = 0.0D;
/*    */   public weberMeleeAppendDamageEvent(String name, int trigger) {
/* 12 */     this.name = name;
/* 13 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 18 */     this.value = (attrs.get("meleeAppendDamage") == null) ? new Double(0.0D) : attrs.get("meleeAppendDamage");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 23 */     return super.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 28 */     if (entityDamageByEntityEvent.getDamager() instanceof org.bukkit.entity.Projectile) {
/* 29 */       this.opd = 0.0D;
/*    */     } else {
/* 31 */       this.opd = this.value.doubleValue();
/*    */     } 
/* 33 */     return Double.valueOf(modDamage.doubleValue() + this.opd);
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 38 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 43 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 48 */     if (this.opd != 0.0D) {
/* 49 */       return msg + String.format("§b%s近战附加伤害", new Object[] { (this.value.doubleValue() > 0.0D) ? "+" : "-" });
/*    */     }
/* 51 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 56 */     if (this.opd != 0.0D) {
/* 57 */       return msg + String.format("§b%.2f近战附加伤害", new Object[] { this.value });
/*    */     }
/* 59 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberMeleeAppendDamageEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */