package com.weberpyth.weberEvents;
import com.weberpyth.util.WeberEntityUtil;
import java.util.HashMap;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class weberExecuteDamageEvent
  extends weberAttributesEvent
{
  double opd;
  public weberExecuteDamageEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("executeDamage") == null) ? 0.0D : attrs.get("executeDamage");
  }
  public Double getValue() {
    return super.getValue();
  }
  public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    if (entityDamageByEntityEvent.getEntity() instanceof LivingEntity) {
      Double maxHealthValue = WeberEntityUtil.getMaxHealth((LivingEntity)entityDamageByEntityEvent.getEntity());
      double maxHealth = (maxHealthValue == null) ? 0.0D : maxHealthValue.doubleValue();
      double currentHealth = ((LivingEntity)entityDamageByEntityEvent.getEntity()).getHealth();
      double mul = (maxHealth > 0.0D) ? (1.0D - currentHealth / maxHealth) : 0.0D;
      double orginDamage = entityDamageByEntityEvent.getDamage() + modDamage.doubleValue();
      this.opd = (mul > 0.0D) ? (orginDamage * mul * 100.0D * this.value.doubleValue()) : 0.0D;
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
      return msg + String.format("§c%s斩杀伤害", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
    }
    return msg;
  }
  public String setNumInfo(String msg) {
    if (this.opd != 0.0D) {
      return msg + String.format("§c%.2f斩杀伤害", new Object[] { Double.valueOf(this.opd) });
    }
    return msg;
  }
}
