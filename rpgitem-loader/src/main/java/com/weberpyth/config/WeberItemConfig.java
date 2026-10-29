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

    // ------------------------------------------------------------------ runtime editing

    /** Returns the canonical attribute key for a case-insensitive user input, or {@code null}. */
    public static String canonicalAttribute(String input) {
        if (input == null) {
            return null;
        }
        for (String key : ATTRIBUTE_KEYS) {
            if (key.equalsIgnoreCase(input)) {
                return key;
            }
        }
        return null;
    }

    /** A new template whose attributes are all zero. */
    public static WeberRPGItem blankTemplate(String id) {
        WeberRPGItem item = new WeberRPGItem();
        item.setId(id);
        item.setInnerName(id);
        HashMap<String, Double> attributes = new HashMap<>();
        for (String key : ATTRIBUTE_KEYS) {
            attributes.put(key, 0.0D);
        }
        item.attributes = attributes;
        return item;
    }

    /** Writes a new (all-zero) template node and saves {@code items.yml}. */
    public boolean createTemplate(String id) {
        ConfigurationSection section = getCustomConfig().getConfigurationSection(id);
        if (section == null) {
            section = getCustomConfig().createSection(id);
        }
        // Keep the node alive even before the first attribute is added.
        section.set("innerName", id);
        saveCustomConfig();
        return true;
    }

    /** Removes a template node and saves {@code items.yml}. */
    public boolean deleteTemplate(String id) {
        if (!getCustomConfig().contains(id)) {
            return false;
        }
        getCustomConfig().set(id, null);
        saveCustomConfig();
        return true;
    }

    /** Sets one attribute of a template and saves {@code items.yml}. */
    public boolean setAttribute(String id, String key, double value) {
        ConfigurationSection section = getCustomConfig().getConfigurationSection(id);
        if (section == null) {
            section = getCustomConfig().createSection(id);
        }
        section.set(key, value);
        saveCustomConfig();
        return true;
    }

    /**
     * Removes one attribute from a template and saves {@code items.yml}.
     *
     * <p>When it was the only attribute the key is kept at {@code 0} instead, so the template node
     * never becomes empty (an empty node would disappear from the saved YAML).
     */
    public boolean removeAttribute(String id, String key) {
        ConfigurationSection section = getCustomConfig().getConfigurationSection(id);
        if (section == null) {
            return false;
        }
        section.set(key, null);
        if (section.getKeys(false).isEmpty()) {
            section.set(key, 0.0D);
        }
        saveCustomConfig();
        return true;
    }
}
