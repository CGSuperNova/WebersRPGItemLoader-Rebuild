/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ public class weberDamageEvent
/*    */   extends weberAttributesEvent
/*    */ {
/*    */   public weberDamageEvent(String name, int trigger) {
/* 10 */     this.name = name;
/* 11 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 16 */     this.value = (attrs.get("damage") == null) ? new Double(0.0D) : attrs.get("damage");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 21 */     return super.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 26 */     return Double.valueOf(modDamage.doubleValue() + this.value.doubleValue());
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 31 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 36 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 41 */     if (this.value.doubleValue() != 0.0D) {
/* 42 */       return msg + String.format("§f%s伤害", new Object[] { (this.value.doubleValue() > 0.0D) ? "+" : "-" });
/*    */     }
/* 44 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 49 */     if (this.value.doubleValue() != 0.0D) {
/* 50 */       return msg + String.format("§f%.2f伤害", new Object[] { this.value });
/*    */     }
/* 52 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberDamageEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */