package com.weberpyth.weberInterface;

import java.util.HashMap;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

@FunctionalInterface
public interface AttrModFunction {
  HashMap<String, Double> filter(HashMap<String, Double> paramHashMap, EntityDamageByEntityEvent paramEntityDamageByEntityEvent);
}


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberInterface\AttrModFunction.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */