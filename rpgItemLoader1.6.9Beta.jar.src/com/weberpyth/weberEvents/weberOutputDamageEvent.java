/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ public class weberOutputDamageEvent
/*    */   extends weberAttributesEvent
/*    */ {
/*  9 */   double opd = 0.0D;
/*    */   public weberOutputDamageEvent(String name, int trigger) {
/* 11 */     this.name = name;
/* 12 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 17 */     this.value = (attrs.get("outputDamage") == null) ? new Double(0.0D) : attrs.get("outputDamage");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 22 */     return super.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 27 */     this.opd = (entityDamageByEntityEvent.getDamage() + modDamage.doubleValue()) * this.value.doubleValue();
/* 28 */     return Double.valueOf(modDamage.doubleValue() + this.opd);
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 33 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 38 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 43 */     if (this.opd != 0.0D) {
/* 44 */       return msg + String.format("§e%s输出伤害", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
/*    */     }
/* 46 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 51 */     if (this.opd != 0.0D) {
/* 52 */       return msg + String.format("§e%.2f输出伤害", new Object[] { Double.valueOf(this.opd) });
/*    */     }
/* 54 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberOutputDamageEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */