package com.weberpyth.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.weberpyth.item.WeberRPGItem;
import com.weberpyth.nbtEditor.WeberNBTEditor;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

/**
 * Detects and converts items written by the pre-1.21.11 build of the plugin.
 *
 * <p>The old build stored {@code weber_id} in the item's root NBT. Since Minecraft 1.20.5 the
 * item data fixer moves unknown root tags into the {@code minecraft:custom_data} component, so
 * those items now carry:
 *
 * <pre>components: { "minecraft:custom_data": { weber_id: "test1" } }</pre>
 *
 * Bukkit's {@code PersistentDataContainer} only exposes its own {@code PublicBukkitValues}
 * subtree, so the legacy value is read through {@link org.bukkit.UnsafeValues#serializeItemAsJson}
 * (the data-converter based JSON view of the whole stack). On Paper 1.21.11 that view renders the
 * component as a <em>string</em> of SNBT, e.g.
 * {@code "components":{"minecraft:custom_data":"{weber_id:\"test1\"}"}}, while other versions may
 * render a nested object - both forms are handled.
 */
public final class LegacyItemUtil {

    public static final String KEY_ID = "weber_id";
    public static final String KEY_INFO_ID = "weber_info_id";
    public static final String CUSTOM_DATA = "minecraft:custom_data";

    private static final int MAX_DEPTH = 6;
    private static final Pattern SNBT_ID = snbtPattern(KEY_ID);
    private static final Pattern SNBT_INFO_ID = snbtPattern(KEY_INFO_ID);

    private LegacyItemUtil() {
    }

    /** Legacy tags found on an item. */
    public record LegacyTags(String id, String infoId) {
        public boolean isEmpty() {
            return this.id == null && this.infoId == null;
        }
    }

    /** Result of a conversion attempt. */
    public enum ConvertStatus {
        NOT_LEGACY,
        CONVERTED,
        UNKNOWN_TEMPLATE,
        INVALID
    }

    /**
     * Converts a legacy item in place by writing the new persistent-data tags.
     *
     * <p>The display name and lore are deliberately left untouched: legacy stacks already carry
     * hand-written lore, and re-applying {@code info.yml} would overwrite it. Owners who do want
     * the generated attribute lore can still run {@code /wrlLBI <id>} afterwards.
     *
     * <p>The old {@code custom_data} keys stay in place (harmless: the new tags take precedence and
     * detection ignores converted items).
     */
    public static ConvertStatus convert(ItemStack item, Map<String, WeberRPGItem> items) {
        if (item == null || item.getType().isAir() || item.getAmount() <= 0) {
            return ConvertStatus.INVALID;
        }
        LegacyTags tags = read(item);
        if (tags == null) {
            return ConvertStatus.NOT_LEGACY;
        }
        if (tags.id() != null) {
            if (items == null || !items.containsKey(tags.id())) {
                return ConvertStatus.UNKNOWN_TEMPLATE;
            }
            WeberNBTEditor.setItemNBTStringValue(item, KEY_ID, tags.id());
            if (tags.infoId() != null) {
                WeberNBTEditor.setItemNBTStringValue(item, KEY_INFO_ID, tags.infoId());
            }
            return ConvertStatus.CONVERTED;
        }
        // Info-template-only legacy item: no combat attributes, just re-tag the display id.
        WeberNBTEditor.setItemNBTStringValue(item, KEY_INFO_ID, tags.infoId());
        return ConvertStatus.CONVERTED;
    }

    /**
     * Returns the legacy tags of an item, or {@code null} when the item is not a legacy Weber item
     * (including items that already carry the new persistent data tags).
     */
    public static LegacyTags read(ItemStack item) {
        if (item == null || item.getType().isAir() || item.getAmount() <= 0) {
            return null;
        }
        if (WeberNBTEditor.getItemNBTStringValue(item, KEY_ID) != null
                || WeberNBTEditor.getItemNBTStringValue(item, KEY_INFO_ID) != null) {
            return null;
        }
        String id;
        String infoId;
        try {
            JsonObject json = Bukkit.getUnsafe().serializeItemAsJson(item);
            JsonElement customData = findCustomData(json);
            id = readKey(customData, KEY_ID);
            infoId = readKey(customData, KEY_INFO_ID);
            if (id == null && infoId == null) {
                // Unknown JSON layout: fall back to a bounded search for the key itself.
                JsonObject holder = deepFind(json, KEY_ID, 0);
                if (holder != null) {
                    id = string(holder, KEY_ID);
                }
                holder = deepFind(json, KEY_INFO_ID, 0);
                if (holder != null) {
                    infoId = string(holder, KEY_INFO_ID);
                }
            }
        } catch (Throwable t) {
            return null;
        }
        if (id == null && infoId == null) {
            return null;
        }
        return new LegacyTags(id, infoId);
    }

    /** Locates the {@code minecraft:custom_data} element, accepting both object and SNBT string forms. */
    private static JsonElement findCustomData(JsonObject root) {
        if (root == null) {
            return null;
        }
        JsonElement direct = firstChild(root, CUSTOM_DATA, "custom_data");
        if (direct != null) {
            return direct;
        }
        JsonElement components = root.get("components");
        if (components != null && components.isJsonObject()) {
            return firstChild(components.getAsJsonObject(), CUSTOM_DATA, "custom_data");
        }
        return null;
    }

    private static JsonElement firstChild(JsonObject parent, String... keys) {
        for (String key : keys) {
            JsonElement element = parent.get(key);
            if (element != null) {
                return element;
            }
        }
        return null;
    }

    private static String readKey(JsonElement customData, String key) {
        if (customData == null) {
            return null;
        }
        if (customData.isJsonObject()) {
            return string(customData.getAsJsonObject(), key);
        }
        if (customData.isJsonPrimitive() && customData.getAsJsonPrimitive().isString()) {
            return snbtValue(customData.getAsString(), KEY_ID.equals(key) ? SNBT_ID : SNBT_INFO_ID);
        }
        return null;
    }

    private static Pattern snbtPattern(String key) {
        return Pattern.compile("(?:^|[,{\\s])" + Pattern.quote(key) + "\\s*:\\s*\"((?:\\\\.|[^\"\\\\])*)\"");
    }

    private static String snbtValue(String snbt, Pattern pattern) {
        if (snbt == null) {
            return null;
        }
        Matcher matcher = pattern.matcher(snbt);
        if (!matcher.find()) {
            return null;
        }
        String raw = matcher.group(1);
        return raw.replace("\\\"", "\"").replace("\\\\", "\\");
    }

    /** Depth-limited search for the object that directly contains {@code key}. */
    private static JsonObject deepFind(JsonObject object, String key, int depth) {
        if (object == null || depth > MAX_DEPTH) {
            return null;
        }
        if (object.has(key)) {
            return object;
        }
        for (Map.Entry<String, JsonElement> entry : object.entrySet()) {
            if (entry.getValue().isJsonObject()) {
                JsonObject found = deepFind(entry.getValue().getAsJsonObject(), key, depth + 1);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }

    private static String string(JsonObject object, String key) {
        JsonElement element = object.get(key);
        if (element == null || element.isJsonNull() || !element.isJsonPrimitive()) {
            return null;
        }
        try {
            String value = element.getAsString();
            return value == null || value.isEmpty() ? null : value;
        } catch (RuntimeException e) {
            return null;
        }
    }
}
