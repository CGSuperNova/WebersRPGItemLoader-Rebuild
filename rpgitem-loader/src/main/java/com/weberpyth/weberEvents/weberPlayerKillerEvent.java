package com.weberpyth.weberEvents;
import java.util.HashMap;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class weberPlayerKillerEvent
  extends weberAttributesEvent
{
  double opd;
  public weberPlayerKillerEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("playerKiller") == null) ? 0.0D : attrs.get("playerKiller");
  }
  public Double getValue() {
    return super.getValue();
  }
  public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    if (entityDamageByEntityEvent.getEntity() instanceof org.bukkit.entity.Player) {
      this.opd = (entityDamageByEntityEvent.getDamage() + modDamage.doubleValue()) * this.value.doubleValue();
      return Double.valueOf(modDamage.doubleValue() + this.opd);
    } 
    this.opd = 0.0D;
    return modDamage;
  }
  public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return modHealth;
  }
  public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return modInjury;
  }
  public String setNoNumInfo(String msg) {
    if (this.opd != 0.0D) {
      return msg + String.format("§e%s玩家杀手", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
    }
    return msg;
  }
  public String setNumInfo(String msg) {
    if (this.opd != 0.0D) {
      return msg + String.format("§e%.2f玩家杀手", new Object[] { Double.valueOf(this.opd) });
    }
    return msg;
  }
}
