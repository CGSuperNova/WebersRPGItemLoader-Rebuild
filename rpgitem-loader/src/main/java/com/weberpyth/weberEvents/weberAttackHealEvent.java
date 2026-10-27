package com.weberpyth.weberEvents;
import java.util.HashMap;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class weberAttackHealEvent
  extends weberAttributesEvent
{
  public weberAttackHealEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("attackHeal") == null) ? 0.0D : attrs.get("attackHeal");
  }
  public Double getValue() {
    return super.getValue();
  }
  public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return modDamage;
  }
  public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return Double.valueOf(modHealth.doubleValue() + this.value.doubleValue());
  }
  public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return modInjury;
  }
  public String setNoNumInfo(String msg) {
    if (this.value.doubleValue() != 0.0D) {
      return msg + String.format("§3%s附加治疗", new Object[] { (this.value.doubleValue() > 0.0D) ? "+" : "-" });
    }
    return msg;
  }
  public String setNumInfo(String msg) {
    if (this.value.doubleValue() != 0.0D) {
      return msg + String.format("§3%.2f附加治疗", new Object[] { this.value });
    }
    return msg;
  }
}
