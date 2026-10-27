package com.weberpyth.config;

import com.weberpyth.item.WeberRPGItem;
import com.weberpyth.weberBuffData.WeberBuffModel;
import java.util.HashMap;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;

/**
 * Loads the buff templates from {@code buff.yml}. A buff is an RPG item template plus a
 * {@code length} (duration in milliseconds) and a display {@code name}.
 */
public class WeberBuffLoadConfig extends SimpleYamlConfig {

    public WeberBuffLoadConfig() {
        super("buff.yml");
    }

    public HashMap<String, WeberBuffModel> itemsParser() {
        HashMap<String, WeberBuffModel> models = new HashMap<>();
        ConfigurationSection root = getCustomConfig().getRoot();
        if (root == null) {
            return models;
        }
        Set<String> ids = root.getKeys(false);
        WeberBuffConfig.debugMsg("等待载入WRB的Buff数量：" + ids.size());
        for (String id : ids) {
            WeberRPGItem parent = WeberItemConfig.loadFromConf(root, id, "Buff");
            if (parent == null) {
                continue;
            }
            ConfigurationSection section = root.getConfigurationSection(id);
            WeberBuffModel model = new WeberBuffModel();
            model.loadParent(parent);
            model.attributes.put("length", section.getDouble("length", 0.0D));
            model.name = section.getString("name", id);
            models.put(id, model);
        }
        WeberBuffConfig.showPublicMsg("Buff模板载入完成，共 " + models.size() + " 项");
        return models;
    }
}
