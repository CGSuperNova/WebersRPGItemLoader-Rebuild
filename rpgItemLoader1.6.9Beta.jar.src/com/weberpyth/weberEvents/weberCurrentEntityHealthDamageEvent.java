/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ public class weberCurrentEntityHealthDamageEvent
/*    */   extends weberAttributesEvent {
/*    */   private double opd;
/*    */   
/*    */   public weberCurrentEntityHealthDamageEvent(String name, int trigger) {
/* 12 */     this.name = name;
/* 13 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 18 */     this.value = (attrs.get("currentEntityHealthDamage") == null) ? new Double(0.0D) : attrs.get("currentEntityHealthDamage");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 23 */     return super.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 28 */     if (entityDamageByEntityEvent.getEntity() instanceof LivingEntity) {
/* 29 */       this.opd = ((LivingEntity)entityDamageByEntityEvent.getEntity()).getHealth() * this.value.doubleValue();
/*    */     } else {
/* 31 */       this.opd = 0.0D;
/*    */     } 
/*    */     
/* 34 */     return Double.valueOf(modDamage.doubleValue() + this.opd);
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 39 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 44 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 49 */     if (this.opd != 0.0D) {
/* 50 */       return msg + String.format("§3%.2f当前目标生命值伤害", new Object[] { Double.valueOf(this.opd) });
/*    */     }
/* 52 */     return msg;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 58 */     if (this.opd != 0.0D) {
/* 59 */       return msg + String.format("§3%s当前目标生命值伤害", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
/*    */     }
/* 61 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberCurrentEntityHealthDamageEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */