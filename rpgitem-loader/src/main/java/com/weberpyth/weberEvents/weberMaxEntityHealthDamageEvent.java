package com.weberpyth.weberEvents;
import com.weberpyth.util.WeberEntityUtil;
import java.util.HashMap;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class weberMaxEntityHealthDamageEvent
  extends weberAttributesEvent
{
  double opd;
  public weberMaxEntityHealthDamageEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("maxEntityHealthDamage") == null) ? 0.0D : attrs.get("maxEntityHealthDamage");
  }
  public Double getValue() {
    return super.getValue();
  }
  public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    if (entityDamageByEntityEvent.getEntity() instanceof LivingEntity) {
      Double maxHealthValue = WeberEntityUtil.getMaxHealth((LivingEntity)entityDamageByEntityEvent.getEntity());
      this.opd = ((maxHealthValue == null) ? 0.0D : maxHealthValue.doubleValue()) * this.value.doubleValue();
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
