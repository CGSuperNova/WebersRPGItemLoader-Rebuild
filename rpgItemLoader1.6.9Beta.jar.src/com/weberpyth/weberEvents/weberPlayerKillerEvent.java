/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ public class weberPlayerKillerEvent
/*    */   extends weberAttributesEvent
/*    */ {
/*    */   double opd;
/*    */   
/*    */   public weberPlayerKillerEvent(String name, int trigger) {
/* 12 */     this.name = name;
/* 13 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 18 */     this.value = (attrs.get("playerKiller") == null) ? new Double(0.0D) : attrs.get("playerKiller");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 23 */     return super.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 28 */     if (entityDamageByEntityEvent.getEntity() instanceof org.bukkit.entity.Player) {
/* 29 */       this.opd = (entityDamageByEntityEvent.getDamage() + modDamage.doubleValue()) * this.value.doubleValue();
/* 30 */       return Double.valueOf(modDamage.doubleValue() + this.opd);
/*    */     } 
/* 32 */     this.opd = 0.0D;
/* 33 */     return modDamage;
/*    */   }
/*    */ 
/*    */ 
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
/* 51 */       return msg + String.format("§e%s玩家杀手", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
/*    */     }
/* 53 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 58 */     if (this.opd != 0.0D) {
/* 59 */       return msg + String.format("§e%.2f玩家杀手", new Object[] { Double.valueOf(this.opd) });
/*    */     }
/* 61 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberPlayerKillerEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */