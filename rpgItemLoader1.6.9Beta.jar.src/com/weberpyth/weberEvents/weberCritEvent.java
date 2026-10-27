/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import com.weberpyth.config.weberConfig;
/*    */ import java.util.HashMap;
/*    */ import java.util.Random;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class weberCritEvent
/*    */   extends weberAttributesEvent
/*    */ {
/* 13 */   double opd = 0.0D;
/* 14 */   double mul = 0.0D;
/* 15 */   int rank = 0;
/*    */   public weberCritEvent(String name, int trigger) {
/* 17 */     this.name = name;
/* 18 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 23 */     this.value = (attrs.get("crit") == null) ? new Double(0.0D) : attrs.get("crit");
/* 24 */     this.mul = (attrs.get("critMul") == null) ? (new Double(0.0D)).doubleValue() : ((Double)attrs.get("critMul")).doubleValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 29 */     return super.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 34 */     this.rank = 0;
/* 35 */     double num = (new Random()).nextDouble();
/* 36 */     double finalMul = 0.0D;
/* 37 */     while (this.value.doubleValue() >= 1.0D) {
/* 38 */       finalMul += this.mul;
/* 39 */       this.value = Double.valueOf(this.value.doubleValue() - 1.0D);
/* 40 */       this.rank++;
/*    */     } 
/* 42 */     this.opd = 0.0D;
/* 43 */     if (num > this.value.doubleValue()) {
/* 44 */       weberConfig.debugMsg("暴击失败");
/*    */     } else {
/* 46 */       finalMul += this.mul;
/* 47 */       this.rank++;
/*    */     } 
/* 49 */     this.opd = (entityDamageByEntityEvent.getDamage() + modDamage.doubleValue()) * finalMul;
/* 50 */     if (this.opd == 0.0D) {
/* 51 */       weberConfig.debugMsg(String.format("[警告]无额外暴击伤害,crit值为%.2f  critMul值为%.2f", new Object[] { this.value, Double.valueOf(this.mul) }));
/*    */     }
/* 53 */     return Double.valueOf(modDamage.doubleValue() + this.opd);
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 58 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 63 */     return modInjury;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 68 */     if (this.opd != 0.0D) {
/* 69 */       if (this.rank == 1) {
/* 70 */         return msg + String.format("§6%s暴击", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
/*    */       }
/* 72 */       if (this.rank == 2) {
/* 73 */         return msg + String.format("§e§l%s暴击!", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
/*    */       }
/* 75 */       if (this.rank >= 3) {
/* 76 */         return msg + String.format("§c§l%s暴击!!", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
/*    */       }
/*    */     } 
/* 79 */     return msg;
/*    */   }
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 84 */     if (this.opd != 0.0D) {
/* 85 */       if (this.rank == 1) {
/* 86 */         return msg + String.format("§6%.2f暴击", new Object[] { Double.valueOf(this.opd) });
/*    */       }
/* 88 */       if (this.rank == 2) {
/* 89 */         return msg + String.format("§e§l%.2f暴击!", new Object[] { Double.valueOf(this.opd) });
/*    */       }
/* 91 */       if (this.rank >= 3) {
/* 92 */         return msg + String.format("§c§l%.2f暴击!!", new Object[] { Double.valueOf(this.opd) });
/*    */       }
/*    */     } 
/*    */     
/* 96 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberCritEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */