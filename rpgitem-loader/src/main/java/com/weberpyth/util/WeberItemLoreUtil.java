package com.weberpyth.util;

import com.weberpyth.config.WeberLoreConfig;
import com.weberpyth.item.WeberRPGItem;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Writes the generated attribute block into an item's lore.
 *
 * <p>The block is appended after a marker line taken from {@code lore.yml}. Applying twice
 * replaces the previous block instead of stacking duplicates, and any lore that existed before
 * the marker (for example the display template applied by {@code /wrlLII}) is preserved.
 */
public final class WeberItemLoreUtil {

    private WeberItemLoreUtil() {
    }

    private static List<String> readLore(ItemMeta meta) {
        List<String> lines = new ArrayList<>();
        List<Component> components = meta.lore();
        if (components != null) {
            for (Component component : components) {
                lines.add(WeberText.serialize(component));
            }
        }
        return lines;
    }

    private static void writeLore(ItemMeta meta, List<String> lines) {
        if (lines.isEmpty()) {
            meta.lore(new ArrayList<>());
        } else {
            meta.lore(WeberText.deserialize(lines));
        }
    }

    public static void apply(ItemStack item, WeberRPGItem template, WeberLoreConfig config) {
        if (item == null || item.getType().isAir() || template == null || config == null) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        List<String> lore = readLore(meta);
        String separator = config.getSeparator();
        boolean hadBlock = false;
        if (separator != null && !separator.isEmpty()) {
            for (int i = 0; i < lore.size(); i++) {
                if (separator.equals(lore.get(i))) {
                    lore = new ArrayList<>(lore.subList(0, i));
                    hadBlock = true;
                    break;
                }
            }
        }
        List<String> lines = config.buildLines(template.attributes);
        if (!lines.isEmpty()) {
            while (!lore.isEmpty() && lore.get(lore.size() - 1).isBlank()) {
                lore.remove(lore.size() - 1);
            }
            // Always start the block with the separator so a second application can find and
            // replace it, even when the item had no lore at all before.
            lore.add(separator);
            lore.addAll(lines);
        }
        if (lore.isEmpty() && !hadBlock) {
            return;
        }
        writeLore(meta, lore);
        item.setItemMeta(meta);
    }

    /** Removes a previously generated attribute block, keeping the display lore. */
    public static void remove(ItemStack item, WeberLoreConfig config) {
        if (item == null || item.getType().isAir() || config == null) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        List<String> lore = readLore(meta);
        String separator = config.getSeparator();
        for (int i = 0; i < lore.size(); i++) {
            if (separator.equals(lore.get(i))) {
                writeLore(meta, new ArrayList<>(lore.subList(0, i)));
                item.setItemMeta(meta);
                return;
            }
        }
    }
}
