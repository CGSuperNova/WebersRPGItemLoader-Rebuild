package com.weberpyth.weberEvents;
import java.util.HashMap;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class weberArmorEvent
  extends weberAttributesEvent
{
  public Double increase = Double.valueOf(0.0D);
  public Double decrease = Double.valueOf(0.0D);
  public Double armorSub = Double.valueOf(0.0D);
  public weberArmorEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("armor") == null) ? 0.0D : attrs.get("armor");
    this.increase = (attrs.get("armorIncrease") == null) ? 0.0D : attrs.get("armorIncrease");
    this.decrease = (attrs.get("armorDecrease") == null) ? 0.0D : attrs.get("armorDecrease");
    this.armorSub = (attrs.get("armorSub") == null) ? 0.0D : attrs.get("armorSub");
  }
  public Double getValue() {
    return super.getValue();
  }
  public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    Double finalArmor = this.value;
    Double mul = Double.valueOf(0.0D);
    if (this.value.doubleValue() > 0.0D) {
      finalArmor = Double.valueOf(this.value.doubleValue() + this.value.doubleValue() * this.increase.doubleValue() - this.value.doubleValue() * this.decrease.doubleValue());
    }
    if (this.value.doubleValue() < 0.0D) {
      finalArmor = Double.valueOf(this.value.doubleValue() - this.value.doubleValue() * this.increase.doubleValue() + this.value.doubleValue() * this.decrease.doubleValue());
    }
    finalArmor = Double.valueOf(finalArmor.doubleValue() - this.armorSub.doubleValue());
    if (finalArmor.doubleValue() < 0.0D) {
      mul = Double.valueOf(1.0D - finalArmor.doubleValue() / 100.0D);
      return Double.valueOf(modDamage.doubleValue() + modDamage.doubleValue() * mul.doubleValue());
    } 
    return modDamage;
  }
  public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    return modHealth;
  }
  public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    Double finalArmor = this.value;
    Double mul = Double.valueOf(0.0D);
    if (this.value.doubleValue() > 0.0D) {
      finalArmor = Double.valueOf(this.value.doubleValue() + this.value.doubleValue() * this.increase.doubleValue() - this.value.doubleValue() * this.decrease.doubleValue());
    }
    if (this.value.doubleValue() < 0.0D) {
      finalArmor = Double.valueOf(this.value.doubleValue() - this.value.doubleValue() * this.increase.doubleValue() + this.value.doubleValue() * this.decrease.doubleValue());
    }
    finalArmor = Double.valueOf(finalArmor.doubleValue() - this.armorSub.doubleValue());
    if (finalArmor.doubleValue() > 0.0D) {
      mul = Double.valueOf(100.0D / (100.0D + finalArmor.doubleValue()));
      return Double.valueOf(modInjury.doubleValue() - modDamage.doubleValue() * mul.doubleValue());
    } 
    return modInjury;
  }
  public String setNoNumInfo(String msg) {
    return msg;
  }
  public String setNumInfo(String msg) {
    return msg;
  }
}
