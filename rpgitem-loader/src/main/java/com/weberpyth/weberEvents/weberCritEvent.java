package com.weberpyth.weberEvents;
import com.weberpyth.config.weberConfig;
import java.util.HashMap;
import java.util.Random;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
public class weberCritEvent
  extends weberAttributesEvent
{
  double opd = 0.0D;
  double mul = 0.0D;
  int rank = 0;
  public weberCritEvent(String name, int trigger) {
    this.name = name;
    this.trigger = trigger;
  }
  public void setValue(HashMap<String, Double> attrs) {
    this.value = (attrs.get("crit") == null) ? 0.0D : attrs.get("crit");
    this.mul = (attrs.get("critMul") == null) ? 0.0D : attrs.get("critMul").doubleValue();
  }
  public Double getValue() {
    return super.getValue();
  }
  public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
    this.rank = 0;
    double num = (new Random()).nextDouble();
    double finalMul = 0.0D;
    double critValue = this.value.doubleValue();
    if (Double.isNaN(critValue) || Double.isInfinite(critValue)) {
      critValue = 0.0D;
    }
    int iterations = 0;
    while (critValue >= 1.0D && iterations < 100) {
      finalMul += this.mul;
      critValue -= 1.0D;
      this.rank++;
      iterations++;
    } 
    this.opd = 0.0D;
    if (num > critValue) {
      weberConfig.debugMsg("暴击失败");
    } else {
      finalMul += this.mul;
      this.rank++;
    } 
    this.opd = (entityDamageByEntityEvent.getDamage() + modDamage.doubleValue()) * finalMul;
    if (this.opd == 0.0D) {
      weberConfig.debugMsg(String.format("[警告]无额外暴击伤害,crit值为%.2f  critMul值为%.2f", new Object[] { Double.valueOf(critValue), Double.valueOf(this.mul) }));
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
      if (this.rank == 1) {
        return msg + String.format("§6%s暴击", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
      }
      if (this.rank == 2) {
        return msg + String.format("§e§l%s暴击!", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
      }
      if (this.rank >= 3) {
        return msg + String.format("§c§l%s暴击!!", new Object[] { (this.opd > 0.0D) ? "+" : "-" });
      }
    } 
    return msg;
  }
  public String setNumInfo(String msg) {
    if (this.opd != 0.0D) {
      if (this.rank == 1) {
        return msg + String.format("§6%.2f暴击", new Object[] { Double.valueOf(this.opd) });
      }
      if (this.rank == 2) {
        return msg + String.format("§e§l%.2f暴击!", new Object[] { Double.valueOf(this.opd) });
      }
      if (this.rank >= 3) {
        return msg + String.format("§c§l%.2f暴击!!", new Object[] { Double.valueOf(this.opd) });
      }
    } 
    return msg;
  }
}
