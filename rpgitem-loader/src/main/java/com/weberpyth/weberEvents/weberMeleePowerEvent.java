package com.weberpyth.weberEvents;
import java.util.HashMap;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class weberMeleePowerEvent
  extends weberAttributesEvent
{
  double opd = 0.0D;
  public weberMeleePowerEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("meleePower") == null) ? 0.0D : attrs.get("meleePower");
  }
  public Double getValue() {
    return super.getValue();
  }
  public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    LivingEntity damager = null;
    if (entityDamageByEntityEvent.getDamager() instanceof org.bukkit.entity.Projectile) {
      this.opd = 0.0D;
    } else {
      this.opd = this.value.doubleValue();
    } 
    return Double.valueOf(modDamage.doubleValue() + this.opd);
  }
  public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return modHealth;
  }
  public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return modInjury;
  }
  public String setNoNumInfo(String msg) {
    if (this.opd != 0.0D) {
      return msg + String.format("§e%s近战伤害", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
    }
    return msg;
  }
  public String setNumInfo(String msg) {
    if (this.opd != 0.0D) {
      return msg + String.format("§e%.2f近战伤害", new Object[] { Double.valueOf(this.opd) });
    }
    return msg;
  }
}
