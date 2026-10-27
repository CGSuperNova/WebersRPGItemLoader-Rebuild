package com.weberpyth.controller;
import com.weberpyth.weberEvents.ProjectileEvents.weberProjectileEvent;
import java.util.HashMap;
import java.util.List;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityShootBowEvent;
public class ProjectileController
{
  public EntityShootBowEvent entityShootBowEvent;
  public Entity shooter;
  public HashMap<String, Double> attrs;
  public List<weberProjectileEvent> events;
  public static void debugProjectileInfo(Projectile projectile, LivingEntity sender) {
    sender.sendMessage(String.format("投掷物向量模%.2f", new Object[] { Double.valueOf(projectile.getVelocity().length()) }));
  }
  public void addEvent(weberProjectileEvent w) {}
  public void initEventsLink() {}
  public void countMods() {}
  public void doEvents() {
    for (weberProjectileEvent w : this.events)
      w.setProjectile((Projectile)this.entityShootBowEvent.getProjectile()); 
  }
}
