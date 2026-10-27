package com.weberpyth.config;

import com.weberpyth.weberBuffData.WeberBuffEntity;
import java.util.HashMap;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Persists active buffs in {@code combat.yml} as {@code <player>.buff.<buffId>: <startTimestamp>}.
 */
public class WeberBuffCombatConfig extends SimpleYamlConfig {

    public static HashMap<String, HashMap<String, WeberBuffEntity>> combatInfo = new HashMap<>();

    public WeberBuffCombatConfig() {
        super("combat.yml");
    }

    public void saveCombatInfo() {
        if (combatInfo == null) {
            return;
        }
        for (String player : combatInfo.keySet()) {
            HashMap<String, WeberBuffEntity> buffs = combatInfo.get(player);
            if (buffs == null) {
                continue;
            }
            for (String buffId : buffs.keySet()) {
                WeberBuffEntity entity = buffs.get(buffId);
                if (entity == null || entity.timer == null) {
                    continue;
                }
                getCustomConfig().set(String.format("%s.buff.%s", player, buffId), entity.timer);
            }
        }
        saveCustomConfig();
    }

    public HashMap<String, HashMap<String, WeberBuffEntity>> loadCombatInfo() {
        combatInfo = new HashMap<>();
        ConfigurationSection root = getCustomConfig().getRoot();
        if (root == null) {
            return combatInfo;
        }
        Set<String> players = root.getKeys(false);
        WeberBuffConfig.debugMsg("等待载入WRB战斗数据数量：" + players.size());
        for (String player : players) {
            ConfigurationSection buffList = root.getConfigurationSection(player + ".buff");
            if (buffList == null) {
                continue;
            }
            HashMap<String, WeberBuffEntity> buffs = new HashMap<>();
            for (String buffId : buffList.getKeys(false)) {
                WeberBuffEntity entity = new WeberBuffEntity();
                entity.weberBuffModel = WeberBuffConfig.models == null ? null : WeberBuffConfig.models.get(buffId);
                entity.timer = buffList.getLong(buffId);
                if (entity.weberBuffModel == null) {
                    // The buff template was deleted from buff.yml; drop the stale entry instead of
                    // keeping an object that would NPE on the next combat tick.
                    WeberBuffConfig.debugMsg("角色" + player + " 的 buff " + buffId + " 已不存在，忽略");
                    continue;
                }
                buffs.put(buffId, entity);
            }
            if (!buffs.isEmpty()) {
                combatInfo.put(player, buffs);
            }
        }
        WeberBuffConfig.debugMsg("WRB战斗数据载入完成!");
        return combatInfo;
    }
}
