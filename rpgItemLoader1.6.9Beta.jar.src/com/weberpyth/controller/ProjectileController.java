/*    */ package com.weberpyth.controller;
/*    */ 
/*    */ import com.weberpyth.weberEvents.ProjectileEvents.weberProjectileEvent;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import javax.swing.text.html.parser.Entity;
/*    */ import org.bukkit.entity.LivingEntity;
/*    */ import org.bukkit.entity.Projectile;
/*    */ import org.bukkit.event.entity.EntityShootBowEvent;
/*    */ 
/*    */ public class ProjectileController
/*    */ {
/*    */   public EntityShootBowEvent entityShootBowEvent;
/*    */   public Entity shooter;
/*    */   public HashMap<String, Double> attrs;
/*    */   public List<weberProjectileEvent> events;
/*    */   
/*    */   public static void debugProjectileInfo(Projectile projectile, LivingEntity sender) {
/* 19 */     sender.sendMessage(String.format("投掷物向量模%.2f", new Object[] { Double.valueOf(projectile.getVelocity().length()) }));
/*    */   }
/*    */   
/*    */   public void addEvent(weberProjectileEvent w) {}
/*    */   
/*    */   public void initEventsLink() {}
/*    */   
/*    */   public void countMods() {}
/*    */   
/*    */   public void doEvents() {
/* 29 */     for (weberProjectileEvent w : this.events)
/* 30 */       w.setProjectile((Projectile)this.entityShootBowEvent.getProjectile()); 
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\controller\ProjectileController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */