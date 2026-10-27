/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ public class weberAppendDamageEvent
/*    */   extends weberAttributesEvent
/*    */ {
/*    */   public weberAppendDamageEvent(String name, int trigger) {
/* 10 */     this.name = name;
/* 11 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 16 */     this.value = (attrs.get("appendDamage") == null) ? new Double(0.0D) : attrs.get("appendDamage");
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
/* 27 */     return Double.valueOf(modDamage.doubleValue() + this.value.doubleValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 32 */     return modHealth;
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
/* 43 */       return msg + String.format("§b%s附加伤害", new Object[] { (this.value.doubleValue() > 0.0D) ? "+" : "-" });
/*    */     }
/* 45 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 50 */     if (this.value.doubleValue() != 0.0D) {
/* 51 */       return msg + String.format("§b%.2f附加伤害", new Object[] { this.value });
/*    */     }
/* 53 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberAppendDamageEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */