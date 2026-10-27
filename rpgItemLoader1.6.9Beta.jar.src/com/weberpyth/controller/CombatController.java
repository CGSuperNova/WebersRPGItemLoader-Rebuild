/*     */ package com.weberpyth.controller;
/*     */ import com.weberpyth.config.weberConfig;
/*     */ import com.weberpyth.item.WeberRPGItem;
/*     */ import com.weberpyth.nbtEditor.WeberNBTEditor;
/*     */ import com.weberpyth.util.WeberEntityUtil;
/*     */ import com.weberpyth.util.WeberEquipmentUtil;
/*     */ import com.weberpyth.weberEvents.weberArmorEvent;
/*     */ import com.weberpyth.weberEvents.weberAttackHealEvent;
/*     */ import com.weberpyth.weberEvents.weberAttributesEvent;
/*     */ import com.weberpyth.weberEvents.weberBackstabEvent;
/*     */ import com.weberpyth.weberEvents.weberBlowEvent;
/*     */ import com.weberpyth.weberEvents.weberIncreasesDamageOnTheGroundEvent;
/*     */ import com.weberpyth.weberEvents.weberInjuryReductionEvent;
/*     */ import com.weberpyth.weberEvents.weberMaxEntityHealthDamageEvent;
/*     */ import com.weberpyth.weberEvents.weberOutputDamageEvent;
/*     */ import com.weberpyth.weberEvents.weberPlayerKillerEvent;
/*     */ import com.weberpyth.weberEvents.weberRangedAppendDamageEvent;
/*     */ import com.weberpyth.weberEvents.weberRepelEvent;
/*     */ import com.weberpyth.weberEvents.weberStealHealthEvent;
/*     */ import com.weberpyth.weberEvents.weberTenacityEvent;
/*     */ import com.weberpyth.weberInterface.AttrModFunction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.event.entity.EntityDamageByEntityEvent;
/*     */ import org.bukkit.event.entity.EntityDamageEvent;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ 
/*     */ public class CombatController {
/*     */   public EntityDamageByEntityEvent entityDamageByEntityEvent;
/*     */   public boolean notLivingDamager;
/*     */   public boolean notLivingEntity;
/*     */   public Entity damager;
/*     */   public EntityEquipment entityEquipment;
/*     */   public ItemStack main;
/*     */   public ItemStack second;
/*     */   public ItemStack[] armors;
/*     */   public ItemStack[] equipments;
/*  44 */   public static ArrayList<AttrModFunction> beforeCountModsLink = new ArrayList<>(); public ItemStack entityMain; public ItemStack entitySecond; public ItemStack[] entityArmors; public ItemStack[] entityEquipments; private HashMap<String, WeberRPGItem> items; public static HashMap<String, Double> attrs; public static HashMap<String, weberAttributesEvent> attrEvents; public static String[] attrEventsDamager; public static String[] attrEventsEntity; public static String[] attrEventsOrder;
/*  45 */   public static ArrayList<AttrModFunction> afterCountModsLink = new ArrayList<>();
/*     */   
/*     */   public double modDamage;
/*     */   public double modHeal;
/*     */   public double modInjury;
/*     */   public double block;
/*     */   public double failTime;
/*     */   public boolean noOriginDamage;
/*     */   public String msg;
/*     */   public String lMsg;
/*     */   public String tMsg;
/*     */   public WeberRPGItem weberRPGItem;
/*     */   
/*     */   public void setItems(HashMap<String, WeberRPGItem> items) {
/*  59 */     this.items = items;
/*     */   }
/*     */   
/*     */   public void setEntityDamageByEntityEvent(EntityDamageByEntityEvent entityDamageByEntityEvent) {
/*  63 */     this.entityDamageByEntityEvent = entityDamageByEntityEvent;
/*  64 */     this.weberRPGItem = new WeberRPGItem();
/*     */   }
/*     */   public void response() {
/*  67 */     this.equipments = null;
/*  68 */     this.armors = null;
/*  69 */     this.main = null;
/*  70 */     this.second = null;
/*  71 */     this.entityEquipments = null;
/*  72 */     this.damager = null;
/*  73 */     this.entityEquipment = null;
/*  74 */     this; attrs.clear();
/*     */     
/*  76 */     if (this.entityDamageByEntityEvent.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK) {
/*  77 */       weberConfig.debugMsg("触发有来源的燃烧事件");
/*     */       return;
/*     */     } 
/*  80 */     if (this.entityDamageByEntityEvent.getCause() == EntityDamageEvent.DamageCause.POISON) {
/*  81 */       weberConfig.debugMsg("触发有来源的中毒事件");
/*     */       return;
/*     */     } 
/*  84 */     if (this.entityDamageByEntityEvent.getCause() == EntityDamageEvent.DamageCause.WITHER) {
/*     */       return;
/*     */     }
/*  87 */     this.notLivingDamager = false;
/*  88 */     this.notLivingEntity = false;
/*  89 */     this.damager = this.entityDamageByEntityEvent.getDamager();
/*     */ 
/*     */     
/*  92 */     if (this.damager instanceof com.weberpyth.weberInterface.WebersVirtualEntity) {
/*  93 */       weberConfig.debugMsg("检测到虚构实体事件");
/*     */       return;
/*     */     } 
/*  96 */     if (this.entityDamageByEntityEvent.getEntity() instanceof com.weberpyth.weberInterface.WebersVirtualEntity) {
/*  97 */       weberConfig.debugMsg("检测到虚构实体事件");
/*     */       
/*     */       return;
/*     */     } 
/* 101 */     if (this.damager instanceof org.bukkit.entity.Projectile) {
/* 102 */       this.damager = (Entity)WeberEntityUtil.findShooter(this.damager);
/* 103 */       if (this.damager == null) {
/*     */         return;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 109 */     if (this.damager instanceof LivingEntity) {
/* 110 */       this.entityEquipment = ((LivingEntity)this.damager).getEquipment();
/*     */     } else {
/* 112 */       this.notLivingDamager = true;
/*     */     } 
/*     */     
/* 115 */     if (!(this.entityDamageByEntityEvent.getEntity() instanceof LivingEntity))
/*     */     {
/*     */       
/* 118 */       this.notLivingEntity = true;
/*     */     }
/* 120 */     if (this.notLivingEntity && this.notLivingDamager) {
/* 121 */       weberConfig.debugMsg("检测到无装备栏实体伤害事件");
/*     */       return;
/*     */     } 
/* 124 */     weberConfig.debugMsg("检测到有装备栏实体伤害事件");
/*     */ 
/*     */ 
/*     */     
/* 128 */     if (!this.notLivingEntity && 
/* 129 */       weberConfig.noImmutableTick) {
/* 130 */       ((LivingEntity)this.entityDamageByEntityEvent.getEntity()).setMaximumNoDamageTicks(0);
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 135 */     if (!weberConfig.playerOnly || 
/* 136 */       this.damager instanceof org.bukkit.entity.Player || this.entityDamageByEntityEvent.getEntity() instanceof org.bukkit.entity.Player) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 146 */       if (this.notLivingDamager) {
/* 147 */         this.equipments = new ItemStack[] { null, null, null, null, null, null };
/*     */       } else {
/*     */         
/* 150 */         this.armors = this.entityEquipment.getArmorContents();
/* 151 */         this.equipments = new ItemStack[] { WeberEquipmentUtil.getItemInMainHand(this.entityEquipment), WeberEquipmentUtil.getItemInOffHand(this.entityEquipment), this.armors[0], this.armors[1], this.armors[2], this.armors[3] };
/*     */       } 
/*     */ 
/*     */       
/* 155 */       if (this.notLivingEntity) {
/* 156 */         this.entityEquipments = new ItemStack[] { null, null, null, null, null, null };
/*     */       } else {
/* 158 */         this.entityMain = WeberEquipmentUtil.getItemInMainHand(((LivingEntity)this.entityDamageByEntityEvent.getEntity()).getEquipment());
/* 159 */         this.entitySecond = WeberEquipmentUtil.getItemInOffHand(((LivingEntity)this.entityDamageByEntityEvent.getEntity()).getEquipment());
/* 160 */         this.entityArmors = ((LivingEntity)this.entityDamageByEntityEvent.getEntity()).getEquipment().getArmorContents();
/* 161 */         this.entityEquipments = new ItemStack[] { this.entityMain, this.entitySecond, this.entityArmors[0], this.entityArmors[1], this.entityArmors[2], this.entityArmors[3] };
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 167 */       this.modDamage = 0.0D;
/* 168 */       this.modHeal = 0.0D;
/* 169 */       this.modInjury = 0.0D;
/*     */       
/* 171 */       this.block = 0.0D;
/*     */       
/* 173 */       this.msg = "§f§r[ §6§oWeber's RPGItem Loader§f§r ] : ";
/* 174 */       this.lMsg = "§f§r[ §6§oWeber's RPGItem Loader§f§r ] : ";
/* 175 */       this.tMsg = "§f§r[ §6§oWeber's RPGItem Loader§f§r ] : ";
/* 176 */       this.weberRPGItem = null;
/*     */ 
/*     */ 
/*     */       
/* 180 */       this.failTime = 0.0D;
/* 181 */       if (weberConfig.playerOnly) {
/* 182 */         if (this.damager instanceof org.bukkit.entity.Player || this.entityDamageByEntityEvent.getEntity() instanceof org.bukkit.entity.Player) {
/* 183 */           beforeCountMods();
/*     */           
/* 185 */           if (!weberConfig.noNBTAPI) {
/* 186 */             countMods();
/*     */           }
/*     */         } else {
/* 189 */           weberConfig.debugMsg("[仅玩家模式]非玩家事件，处理终止");
/*     */           return;
/*     */         } 
/*     */       } else {
/* 193 */         beforeCountMods();
/*     */         
/* 195 */         if (!weberConfig.noNBTAPI) {
/* 196 */           countMods();
/*     */         }
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 202 */       if (weberConfig.loreCheck) {
/*     */         try {
/* 204 */           countLore();
/* 205 */         } catch (Exception e) {
/* 206 */           weberConfig.showPublicMsg("[警告]lore数据伤害结算异常");
/* 207 */           e.printStackTrace();
/*     */         } 
/*     */       }
/* 210 */       afterCountMods();
/* 211 */       if (this.failTime >= (this.entityEquipments.length + this.equipments.length) && beforeCountModsLink.size() <= 0 && !weberConfig.loreCheck) {
/* 212 */         weberConfig.debugMsg("双方无插件影响，停止后续结算");
/*     */         
/*     */         return;
/*     */       } 
/* 216 */       this.noOriginDamage = weberConfig.noOriginDamage;
/* 217 */       setNoOriginDamage();
/*     */       
/*     */       try {
/* 220 */         finalCount();
/* 221 */       } catch (Exception e) {
/* 222 */         weberConfig.showPublicMsg("[警告]伤害结算异常");
/* 223 */         e.printStackTrace();
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 228 */       this.equipments = null;
/* 229 */       this.armors = null;
/* 230 */       this.main = null;
/* 231 */       this.second = null;
/* 232 */       this.entityEquipments = null;
/* 233 */       this.entityDamageByEntityEvent = null;
/* 234 */       this.damager = null;
/* 235 */       this.entityEquipment = null;
/* 236 */       this.weberRPGItem = null;
/* 237 */       this; attrs.clear();
/*     */       return;
/*     */     } 
/*     */     weberConfig.debugMsg("[仅玩家模式]非玩家事件，处理终止");
/*     */   } public void beforeCountMods() {
/* 242 */     if (beforeCountModsLink.size() <= 0) {
/*     */       return;
/*     */     }
/* 245 */     for (AttrModFunction mod : beforeCountModsLink) {
/* 246 */       this; this; attrs = mod.filter(attrs, this.entityDamageByEntityEvent);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void afterCountMods() {
/* 252 */     if (afterCountModsLink.size() <= 0) {
/*     */       return;
/*     */     }
/* 255 */     for (AttrModFunction mod : afterCountModsLink) {
/* 256 */       this; this; attrs = mod.filter(attrs, this.entityDamageByEntityEvent);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void countLore() {
/* 264 */     Set<String> keys = new HashSet<>();
/* 265 */     for (ItemStack im : this.equipments) {
/* 266 */       if (im != null)
/*     */       {
/*     */         
/* 269 */         for (String s : this.items.keySet()) {
/* 270 */           String innerName = ((WeberRPGItem)this.items.get(s)).getInnerName();
/* 271 */           if (innerName == null || innerName.equals("")) {
/*     */             continue;
/*     */           }
/* 274 */           ItemMeta ime = im.getItemMeta();
/* 275 */           if (ime == null) {
/*     */             continue;
/*     */           }
/* 278 */           List<String> lore = ime.getLore();
/* 279 */           if (lore == null || lore.size() < 1) {
/*     */             continue;
/*     */           }
/* 282 */           for (String l : lore) {
/*     */             
/* 284 */             if (l.contains(innerName)) {
/* 285 */               weberConfig.debugMsg("lore检测实现加成");
/* 286 */               keys.add(s);
/*     */             } 
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 293 */     for (String key : keys) {
/*     */       
/* 295 */       this.weberRPGItem = this.items.get(key);
/*     */       
/* 297 */       for (String k : attrEventsDamager) {
/* 298 */         this; this; Double value = attrs.containsKey(k) ? Double.valueOf(((Double)attrs.get(k)).doubleValue() + ((Double)this.weberRPGItem.attributes.get(k)).doubleValue()) : (Double)this.weberRPGItem.attributes.get(k);
/* 299 */         this; attrs.put(k, value);
/*     */       } 
/*     */     } 
/*     */     
/* 303 */     keys = new HashSet<>();
/* 304 */     for (ItemStack im : this.entityEquipments) {
/* 305 */       if (im != null)
/*     */       {
/*     */         
/* 308 */         for (String s : this.items.keySet()) {
/* 309 */           String innerName = ((WeberRPGItem)this.items.get(s)).getInnerName();
/* 310 */           if (innerName == null || innerName.equals("")) {
/*     */             continue;
/*     */           }
/* 313 */           ItemMeta ime = im.getItemMeta();
/* 314 */           if (ime == null) {
/*     */             continue;
/*     */           }
/* 317 */           List<String> lore = ime.getLore();
/* 318 */           if (lore == null || lore.size() < 1) {
/*     */             continue;
/*     */           }
/* 321 */           for (String l : lore) {
/*     */             
/* 323 */             if (l.contains(innerName)) {
/* 324 */               keys.add(s);
/*     */             }
/*     */           } 
/*     */         } 
/*     */       }
/*     */     } 
/*     */     
/* 331 */     for (String key : keys) {
/*     */       
/* 333 */       this.weberRPGItem = this.items.get(key);
/*     */       
/* 335 */       for (String k : attrEventsEntity) {
/* 336 */         this; this; Double value = attrs.containsKey(k) ? Double.valueOf(((Double)attrs.get(k)).doubleValue() + ((Double)this.weberRPGItem.attributes.get(k)).doubleValue()) : (Double)this.weberRPGItem.attributes.get(k);
/* 337 */         this; attrs.put(k, value);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void countMods() {
/* 345 */     if (!WeberNBTEditor.hasNBTAPI) {
/* 346 */       weberConfig.debugMsg("前置NBTAPI未正确工作，不予结算NBT装备系统");
/* 347 */       this.failTime += (this.entityEquipments.length + this.equipments.length);
/*     */     } 
/*     */     
/* 350 */     for (ItemStack im : this.equipments) {
/* 351 */       if (im == null) {
/* 352 */         weberConfig.debugMsg("[警告]发现空装备信息");
/* 353 */         this.failTime++;
/*     */       } else {
/*     */         
/* 356 */         String itemID = null;
/*     */ 
/*     */         
/*     */         try {
/* 360 */           itemID = WeberNBTEditor.getItemNBTStringValue(im, "weber_id");
/* 361 */         } catch (Exception e) {
/* 362 */           weberConfig.debugMsg("装备ID获取失败，可能为空");
/*     */           
/* 364 */           this.failTime++;
/*     */         } 
/*     */ 
/*     */         
/* 368 */         if (itemID == null || !this.items.containsKey(itemID) || itemID.equals("")) {
/* 369 */           weberConfig.debugMsg("事件中存在不属于集合物品: [" + itemID + "]");
/* 370 */           this.failTime++;
/*     */         }
/*     */         else {
/*     */           
/* 374 */           this.weberRPGItem = this.items.get(itemID);
/*     */           
/* 376 */           for (String k : attrEventsDamager) {
/* 377 */             this; this; Double value = attrs.containsKey(k) ? Double.valueOf(((Double)attrs.get(k)).doubleValue() + ((Double)this.weberRPGItem.attributes.get(k)).doubleValue()) : (Double)this.weberRPGItem.attributes.get(k);
/* 378 */             this; attrs.put(k, value);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/* 383 */     for (ItemStack im : this.entityEquipments) {
/* 384 */       if (im == null) {
/* 385 */         weberConfig.debugMsg("[警告]发现空装备信息");
/* 386 */         this.failTime++;
/*     */       } else {
/*     */         
/* 389 */         String itemID = null;
/*     */ 
/*     */         
/*     */         try {
/* 393 */           itemID = WeberNBTEditor.getItemNBTStringValue(im, "weber_id");
/* 394 */         } catch (Exception e) {
/* 395 */           weberConfig.debugMsg("装备ID获取失败，可能为空");
/*     */           
/* 397 */           this.failTime++;
/*     */         } 
/*     */ 
/*     */         
/* 401 */         if (itemID == null || !this.items.containsKey(itemID) || itemID.equals("")) {
/* 402 */           weberConfig.debugMsg("事件中存在不属于集合物品: [" + itemID + "]");
/* 403 */           this.failTime++;
/*     */         } else {
/*     */           
/* 406 */           this.weberRPGItem = this.items.get(itemID);
/*     */           
/* 408 */           for (String k : attrEventsEntity) {
/* 409 */             this; this; Double value = attrs.containsKey(k) ? Double.valueOf(((Double)attrs.get(k)).doubleValue() + ((Double)this.weberRPGItem.attributes.get(k)).doubleValue()) : (Double)this.weberRPGItem.attributes.get(k);
/* 410 */             this; attrs.put(k, value);
/*     */           } 
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void setNoOriginDamage() {
/* 419 */     weberConfig.debugMsg("发生设置原版伤害事件");
/* 420 */     if (this.noOriginDamage) {
/* 421 */       this.entityDamageByEntityEvent.setDamage(0.0D);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void finalCount() {
/* 428 */     beforeFinalDamageCount();
/* 429 */     finalDamageCount();
/* 430 */     beforeFinalHealthCount();
/* 431 */     finalHealthCount();
/* 432 */     showBattleInfo();
/*     */   }
/*     */ 
/*     */   
/*     */   public static void initCombatController() {
/* 437 */     attrs = new HashMap<>();
/* 438 */     initAttrEventsLink();
/*     */   }
/*     */   public static void initAttrEventsLink() {
/* 441 */     attrEvents = new HashMap<>();
/*     */     
/* 443 */     attrEventAdd("damage", (weberAttributesEvent)new weberDamageEvent("damage", 0));
/* 444 */     attrEventAdd("outputDamage", (weberAttributesEvent)new weberOutputDamageEvent("outputDamage", 0));
/* 445 */     attrEventAdd("currentEntityHealthDamage", (weberAttributesEvent)new weberCurrentEntityHealthDamageEvent("currentEntityHealthDamage", 0));
/* 446 */     attrEventAdd("appendDamage", (weberAttributesEvent)new weberAppendDamageEvent("appendDamage", 0));
/* 447 */     attrEventAdd("maxEntityHealthDamage", (weberAttributesEvent)new weberMaxEntityHealthDamageEvent("maxEntityHealthDamage", 0));
/* 448 */     attrEventAdd("crit", (weberAttributesEvent)new weberCritEvent("crit", 0));
/* 449 */     attrEventAdd("attackHeal", (weberAttributesEvent)new weberAttackHealEvent("attackHeal", 1));
/* 450 */     attrEventAdd("stealHealth", (weberAttributesEvent)new weberStealHealthEvent("stealHealth", 1));
/* 451 */     attrEventAdd("block", (weberAttributesEvent)new weberBlockEvent("block", 0));
/* 452 */     attrEventAdd("angerDamage", (weberAttributesEvent)new weberAngerDamageEvent("angerDamage", 0));
/* 453 */     attrEventAdd("regurgitationCurrentHealthDamage", (weberAttributesEvent)new weberRegurgitationCurrentHealthDamageEvent("regurgitationCurrentHealthDamage", 1));
/* 454 */     attrEventAdd("backstab", (weberAttributesEvent)new weberBackstabEvent("backstab", 0));
/* 455 */     attrEventAdd("executeDamage", (weberAttributesEvent)new weberExecuteDamageEvent("executeDamage", 0));
/* 456 */     attrEventAdd("repel", (weberAttributesEvent)new weberRepelEvent("repel", 0));
/* 457 */     attrEventAdd("annihilation", (weberAttributesEvent)new weberAnnihilationDamageEvent("annihilation", 0));
/* 458 */     attrEventAdd("playerKiller", (weberAttributesEvent)new weberPlayerKillerEvent("playerKiller", 0));
/* 459 */     attrEventAdd("tenacity", (weberAttributesEvent)new weberTenacityEvent("tenacity", 0));
/* 460 */     attrEventAdd("increasesDamageOnTheGround", (weberAttributesEvent)new weberIncreasesDamageOnTheGroundEvent("increasesDamageOnTheGround", 0));
/* 461 */     attrEventAdd("increaseDefenseOnTheGround", (weberAttributesEvent)new weberIncreaseDefenseOnTheGroundEvent("increaseDefenseOnTheGround", 0));
/* 462 */     attrEventAdd("blow", (weberAttributesEvent)new weberBlowEvent("blow", 0));
/* 463 */     attrEventAdd("armor", (weberAttributesEvent)new weberArmorEvent("armor", 0));
/* 464 */     attrEventAdd("injuryReduction", (weberAttributesEvent)new weberInjuryReductionEvent("injuryReduction", 0));
/* 465 */     attrEventAdd("giantDamage", (weberAttributesEvent)new weberGiantDamageEvent("giantDamage", 0));
/* 466 */     attrEventAdd("meleeDamage", (weberAttributesEvent)new weberMeleeDamageEvent("meleeDamage", 0));
/* 467 */     attrEventAdd("meleeAppendDamage", (weberAttributesEvent)new weberMeleeAppendDamageEvent("meleeAppendDamage", 0));
/* 468 */     attrEventAdd("rangedDamage", (weberAttributesEvent)new weberRangedDamageEvent("rangedDamage", 0));
/* 469 */     attrEventAdd("rangedAppendDamage", (weberAttributesEvent)new weberRangedAppendDamageEvent("rangedAppendDamage", 0));
/* 470 */     attrEventAdd("meleePower", (weberAttributesEvent)new weberMeleePowerEvent("meleePower", 0));
/* 471 */     attrEventAdd("rangedPower", (weberAttributesEvent)new weberRangedPowerEvent("rangedPower", 0));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 476 */     attrEventsDamager = new String[] { "damage", "meleePower", "rangedPower", "crit", "critMul", "currentEntityHealthDamage", "appendDamage", "rangedAppendDamage", "meleeAppendDamage", "maxEntityHealthDamage", "executeDamage", "backstab", "angerDamage", "giantDamage", "outputDamage", "rangedDamage", "meleeDamage", "increasesDamageOnTheGround", "playerKiller", "regurgitationCurrentHealthDamage", "armorDecrease", "armorSub", "annihilation", "repel", "blow", "attackHeal", "stealHealth" };
/* 477 */     attrEventsEntity = new String[] { "block", "tenacity", "increaseDefenseOnTheGround", "armor", "armorIncrease", "injuryReduction" };
/*     */     
/* 479 */     attrEventsOrder = new String[] { "damage", "meleePower", "rangedPower", "crit", "currentEntityHealthDamage", "appendDamage", "rangedAppendDamage", "meleeAppendDamage", "maxEntityHealthDamage", "block", "executeDamage", "backstab", "angerDamage", "increasesDamageOnTheGround", "playerKiller", "giantDamage", "outputDamage", "rangedDamage", "meleeDamage", "armor", "tenacity", "increaseDefenseOnTheGround", "injuryReduction", "repel", "blow", "attackHeal", "stealHealth", "annihilation", "regurgitationCurrentHealthDamage" };
/*     */   }
/*     */ 
/*     */   
/*     */   public static void attrEventAdd(String key, weberAttributesEvent wae) {
/* 484 */     attrEvents.put(key, wae);
/*     */   }
/*     */   
/*     */   public void beforeFinalDamageCount() {
/* 488 */     for (String k : attrEventsOrder) {
/* 489 */       weberAttributesEvent wae = attrEvents.get(k);
/* 490 */       if (wae == null) {
/* 491 */         weberConfig.showPublicMsg("[警告]有未生效的键");
/*     */       }
/* 493 */       if (wae.getTrigger() == 0) {
/*     */ 
/*     */         
/* 496 */         wae.setValue(attrs);
/* 497 */         this.modDamage = wae.countAttack(Double.valueOf(this.modDamage), this.entityDamageByEntityEvent).doubleValue();
/* 498 */         this.modInjury = wae.countDefence(Double.valueOf(this.modInjury), Double.valueOf(this.modDamage), this.entityDamageByEntityEvent).doubleValue();
/* 499 */         this.modHeal = wae.countHealth(Double.valueOf(this.modHeal), this.entityDamageByEntityEvent).doubleValue();
/* 500 */         weberConfig.debugMsg("检测到[beforeFinalDamage][attrs][" + k + "]事件");
/* 501 */         this.msg = wae.setNumInfo(this.msg);
/* 502 */         if (k != "damage" || k != "") {
/* 503 */           this.lMsg = wae.setNoNumInfo(this.lMsg);
/*     */         }
/* 505 */         wae = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void finalDamageCount() {
/* 510 */     double originDamage = this.entityDamageByEntityEvent.getDamage();
/* 511 */     double finalDamage = (originDamage + this.modDamage < 0.0D) ? 0.0D : (originDamage + this.modDamage);
/* 512 */     this.entityDamageByEntityEvent.setDamage(finalDamage);
/* 513 */     weberConfig.debugMsg("此事件最终造成伤害" + finalDamage);
/*     */ 
/*     */     
/* 516 */     double finalInjury = (finalDamage + this.modInjury < 0.0D) ? 0.0D : (finalDamage + this.modInjury);
/* 517 */     this.entityDamageByEntityEvent.setDamage(finalInjury);
/* 518 */     weberConfig.debugMsg("此事件最终承受伤害" + finalInjury);
/*     */   }
/*     */   
/*     */   public void beforeFinalHealthCount() {
/* 522 */     for (String k : attrEventsOrder) {
/* 523 */       weberAttributesEvent wae = attrEvents.get(k);
/* 524 */       if (wae.getTrigger() == 1) {
/*     */ 
/*     */         
/* 527 */         this; wae.setValue(attrs);
/* 528 */         this.modDamage = wae.countAttack(Double.valueOf(this.modDamage), this.entityDamageByEntityEvent).doubleValue();
/* 529 */         this.modInjury = wae.countDefence(Double.valueOf(this.modInjury), Double.valueOf(this.modDamage), this.entityDamageByEntityEvent).doubleValue();
/* 530 */         this.modHeal = wae.countHealth(Double.valueOf(this.modHeal), this.entityDamageByEntityEvent).doubleValue();
/* 531 */         weberConfig.debugMsg("检测到[beforeFinalHealthCount][attrs][" + k + "]事件");
/* 532 */         this.msg = wae.setNumInfo(this.msg);
/* 533 */         if (k != "damage" || k != "") {
/* 534 */           this.lMsg = wae.setNoNumInfo(this.lMsg);
/*     */         }
/* 536 */         wae = null;
/*     */       } 
/*     */     } 
/*     */   }
/*     */   public void finalHealthCount() {
/* 541 */     if (!(this.damager instanceof LivingEntity)) {
/*     */       return;
/*     */     }
/*     */     
/* 545 */     double finalDamage = this.entityDamageByEntityEvent.getFinalDamage();
/* 546 */     double maxHealth = WeberEntityUtil.getMaxHealth((LivingEntity)this.damager).doubleValue();
/* 547 */     double currentHealth = ((LivingEntity)this.damager).getHealth();
/* 548 */     double finalHealth = (currentHealth + this.modHeal > maxHealth) ? maxHealth : ((currentHealth + this.modHeal < 0.0D) ? 0.0D : (currentHealth + this.modHeal));
/* 549 */     weberConfig.debugMsg("此事件最终治疗效果" + this.modHeal);
/*     */     
/* 551 */     ((LivingEntity)this.damager).setHealth(finalHealth);
/* 552 */     this.msg += String.format("§f  总计: §6%.2f伤害 §2%.2f治疗", new Object[] { Double.valueOf(finalDamage), Double.valueOf((currentHealth + this.modHeal > maxHealth) ? (maxHealth - currentHealth) : ((currentHealth + this.modHeal < 0.0D) ? 0.0D : this.modHeal)) });
/* 553 */     this.lMsg += String.format("§f  总计: §6%.2f伤害 §2%.2f治疗", new Object[] { Double.valueOf(finalDamage), Double.valueOf((currentHealth + this.modHeal > maxHealth) ? (maxHealth - currentHealth) : ((currentHealth + this.modHeal < 0.0D) ? 0.0D : this.modHeal)) });
/*     */   }
/*     */   
/*     */   public void showBattleInfo() {
/* 557 */     if (weberConfig.battleEcho && this.damager instanceof org.bukkit.entity.Player)
/* 558 */       if (weberConfig.lightBattleEcho) {
/* 559 */         this.damager.sendMessage(this.lMsg);
/*     */       } else {
/* 561 */         this.damager.sendMessage(this.msg);
/*     */       }  
/*     */   }
/*     */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\controller\CombatController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */