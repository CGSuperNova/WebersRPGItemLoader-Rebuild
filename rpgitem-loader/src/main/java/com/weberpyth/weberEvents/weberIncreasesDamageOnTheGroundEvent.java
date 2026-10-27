package com.weberpyth.weberEvents;
import com.weberpyth.util.WeberEntityUtil;
import java.util.HashMap;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class weberIncreasesDamageOnTheGroundEvent
  extends weberAttributesEvent
{
  double opd;
  public weberIncreasesDamageOnTheGroundEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("increasesDamageOnTheGround") == null) ? 0.0D : attrs.get("increasesDamageOnTheGround");
  }
  public Double getValue() {
    return super.getValue();
  }
  public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    this.opd = 0.0D;
    LivingEntity damager = null;
    if (entityDamageByEntityEvent.getDamager() instanceof org.bukkit.entity.Projectile) {
      damager = WeberEntityUtil.findShooter(entityDamageByEntityEvent.getDamager());
    } else if (entityDamageByEntityEvent.getDamager() instanceof LivingEntity) {
      damager = (LivingEntity)entityDamageByEntityEvent.getDamager();
    } else {
      damager = null;
    } 
    if (damager != null && damager.isOnGround()) {
      this.opd = (entityDamageByEntityEvent.getDamage() + modDamage.doubleValue()) * this.value.doubleValue();
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
      return msg + String.format("§e%s地面攻击增益", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
    }
    return msg;
  }
  public String setNumInfo(String msg) {
    if (this.opd != 0.0D) {
      return msg + String.format("§e%.2f地面攻击增益", new Object[] { Double.valueOf(this.opd) });
    }
    return msg;
  }
}
