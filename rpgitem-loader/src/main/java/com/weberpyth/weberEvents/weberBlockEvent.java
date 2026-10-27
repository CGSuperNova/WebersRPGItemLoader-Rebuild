package com.weberpyth.weberEvents;
import java.util.HashMap;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class weberBlockEvent
  extends weberAttributesEvent
{
  public weberBlockEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("block") == null) ? 0.0D : attrs.get("block");
  }
  public Double getValue() {
    return super.getValue();
  }
  public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return modDamage;
  }
  public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return modHealth;
  }
  public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return Double.valueOf(modInjury.doubleValue() - this.value.doubleValue());
  }
  public String setNoNumInfo(String msg) {
    return msg;
  }
  public String setNumInfo(String msg) {
    return msg;
  }
}
