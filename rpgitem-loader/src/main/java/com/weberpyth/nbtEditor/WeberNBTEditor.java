package com.weberpyth.nbtEditor;

import com.weberpyth.item.WeberRPGItem;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * Item tag access layer.
 *
 * <p>Historically this class wrapped the third-party NBT-API (de.tr7zw) which reached into
 * server internals through reflection. That API stopped working when Minecraft 1.20.5 replaced
 * raw item NBT with data components, and its absence left {@code WeberNBTEditor} unloadable,
 * which in turn produced the {@code ClassNotFoundException} seen in WebersBuff.
 *
 * <p>The implementation now uses Bukkit's official {@link PersistentDataContainer} on the item
 * meta. This is a stable, version independent API and needs no external plugin.
 *
 * <p>Tags live in the {@code weberrpg} namespace, e.g. {@code weberrpg:weber_id}. Item stacks
 * written by the old NBT-API build (raw root NBT) are no longer readable; re-tag them with
 * {@code /wrlLBI} / {@code /wrlLII} or {@code /wrbGive}.
 */
public final class WeberNBTEditor {

    /** Namespace used for every tag written by the Weber RPG item family. */
    public static final String NAMESPACE = "weberrpg";

    /** Kept for binary compatibility with the old NBT-API based build. */
    public static boolean hasNBTAPI = true;

    private static final Map<String, NamespacedKey> KEY_CACHE = new HashMap<>();

    private WeberNBTEditor() {
    }

    /** Resolves (and caches) a tag key inside the {@value #NAMESPACE} namespace. */
    public static NamespacedKey key(String rawKey) {
        if (rawKey == null || rawKey.isEmpty()) {
            throw new IllegalArgumentException("NBT key must not be empty");
        }
        String normalised = rawKey.toLowerCase(Locale.ROOT);
        NamespacedKey cached = KEY_CACHE.get(normalised);
        if (cached != null) {
            return cached;
        }
        NamespacedKey created = NamespacedKey.fromString(NAMESPACE + ":" + normalised);
        if (created == null) {
            throw new IllegalArgumentException("Illegal NBT key: " + rawKey);
        }
        KEY_CACHE.put(normalised, created);
        return created;
    }

    /**
     * No longer needs to probe the server version or a third-party plugin. Kept so existing
     * callers (and WebersBuff) keep working; it only resets the availability flag.
     */
    public static void initWeberNBTEditor() {
        hasNBTAPI = true;
    }

    private static ItemMeta metaOf(ItemStack item) {
        if (item == null || item.getType().isAir() || item.getAmount() <= 0) {
            return null;
        }
        return item.getItemMeta();
    }

    public static void setItemNBTStringValue(ItemStack item, String key, String value) {
        ItemMeta meta = metaOf(item);
        if (meta == null) {
            return;
        }
        if (value == null) {
            meta.getPersistentDataContainer().remove(key(key));
        } else {
            meta.getPersistentDataContainer().set(key(key), PersistentDataType.STRING, value);
        }
        item.setItemMeta(meta);
    }

    public static String getItemNBTStringValue(ItemStack item, String key) {
        ItemMeta meta = metaOf(item);
        if (meta == null) {
            return null;
        }
        PersistentDataContainer container = meta.getPersistentDataContainer();
        return container.get(key(key), PersistentDataType.STRING);
    }

    public static void setItemNBTDoubleValue(ItemStack item, String key, double value) {
        ItemMeta meta = metaOf(item);
        if (meta == null) {
            return;
        }
        meta.getPersistentDataContainer().set(key(key), PersistentDataType.DOUBLE, value);
        item.setItemMeta(meta);
    }

    public static double getItemNBTDoubleValue(ItemStack item, String key) {
        ItemMeta meta = metaOf(item);
        if (meta == null) {
            return 0.0D;
        }
        Double value = meta.getPersistentDataContainer().get(key(key), PersistentDataType.DOUBLE);
        return value == null ? 0.0D : value;
    }

    public static void setItemNBTFromWRPGItem(ItemStack item, WeberRPGItem weberRPGItem) {
        if (weberRPGItem == null) {
            return;
        }
        setItemNBTStringValue(item, "weber_id", weberRPGItem.id);
    }

    /** Returns the item template id stored on the stack, or {@code null} when it is not a Weber item. */
    public static String getWRPGItemId(ItemStack item) {
        return getItemNBTStringValue(item, "weber_id");
    }

    public static WeberRPGItem getWRPGItemFromNBT(ItemStack item) {
        String id = getWRPGItemId(item);
        if (id == null) {
            return null;
        }
        WeberRPGItem weberRPGItem = new WeberRPGItem();
        weberRPGItem.id = id;
        return weberRPGItem;
    }
}
