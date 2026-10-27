package com.weberpyth.weberEvents;
import java.util.HashMap;
import org.bukkit.event.entity.EntityDamageEvent;
public class weberDotEvent
{
  String name = null;
  Double value = 0.0D;
  int trigger = 0;
  public Double countAttack(Double modDamage, EntityDamageEvent entityDamageEvent) { return null; }
  public Double countHealth(Double modHealth, EntityDamageEvent entityDamageEvent) { return null; } public Double countDefence(Double modInjury, Double modDamage, EntityDamageEvent entityDamageEvent) {
    return null;
  } public int getTrigger() {
    return this.trigger;
  } public void setValue(HashMap<String, Double> attrs) {} public String getName() {
    return this.name;
  }
  public Double getValue() {
    return this.value;
  }
  public String setNumInfo(String msg) { return msg; } public String setNoNumInfo(String msg) {
    return msg;
  }
}
