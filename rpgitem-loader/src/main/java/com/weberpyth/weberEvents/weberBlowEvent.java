package com.weberpyth.weberEvents;
import com.weberpyth.config.weberConfig;
import com.weberpyth.rpgitemloader.rpgitemloader;
import java.util.HashMap;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;
public class weberBlowEvent
  extends weberAttributesEvent {
  double opd;
  public weberBlowEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("blow") == null) ? 0.0D : attrs.get("blow");
  }
  public Double getValue() {
    return super.getValue();
  }
  public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    LivingEntity e1 = null;
    LivingEntity e2 = null;
    if (entityDamageByEntityEvent.getDamager() instanceof LivingEntity && entityDamageByEntityEvent.getEntity() instanceof LivingEntity) {
      e1 = (LivingEntity)entityDamageByEntityEvent.getDamager();
      e2 = (LivingEntity)entityDamageByEntityEvent.getEntity();
    } else {
      return modDamage;
    } 
    if (this.value.doubleValue() == 0.0D) {
      return modDamage;
    }
    weberConfig.debugMsg("启动击飞测试");
    final LivingEntity taker = e2;
    final Double h = this.value;
    final org.bukkit.World world = taker.getWorld();
    BukkitTask task = (new BukkitRunnable() { public void run() { if (!taker.isValid() || taker.isDead() || !world.equals(taker.getWorld())) { return; } taker.setVelocity(taker.getVelocity().add(new Vector(0.0D, h.doubleValue(), 0.0D))); } }).runTaskLater((Plugin)rpgitemloader.plugin, 1L);
    return modDamage;
  }
  public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return modHealth;
  }
  public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return modInjury;
  }
  public String setNoNumInfo(String msg) {
    if (this.value.doubleValue() != 0.0D) {
      return msg + String.format("§e%s额外击飞高度", new Object[] { (this.value.doubleValue() > 0.0D) ? "+" : "-" });
    }
    return msg;
  }
  public String setNumInfo(String msg) {
    if (this.value.doubleValue() != 0.0D) {
      return msg + String.format("§e%.2f额外击飞高度", new Object[] { this.value });
    }
    return msg;
  }
}
