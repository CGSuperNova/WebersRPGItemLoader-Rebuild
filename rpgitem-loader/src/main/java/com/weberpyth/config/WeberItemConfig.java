package com.weberpyth.config;

import com.weberpyth.item.WeberRPGItem;
import java.util.HashMap;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Loads the RPG item templates from {@code items.yml}.
 *
 * <p>Every numeric attribute is optional and defaults to {@code 0}; the old implementation called
 * {@code getConfigurationSection(s).getDouble(...)} for each attribute, which threw an NPE for a
 * malformed node and repeated the lookup more than thirty times per item.
 */
public class WeberItemConfig extends SimpleYamlConfig {

    /** Attribute keys understood by the combat pipeline (order defines nothing but readability). */
    public static final String[] ATTRIBUTE_KEYS = {
            "damage", "outputDamage", "currentEntityHealthDamage", "appendDamage",
            "maxEntityHealthDamage", "crit", "critMul", "attackHeal", "stealHealth", "block",
            "angerDamage", "regurgitationCurrentHealthDamage", "backstab", "executeDamage",
            "repel", "annihilation", "playerKiller", "tenacity", "increasesDamageOnTheGround",
            "increaseDefenseOnTheGround", "blow", "armor", "armorIncrease", "armorDecrease",
            "armorSub", "injuryReduction", "giantDamage", "meleeDamage", "rangedDamage",
            "meleeAppendDamage", "rangedAppendDamage", "meleePower", "rangedPower"
    };

    public WeberItemConfig() {
        super("items.yml");
    }

    /**
     * Reads one template node. Returns {@code null} when the node is missing or not a section,
     * so callers can skip it instead of failing the whole file.
     */
    public static WeberRPGItem loadFromConf(ConfigurationSection itemsCS, String id, String type) {
        if (itemsCS == null) {
            return null;
        }
        ConfigurationSection section = itemsCS.getConfigurationSection(id);
        if (section == null) {
            weberConfig.showPublicMsg("[警告]" + type + "配置项 " + id + " 不是有效节点，已跳过");
            return null;
        }
        WeberRPGItem item = new WeberRPGItem();
        item.setId(id);
        item.setInnerName(section.getString("innerName"));
        HashMap<String, Double> attributes = new HashMap<>();
        for (String key : ATTRIBUTE_KEYS) {
            attributes.put(key, section.getDouble(key, 0.0D));
        }
        item.attributes = attributes;
        weberConfig.debugMsg("载入" + type + " " + id + "，属性项 " + attributes.size() + " 个");
        return item;
    }

    public HashMap<String, WeberRPGItem> itemsParser() {
        HashMap<String, WeberRPGItem> items = new HashMap<>();
        ConfigurationSection root = getCustomConfig().getRoot();
        if (root == null) {
            return items;
        }
        Set<String> ids = root.getKeys(false);
        weberConfig.debugMsg("等待载入WRL物品数量：" + ids.size());
        for (String id : ids) {
            WeberRPGItem item = loadFromConf(root, id, "物品");
            if (item != null) {
                items.put(id, item);
            }
        }
        weberConfig.showPublicMsg("WRL物品模板载入完成，共 " + items.size() + " 项");
        return items;
    }
}
