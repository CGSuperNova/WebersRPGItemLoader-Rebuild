/*     */ package com.weberpyth.controller;
/*     */ 
/*     */ import com.weberpyth.config.weberConfig;
/*     */ import com.weberpyth.item.WeberRPGItem;
/*     */ import com.weberpyth.item.WeberRPGItemInfo;
/*     */ import com.weberpyth.nbtEditor.WeberNBTEditor;
/*     */ import com.weberpyth.rpgitemloader.rpgitemloader;
/*     */ import com.weberpyth.util.WeberEquipmentUtil;
/*     */ import com.weberpyth.util.WeberItemRenameUtil;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Set;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.inventory.meta.ItemMeta;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class CommandController
/*     */ {
/*     */   public CommandSender sender;
/*     */   public Command command;
/*     */   public String label;
/*     */   public String[] args;
/*     */   public rpgitemloader plugin;
/*     */   private HashMap<String, WeberRPGItem> items;
/*     */   private HashMap<String, WeberRPGItemInfo> itemInfo;
/*     */   private WeberRPGItem weberRPGItem;
/*     */   private WeberRPGItemInfo weberRPGItemInfo;
/*     */   private String msg;
/*     */   
/*     */   public HashMap<String, WeberRPGItem> getItems() {
/*  44 */     return this.items;
/*     */   }
/*     */   
/*     */   public void setItems(HashMap<String, WeberRPGItem> items) {
/*  48 */     this.items = items;
/*     */   }
/*     */   
/*     */   public HashMap<String, WeberRPGItemInfo> getItemInfo() {
/*  52 */     return this.itemInfo;
/*     */   }
/*     */   
/*     */   public void setItemInfo(HashMap<String, WeberRPGItemInfo> itemInfo) {
/*  56 */     this.itemInfo = itemInfo;
/*     */   }
/*     */ 
/*     */   
/*     */   public CommandController(rpgitemloader plugin) {
/*  61 */     this.plugin = plugin;
/*     */   }
/*     */   public void setCommand(CommandSender sender, Command command, String label, String[] args, rpgitemloader plugin) {
/*  64 */     this.sender = sender;
/*  65 */     this.command = command;
/*  66 */     this.label = label;
/*  67 */     this.args = args;
/*  68 */     this.plugin = plugin;
/*  69 */     this.weberRPGItem = new WeberRPGItem();
/*     */   }
/*     */   
/*     */   public void showItemMessage(ItemStack itemStack) {
/*  73 */     if (!weberConfig.noNBTAPI) {
/*     */       try {
/*  75 */         String item_id = WeberNBTEditor.getItemNBTStringValue(itemStack, "weber_id");
/*  76 */         showItemMessage(item_id);
/*  77 */       } catch (Throwable throwable) {}
/*     */     }
/*     */ 
/*     */     
/*  81 */     if (weberConfig.loreCheck && this.sender instanceof Player) {
/*  82 */       Set<String> keys = new HashSet<>();
/*  83 */       Player p = (Player)this.sender;
/*  84 */       ItemStack[] eq = { WeberEquipmentUtil.getItemInMainHand(p.getEquipment()), WeberEquipmentUtil.getItemInOffHand(p.getEquipment()), p.getEquipment().getArmorContents()[0], p.getEquipment().getArmorContents()[1], p.getEquipment().getArmorContents()[2], p.getEquipment().getArmorContents()[3] };
/*  85 */       for (ItemStack itemS : eq) {
/*  86 */         if (itemS != null)
/*     */         {
/*     */           
/*  89 */           for (String s : this.items.keySet()) {
/*  90 */             String innerName = ((WeberRPGItem)this.items.get(s)).getInnerName();
/*  91 */             if (innerName == null || innerName.equals("")) {
/*     */               continue;
/*     */             }
/*  94 */             ItemMeta ime = itemS.getItemMeta();
/*  95 */             if (ime == null) {
/*     */               continue;
/*     */             }
/*  98 */             List<String> lore = ime.getLore();
/*  99 */             if (lore == null || lore.size() < 1) {
/*     */               continue;
/*     */             }
/* 102 */             for (String l : lore) {
/*     */               
/* 104 */               if (l.contains(innerName)) {
/* 105 */                 keys.add(s);
/*     */               }
/*     */             } 
/*     */           } 
/*     */         }
/*     */       } 
/* 111 */       String msg = "";
/* 112 */       for (String s : keys) {
/* 113 */         msg = msg + String.format(" [%s]", new Object[] { s });
/*     */       } 
/* 115 */       this.sender.sendMessage("生效的LoreCheck词条： " + msg);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void showItemMessage(String id) {
/* 120 */     this.weberRPGItem = this.items.get(id);
/* 121 */     this.msg = "该物品战斗附加id: " + this.weberRPGItem.id;
/* 122 */     this.sender.sendMessage(this.msg);
/*     */     
/* 124 */     int counter = 0;
/* 125 */     this.msg = "";
/* 126 */     for (String k : this.weberRPGItem.attributes.keySet()) {
/* 127 */       counter++;
/* 128 */       if (counter % 2 == 0) {
/* 129 */         this.sender.sendMessage(this.msg);
/* 130 */         this.msg = "";
/*     */       } 
/* 132 */       this.msg += String.format("[%s]: %.2f  ", new Object[] { k, this.weberRPGItem.attributes.get(k) });
/*     */     } 
/*     */     
/* 135 */     this.sender.sendMessage(this.msg);
/* 136 */     this.sender.sendMessage("以上统计内容未包含loreCheck属性，loreCheck属性独立计算");
/*     */   }
/*     */   
/*     */   public void showItemInfo(String id) {
/* 140 */     this.weberRPGItemInfo = this.itemInfo.get(id);
/* 141 */     this.msg = "该物品信息附加id: " + this.weberRPGItemInfo.id;
/* 142 */     this.sender.sendMessage(this.msg);
/* 143 */     this.msg = "该物品修正名: " + this.weberRPGItemInfo.name;
/* 144 */     this.sender.sendMessage(this.msg);
/* 145 */     for (String s : this.weberRPGItemInfo.lores) {
/* 146 */       this.msg = "物品描述: " + s;
/* 147 */       this.sender.sendMessage(s);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean response() {
/* 153 */     if (this.command.getName().equalsIgnoreCase("WRL") && !(this.sender instanceof Player)) {
/* 154 */       weberConfig.debugMsg("此命令由非玩家发出");
/* 155 */       weberConfig.showPublicMsg("Weber's RPGItem Loader version 1.6.7 Beta");
/* 156 */       return true;
/*     */     } 
/* 158 */     if (this.command.getName().equalsIgnoreCase("wrlLBI") && this.sender instanceof Player) {
/* 159 */       weberConfig.debugMsg("此命令由玩家发出");
/* 160 */       if (weberConfig.noNBTAPI) {
/* 161 */         this.sender.sendMessage("很抱歉，由于前置NBTAPI插件未正确工作，此指令不可用。但战斗系统仍可正常运作，您可正常使用其他扩展插件。");
/* 162 */         return true;
/*     */       } 
/* 164 */       this.msg = "物品" + this.args[0] + "战斗属性附加至手持物品";
/* 165 */       this.sender.sendMessage(this.msg);
/*     */       
/* 167 */       WeberNBTEditor.setItemNBTStringValue(((Player)this.sender).getEquipment().getItemInMainHand(), "weber_id", this.args[0]);
/*     */ 
/*     */ 
/*     */       
/* 171 */       return true;
/*     */     } 
/* 173 */     if (this.command.getName().equalsIgnoreCase("wrlLII") && this.sender instanceof Player) {
/* 174 */       weberConfig.debugMsg("此命令由玩家发出");
/* 175 */       this.msg = "物品" + this.args[0] + "信息属性附加至手持物品";
/* 176 */       this.sender.sendMessage(this.msg);
/*     */       
/* 178 */       if (!weberConfig.noNBTAPI) {
/* 179 */         WeberNBTEditor.setItemNBTStringValue(((Player)this.sender).getEquipment().getItemInMainHand(), "weber_info_id", this.args[0]);
/*     */       }
/*     */       
/* 182 */       WeberItemRenameUtil.setWeberRPGItemInfo(WeberEquipmentUtil.getItemInMainHand(((Player)this.sender).getEquipment()), this.itemInfo.get(this.args[0]));
/*     */       
/* 184 */       showItemInfo(this.args[0]);
/*     */ 
/*     */ 
/*     */       
/* 188 */       return true;
/*     */     } 
/* 190 */     if (this.command.getName().equalsIgnoreCase("wrlCheck") && this.sender instanceof Player) {
/* 191 */       weberConfig.debugMsg("此命令由玩家发出");
/*     */       
/*     */       try {
/* 194 */         showItemMessage(WeberEquipmentUtil.getItemInMainHand(((Player)this.sender).getEquipment()));
/* 195 */         weberConfig.debugMsg("[物品展示名]" + WeberItemRenameUtil.getDisplayName(((Player)this.sender).getEquipment().getItemInMainHand()));
/* 196 */       } catch (Exception e) {
/* 197 */         weberConfig.debugMsg("此物品可能不是WRL插件物品");
/* 198 */         this.sender.sendMessage("此物品可能不是WRL插件物品");
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 203 */       return true;
/*     */     } 
/* 205 */     if (this.command.getName().equalsIgnoreCase("wrlJump") && this.sender instanceof Player) {
/* 206 */       weberConfig.debugMsg("此命令由玩家发出");
/*     */       
/*     */       try {
/* 209 */         double h = Double.parseDouble(this.args[0]);
/* 210 */         ((Player)this.sender).setVelocity(new Vector(0.0D, h, 0.0D));
/*     */       }
/* 212 */       catch (Exception exception) {}
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 217 */       return true;
/*     */     } 
/*     */     
/* 220 */     return false;
/*     */   }
/*     */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\rpgItemLoader1.6.9Beta.jar!\com\weberpyth\controller\CommandController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */