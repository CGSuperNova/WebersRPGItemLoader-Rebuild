/*    */ package com.weberpyth.weberEvents;
/*    */ 
/*    */ import java.util.HashMap;
/*    */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*    */ 
/*    */ public class weberArmorEvent
/*    */   extends weberAttributesEvent
/*    */ {
/*  9 */   public Double increase = Double.valueOf(0.0D);
/* 10 */   public Double decrease = Double.valueOf(0.0D);
/* 11 */   public Double armorSub = Double.valueOf(0.0D);
/*    */   public weberArmorEvent(String name, int trigger) {
/* 13 */     this.name = name;
/* 14 */     this.trigger = trigger;
/*    */   }
/*    */ 
/*    */   
/*    */   public void setValue(HashMap<String, Double> attrs) {
/* 19 */     this.value = (attrs.get("armor") == null) ? new Double(0.0D) : attrs.get("armor");
/* 20 */     this.increase = (attrs.get("armorIncrease") == null) ? new Double(0.0D) : attrs.get("armorIncrease");
/* 21 */     this.decrease = (attrs.get("armorDecrease") == null) ? new Double(0.0D) : attrs.get("armorDecrease");
/* 22 */     this.armorSub = (attrs.get("armorSub") == null) ? new Double(0.0D) : attrs.get("armorSub");
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Double getValue() {
/* 28 */     return super.getValue();
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countAttack(Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 33 */     Double finalArmor = this.value;
/* 34 */     Double mul = Double.valueOf(0.0D);
/*    */     
/* 36 */     if (this.value.doubleValue() > 0.0D) {
/* 37 */       finalArmor = Double.valueOf(this.value.doubleValue() + this.value.doubleValue() * this.increase.doubleValue() - this.value.doubleValue() * this.decrease.doubleValue());
/*    */     }
/*    */     
/* 40 */     if (this.value.doubleValue() < 0.0D) {
/* 41 */       finalArmor = Double.valueOf(this.value.doubleValue() - this.value.doubleValue() * this.increase.doubleValue() + this.value.doubleValue() * this.decrease.doubleValue());
/*    */     }
/*    */     
/* 44 */     finalArmor = Double.valueOf(finalArmor.doubleValue() - this.armorSub.doubleValue());
/*    */     
/* 46 */     if (finalArmor.doubleValue() < 0.0D) {
/* 47 */       mul = Double.valueOf(1.0D - finalArmor.doubleValue() / 100.0D);
/* 48 */       return Double.valueOf(modDamage.doubleValue() + modDamage.doubleValue() * mul.doubleValue());
/*    */     } 
/*    */     
/* 51 */     return modDamage;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countHealth(Double modHealth, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 56 */     return modHealth;
/*    */   }
/*    */ 
/*    */   
/*    */   public Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent entityDamageByEntityEvent) {
/* 61 */     Double finalArmor = this.value;
/* 62 */     Double mul = Double.valueOf(0.0D);
/* 63 */     if (this.value.doubleValue() > 0.0D) {
/* 64 */       finalArmor = Double.valueOf(this.value.doubleValue() + this.value.doubleValue() * this.increase.doubleValue() - this.value.doubleValue() * this.decrease.doubleValue());
/*    */     }
/*    */     
/* 67 */     if (this.value.doubleValue() < 0.0D) {
/* 68 */       finalArmor = Double.valueOf(this.value.doubleValue() - this.value.doubleValue() * this.increase.doubleValue() + this.value.doubleValue() * this.decrease.doubleValue());
/*    */     }
/*    */     
/* 71 */     finalArmor = Double.valueOf(finalArmor.doubleValue() - this.armorSub.doubleValue());
/* 72 */     if (finalArmor.doubleValue() > 0.0D) {
/* 73 */       mul = Double.valueOf(100.0D / (100.0D + finalArmor.doubleValue()));
/* 74 */       return Double.valueOf(modInjury.doubleValue() - modDamage.doubleValue() * mul.doubleValue());
/*    */     } 
/*    */     
/* 77 */     return modInjury;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String setNoNumInfo(String msg) {
/* 85 */     return msg;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String setNumInfo(String msg) {
/* 93 */     return msg;
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEvents\weberArmorEvent.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */