package com.weberpyth.weberEvents;
import java.util.HashMap;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class weberAppendDamageEvent
  extends weberAttributesEvent
{
  public weberAppendDamageEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("appendDamage") == null) ? 0.0D : attrs.get("appendDamage");
  }
  public Double getValue() {
    return super.getValue();
  }
  public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return Double.valueOf(modDamage.doubleValue() + this.value.doubleValue());
  }
  public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return modHealth;
  }
  public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return modInjury;
  }
  public String setNoNumInfo(String msg) {
    if (this.value.doubleValue() != 0.0D) {
      return msg + String.format("§b%s附加伤害", new Object[] { (this.value.doubleValue() > 0.0D) ? "+" : "-" });
    }
    return msg;
  }
  public String setNumInfo(String msg) {
    if (this.value.doubleValue() != 0.0D) {
      return msg + String.format("§b%.2f附加伤害", new Object[] { this.value });
    }
    return msg;
  }
}
