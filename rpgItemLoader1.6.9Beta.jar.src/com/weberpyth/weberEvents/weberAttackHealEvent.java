/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ public class weberAttackHealEvent
/*    */   extends weberAttributesEvent
/*    */ {
/*    */   public weberAttackHealEvent(String name, int trigger) {
/* 10 */     this.name = name;
/* 11 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 16 */     this.value = (attrs.get("attackHeal") == null) ? new Double(0.0D) : attrs.get("attackHeal");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 21 */     return super.getValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 27 */     return modDamage;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 32 */     return Double.valueOf(modHealth.doubleValue() + this.value.doubleValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 37 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 42 */     if (this.value.doubleValue() != 0.0D) {
/* 43 */       return msg + String.format("§3%s附加治疗", new Object[] { (this.value.doubleValue() > 0.0D) ? "+" : "-" });
/*    */     }
/* 45 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 50 */     if (this.value.doubleValue() != 0.0D) {
/* 51 */       return msg + String.format("§3%.2f附加治疗", new Object[] { this.value });
/*    */     }
/* 53 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberAttackHealEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */