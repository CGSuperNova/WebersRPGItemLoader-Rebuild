/*    */ package com.weberpyth.weberBuffData;
/*    */ 
/*    */ 
/*    */ public class WeberBuffEntity
/*    */ {
/*    */   public WeberBuffModel weberBuffModel;
/*    */   public Long timer;
/*    */   
/*    */   public void setTimer(Long t) {
/* 10 */     this.timer = t;
/*    */   }
/*    */   
/*    */   public boolean isActive() {
/* 14 */     if (this.timer.longValue() < 0L) {
/* 15 */       return true;
/*    */     }
/* 17 */     if (((Double)this.weberBuffModel.attributes.get("length")).doubleValue() < 0.0D) {
/* 18 */       return true;
/*    */     }
/* 20 */     return (this.timer.longValue() + ((Double)this.weberBuffModel.attributes.get("length")).doubleValue() > System.currentTimeMillis());
/*    */   }
/*    */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\webersBuff.jar!\com\weberpyth\weberBuffData\WeberBuffEntity.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */