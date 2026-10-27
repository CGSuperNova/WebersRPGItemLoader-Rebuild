package com.weberpyth.weberEvents;
import java.util.HashMap;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class weberInjuryReductionEvent
  extends weberAttributesEvent
{
  double opd;
  public weberInjuryReductionEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("injuryReduction") == null) ? 0.0D : attrs.get("injuryReduction");
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
    if (entityDamageByEntityEvent.getEntity() instanceof org.bukkit.entity.LivingEntity) {
      double orginDamage = entityDamageByEntityEvent.getDamage() + modDamage.doubleValue();
      this.opd = orginDamage * this.value.doubleValue();
    } else {
      this.opd = 0.0D;
    } 
    return Double.valueOf(modInjury.doubleValue() - this.opd);
  }
  public String setNoNumInfo(String msg) {
    return msg;
  }
  public String setNumInfo(String msg) {
    return msg;
  }
}
