/*    */ package com.weberpyth.WeberBuffController;
/*    */ 
/*    */ import com.weberpyth.config.WeberBuffCombatConfig;
/*    */ import com.weberpyth.config.WeberBuffConfig;
/*    */ import com.weberpyth.weberBuffData.WeberBuffEntity;
/*    */ import com.weberpyth.weberInterface.AttrModFunction;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.entity.Player;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class BuffCombatController
/*    */   implements AttrModFunction
/*    */ {
/*    */   public HashMap<String, Double> filter(HashMap<String, Double> attrs, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 18 */     WeberBuffConfig.debugMsg("触发Buff判定事件");
/* 19 */     String damagerName = null;
/* 20 */     if (entityDamageByEntityEvent.getDamager() instanceof Player) {
/* 21 */       damagerName = ((Player)entityDamageByEntityEvent.getDamager()).getDisplayName();
/*    */     }
/* 23 */     WeberBuffConfig.debugMsg(String.format("攻击者名称%s", new Object[] { damagerName }));
/*    */     
/* 25 */     if (damagerName != null && WeberBuffCombatConfig.combatInfo.containsKey(damagerName)) {
/* 26 */       WeberBuffConfig.debugMsg(String.format("进入攻击者属性结算", new Object[0]));
/*    */       
/* 28 */       for (String k : ((HashMap)WeberBuffCombatConfig.combatInfo.get(damagerName)).keySet()) {
/*    */         
/* 30 */         WeberBuffEntity wbe = (WeberBuffEntity)((HashMap)WeberBuffCombatConfig.combatInfo.get(damagerName)).get(k);
/* 31 */         if (wbe.weberBuffModel == null) {
/* 32 */           WeberBuffConfig.debugMsg("[警告]CombatController.filter 位置0");
/*    */           continue;
/*    */         } 
/* 35 */         if (wbe.weberBuffModel.attributes == null) {
/* 36 */           WeberBuffConfig.debugMsg("[警告]CombatController.filter 位置1");
/*    */           continue;
/*    */         } 
/* 39 */         if (!wbe.isActive()) {
/*    */           continue;
/*    */         }
/* 42 */         for (String a : wbe.weberBuffModel.attributes.keySet()) {
/* 43 */           if (attrs.containsKey(a)) {
/* 44 */             attrs.put(a, Double.valueOf(((Double)attrs.get(a)).doubleValue() + ((Double)wbe.weberBuffModel.attributes.get(a)).doubleValue())); continue;
/*    */           } 
/* 46 */           attrs.put(a, (Double)wbe.weberBuffModel.attributes.get(a));
/*    */         } 
/*    */       } 
/*    */       
/* 50 */       WeberBuffConfig.debugMsg(String.format("攻击者暴击率%.2f", new Object[] { attrs.get("crit") }));
/*    */     } 
/*    */     
/* 53 */     String defenderName = null;
/* 54 */     if (entityDamageByEntityEvent.getEntity() instanceof Player) {
/* 55 */       defenderName = ((Player)entityDamageByEntityEvent.getEntity()).getDisplayName();
/*    */     }
/* 57 */     if (defenderName != null && WeberBuffCombatConfig.combatInfo.containsKey(defenderName))
/*    */     {
/* 59 */       for (String k : ((HashMap)WeberBuffCombatConfig.combatInfo.get(defenderName)).keySet()) {
/*    */         
/* 61 */         WeberBuffEntity wbe = (WeberBuffEntity)((HashMap)WeberBuffCombatConfig.combatInfo.get(defenderName)).get(k);
/* 62 */         if (wbe.weberBuffModel == null) {
/* 63 */           WeberBuffConfig.debugMsg("[警告]CombatController.filter 位置2");
/*    */           continue;
/*    */         } 
/* 66 */         if (wbe.weberBuffModel.attributes == null) {
/* 67 */           WeberBuffConfig.debugMsg("[警告]CombatController.filter 位置3");
/*    */           continue;
/*    */         } 
/* 70 */         if (!wbe.isActive()) {
/*    */           continue;
/*    */         }
/* 73 */         for (String a : wbe.weberBuffModel.attributes.keySet()) {
/* 74 */           if (attrs.containsKey(a)) {
/* 75 */             attrs.put(a, Double.valueOf(((Double)attrs.get(a)).doubleValue() + ((Double)wbe.weberBuffModel.attributes.get(a)).doubleValue())); continue;
/*    */           } 
/* 77 */           attrs.put(a, (Double)wbe.weberBuffModel.attributes.get(a));
/*    */         } 
/*    */       } 
/*    */     }
/*    */ 
/*    */     
/* 83 */     return attrs;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\webersBuff.jar!\com\weberpyth\WeberBuffController\BuffCombatController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */