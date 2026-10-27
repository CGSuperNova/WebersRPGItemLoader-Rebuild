/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import com.weberpyth.util.WeberEntityUtil;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ public class weberMaxEntityHealthDamageEvent
/*    */   extends weberAttributesEvent
/*    */ {
/*    */   double opd;
/*    */   
/*    */   public weberMaxEntityHealthDamageEvent(String name, int trigger) {
/* 14 */     this.name = name;
/* 15 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 20 */     this.value = (attrs.get("maxEntityHealthDamage") == null) ? new Double(0.0D) : attrs.get("maxEntityHealthDamage");
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
/* 31 */       this.opd = WeberEntityUtil.getMaxHealth((LivingEntity)entityDamageByEntityEvent.getEntity()).doubleValue() * this.value.doubleValue();
/*    */     } else {
/* 33 */       this.opd = 0.0D;
/*    */     } 
/*    */     
/* 36 */     return Double.valueOf(modDamage.doubleValue() + this.opd);
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 41 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 46 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 51 */     if (this.opd != 0.0D) {
/* 52 */       return msg + String.format("§7%s目标最大生命值伤害", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
/*    */     }
/* 54 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 59 */     if (this.opd != 0.0D) {
/* 60 */       return msg + String.format("§7%.2f目标最大生命值伤害", new Object[] { Double.valueOf(this.opd) });
/*    */     }
/* 62 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberMaxEntityHealthDamageEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */