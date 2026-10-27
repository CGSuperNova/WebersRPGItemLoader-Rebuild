/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import com.weberpyth.config.weberConfig;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.Location;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ import org.bukkit.util.Vector;
/*    */ 
/*    */ 
/*    */ public class weberBackstabEvent
/*    */   extends weberAttributesEvent
/*    */ {
/*    */   double opd;
/*    */   
/*    */   public weberBackstabEvent(String name, int trigger) {
/* 18 */     this.name = name;
/* 19 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 24 */     this.value = (attrs.get("backstab") == null) ? new Double(0.0D) : attrs.get("backstab");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 29 */     return super.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 34 */     this.opd = 0.0D;
/* 35 */     Entity damager = entityDamageByEntityEvent.getDamager();
/* 36 */     Entity entity = entityDamageByEntityEvent.getEntity();
/*    */     
/* 38 */     Location damagerL = damager.getLocation();
/* 39 */     Location entityL = entity.getLocation();
/*    */     
/* 41 */     Vector DEL = damagerL.subtract(entityL).toVector();
/* 42 */     Vector ES = null;
/*    */     
/* 44 */     if (entity instanceof LivingEntity) {
/* 45 */       ES = ((LivingEntity)entity).getEyeLocation().getDirection();
/*    */     } else {
/* 47 */       return modDamage;
/*    */     } 
/*    */     
/* 50 */     float sight = DEL.angle(ES);
/* 51 */     if (Math.toDegrees(sight) > 120.0D)
/*    */     {
/* 53 */       this.opd = (entityDamageByEntityEvent.getDamage() + modDamage.doubleValue()) * this.value.doubleValue();
/*    */     }
/* 55 */     if (this.opd == 0.0D) {
/* 56 */       weberConfig.debugMsg(String.format("背刺失败:未从背后袭击 位置夹角值%.2f 参照值%.2f", new Object[] { Double.valueOf(Math.toDegrees(sight)), new Double(120.0D) }));
/*    */     }
/* 58 */     return Double.valueOf(modDamage.doubleValue() + this.opd);
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 63 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 68 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 73 */     if (this.opd != 0.0D) {
/* 74 */       return msg + String.format("§e%s背刺伤害", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
/*    */     }
/* 76 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 81 */     if (this.opd != 0.0D) {
/* 82 */       return msg + String.format("§e%.2f背刺伤害", new Object[] { Double.valueOf(this.opd) });
/*    */     }
/* 84 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberBackstabEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */