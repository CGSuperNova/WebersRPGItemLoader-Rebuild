package com.weberpyth.weberEvents;
import com.weberpyth.util.WeberEntityUtil;
import java.util.HashMap;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class weberRegurgitationCurrentHealthDamageEvent
  extends weberAttributesEvent
{
  double opd;
  public weberRegurgitationCurrentHealthDamageEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("regurgitationCurrentHealthDamage") == null) ? 0.0D : attrs.get("regurgitationCurrentHealthDamage");
  }
  public Double getValue() {
    return super.getValue();
  }
  public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return modDamage;
  }
  public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    LivingEntity damager = null;
    if (entityDamageByEntityEvent.getDamager() instanceof org.bukkit.entity.Projectile) {
      damager = WeberEntityUtil.findShooter(entityDamageByEntityEvent.getDamager());
    } else if (entityDamageByEntityEvent.getDamager() instanceof LivingEntity) {
      damager = (LivingEntity)entityDamageByEntityEvent.getDamager();
    } else {
      damager = null;
    } 
    if (damager != null) {
      double currentHealth = damager.getHealth();
      this.opd = currentHealth * this.value.doubleValue();
    } else {
      this.opd = 0.0D;
    } 
    return Double.valueOf(modHealth.doubleValue() - this.opd);
  }
  public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return modInjury;
  }
  public String setNoNumInfo(String msg) {
    if (this.opd != 0.0D) {
      return msg + String.format("§4%s伤害反噬", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
    }
    return msg;
  }
  public String setNumInfo(String msg) {
    if (this.opd != 0.0D) {
      return msg + String.format("§4%.2f伤害反噬", new Object[] { Double.valueOf(this.opd) });
    }
    return msg;
  }
}
