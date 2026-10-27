/*     */ package com.weberpyth.weberEntity;
/*     */ import com.weberpyth.weberInterface.WebersVirtualEntity;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.bukkit.EntityEffect;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.Server;
/*     */ import org.bukkit.attribute.Attribute;
/*     */ import org.bukkit.attribute.AttributeInstance;
/*     */ import org.bukkit.block.Block;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.event.entity.EntityDamageEvent;
/*     */ import org.bukkit.event.player.PlayerTeleportEvent;
/*     */ import org.bukkit.metadata.MetadataValue;
/*     */ import org.bukkit.permissions.Permission;
/*     */ import org.bukkit.permissions.PermissionAttachment;
/*     */ import org.bukkit.permissions.PermissionAttachmentInfo;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.potion.PotionEffect;
/*     */ import org.bukkit.potion.PotionEffectType;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class WebersDamageEventVirtualEntity implements Entity, WebersVirtualEntity, LivingEntity {
/*  28 */   String name = "unnamed_webers_virtual_entity";
/*  29 */   String customName = "unnamed weber's virtual entity";
/*     */   
/*     */   public Location getLocation() {
/*  32 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Location getLocation(Location location) {
/*  37 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setVelocity(Vector vector) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Vector getVelocity() {
/*  47 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getHeight() {
/*  52 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getWidth() {
/*  57 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOnGround() {
/*  62 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public World getWorld() {
/*  67 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean teleport(Location location) {
/*  72 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean teleport(Location location, PlayerTeleportEvent.TeleportCause teleportCause) {
/*  77 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean teleport(Entity entity) {
/*  82 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean teleport(Entity entity, PlayerTeleportEvent.TeleportCause teleportCause) {
/*  87 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Entity> getNearbyEntities(double v, double v1, double v2) {
/*  92 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getEntityId() {
/*  97 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getFireTicks() {
/* 102 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getMaxFireTicks() {
/* 107 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFireTicks(int i) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void remove() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isDead() {
/* 122 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isValid() {
/* 127 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendMessage(String s) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void sendMessage(String[] strings) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Server getServer() {
/* 142 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getName() {
/* 147 */     return this.name;
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getPassenger() {
/* 152 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setPassenger(Entity entity) {
/* 157 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Entity> getPassengers() {
/* 162 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addPassenger(Entity entity) {
/* 167 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removePassenger(Entity entity) {
/* 172 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isEmpty() {
/* 177 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean eject() {
/* 182 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public float getFallDistance() {
/* 187 */     return 0.0F;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setFallDistance(float v) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastDamageCause(EntityDamageEvent entityDamageEvent) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityDamageEvent getLastDamageCause() {
/* 202 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public UUID getUniqueId() {
/* 207 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getTicksLived() {
/* 212 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setTicksLived(int i) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void playEffect(EntityEffect entityEffect) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityType getType() {
/* 227 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isInsideVehicle() {
/* 232 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean leaveVehicle() {
/* 237 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getVehicle() {
/* 242 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCustomNameVisible(boolean b) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCustomNameVisible() {
/* 252 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGlowing(boolean b) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isGlowing() {
/* 262 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setInvulnerable(boolean b) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isInvulnerable() {
/* 272 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isSilent() {
/* 277 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setSilent(boolean b) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasGravity() {
/* 287 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGravity(boolean b) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int getPortalCooldown() {
/* 297 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setPortalCooldown(int i) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<String> getScoreboardTags() {
/* 307 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addScoreboardTag(String s) {
/* 312 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean removeScoreboardTag(String s) {
/* 317 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public PistonMoveReaction getPistonMoveReaction() {
/* 322 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public String getCustomName() {
/* 327 */     return this.customName;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setCustomName(String s) {
/* 332 */     this.customName = s;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMetadata(String s, MetadataValue metadataValue) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MetadataValue> getMetadata(String s) {
/* 342 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasMetadata(String s) {
/* 347 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeMetadata(String s, Plugin plugin) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isPermissionSet(String s) {
/* 357 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isPermissionSet(Permission permission) {
/* 362 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasPermission(String s) {
/* 367 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasPermission(Permission permission) {
/* 372 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b) {
/* 377 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public PermissionAttachment addAttachment(Plugin plugin) {
/* 382 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public PermissionAttachment addAttachment(Plugin plugin, String s, boolean b, int i) {
/* 387 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public PermissionAttachment addAttachment(Plugin plugin, int i) {
/* 392 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void removeAttachment(PermissionAttachment permissionAttachment) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void recalculatePermissions() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Set<PermissionAttachmentInfo> getEffectivePermissions() {
/* 407 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOp() {
/* 412 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setOp(boolean b) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setName(String s) {
/* 422 */     this.name = s;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getEyeHeight() {
/* 427 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public double getEyeHeight(boolean b) {
/* 432 */     return 0.0D;
/*     */   }
/*     */ 
/*     */   
/*     */   public Location getEyeLocation() {
/* 437 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Block> getLineOfSight(Set<Material> set, int i) {
/* 442 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public Block getTargetBlock(Set<Material> set, int i) {
/* 447 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<Block> getLastTwoTargetBlocks(Set<Material> set, int i) {
/* 452 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getRemainingAir() {
/* 457 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemainingAir(int i) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaximumAir() {
/* 467 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaximumAir(int i) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int getMaximumNoDamageTicks() {
/* 477 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaximumNoDamageTicks(int i) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public double getLastDamage() {
/* 487 */     return 0.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setLastDamage(double v) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public int getNoDamageTicks() {
/* 497 */     return 0;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setNoDamageTicks(int i) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Player getKiller() {
/* 507 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addPotionEffect(PotionEffect potionEffect) {
/* 512 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addPotionEffect(PotionEffect potionEffect, boolean b) {
/* 517 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean addPotionEffects(Collection<PotionEffect> collection) {
/* 522 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasPotionEffect(PotionEffectType potionEffectType) {
/* 527 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public PotionEffect getPotionEffect(PotionEffectType potionEffectType) {
/* 532 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void removePotionEffect(PotionEffectType potionEffectType) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<PotionEffect> getActivePotionEffects() {
/* 542 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean hasLineOfSight(Entity entity) {
/* 547 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean getRemoveWhenFarAway() {
/* 552 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setRemoveWhenFarAway(boolean b) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public EntityEquipment getEquipment() {
/* 562 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCanPickupItems(boolean b) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean getCanPickupItems() {
/* 572 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isLeashed() {
/* 577 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getLeashHolder() throws IllegalStateException {
/* 582 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean setLeashHolder(Entity entity) {
/* 587 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isGliding() {
/* 592 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setGliding(boolean b) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void setAI(boolean b) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean hasAI() {
/* 607 */     return false;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setCollidable(boolean b) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isCollidable() {
/* 617 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public AttributeInstance getAttribute(Attribute attribute) {
/* 622 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void damage(double v) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void damage(double v, Entity entity) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public double getHealth() {
/* 636 */     return 0.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void setHealth(double v) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public double getMaxHealth() {
/* 646 */     return 0.0D;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void setMaxHealth(double v) {}
/*     */ 
/*     */ 
/*     */   
/*     */   public void resetMaxHealth() {}
/*     */ 
/*     */ 
/*     */   
/*     */   public <T extends org.bukkit.entity.Projectile> T launchProjectile(Class<? extends T> aClass) {
/* 661 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public <T extends org.bukkit.entity.Projectile> T launchProjectile(Class<? extends T> aClass, Vector vector) {
/* 666 */     return null;
/*     */   }
/*     */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\weberEntity\WebersDamageEventVirtualEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */