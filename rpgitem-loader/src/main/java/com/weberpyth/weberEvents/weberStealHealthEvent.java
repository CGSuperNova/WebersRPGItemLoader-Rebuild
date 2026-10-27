package com.weberpyth.weberEvents;
import java.util.HashMap;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class weberStealHealthEvent
  extends weberAttributesEvent {
  double oph;
  public weberStealHealthEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("stealHealth") == null) ? 0.0D : attrs.get("stealHealth");
  }
  public Double getValue() {
    return super.getValue();
  }
  public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    this.oph = entityDamageByEntityEvent.getDamage() * this.value.doubleValue();
    return modDamage;
  }
  public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return Double.valueOf(modHealth.doubleValue() + this.oph);
  }
  public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return modInjury;
  }
  public String setNoNumInfo(String msg) {
    if (this.oph != 0.0D) {
      return msg + String.format("§c%s生命偷取", new Object[] { (this.oph > 0.0D) ? "+" : "-" });
    }
    return msg;
  }
  public String setNumInfo(String msg) {
    if (this.oph != 0.0D) {
      return msg + String.format("§c%.2f生命偷取", new Object[] { Double.valueOf(this.oph) });
    }
    return msg;
  }
}
