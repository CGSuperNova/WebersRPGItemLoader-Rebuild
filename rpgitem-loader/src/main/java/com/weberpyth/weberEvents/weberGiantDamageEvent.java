package com.weberpyth.weberEvents;
import com.weberpyth.util.WeberEntityUtil;
import java.util.HashMap;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class weberGiantDamageEvent
  extends weberAttributesEvent
{
  double opd;
  public weberGiantDamageEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("giantDamage") == null) ? 0.0D : attrs.get("giantDamage");
  }
  public Double getValue() {
    return super.getValue();
  }
  public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    LivingEntity damager = null;
    if (entityDamageByEntityEvent.getDamager() instanceof org.bukkit.entity.Projectile) {
      damager = WeberEntityUtil.findShooter(entityDamageByEntityEvent.getDamager());
    } else if (entityDamageByEntityEvent.getDamager() instanceof LivingEntity) {
      damager = (LivingEntity)entityDamageByEntityEvent.getDamager();
    } else {
      damager = null;
    } 
    if (damager != null) {
      Double maxHealthValue = WeberEntityUtil.getMaxHealth(damager);
      double maxHealth = (maxHealthValue == null) ? 0.0D : maxHealthValue.doubleValue();
      double currentHealth = damager.getHealth();
      double mul = (maxHealth > 0.0D) ? (currentHealth / maxHealth) : 0.0D;
      double originDamage = entityDamageByEntityEvent.getDamage() + modDamage.doubleValue();
      this.opd = (mul > 0.0D) ? (originDamage * mul * 100.0D * this.value.doubleValue()) : 0.0D;
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
      return msg + String.format("§7%s目标最大生命值伤害", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
    }
    return msg;
  }
  public String setNumInfo(String msg) {
    if (this.opd != 0.0D) {
      return msg + String.format("§7%.2f目标最大生命值伤害", new Object[] { Double.valueOf(this.opd) });
    }
    return msg;
  }
}
