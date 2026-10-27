package com.weberpyth.controller;
import com.weberpyth.config.weberConfig;
import com.weberpyth.item.WeberRPGItem;
import com.weberpyth.nbtEditor.WeberNBTEditor;
import com.weberpyth.util.WeberEntityUtil;
import com.weberpyth.util.WeberText;
import com.weberpyth.util.WeberEquipmentUtil;
import com.weberpyth.weberEvents.weberArmorEvent;
import com.weberpyth.weberEvents.weberAnnihilationDamageEvent;
import com.weberpyth.weberEvents.weberAngerDamageEvent;
import com.weberpyth.weberEvents.weberAppendDamageEvent;
import com.weberpyth.weberEvents.weberAttackHealEvent;
import com.weberpyth.weberEvents.weberAttributesEvent;
import com.weberpyth.weberEvents.weberBackstabEvent;
import com.weberpyth.weberEvents.weberBlowEvent;
import com.weberpyth.weberEvents.weberBlockEvent;
import com.weberpyth.weberEvents.weberCritEvent;
import com.weberpyth.weberEvents.weberCurrentEntityHealthDamageEvent;
import com.weberpyth.weberEvents.weberDamageEvent;
import com.weberpyth.weberEvents.weberExecuteDamageEvent;
import com.weberpyth.weberEvents.weberGiantDamageEvent;
import com.weberpyth.weberEvents.weberIncreaseDefenseOnTheGroundEvent;
import com.weberpyth.weberEvents.weberIncreasesDamageOnTheGroundEvent;
import com.weberpyth.weberEvents.weberInjuryReductionEvent;
import com.weberpyth.weberEvents.weberMaxEntityHealthDamageEvent;
import com.weberpyth.weberEvents.weberMeleeAppendDamageEvent;
import com.weberpyth.weberEvents.weberMeleeDamageEvent;
import com.weberpyth.weberEvents.weberMeleePowerEvent;
import com.weberpyth.weberEvents.weberOutputDamageEvent;
import com.weberpyth.weberEvents.weberPlayerKillerEvent;
import com.weberpyth.weberEvents.weberRangedAppendDamageEvent;
import com.weberpyth.weberEvents.weberRangedDamageEvent;
import com.weberpyth.weberEvents.weberRangedPowerEvent;
import com.weberpyth.weberEvents.weberRegurgitationCurrentHealthDamageEvent;
import com.weberpyth.weberEvents.weberRepelEvent;
import com.weberpyth.weberEvents.weberStealHealthEvent;
import com.weberpyth.weberEvents.weberTenacityEvent;
import com.weberpyth.weberInterface.AttrModFunction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
public class CombatController {
  public EntityDamageByEntityEvent entityDamageByEntityEvent;
  public boolean notLivingDamager;
  public boolean notLivingEntity;
  public Entity damager;
  public EntityEquipment entityEquipment;
  public ItemStack main;
  public ItemStack second;
  public ItemStack[] armors;
  public ItemStack[] equipments;
  public static ArrayList<AttrModFunction> beforeCountModsLink = new ArrayList<>(); public ItemStack entityMain; public ItemStack entitySecond; public ItemStack[] entityArmors; public ItemStack[] entityEquipments; private HashMap<String, WeberRPGItem> items; public static HashMap<String, Double> attrs; public static HashMap<String, weberAttributesEvent> attrEvents; public static String[] attrEventsDamager; public static String[] attrEventsEntity; public static String[] attrEventsOrder;
  public static ArrayList<AttrModFunction> afterCountModsLink = new ArrayList<>();
  public double modDamage;
  public double modHeal;
  public double modInjury;
  public double block;
  public double failTime;
  public boolean noOriginDamage;
  public String msg;
  public String lMsg;
  public String tMsg;
  public WeberRPGItem weberRPGItem;
  public void setItems(HashMap<String, WeberRPGItem> items) {
    this.items = items;
  }

  private static List<String> loreLines(ItemMeta meta) {
    List<String> lines = new ArrayList<>();
    List<net.kyori.adventure.text.Component> components = meta.lore();
    if (components != null) {
      for (net.kyori.adventure.text.Component component : components) {
        lines.add(WeberText.serialize(component));
      }
    }
    return lines;
  }

  /**
   * Adds the attributes of one item (or buff, through WebersBuff's AttrModFunction) to the shared
   * attribute map. Missing keys are treated as zero instead of throwing on unboxing.
   */
  private void mergeAttributes(WeberRPGItem item, String[] keys) {
    if (item == null || item.attributes == null || keys == null) {
      return;
    }
    for (String key : keys) {
      Double modifier = item.attributes.get(key);
      if (modifier == null) {
        continue;
      }
      Double current = attrs.get(key);
      attrs.put(key, current == null ? modifier : current + modifier);
    }
  }
  public void setEntityDamageByEntityEvent(EntityDamageByEntityEvent entityDamageByEntityEvent) {
    this.entityDamageByEntityEvent = entityDamageByEntityEvent;
    this.weberRPGItem = new WeberRPGItem();
  }
  public void response() {
    this.equipments = null;
    this.armors = null;
    this.main = null;
    this.second = null;
    this.entityEquipments = null;
    this.damager = null;
    this.entityEquipment = null;
    if (attrs == null) {
      attrs = new HashMap<>();
    }
    attrs.clear();
    if (this.entityDamageByEntityEvent.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
      weberConfig.debugMsg("触发有来源的燃烧事件");
      return;
    } 
    if (this.entityDamageByEntityEvent.getCause() == EntityDamageEvent.DamageCause.POISON) {
      weberConfig.debugMsg("触发有来源的中毒事件");
      return;
    } 
    if (this.entityDamageByEntityEvent.getCause() == EntityDamageEvent.DamageCause.WITHER) {
      return;
    }
    this.notLivingDamager = false;
    this.notLivingEntity = false;
    this.damager = this.entityDamageByEntityEvent.getDamager();
    if (this.damager instanceof com.weberpyth.weberInterface.WebersVirtualEntity) {
      weberConfig.debugMsg("检测到虚构实体事件");
      return;
    } 
    if (this.entityDamageByEntityEvent.getEntity() instanceof com.weberpyth.weberInterface.WebersVirtualEntity) {
      weberConfig.debugMsg("检测到虚构实体事件");
      return;
    } 
    if (this.damager instanceof org.bukkit.entity.Projectile) {
      this.damager = (Entity)WeberEntityUtil.findShooter(this.damager);
      if (this.damager == null) {
        return;
      }
    } 
    if (this.damager instanceof LivingEntity) {
      this.entityEquipment = ((LivingEntity)this.damager).getEquipment();
      if (this.entityEquipment == null) {
        this.notLivingDamager = true;
      }
    } else {
      this.notLivingDamager = true;
    } 
    if (!(this.entityDamageByEntityEvent.getEntity() instanceof LivingEntity))
    {
      this.notLivingEntity = true;
    }
    if (this.notLivingEntity && this.notLivingDamager) {
      weberConfig.debugMsg("检测到无装备栏实体伤害事件");
      return;
    } 
    weberConfig.debugMsg("检测到有装备栏实体伤害事件");
    if (!this.notLivingEntity && 
      weberConfig.noImmutableTick) {
      ((LivingEntity)this.entityDamageByEntityEvent.getEntity()).setMaximumNoDamageTicks(0);
    }
    if (!weberConfig.playerOnly || 
      this.damager instanceof org.bukkit.entity.Player || this.entityDamageByEntityEvent.getEntity() instanceof org.bukkit.entity.Player) {
      if (this.notLivingDamager) {
        this.equipments = new ItemStack[] { null, null, null, null, null, null };
      } else {
        this.armors = this.entityEquipment.getArmorContents();
        this.equipments = new ItemStack[] { WeberEquipmentUtil.getItemInMainHand(this.entityEquipment), WeberEquipmentUtil.getItemInOffHand(this.entityEquipment), this.armors[0], this.armors[1], this.armors[2], this.armors[3] };
      } 
      if (this.notLivingEntity) {
        this.entityEquipments = new ItemStack[] { null, null, null, null, null, null };
      } else {
        EntityEquipment victimEquipment = ((LivingEntity)this.entityDamageByEntityEvent.getEntity()).getEquipment();
        if (victimEquipment == null) {
          this.entityEquipments = new ItemStack[] { null, null, null, null, null, null };
        } else {
          this.entityMain = WeberEquipmentUtil.getItemInMainHand(victimEquipment);
          this.entitySecond = WeberEquipmentUtil.getItemInOffHand(victimEquipment);
          this.entityArmors = victimEquipment.getArmorContents();
          this.entityEquipments = new ItemStack[] { this.entityMain, this.entitySecond, this.entityArmors[0], this.entityArmors[1], this.entityArmors[2], this.entityArmors[3] };
        } 
      } 
      this.modDamage = 0.0D;
      this.modHeal = 0.0D;
      this.modInjury = 0.0D;
      this.block = 0.0D;
      this.msg = "§f§r[ §6§oWeber's RPGItem Loader§f§r ] : ";
      this.lMsg = "§f§r[ §6§oWeber's RPGItem Loader§f§r ] : ";
      this.tMsg = "§f§r[ §6§oWeber's RPGItem Loader§f§r ] : ";
      this.weberRPGItem = null;
      this.failTime = 0.0D;
      if (weberConfig.playerOnly) {
        if (this.damager instanceof org.bukkit.entity.Player || this.entityDamageByEntityEvent.getEntity() instanceof org.bukkit.entity.Player) {
          beforeCountMods();
          if (!weberConfig.noNBTAPI) {
            countMods();
          }
        } else {
          weberConfig.debugMsg("[仅玩家模式]非玩家事件，处理终止");
          return;
        } 
      } else {
        beforeCountMods();
        if (!weberConfig.noNBTAPI) {
          countMods();
        }
      } 
      if (weberConfig.loreCheck) {
        try {
          countLore();
        } catch (Exception e) {
          weberConfig.showPublicMsg("[警告]lore数据伤害结算异常");
          e.printStackTrace();
        } 
      }
      afterCountMods();
      if (this.failTime >= (this.entityEquipments.length + this.equipments.length) && beforeCountModsLink.size() <= 0 && !weberConfig.loreCheck) {
        weberConfig.debugMsg("双方无插件影响，停止后续结算");
        return;
      } 
      this.noOriginDamage = weberConfig.noOriginDamage;
      setNoOriginDamage();
      try {
        finalCount();
      } catch (Exception e) {
        weberConfig.showPublicMsg("[警告]伤害结算异常");
        e.printStackTrace();
      } 
      this.equipments = null;
      this.armors = null;
      this.main = null;
      this.second = null;
      this.entityEquipments = null;
      this.entityDamageByEntityEvent = null;
      this.damager = null;
      this.entityEquipment = null;
      this.weberRPGItem = null;
      attrs.clear();
      return;
    } 
    weberConfig.debugMsg("[仅玩家模式]非玩家事件，处理终止");
  } public void beforeCountMods() {
    if (beforeCountModsLink.size() <= 0) {
      return;
    }
    for (AttrModFunction mod : beforeCountModsLink) {
      HashMap<String, Double> filtered = mod.filter(attrs, this.entityDamageByEntityEvent);
      if (filtered != null) {
        attrs = filtered;
      }
    } 
  }
  public void afterCountMods() {
    if (afterCountModsLink.size() <= 0) {
      return;
    }
    for (AttrModFunction mod : afterCountModsLink) {
      HashMap<String, Double> filtered = mod.filter(attrs, this.entityDamageByEntityEvent);
      if (filtered != null) {
        attrs = filtered;
      }
    } 
  }
  public void countLore() {
    Set<String> keys = new HashSet<>();
    for (ItemStack im : this.equipments) {
      if (im != null)
      {
        for (String s : this.items.keySet()) {
          String innerName = ((WeberRPGItem)this.items.get(s)).getInnerName();
          if (innerName == null || innerName.equals("")) {
            continue;
          }
          ItemMeta ime = im.getItemMeta();
          if (ime == null) {
            continue;
          }
          List<String> lore = loreLines(ime);
          if (lore.isEmpty()) {
            continue;
          }
          for (String l : lore) {
            if (l.contains(innerName)) {
              weberConfig.debugMsg("lore检测实现加成");
              keys.add(s);
            } 
          } 
        } 
      }
    } 
    for (String key : keys) {
      this.weberRPGItem = this.items.get(key);
      mergeAttributes(this.weberRPGItem, attrEventsDamager);
    } 
    keys = new HashSet<>();
    for (ItemStack im : this.entityEquipments) {
      if (im != null)
      {
        for (String s : this.items.keySet()) {
          String innerName = ((WeberRPGItem)this.items.get(s)).getInnerName();
          if (innerName == null || innerName.equals("")) {
            continue;
          }
          ItemMeta ime = im.getItemMeta();
          if (ime == null) {
            continue;
          }
          List<String> lore = loreLines(ime);
          if (lore.isEmpty()) {
            continue;
          }
          for (String l : lore) {
            if (l.contains(innerName)) {
              keys.add(s);
            }
          } 
        } 
      }
    } 
    for (String key : keys) {
      this.weberRPGItem = this.items.get(key);
      mergeAttributes(this.weberRPGItem, attrEventsEntity);
    } 
  }
  public void countMods() {
    if (!WeberNBTEditor.hasNBTAPI) {
      weberConfig.debugMsg("前置NBTAPI未正确工作，不予结算NBT装备系统");
      this.failTime += (this.entityEquipments.length + this.equipments.length);
    } 
    for (ItemStack im : this.equipments) {
      if (im == null) {
        weberConfig.debugMsg("[警告]发现空装备信息");
        this.failTime++;
      } else {
        String itemID = null;
        try {
          itemID = WeberNBTEditor.getItemNBTStringValue(im, "weber_id");
        } catch (Exception e) {
          weberConfig.debugMsg("装备ID获取失败，可能为空");
          this.failTime++;
        } 
        if (itemID == null || !this.items.containsKey(itemID) || itemID.equals("")) {
          weberConfig.debugMsg("事件中存在不属于集合物品: [" + itemID + "]");
          this.failTime++;
        }
        else {
          this.weberRPGItem = this.items.get(itemID);
          mergeAttributes(this.weberRPGItem, attrEventsDamager);
        } 
      } 
    } 
    for (ItemStack im : this.entityEquipments) {
      if (im == null) {
        weberConfig.debugMsg("[警告]发现空装备信息");
        this.failTime++;
      } else {
        String itemID = null;
        try {
          itemID = WeberNBTEditor.getItemNBTStringValue(im, "weber_id");
        } catch (Exception e) {
          weberConfig.debugMsg("装备ID获取失败，可能为空");
          this.failTime++;
        } 
        if (itemID == null || !this.items.containsKey(itemID) || itemID.equals("")) {
          weberConfig.debugMsg("事件中存在不属于集合物品: [" + itemID + "]");
          this.failTime++;
        } else {
          this.weberRPGItem = this.items.get(itemID);
          mergeAttributes(this.weberRPGItem, attrEventsEntity);
        } 
      } 
    } 
  }
  public void setNoOriginDamage() {
    weberConfig.debugMsg("发生设置原版伤害事件");
    if (this.noOriginDamage) {
      this.entityDamageByEntityEvent.setDamage(0.0D);
    }
  }
  public void finalCount() {
    beforeFinalDamageCount();
    finalDamageCount();
    beforeFinalHealthCount();
    finalHealthCount();
    showBattleInfo();
  }
  public static void initCombatController() {
    attrs = new HashMap<>();
    initAttrEventsLink();
  }
  public static void initAttrEventsLink() {
    attrEvents = new HashMap<>();
    attrEventAdd("damage", (weberAttributesEvent)new weberDamageEvent("damage", 0));
    attrEventAdd("outputDamage", (weberAttributesEvent)new weberOutputDamageEvent("outputDamage", 0));
    attrEventAdd("currentEntityHealthDamage", (weberAttributesEvent)new weberCurrentEntityHealthDamageEvent("currentEntityHealthDamage", 0));
    attrEventAdd("appendDamage", (weberAttributesEvent)new weberAppendDamageEvent("appendDamage", 0));
    attrEventAdd("maxEntityHealthDamage", (weberAttributesEvent)new weberMaxEntityHealthDamageEvent("maxEntityHealthDamage", 0));
    attrEventAdd("crit", (weberAttributesEvent)new weberCritEvent("crit", 0));
    attrEventAdd("attackHeal", (weberAttributesEvent)new weberAttackHealEvent("attackHeal", 1));
    attrEventAdd("stealHealth", (weberAttributesEvent)new weberStealHealthEvent("stealHealth", 1));
    attrEventAdd("block", (weberAttributesEvent)new weberBlockEvent("block", 0));
    attrEventAdd("angerDamage", (weberAttributesEvent)new weberAngerDamageEvent("angerDamage", 0));
    attrEventAdd("regurgitationCurrentHealthDamage", (weberAttributesEvent)new weberRegurgitationCurrentHealthDamageEvent("regurgitationCurrentHealthDamage", 1));
    attrEventAdd("backstab", (weberAttributesEvent)new weberBackstabEvent("backstab", 0));
    attrEventAdd("executeDamage", (weberAttributesEvent)new weberExecuteDamageEvent("executeDamage", 0));
    attrEventAdd("repel", (weberAttributesEvent)new weberRepelEvent("repel", 0));
    attrEventAdd("annihilation", (weberAttributesEvent)new weberAnnihilationDamageEvent("annihilation", 0));
    attrEventAdd("playerKiller", (weberAttributesEvent)new weberPlayerKillerEvent("playerKiller", 0));
    attrEventAdd("tenacity", (weberAttributesEvent)new weberTenacityEvent("tenacity", 0));
    attrEventAdd("increasesDamageOnTheGround", (weberAttributesEvent)new weberIncreasesDamageOnTheGroundEvent("increasesDamageOnTheGround", 0));
    attrEventAdd("increaseDefenseOnTheGround", (weberAttributesEvent)new weberIncreaseDefenseOnTheGroundEvent("increaseDefenseOnTheGround", 0));
    attrEventAdd("blow", (weberAttributesEvent)new weberBlowEvent("blow", 0));
    attrEventAdd("armor", (weberAttributesEvent)new weberArmorEvent("armor", 0));
    attrEventAdd("injuryReduction", (weberAttributesEvent)new weberInjuryReductionEvent("injuryReduction", 0));
    attrEventAdd("giantDamage", (weberAttributesEvent)new weberGiantDamageEvent("giantDamage", 0));
    attrEventAdd("meleeDamage", (weberAttributesEvent)new weberMeleeDamageEvent("meleeDamage", 0));
    attrEventAdd("meleeAppendDamage", (weberAttributesEvent)new weberMeleeAppendDamageEvent("meleeAppendDamage", 0));
    attrEventAdd("rangedDamage", (weberAttributesEvent)new weberRangedDamageEvent("rangedDamage", 0));
    attrEventAdd("rangedAppendDamage", (weberAttributesEvent)new weberRangedAppendDamageEvent("rangedAppendDamage", 0));
    attrEventAdd("meleePower", (weberAttributesEvent)new weberMeleePowerEvent("meleePower", 0));
    attrEventAdd("rangedPower", (weberAttributesEvent)new weberRangedPowerEvent("rangedPower", 0));
    attrEventsDamager = new String[] { "damage", "meleePower", "rangedPower", "crit", "critMul", "currentEntityHealthDamage", "appendDamage", "rangedAppendDamage", "meleeAppendDamage", "maxEntityHealthDamage", "executeDamage", "backstab", "angerDamage", "giantDamage", "outputDamage", "rangedDamage", "meleeDamage", "increasesDamageOnTheGround", "playerKiller", "regurgitationCurrentHealthDamage", "armorDecrease", "armorSub", "annihilation", "repel", "blow", "attackHeal", "stealHealth" };
    attrEventsEntity = new String[] { "block", "tenacity", "increaseDefenseOnTheGround", "armor", "armorIncrease", "injuryReduction" };
    attrEventsOrder = new String[] { "damage", "meleePower", "rangedPower", "crit", "currentEntityHealthDamage", "appendDamage", "rangedAppendDamage", "meleeAppendDamage", "maxEntityHealthDamage", "block", "executeDamage", "backstab", "angerDamage", "increasesDamageOnTheGround", "playerKiller", "giantDamage", "outputDamage", "rangedDamage", "meleeDamage", "armor", "tenacity", "increaseDefenseOnTheGround", "injuryReduction", "repel", "blow", "attackHeal", "stealHealth", "annihilation", "regurgitationCurrentHealthDamage" };
  }
  public static void attrEventAdd(String key, weberAttributesEvent wae) {
    attrEvents.put(key, wae);
  }
  public void beforeFinalDamageCount() {
    for (String k : attrEventsOrder) {
      weberAttributesEvent wae = attrEvents.get(k);
      if (wae == null) {
        weberConfig.showPublicMsg("[警告]有未生效的键");
        continue;
      }
      if (wae.getTrigger() == 0) {
        wae.setValue(attrs);
        this.modDamage = wae.countAttack(Double.valueOf(this.modDamage), this.entityDamageByEntityEvent).doubleValue();
        this.modInjury = wae.countDefence(Double.valueOf(this.modInjury), Double.valueOf(this.modDamage), this.entityDamageByEntityEvent).doubleValue();
        this.modHeal = wae.countHealth(Double.valueOf(this.modHeal), this.entityDamageByEntityEvent).doubleValue();
        weberConfig.debugMsg("检测到[beforeFinalDamage][attrs][" + k + "]事件");
        this.msg = wae.setNumInfo(this.msg);
        if (!"damage".equals(k)) {
          this.lMsg = wae.setNoNumInfo(this.lMsg);
        }
        wae = null;
      } 
    } 
  }
  public void finalDamageCount() {
    double originDamage = this.entityDamageByEntityEvent.getDamage();
    double finalDamage = (originDamage + this.modDamage < 0.0D) ? 0.0D : (originDamage + this.modDamage);
    this.entityDamageByEntityEvent.setDamage(finalDamage);
    weberConfig.debugMsg("此事件最终造成伤害" + finalDamage);
    double finalInjury = (finalDamage + this.modInjury < 0.0D) ? 0.0D : (finalDamage + this.modInjury);
    this.entityDamageByEntityEvent.setDamage(finalInjury);
    weberConfig.debugMsg("此事件最终承受伤害" + finalInjury);
  }
  public void beforeFinalHealthCount() {
    for (String k : attrEventsOrder) {
      weberAttributesEvent wae = attrEvents.get(k);
      if (wae == null) {
        weberConfig.showPublicMsg("[警告]有未生效的键");
        continue;
      }
      if (wae.getTrigger() == 1) {
        wae.setValue(attrs);
        this.modDamage = wae.countAttack(Double.valueOf(this.modDamage), this.entityDamageByEntityEvent).doubleValue();
        this.modInjury = wae.countDefence(Double.valueOf(this.modInjury), Double.valueOf(this.modDamage), this.entityDamageByEntityEvent).doubleValue();
        this.modHeal = wae.countHealth(Double.valueOf(this.modHeal), this.entityDamageByEntityEvent).doubleValue();
        weberConfig.debugMsg("检测到[beforeFinalHealthCount][attrs][" + k + "]事件");
        this.msg = wae.setNumInfo(this.msg);
        if (!"damage".equals(k)) {
          this.lMsg = wae.setNoNumInfo(this.lMsg);
        }
        wae = null;
      } 
    } 
  }
  public void finalHealthCount() {
    if (!(this.damager instanceof LivingEntity)) {
      return;
    }
    double finalDamage = this.entityDamageByEntityEvent.getFinalDamage();
    double maxHealth = WeberEntityUtil.getMaxHealth((LivingEntity)this.damager).doubleValue();
    double currentHealth = ((LivingEntity)this.damager).getHealth();
    double finalHealth = (currentHealth + this.modHeal > maxHealth) ? maxHealth : ((currentHealth + this.modHeal < 0.0D) ? 0.0D : (currentHealth + this.modHeal));
    weberConfig.debugMsg("此事件最终治疗效果" + this.modHeal);
    ((LivingEntity)this.damager).setHealth(finalHealth);
    this.msg += String.format("§f  总计: §6%.2f伤害 §2%.2f治疗", new Object[] { Double.valueOf(finalDamage), Double.valueOf((currentHealth + this.modHeal > maxHealth) ? (maxHealth - currentHealth) : ((currentHealth + this.modHeal < 0.0D) ? 0.0D : this.modHeal)) });
    this.lMsg += String.format("§f  总计: §6%.2f伤害 §2%.2f治疗", new Object[] { Double.valueOf(finalDamage), Double.valueOf((currentHealth + this.modHeal > maxHealth) ? (maxHealth - currentHealth) : ((currentHealth + this.modHeal < 0.0D) ? 0.0D : this.modHeal)) });
  }
  public void showBattleInfo() {
    if (weberConfig.battleEcho && this.damager instanceof org.bukkit.entity.Player)
      if (weberConfig.lightBattleEcho) {
        this.damager.sendMessage(this.lMsg);
      } else {
        this.damager.sendMessage(this.msg);
      }  
  }
}
