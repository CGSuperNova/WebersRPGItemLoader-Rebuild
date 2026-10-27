/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ 
/*    */ public class weberInjuryReductionEvent
/*    */   extends weberAttributesEvent
/*    */ {
/*    */   double opd;
/*    */   
/*    */   public weberInjuryReductionEvent(String name, int trigger) {
/* 13 */     this.name = name;
/* 14 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 19 */     this.value = (attrs.get("injuryReduction") == null) ? new Double(0.0D) : attrs.get("injuryReduction");
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
/* 40 */     if (entityDamageByEntityEvent.getEntity() instanceof org.bukkit.entity.LivingEntity) {
/* 41 */       double orginDamage = entityDamageByEntityEvent.getDamage() + modDamage.doubleValue();
/*    */       
/* 43 */       this.opd = orginDamage * this.value.doubleValue();
/*    */     } else {
/* 45 */       this.opd = 0.0D;
/*    */     } 
/*    */     
/* 48 */     return Double.valueOf(modInjury.doubleValue() - this.opd);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 56 */     return msg;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 64 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberInjuryReductionEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */