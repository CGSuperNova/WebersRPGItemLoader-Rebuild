/*    */ package com.weberpyth.controller;
/*    */ 
/*    */ import com.weberpyth.weberEvents.weberAttributesEvent;
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.entity.Entity;
/*    */ import org.bukkit.event.entity.EntityDamageEvent;
/*    */ 
/*    */ 
/*    */ public class DotController
/*    */ {
/*    */   public EntityDamageEvent entityDamageEvent;
/*    */   public static HashMap<String, weberAttributesEvent> attrEvents;
/*    */   public Entity damager;
/*    */   public Entity entity;
/*    */   
/*    */   public DotController() {
/* 17 */     attrEvents = new HashMap<>();
/*    */   }
/*    */ 
/*    */   
/*    */   public void setEntityDamageEvent(EntityDamageEvent e) {
/* 22 */     this.entityDamageEvent = e;
/*    */   }
/*    */   
/*    */   public void response() {
/* 26 */     this.entity = this.entityDamageEvent.getEntity();
/*    */     
/* 28 */     if (this.entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.WITHER && this.entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.POISON && this.entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK) {
/*    */       return;
/*    */     }
/*    */ 
/*    */     
/* 33 */     initAttrsEventsLink();
/* 34 */     countMods();
/*    */   }
/*    */ 
/*    */   
/*    */   public void initAttrsEventsLink() {}
/*    */ 
/*    */   
/*    */   public void countMods() {}
/*    */ 
/*    */   
/*    */   public static void attrEventAdd(String key, weberAttributesEvent wae) {
/* 45 */     attrEvents.put(key, wae);
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\controller\DotController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */