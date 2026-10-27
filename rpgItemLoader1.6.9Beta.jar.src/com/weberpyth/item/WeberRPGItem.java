/*     */ package com.weberpyth.item;
/*     */ 
/*     */ import java.util.HashMap;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WeberRPGItem
/*     */ {
/*     */   public String id;
/*     */   public HashMap<String, Double> attributes;
/*     */   public float crit;
/*     */   public float critMul;
/*     */   public double damage;
/*     */   public double stealHealth;
/*     */   public int maxHealth;
/*     */   public double block;
/*     */   public int armor;
/*     */   public double damageReduction;
/*     */   public double attackHeal;
/*     */   public double appendDamage;
/*     */   public double currentEntityHealthDamage;
/*     */   public double maxEntityHealthDamage;
/*     */   public double outputDamage;
/*     */   public double entityLostHealthDamage;
/*     */   public String innerName;
/*     */   
/*     */   public double getOutputDamage() {
/*  30 */     return this.outputDamage;
/*     */   }
/*     */   
/*     */   public void setOutputDamage(double outputDamage) {
/*  34 */     this.outputDamage = outputDamage;
/*     */   }
/*     */   
/*     */   public double getMaxEntityHealthDamage() {
/*  38 */     return this.maxEntityHealthDamage;
/*     */   }
/*     */   
/*     */   public void setMaxEntityHealthDamage(double maxEntityHealthDamage) {
/*  42 */     this.maxEntityHealthDamage = maxEntityHealthDamage;
/*     */   }
/*     */   
/*     */   public double getCurrentEntityHealthDamage() {
/*  46 */     return this.currentEntityHealthDamage;
/*     */   }
/*     */   
/*     */   public void setCurrentEntityHealthDamage(double currentEntityHealthDamage) {
/*  50 */     this.currentEntityHealthDamage = currentEntityHealthDamage;
/*     */   }
/*     */   public double getAppendDamage() {
/*  53 */     return this.appendDamage;
/*     */   }
/*     */   
/*     */   public void setAppendDamage(double appendDamage) {
/*  57 */     this.appendDamage = appendDamage;
/*     */   }
/*     */   public double getAttackHeal() {
/*  60 */     return this.attackHeal;
/*     */   }
/*     */   
/*     */   public void setAttackHeal(double attackHeal) {
/*  64 */     this.attackHeal = attackHeal;
/*     */   }
/*     */   
/*     */   public float getCrit() {
/*  68 */     return this.crit;
/*     */   }
/*     */   
/*     */   public void setCrit(float crit) {
/*  72 */     this.crit = crit;
/*     */   }
/*     */   
/*     */   public float getCritMul() {
/*  76 */     return this.critMul;
/*     */   }
/*     */   
/*     */   public void setCritMul(float critMul) {
/*  80 */     this.critMul = critMul;
/*     */   }
/*     */   
/*     */   public String getId() {
/*  84 */     return this.id;
/*     */   }
/*     */   
/*     */   public void setId(String id) {
/*  88 */     this.id = id;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaxHealth() {
/*  94 */     return this.maxHealth;
/*     */   }
/*     */   
/*     */   public void setMaxHealth(int maxHealth) {
/*  98 */     this.maxHealth = maxHealth;
/*     */   }
/*     */   
/*     */   public double getBlock() {
/* 102 */     return this.block;
/*     */   }
/*     */   
/*     */   public void setBlock(double block) {
/* 106 */     this.block = block;
/*     */   }
/*     */   
/*     */   public int getArmor() {
/* 110 */     return this.armor;
/*     */   }
/*     */   
/*     */   public void setArmor(int armor) {
/* 114 */     this.armor = armor;
/*     */   }
/*     */   
/*     */   public double getDamageReduction() {
/* 118 */     return this.damageReduction;
/*     */   }
/*     */   
/*     */   public void setDamageReduction(double damageReduction) {
/* 122 */     this.damageReduction = damageReduction;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public double getDamage() {
/* 128 */     return this.damage;
/*     */   }
/*     */   
/*     */   public void setDamage(double damage) {
/* 132 */     this.damage = damage;
/*     */   }
/*     */   
/*     */   public double getStealHealth() {
/* 136 */     return this.stealHealth;
/*     */   }
/*     */   
/*     */   public void setStealHealth(double stealHealth) {
/* 140 */     this.stealHealth = stealHealth;
/*     */   }
/*     */   
/*     */   public String getInnerName() {
/* 144 */     return this.innerName;
/*     */   }
/*     */   
/*     */   public void setInnerName(String name) {
/* 148 */     this.innerName = name;
/*     */   }
/*     */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\item\WeberRPGItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */