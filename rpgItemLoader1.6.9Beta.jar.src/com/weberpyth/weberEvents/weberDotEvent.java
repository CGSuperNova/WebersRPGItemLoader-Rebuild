/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.event.entity.EntityDamageEvent;
/*    */ 
/*    */ 
/*    */ public class weberDotEvent
/*    */ {
/*  9 */   String name = null;
/* 10 */   Double value = new Double(0.0D);
/* 11 */   int trigger = 0;
/* 12 */   public Double countAttack(Double modDamage, EntityDamageEvent entityDamageEvent) { return null; }
/* 13 */   public Double countHealth(Double modHealth, EntityDamageEvent entityDamageEvent) { return null; } public Double countDefence(Double modInjury, Double modDamage, EntityDamageEvent entityDamageEvent) {
/* 14 */     return null;
/*    */   } public int getTrigger() {
/* 16 */     return this.trigger;
/*    */   } public void setValue(HashMap<String, Double> attrs) {} public String getName() {
/* 18 */     return this.name;
/*    */   }
/*    */   public Double getValue() {
/* 21 */     return this.value;
/*    */   }
/* 23 */   public String setNumInfo(String msg) { return msg; } public String setNoNumInfo(String msg) {
/* 24 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberDotEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */