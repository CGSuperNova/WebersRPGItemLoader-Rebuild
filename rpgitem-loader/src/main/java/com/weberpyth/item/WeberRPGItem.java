package com.weberpyth.item;
import java.util.HashMap;
public class WeberRPGItem
{
  public String id;
  public HashMap<String, Double> attributes;
  public float crit;
  public float critMul;
  public double damage;
  public double stealHealth;
  public int maxHealth;
  public double block;
  public int armor;
  public double damageReduction;
  public double attackHeal;
  public double appendDamage;
  public double currentEntityHealthDamage;
  public double maxEntityHealthDamage;
  public double outputDamage;
  public double entityLostHealthDamage;
  public String innerName;
  public double getOutputDamage() {
    return this.outputDamage;
  }
  public void setOutputDamage(double outputDamage) {
    this.outputDamage = outputDamage;
  }
  public double getMaxEntityHealthDamage() {
    return this.maxEntityHealthDamage;
  }
  public void setMaxEntityHealthDamage(double maxEntityHealthDamage) {
    this.maxEntityHealthDamage = maxEntityHealthDamage;
  }
  public double getCurrentEntityHealthDamage() {
    return this.currentEntityHealthDamage;
  }
  public void setCurrentEntityHealthDamage(double currentEntityHealthDamage) {
    this.currentEntityHealthDamage = currentEntityHealthDamage;
  }
  public double getAppendDamage() {
    return this.appendDamage;
  }
  public void setAppendDamage(double appendDamage) {
    this.appendDamage = appendDamage;
  }
  public double getAttackHeal() {
    return this.attackHeal;
  }
  public void setAttackHeal(double attackHeal) {
    this.attackHeal = attackHeal;
  }
  public float getCrit() {
    return this.crit;
  }
  public void setCrit(float crit) {
    this.crit = crit;
  }
  public float getCritMul() {
    return this.critMul;
  }
  public void setCritMul(float critMul) {
    this.critMul = critMul;
  }
  public String getId() {
    return this.id;
  }
  public void setId(String id) {
    this.id = id;
  }
  public int getMaxHealth() {
    return this.maxHealth;
  }
  public void setMaxHealth(int maxHealth) {
    this.maxHealth = maxHealth;
  }
  public double getBlock() {
    return this.block;
  }
  public void setBlock(double block) {
    this.block = block;
  }
  public int getArmor() {
    return this.armor;
  }
  public void setArmor(int armor) {
    this.armor = armor;
  }
  public double getDamageReduction() {
    return this.damageReduction;
  }
  public void setDamageReduction(double damageReduction) {
    this.damageReduction = damageReduction;
  }
  public double getDamage() {
    return this.damage;
  }
  public void setDamage(double damage) {
    this.damage = damage;
  }
  public double getStealHealth() {
    return this.stealHealth;
  }
  public void setStealHealth(double stealHealth) {
    this.stealHealth = stealHealth;
  }
  public String getInnerName() {
    return this.innerName;
  }
  public void setInnerName(String name) {
    this.innerName = name;
  }
}
