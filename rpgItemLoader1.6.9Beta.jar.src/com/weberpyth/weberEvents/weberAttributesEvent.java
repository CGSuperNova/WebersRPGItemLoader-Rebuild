/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ public class weberAttributesEvent
/*    */ {
/*  8 */   String name = null;
/*  9 */   Double value = new Double(0.0D);
/* 10 */   int trigger = 0;
/* 11 */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) { return null; }
/* 12 */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) { return null; } public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 13 */     return null;
/*    */   } public int getTrigger() {
/* 15 */     return this.trigger;
/*    */   } public void setValue(HashMap<String, Double> attrs) {} public String getName() {
/* 17 */     return this.name;
/*    */   }
/*    */   public Double getValue() {
/* 20 */     return this.value;
/*    */   }
/* 22 */   public String setNumInfo(String msg) { return msg; } public String setNoNumInfo(String msg) {
/* 23 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberAttributesEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */