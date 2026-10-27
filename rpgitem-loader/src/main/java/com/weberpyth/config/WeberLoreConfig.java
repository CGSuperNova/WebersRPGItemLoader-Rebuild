package com.weberpyth.config;

import com.weberpyth.util.WeberText;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * Builds the Chinese attribute lore block for RPG items.
 *
 * <p>Every format is configurable through {@code lore.yml}: the attribute order, whether a value
 * is shown as a percentage, the number of decimals and the literal text around {@code %value%}.
 * Unknown attributes fall back to {@code <key> +<value>} so a newly added attribute is never
 * silently invisible.
 */
public class WeberLoreConfig extends SimpleYamlConfig {

    /** One display rule. */
    public static final class Entry {
        public final String key;
        public final String format;
        public final boolean percent;
        public final int decimals;

        Entry(String key, String format, boolean percent, int decimals) {
            this.key = key;
            this.format = format;
            this.percent = percent;
            this.decimals = decimals;
        }

        String render(double rawValue) {
            double value = percent ? rawValue * 100.0D : rawValue;
            String number = formatNumber(value, decimals);
            return WeberText.toSection(format.replace("%value%", number).replace("%key%", key));
        }
    }

    private final List<Entry> entries = new ArrayList<>();
    private final Map<String, Entry> byKey = new LinkedHashMap<>();
    private String separator = "\u00a78\u00a7m--------------------";
    private boolean showZero = false;
    private boolean parsed = false;

    public WeberLoreConfig() {
        super("lore.yml");
    }

    @Override
    public void reloadCustomConfig() {
        super.reloadCustomConfig();
        this.parsed = false;
    }

    @Override
    public FileConfiguration getCustomConfig() {
        FileConfiguration configuration = super.getCustomConfig();
        if (!this.parsed) {
            parse(configuration);
            this.parsed = true;
        }
        return configuration;
    }

    private void parse(FileConfiguration configuration) {
        this.entries.clear();
        this.byKey.clear();
        this.separator = WeberText.toSection(configuration.getString("separator", "&8&m--------------------"));
        this.showZero = configuration.getBoolean("showZero", false);

        ConfigurationSection section = configuration.getConfigurationSection("entries");
        List<String> order = configuration.getStringList("order");
        if (order.isEmpty() && section != null) {
            order = new ArrayList<>(section.getKeys(false));
        }
        for (String key : order) {
            ConfigurationSection entry = section == null ? null : section.getConfigurationSection(key);
            String format = entry == null ? null : entry.getString("format");
            if (format == null || format.isEmpty()) {
                format = "&7" + key + " &f+%value%";
            }
            boolean percent = entry != null ? entry.getBoolean("percent", isPercentByDefault(key)) : isPercentByDefault(key);
            int decimals = entry != null ? entry.getInt("decimals", -1) : -1;
            // Keep the configured spelling: attribute maps are keyed by camelCase names.
            Entry rule = new Entry(key, format, percent, decimals);
            this.entries.add(rule);
            this.byKey.put(key.toLowerCase(Locale.ROOT), rule);
        }
        if (this.entries.isEmpty()) {
            weberConfig.showPublicMsg("[警告]lore.yml 未配置任何属性显示规则，物品将不会自动生成属性 Lore");
        }
    }

    public String getSeparator() {
        return this.separator;
    }

    public boolean isShowZero() {
        return this.showZero;
    }

    public boolean hasEntry(String key) {
        getCustomConfig();
        return this.byKey.containsKey(key.toLowerCase(Locale.ROOT));
    }

    /** Renders all non-zero attributes into coloured lore lines, in configuration order. */
    public List<String> buildLines(Map<String, Double> attributes) {
        getCustomConfig();
        List<String> lines = new ArrayList<>();
        if (attributes == null) {
            return lines;
        }
        for (Entry entry : this.entries) {
            Double value = attributes.get(entry.key);
            if (value == null) {
                continue;
            }
            if (!this.showZero && Math.abs(value) < 1.0E-9D) {
                continue;
            }
            lines.add(entry.render(value));
        }
        return lines;
    }

    /** Renders a single attribute without colour codes (used by {@code /wrl list}). */
    public String describe(String key, double value) {
        getCustomConfig();
        Entry entry = this.byKey.get(key.toLowerCase(Locale.ROOT));
        if (entry == null) {
            return key + ": " + formatNumber(value, -1);
        }
        return stripColour(entry.render(value));
    }

    public List<String> getOrderedKeys() {
        getCustomConfig();
        List<String> keys = new ArrayList<>(this.entries.size());
        for (Entry entry : this.entries) {
            keys.add(entry.key);
        }
        return keys;
    }

    private static final Pattern SECTION_CODES = Pattern.compile("(?i)\u00a7[0-9a-fk-orx]");

    static String stripColour(String text) {
        return text == null ? "" : SECTION_CODES.matcher(text).replaceAll("");
    }

    static String formatNumber(double value, int decimals) {        if (decimals >= 0) {
            return BigDecimal.valueOf(value).setScale(decimals, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
        }
        if (Math.abs(value - Math.rint(value)) < 1.0E-9D) {
            return String.valueOf((long) Math.rint(value));
        }
        return BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

    /** Attributes whose raw value is a 0..1 ratio and therefore reads better as a percentage. */
    private static boolean isPercentByDefault(String key) {
        switch (key.toLowerCase(Locale.ROOT)) {
            case "crit":
            case "critmul":
            case "stealhealth":
            case "currententityhealthdamage":
            case "maxentityhealthdamage":
            case "outputdamage":
            case "angerdamage":
            case "regurgitationcurrenthealthdamage":
            case "backstab":
            case "executedamage":
            case "annihilation":
            case "playerkiller":
            case "tenacity":
            case "increasesdamageontheground":
            case "increasedefenseontheground":
            case "armorincrease":
            case "armordecrease":
            case "injuryreduction":
            case "giantdamage":
            case "meleedamage":
            case "rangeddamage":
                return true;
            default:
                return false;
        }
    }
}
