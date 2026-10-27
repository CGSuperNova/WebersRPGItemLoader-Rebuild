package com.weberpyth.weberInterface;

import java.util.HashMap;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@FunctionalInterface
public interface AttrModFunction {
  HashMap<String, Double> filter(HashMap<String, Double> paramHashMap, EntityDamageByEntityEvent paramEntityDamageByEntityEvent);
}
