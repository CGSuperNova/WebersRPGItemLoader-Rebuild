package com.weberpyth.weberEvents;
import com.weberpyth.util.WeberEntityUtil;
import java.util.HashMap;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class weberTenacityEvent
  extends weberAttributesEvent
{
  double opd;
  public weberTenacityEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("tenacity") == null) ? 0.0D : attrs.get("tenacity");
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
    if (entityDamageByEntityEvent.getEntity() instanceof LivingEntity) {
      Double maxHealthValue = WeberEntityUtil.getMaxHealth((LivingEntity)entityDamageByEntityEvent.getEntity());
      double maxHealth = (maxHealthValue == null) ? 0.0D : maxHealthValue.doubleValue();
      double currentHealth = ((LivingEntity)entityDamageByEntityEvent.getEntity()).getHealth();
      double mul = (maxHealth > 0.0D) ? (1.0D - currentHealth / maxHealth) : 0.0D;
      double originDamage = entityDamageByEntityEvent.getDamage() + modDamage.doubleValue();
      this.opd = (mul > 0.0D) ? (originDamage * mul * 100.0D * this.value.doubleValue()) : 0.0D;
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
