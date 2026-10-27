/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import com.weberpyth.config.weberConfig;
/*    */ import com.weberpyth.rpgitemloader.rpgitemloader;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ import org.bukkit.plugin.Plugin;
/*    */ import org.bukkit.scheduler.BukkitRunnable;
/*    */ import org.bukkit.scheduler.BukkitTask;
/*    */ import org.bukkit.util.Vector;
/*    */ 
/*    */ public class weberBlowEvent
/*    */   extends weberAttributesEvent {
/*    */   double opd;
/*    */   
/*    */   public weberBlowEvent(String name, int trigger) {
/* 18 */     this.name = name;
/* 19 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 24 */     this.value = (attrs.get("blow") == null) ? new Double(0.0D) : attrs.get("blow");
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 29 */     return super.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 34 */     LivingEntity e1 = null;
/* 35 */     LivingEntity e2 = null;
/*    */     
/* 37 */     if (entityDamageByEntityEvent.getDamager() instanceof LivingEntity && entityDamageByEntityEvent.getEntity() instanceof LivingEntity) {
/* 38 */       e1 = (LivingEntity)entityDamageByEntityEvent.getDamager();
/* 39 */       e2 = (LivingEntity)entityDamageByEntityEvent.getEntity();
/*    */     } else {
/* 41 */       return modDamage;
/*    */     } 
/* 43 */     if (this.value.doubleValue() == 0.0D) {
/* 44 */       return modDamage;
/*    */     }
/*    */ 
/*    */     
/* 48 */     weberConfig.debugMsg("启动击飞测试");
/*    */     
/* 50 */     final LivingEntity taker = e2;
/* 51 */     final Double h = this.value;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 57 */     BukkitTask task = (new BukkitRunnable() { public void run() { taker.setVelocity(taker.getVelocity().add(new Vector(0.0D, h.doubleValue(), 0.0D))); } }).runTaskLater((Plugin)rpgitemloader.plugin, 1L);
/* 58 */     return modDamage;
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
/* 73 */     if (this.value.doubleValue() != 0.0D) {
/* 74 */       return msg + String.format("§e%s额外击飞高度", new Object[] { (this.value.doubleValue() > 0.0D) ? "+" : "-" });
/*    */     }
/* 76 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 81 */     if (this.value.doubleValue() != 0.0D) {
/* 82 */       return msg + String.format("§e%.2f额外击飞高度", new Object[] { this.value });
/*    */     }
/* 84 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberBlowEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */