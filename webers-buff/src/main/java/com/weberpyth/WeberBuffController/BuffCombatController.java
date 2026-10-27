package com.weberpyth.WeberBuffController;

import com.weberpyth.config.WeberBuffCombatConfig;
import com.weberpyth.config.WeberBuffConfig;
import com.weberpyth.weberBuffData.WeberBuffEntity;
import com.weberpyth.weberInterface.AttrModFunction;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

/**
 * Contributes the attributes of all active buffs of the attacker and the defender to the shared
 * attribute map of the RPG combat pipeline.
 *
 * <p>Registered by WebersBuff into {@code CombatController.beforeCountModsLink}; both plugins are
 * loaded by the same class loader group because WebersBuff declares the loader as a dependency.
 */
public class BuffCombatController implements AttrModFunction {

    @Override
    public HashMap<String, Double> filter(HashMap<String, Double> attrs, EntityDamageByEntityEvent event) {
        if (attrs == null || event == null || WeberBuffCombatConfig.combatInfo == null) {
            return attrs;
        }
        WeberBuffConfig.debugMsg("触发Buff判定事件");

        if (event.getDamager() instanceof Player) {
            applyBuffs(((Player) event.getDamager()).getName(), attrs);
        }
        if (event.getEntity() instanceof Player) {
            applyBuffs(((Player) event.getEntity()).getName(), attrs);
        }
        return attrs;
    }

    private void applyBuffs(String playerName, HashMap<String, Double> attrs) {
        HashMap<String, WeberBuffEntity> buffs = WeberBuffCombatConfig.combatInfo.get(playerName);
        if (buffs == null) {
            return;
        }
        for (WeberBuffEntity entity : buffs.values()) {
            if (entity == null || entity.weberBuffModel == null || entity.weberBuffModel.attributes == null) {
                continue;
            }
            if (!entity.isActive()) {
                continue;
            }
            for (String key : entity.weberBuffModel.attributes.keySet()) {
                if ("length".equals(key)) {
                    continue;
                }
                Double bonus = entity.weberBuffModel.attributes.get(key);
                if (bonus == null) {
                    continue;
                }
                Double current = attrs.get(key);
                attrs.put(key, current == null ? bonus : current + bonus);
            }
        }
    }
}
