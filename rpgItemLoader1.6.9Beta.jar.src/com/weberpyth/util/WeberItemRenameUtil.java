/*    */ package com.weberpyth.util;
/*    */ 
/*    */ import com.weberpyth.item.WeberRPGItemInfo;
/*    */ import org.bukkit.inventory.ItemFlag;
/*    */ import org.bukkit.inventory.ItemStack;
/*    */ import org.bukkit.inventory.meta.ItemMeta;
/*    */ 
/*    */ public class WeberItemRenameUtil
/*    */ {
/* 10 */   public static ItemMeta iMeta = null;
/*    */   public static void setWeberRPGItemInfo(ItemStack im, WeberRPGItemInfo wri) {
/* 12 */     iMeta = im.getItemMeta();
/*    */ 
/*    */ 
/*    */ 
/*    */     
/* 17 */     iMeta.setDisplayName(wri.getName());
/* 18 */     if (wri.getLores() != null && wri.getLores().size() > 0) {
/* 19 */       iMeta.setLore(wri.getLores());
/*    */     }
/*    */     
/*    */     try {
/* 23 */       iMeta.addItemFlags(new ItemFlag[] { ItemFlag.HIDE_ATTRIBUTES });
/* 24 */     } catch (NoClassDefFoundError noClassDefFoundError) {}
/*    */ 
/*    */     
/* 27 */     im.setItemMeta(iMeta);
/* 28 */     iMeta = null;
/*    */   }
/*    */   public static String getDisplayName(ItemStack im) {
/* 31 */     iMeta = im.getItemMeta();
/* 32 */     return iMeta.getDisplayName();
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyt\\util\WeberItemRenameUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */