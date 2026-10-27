/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import com.weberpyth.util.WeberEntityUtil;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ public class weberTenacityEvent
/*    */   extends weberAttributesEvent
/*    */ {
/*    */   double opd;
/*    */   
/*    */   public weberTenacityEvent(String name, int trigger) {
/* 14 */     this.name = name;
/* 15 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 20 */     this.value = (attrs.get("tenacity") == null) ? new Double(0.0D) : attrs.get("tenacity");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 25 */     return super.getValue();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 31 */     return modDamage;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 36 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 41 */     if (entityDamageByEntityEvent.getEntity() instanceof LivingEntity) {
/* 42 */       double maxHealth = WeberEntityUtil.getMaxHealth((LivingEntity)entityDamageByEntityEvent.getEntity()).doubleValue();
/* 43 */       double currentHealth = ((LivingEntity)entityDamageByEntityEvent.getEntity()).getHealth();
/* 44 */       double mul = 1.0D - currentHealth / maxHealth;
/* 45 */       double originDamage = entityDamageByEntityEvent.getDamage() + modDamage.doubleValue();
/*    */       
/* 47 */       this.opd = (mul > 0.0D) ? (originDamage * mul * 100.0D * this.value.doubleValue()) : 0.0D;
/*    */     } else {
/* 49 */       this.opd = 0.0D;
/*    */     } 
/*    */     
/* 52 */     return Double.valueOf(modInjury.doubleValue() - this.opd);
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 60 */     return msg;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 68 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberTenacityEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */