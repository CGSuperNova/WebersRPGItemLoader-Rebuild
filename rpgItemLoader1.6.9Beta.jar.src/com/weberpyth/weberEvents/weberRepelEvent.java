/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ import org.bukkit.util.Vector;
/*    */ 
/*    */ public class weberRepelEvent
/*    */   extends weberAttributesEvent
/*    */ {
/*    */   double opd;
/*    */   
/*    */   public weberRepelEvent(String name, int trigger) {
/* 14 */     this.name = name;
/* 15 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 20 */     this.value = (attrs.get("repel") == null) ? new Double(0.0D) : attrs.get("repel");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 25 */     return super.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 30 */     LivingEntity e1 = null;
/* 31 */     LivingEntity e2 = null;
/*    */     
/* 33 */     if (entityDamageByEntityEvent.getDamager() instanceof LivingEntity && entityDamageByEntityEvent.getEntity() instanceof LivingEntity) {
/* 34 */       e1 = (LivingEntity)entityDamageByEntityEvent.getDamager();
/* 35 */       e2 = (LivingEntity)entityDamageByEntityEvent.getEntity();
/*    */     } else {
/* 37 */       return modDamage;
/*    */     } 
/* 39 */     if (this.value.doubleValue() == 0.0D) {
/* 40 */       return modDamage;
/*    */     }
/*    */     
/* 43 */     Vector v = e2.getLocation().subtract(e1.getLocation()).toVector();
/*    */ 
/*    */ 
/*    */     
/* 47 */     v = v.normalize();
/*    */     
/* 49 */     e2.setVelocity(e2.getVelocity().add(v.multiply(this.value.doubleValue())));
/* 50 */     return modDamage;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 55 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 60 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 65 */     if (this.value.doubleValue() != 0.0D) {
/* 66 */       return msg + String.format("§5%s额外击退强度", new Object[] { (this.value.doubleValue() > 0.0D) ? "+" : "-" });
/*    */     }
/* 68 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 73 */     if (this.value.doubleValue() != 0.0D) {
/* 74 */       return msg + String.format("§5%.2f额外击退强度", new Object[] { this.value });
/*    */     }
/* 76 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberRepelEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */