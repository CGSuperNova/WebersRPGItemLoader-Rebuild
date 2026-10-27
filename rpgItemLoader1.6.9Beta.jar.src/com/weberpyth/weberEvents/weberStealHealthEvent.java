/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ public class weberStealHealthEvent
/*    */   extends weberAttributesEvent {
/*    */   double oph;
/*    */   
/*    */   public weberStealHealthEvent(String name, int trigger) {
/* 11 */     this.name = name;
/* 12 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 17 */     this.value = (attrs.get("stealHealth") == null) ? new Double(0.0D) : attrs.get("stealHealth");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 22 */     return super.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 27 */     this.oph = entityDamageByEntityEvent.getDamage() * this.value.doubleValue();
/* 28 */     return modDamage;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 33 */     return Double.valueOf(modHealth.doubleValue() + this.oph);
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 38 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 43 */     if (this.oph != 0.0D) {
/* 44 */       return msg + String.format("§c%s生命偷取", new Object[] { (this.oph > 0.0D) ? "+" : "-" });
/*    */     }
/* 46 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 51 */     if (this.oph != 0.0D) {
/* 52 */       return msg + String.format("§c%.2f生命偷取", new Object[] { Double.valueOf(this.oph) });
/*    */     }
/* 54 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberStealHealthEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */