package com.weberpyth.util;

import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Projectile;
import org.bukkit.projectiles.ProjectileSource;

public final class WeberEntityUtil {

    private WeberEntityUtil() {
    }

    /**
     * Maximum health of a living entity.
     *
     * <p>1.21.3 renamed the attribute constants: {@code GENERIC_MAX_HEALTH} became
     * {@link Attribute#MAX_HEALTH}. The old {@code LivingEntity#getMaxHealth()} fallback was
     * removed as well, so a missing attribute now falls back to the vanilla default of 20.
     */
    public static Double getMaxHealth(LivingEntity entity) {
        if (entity == null) {
            return 20.0D;
        }
        AttributeInstance attribute = entity.getAttribute(Attribute.MAX_HEALTH);
        if (attribute != null) {
            return attribute.getValue();
        }
        return 20.0D;
    }

    public static Double getCurrentHealth(LivingEntity entity) {
        return entity == null ? 0.0D : (double) entity.getHealth();
    }

    /**
     * Resolves the living entity that shot a projectile. Returns {@code null} when the shooter is
     * unknown, a block, or not a living entity.
     */
    public static LivingEntity findShooter(Entity entity) {
        if (!(entity instanceof Projectile)) {
            return null;
        }
        ProjectileSource shooter = ((Projectile) entity).getShooter();
        if (shooter instanceof LivingEntity) {
            return (LivingEntity) shooter;
        }
        return null;
    }
}
