/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import com.weberpyth.util.WeberEntityUtil;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ public class weberIncreasesDamageOnTheGroundEvent
/*    */   extends weberAttributesEvent
/*    */ {
/*    */   double opd;
/*    */   
/*    */   public weberIncreasesDamageOnTheGroundEvent(String name, int trigger) {
/* 14 */     this.name = name;
/* 15 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 20 */     this.value = (attrs.get("increasesDamageOnTheGround") == null) ? new Double(0.0D) : attrs.get("increasesDamageOnTheGround");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 25 */     return super.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 30 */     this.opd = 0.0D;
/* 31 */     LivingEntity damager = null;
/* 32 */     if (entityDamageByEntityEvent.getDamager() instanceof org.bukkit.entity.Projectile) {
/* 33 */       damager = WeberEntityUtil.findShooter(entityDamageByEntityEvent.getDamager());
/* 34 */     } else if (entityDamageByEntityEvent.getDamager() instanceof LivingEntity) {
/* 35 */       damager = (LivingEntity)entityDamageByEntityEvent.getDamager();
/*    */     } else {
/* 37 */       damager = null;
/*    */     } 
/* 39 */     if (damager != null && damager.isOnGround()) {
/* 40 */       this.opd = (entityDamageByEntityEvent.getDamage() + modDamage.doubleValue()) * this.value.doubleValue();
/*    */     }
/* 42 */     return Double.valueOf(modDamage.doubleValue() + this.opd);
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 47 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 52 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 57 */     if (this.opd != 0.0D) {
/* 58 */       return msg + String.format("§e%s地面攻击增益", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
/*    */     }
/* 60 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 65 */     if (this.opd != 0.0D) {
/* 66 */       return msg + String.format("§e%.2f地面攻击增益", new Object[] { Double.valueOf(this.opd) });
/*    */     }
/* 68 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberIncreasesDamageOnTheGroundEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */