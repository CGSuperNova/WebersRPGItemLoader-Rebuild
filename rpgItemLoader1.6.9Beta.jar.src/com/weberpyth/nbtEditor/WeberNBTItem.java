/*     */ package com.weberpyth.nbtEditor;
/*     */ 
/*     */ import de.tr7zw.nbtapi.NBTCompound;
/*     */ import de.tr7zw.nbtapi.NBTContainer;
/*     */ import de.tr7zw.nbtapi.NBTItem;
/*     */ import de.tr7zw.nbtapi.NBTReflectionUtil;
/*     */ import de.tr7zw.nbtapi.utils.nmsmappings.ReflectionMethod;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class WeberNBTItem
/*     */   extends NBTCompound
/*     */ {
/*     */   private ItemStack bukkitItem;
/*     */   private boolean directApply;
/*     */   private ItemStack originalSrcStack;
/*     */   
/*     */   public WeberNBTItem(ItemStack item) {
/*  24 */     this(item, false);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void reloadItem(ItemStack item, boolean directApply) {
/*  31 */     this.originalSrcStack = null;
/*  32 */     setCompound(null);
/*  33 */     if (item != null && item.getType() != Material.AIR) {
/*  34 */       this.directApply = directApply;
/*  35 */       this.bukkitItem = item.clone();
/*  36 */       if (directApply) {
/*  37 */         this.originalSrcStack = item;
/*     */       }
/*     */     } else {
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void reloadItem(ItemStack item) {
/*  46 */     reloadItem(item, false);
/*     */   }
/*     */   
/*     */   public WeberNBTItem(ItemStack item, boolean directApply) {
/*  50 */     super((NBTCompound)null, (String)null);
/*  51 */     this.originalSrcStack = null;
/*  52 */     if (item != null && item.getType() != Material.AIR) {
/*  53 */       this.directApply = directApply;
/*  54 */       this.bukkitItem = item.clone();
/*  55 */       if (directApply) {
/*  56 */         this.originalSrcStack = item;
/*     */       }
/*     */     } else {
/*     */       return;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public Object getCompound() {
/*  65 */     return NBTReflectionUtil.getItemRootNBTTagCompound(ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, new Object[] { this.bukkitItem }));
/*     */   }
/*     */   
/*     */   protected void setCompound(Object compound) {
/*  69 */     Object stack = ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, new Object[] { this.bukkitItem });
/*  70 */     ReflectionMethod.ITEMSTACK_SET_TAG.run(stack, new Object[] { compound });
/*  71 */     this.bukkitItem = (ItemStack)ReflectionMethod.ITEMSTACK_BUKKITMIRROR.run(null, new Object[] { stack });
/*     */   }
/*     */   
/*     */   public void applyNBT(ItemStack item) {
/*  75 */     if (item != null && item.getType() != Material.AIR) {
/*  76 */       NBTItem nbti = new NBTItem(new ItemStack(item.getType()));
/*  77 */       nbti.mergeCompound(this);
/*  78 */       item.setItemMeta(nbti.getItem().getItemMeta());
/*     */     } else {
/*  80 */       throw new NullPointerException("ItemStack can't be null/Air!");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void mergeNBT(ItemStack item) {
/*  85 */     NBTItem nbti = new NBTItem(item);
/*  86 */     nbti.mergeCompound(this);
/*  87 */     item.setItemMeta(nbti.getItem().getItemMeta());
/*     */   }
/*     */   
/*     */   public void mergeCustomNBT(ItemStack item) {
/*  91 */     if (item != null && item.getType() != Material.AIR) {
/*  92 */       ItemMeta meta = item.getItemMeta();
/*  93 */       NBTReflectionUtil.getUnhandledNBTTags(meta).putAll(NBTReflectionUtil.getUnhandledNBTTags(this.bukkitItem.getItemMeta()));
/*  94 */       item.setItemMeta(meta);
/*     */     } else {
/*  96 */       throw new NullPointerException("ItemStack can't be null/Air!");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void clearCustomNBT() {
/* 101 */     ItemMeta meta = this.bukkitItem.getItemMeta();
/* 102 */     NBTReflectionUtil.getUnhandledNBTTags(meta).clear();
/* 103 */     this.bukkitItem.setItemMeta(meta);
/*     */   }
/*     */   
/*     */   public ItemStack getItem() {
/* 107 */     return this.bukkitItem;
/*     */   }
/*     */   
/*     */   protected void setItem(ItemStack item) {
/* 111 */     this.bukkitItem = item;
/*     */   }
/*     */   
/*     */   public boolean hasNBTData() {
/* 115 */     return (getCompound() != null);
/*     */   }
/*     */   
/*     */   public static NBTContainer convertItemtoNBT(ItemStack item) {
/* 119 */     return NBTReflectionUtil.convertNMSItemtoNBTCompound(ReflectionMethod.ITEMSTACK_NMSCOPY.run(null, new Object[] { item }));
/*     */   }
/*     */   
/*     */   public static ItemStack convertNBTtoItem(NBTCompound comp) {
/* 123 */     return (ItemStack)ReflectionMethod.ITEMSTACK_BUKKITMIRROR.run(null, new Object[] { NBTReflectionUtil.convertNBTCompoundtoNMSItem(comp) });
/*     */   }
/*     */   
/*     */   protected void saveCompound() {
/* 127 */     if (this.directApply)
/* 128 */       applyNBT(this.originalSrcStack); 
/*     */   }
/*     */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\nbtEditor\WeberNBTItem.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */