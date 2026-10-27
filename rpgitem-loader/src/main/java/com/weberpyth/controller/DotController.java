package com.weberpyth.controller;
import com.weberpyth.weberEvents.weberAttributesEvent;
import java.util.HashMap;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;
public class DotController
{
  public EntityDamageEvent entityDamageEvent;
  public static HashMap<String, weberAttributesEvent> attrEvents;
  public Entity damager;
  public Entity entity;
  public DotController() {
    attrEvents = new HashMap<>();
  }
  public void setEntityDamageEvent(EntityDamageEvent e) {
    this.entityDamageEvent = e;
  }
  public void response() {
    this.entity = this.entityDamageEvent.getEntity();
    if (this.entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.WITHER && this.entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.POISON && this.entityDamageEvent.getCause() != EntityDamageEvent.DamageCause.FIRE_TICK) {
      return;
    }
    initAttrsEventsLink();
    countMods();
  }
  public void initAttrsEventsLink() {}
  public void countMods() {}
  public static void attrEventAdd(String key, weberAttributesEvent wae) {
    attrEvents.put(key, wae);
  }
}
