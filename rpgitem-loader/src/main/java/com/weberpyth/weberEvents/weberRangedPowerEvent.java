package com.weberpyth.weberEvents;
import java.util.HashMap;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class weberRangedPowerEvent
  extends weberAttributesEvent
{
  double opd = 0.0D;
  public weberRangedPowerEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("rangedPower") == null) ? 0.0D : attrs.get("rangedPower");
  }
  public Double getValue() {
    return super.getValue();
  }
  public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    if (entityDamageByEntityEvent.getDamager() instanceof org.bukkit.entity.Projectile) {
      this.opd = this.value.doubleValue();
    } else {
      this.opd = 0.0D;
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
      return msg + String.format("§e%s远程伤害", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
    }
    return msg;
  }
  public String setNumInfo(String msg) {
    if (this.opd != 0.0D) {
      return msg + String.format("§e%.2f远程伤害", new Object[] { Double.valueOf(this.opd) });
    }
    return msg;
  }
}
