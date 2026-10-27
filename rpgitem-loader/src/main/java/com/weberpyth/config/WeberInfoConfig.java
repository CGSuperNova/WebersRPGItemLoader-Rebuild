package com.weberpyth.config;

import com.weberpyth.item.WeberRPGItemInfo;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemorySection;

/**
 * Loads the display templates (name + lore) from {@code info.yml}.
 */
public class WeberInfoConfig extends SimpleYamlConfig {

    public WeberInfoConfig() {
        super("info.yml");
    }

    public HashMap<String, WeberRPGItemInfo> itemsParser() {
        HashMap<String, WeberRPGItemInfo> items = new HashMap<>();
        ConfigurationSection root = getCustomConfig().getRoot();
        if (root == null) {
            return items;
        }
        Set<String> ids = root.getKeys(false);
        weberConfig.debugMsg("等待载入WRL信息数量：" + ids.size());
        for (String id : ids) {
            ConfigurationSection section = root.getConfigurationSection(id);
            if (section == null) {
                continue;
            }
            WeberRPGItemInfo info = new WeberRPGItemInfo();
            info.setId(id);
            info.setName(section.getString("name", id));
            ArrayList<String> lores = new ArrayList<>();
            List<?> configured = section.getList("lores");
            if (configured != null) {
                for (Object line : configured) {
                    if (line != null) {
                        lores.add(String.valueOf(line));
                    }
                }
            }
            info.setLores(lores);
            items.put(id, info);
        }
        weberConfig.showPublicMsg("WRL显示模板载入完成，共 " + items.size() + " 项");
        return items;
    }
}
