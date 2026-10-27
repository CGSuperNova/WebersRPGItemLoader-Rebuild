package com.weberpyth.weberEvents;
import java.util.HashMap;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
public class weberRepelEvent
  extends weberAttributesEvent
{
  double opd;
  public weberRepelEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("repel") == null) ? 0.0D : attrs.get("repel");
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
    Vector v = e2.getLocation().subtract(e1.getLocation()).toVector();
    v = v.normalize();
    e2.setVelocity(e2.getVelocity().add(v.multiply(this.value.doubleValue())));
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
      return msg + String.format("§5%s额外击退强度", new Object[] { (this.value.doubleValue() > 0.0D) ? "+" : "-" });
    }
    return msg;
  }
  public String setNumInfo(String msg) {
    if (this.value.doubleValue() != 0.0D) {
      return msg + String.format("§5%.2f额外击退强度", new Object[] { this.value });
    }
    return msg;
  }
}
