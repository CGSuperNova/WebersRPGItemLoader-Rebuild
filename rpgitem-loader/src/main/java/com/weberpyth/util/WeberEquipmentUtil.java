package com.weberpyth.util;

import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * Equipment helpers with the 1.21 semantics.
 *
 * <p>Since 1.9 empty slots are represented by {@link org.bukkit.Material#AIR} instead of
 * {@code null}, and {@code getItemInHand()} has been removed. Normalising air to {@code null}
 * keeps the combat pipeline's "empty slot" checks working exactly as they did on 1.12.
 */
public final class WeberEquipmentUtil {

    private WeberEquipmentUtil() {
    }

    public static ItemStack getItemInMainHand(EntityEquipment entityEquipment) {
        if (entityEquipment == null) {
            return null;
        }
        return emptyToNull(entityEquipment.getItemInMainHand());
    }

    public static ItemStack getItemInOffHand(EntityEquipment entityEquipment) {
        if (entityEquipment == null) {
            return null;
        }
        return emptyToNull(entityEquipment.getItemInOffHand());
    }

    /** Returns {@code null} for air/empty stacks so callers only deal with real items. */
    public static ItemStack emptyToNull(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir() || itemStack.getAmount() <= 0) {
            return null;
        }
        return itemStack;
    }
}
