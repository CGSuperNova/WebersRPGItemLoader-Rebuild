package com.weberpyth.util;

import com.weberpyth.item.WeberRPGItemInfo;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Applies the display template (name + lore) to an item.
 *
 * <p>Uses Adventure components - the String based {@code setDisplayName}/{@code setLore} methods
 * are deprecated for removal and also lose hover/click data. Both {@code §} and {@code &} colour
 * syntax are accepted, see {@link WeberText}.
 */
public final class WeberItemRenameUtil {

    private WeberItemRenameUtil() {
    }

    public static void setWeberRPGItemInfo(ItemStack item, WeberRPGItemInfo info) {
        if (item == null || item.getType().isAir() || info == null) {
            return;
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return;
        }
        if (info.getName() != null) {
            meta.displayName(WeberText.deserialize(info.getName()));
        }
        if (info.getLores() != null && !info.getLores().isEmpty()) {
            meta.lore(WeberText.deserialize(info.getLores()));
        }
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(meta);
    }

    public static String getDisplayName(ItemStack item) {
        if (item == null || item.getType().isAir()) {
            return "";
        }
        ItemMeta meta = item.getItemMeta();
        if (meta == null || !meta.hasDisplayName()) {
            return "";
        }
        Component displayName = meta.displayName();
        return WeberText.serialize(displayName);
    }
}
