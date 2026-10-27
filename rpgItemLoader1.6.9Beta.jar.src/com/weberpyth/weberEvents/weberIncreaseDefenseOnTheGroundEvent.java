/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ 
/*    */ public class weberIncreaseDefenseOnTheGroundEvent
/*    */   extends weberAttributesEvent
/*    */ {
/*    */   double opd;
/*    */   
/*    */   public weberIncreaseDefenseOnTheGroundEvent(String name, int trigger) {
/* 13 */     this.name = name;
/* 14 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 19 */     this.value = (attrs.get("increaseDefenseOnTheGround") == null) ? new Double(0.0D) : attrs.get("increaseDefenseOnTheGround");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 24 */     return super.getValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 30 */     return modDamage;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 35 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 40 */     if (entityDamageByEntityEvent.getEntity().isOnGround()) {
/*    */       
/* 42 */       double originDamage = entityDamageByEntityEvent.getDamage() + modDamage.doubleValue();
/*    */       
/* 44 */       this.opd = originDamage * this.value.doubleValue();
/*    */     } else {
/* 46 */       this.opd = 0.0D;
/*    */     } 
/*    */     
/* 49 */     return Double.valueOf(modInjury.doubleValue() - this.opd);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 57 */     return msg;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 65 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberIncreaseDefenseOnTheGroundEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */