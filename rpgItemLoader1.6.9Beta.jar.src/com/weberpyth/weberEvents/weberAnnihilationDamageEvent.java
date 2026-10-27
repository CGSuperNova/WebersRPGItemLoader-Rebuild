/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import com.weberpyth.util.WeberEntityUtil;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ public class weberAnnihilationDamageEvent
/*    */   extends weberAttributesEvent
/*    */ {
/*    */   double opd;
/*    */   
/*    */   public weberAnnihilationDamageEvent(String name, int trigger) {
/* 14 */     this.name = name;
/* 15 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 20 */     this.value = (attrs.get("annihilation") == null) ? new Double(0.0D) : attrs.get("annihilation");
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
/* 33 */       double mul = currentHealth / maxHealth;
/* 34 */       if (mul < this.value.doubleValue()) {
/* 35 */         this.opd = currentHealth;
/*    */       } else {
/* 37 */         this.opd = 0.0D;
/*    */       } 
/*    */     } else {
/* 40 */       this.opd = 0.0D;
/*    */     } 
/*    */     
/* 43 */     return Double.valueOf(modDamage.doubleValue() + this.opd);
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 48 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 53 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 58 */     if (this.opd != 0.0D) {
/* 59 */       return msg + String.format("§c%s处决伤害", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
/*    */     }
/* 61 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 66 */     if (this.opd != 0.0D) {
/* 67 */       return msg + String.format("§c%.2f处决伤害", new Object[] { Double.valueOf(this.opd) });
/*    */     }
/* 69 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberAnnihilationDamageEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */