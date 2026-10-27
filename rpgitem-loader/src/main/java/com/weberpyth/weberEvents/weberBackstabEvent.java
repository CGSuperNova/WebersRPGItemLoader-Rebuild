package com.weberpyth.weberEvents;
import com.weberpyth.config.weberConfig;
import java.util.HashMap;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
public class weberBackstabEvent
  extends weberAttributesEvent
{
  double opd;
  public weberBackstabEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("backstab") == null) ? 0.0D : attrs.get("backstab");
  }
  public Double getValue() {
    return super.getValue();
  }
  public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    this.opd = 0.0D;
    Entity damager = entityDamageByEntityEvent.getDamager();
    Entity entity = entityDamageByEntityEvent.getEntity();
    Location damagerL = damager.getLocation();
    Location entityL = entity.getLocation();
    Vector DEL = damagerL.subtract(entityL).toVector();
    Vector ES = null;
    if (entity instanceof LivingEntity) {
      ES = ((LivingEntity)entity).getEyeLocation().getDirection();
    } else {
      return modDamage;
    } 
    float sight = DEL.angle(ES);
    if (Math.toDegrees(sight) > 120.0D)
    {
      this.opd = (entityDamageByEntityEvent.getDamage() + modDamage.doubleValue()) * this.value.doubleValue();
    }
    if (this.opd == 0.0D && this.value.doubleValue() != 0.0D) {
      weberConfig.debugMsg(String.format("背刺失败:未从背后袭击 位置夹角值%.2f 参照值%.2f", new Object[] { Double.valueOf(Math.toDegrees(sight)), 120.0D }));
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
      return msg + String.format("§e%s背刺伤害", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
    }
    return msg;
  }
  public String setNumInfo(String msg) {
    if (this.opd != 0.0D) {
      return msg + String.format("§e%.2f背刺伤害", new Object[] { Double.valueOf(this.opd) });
    }
    return msg;
  }
}
