/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import com.weberpyth.util.WeberEntityUtil;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ public class weberExecuteDamageEvent
/*    */   extends weberAttributesEvent
/*    */ {
/*    */   double opd;
/*    */   
/*    */   public weberExecuteDamageEvent(String name, int trigger) {
/* 14 */     this.name = name;
/* 15 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 20 */     this.value = (attrs.get("executeDamage") == null) ? new Double(0.0D) : attrs.get("executeDamage");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 25 */     return super.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 30 */     if (entityDamageByEntityEvent.getEntity() instanceof LivingEntity) {
/* 31 */       double maxHealth = WeberEntityUtil.getMaxHealth((LivingEntity)entityDamageByEntityEvent.getEntity()).doubleValue();
/* 32 */       double currentHealth = ((LivingEntity)entityDamageByEntityEvent.getEntity()).getHealth();
/* 33 */       double mul = 1.0D - currentHealth / maxHealth;
/* 34 */       double orginDamage = entityDamageByEntityEvent.getDamage() + modDamage.doubleValue();
/*    */       
/* 36 */       this.opd = (mul > 0.0D) ? (orginDamage * mul * 100.0D * this.value.doubleValue()) : 0.0D;
/*    */     } else {
/* 38 */       this.opd = 0.0D;
/*    */     } 
/*    */     
/* 41 */     return Double.valueOf(modDamage.doubleValue() + this.opd);
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 46 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 51 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 56 */     if (this.opd != 0.0D) {
/* 57 */       return msg + String.format("§c%s斩杀伤害", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
/*    */     }
/* 59 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 64 */     if (this.opd != 0.0D) {
/* 65 */       return msg + String.format("§c%.2f斩杀伤害", new Object[] { Double.valueOf(this.opd) });
/*    */     }
/* 67 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberExecuteDamageEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */