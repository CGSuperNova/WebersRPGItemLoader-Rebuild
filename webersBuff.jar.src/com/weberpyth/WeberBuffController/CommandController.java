/*     */ package com.weberpyth.WeberBuffController;
/*     */ 
/*     */ import com.weberpyth.config.WeberBuffCombatConfig;
/*     */ import com.weberpyth.config.WeberBuffConfig;
/*     */ import com.weberpyth.item.WeberRPGItem;
/*     */ import com.weberpyth.nbtEditor.WeberNBTEditor;
/*     */ import com.weberpyth.weberBuff.WeberBuff;
/*     */ import com.weberpyth.weberBuffData.WeberBuffEntity;
/*     */ import com.weberpyth.weberBuffData.WeberBuffModel;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import org.bukkit.Location;
/*     */ import org.bukkit.Material;
/*     */ import org.bukkit.command.Command;
/*     */ import org.bukkit.command.CommandSender;
/*     */ import org.bukkit.entity.Entity;
/*     */ import org.bukkit.entity.LivingEntity;
/*     */ import org.bukkit.entity.Player;
/*     */ import org.bukkit.inventory.ItemStack;
/*     */ import org.bukkit.plugin.Plugin;
/*     */ import org.bukkit.scheduler.BukkitRunnable;
/*     */ import org.bukkit.scheduler.BukkitTask;
/*     */ import org.bukkit.util.Vector;
/*     */ 
/*     */ public class CommandController {
/*     */   public CommandSender sender;
/*     */   public Command command;
/*     */   public String label;
/*     */   public String[] args;
/*     */   public WeberBuff plugin;
/*     */   public WeberRPGItem weberRPGItem;
/*     */   public HashMap<String, WeberBuffModel> models;
/*     */   
/*     */   public void setCommand(CommandSender sender, Command command, String label, String[] args, WeberBuff plugin) {
/*  36 */     this.sender = sender;
/*  37 */     this.command = command;
/*  38 */     this.label = label;
/*  39 */     this.args = args;
/*  40 */     this.plugin = plugin;
/*  41 */     this.weberRPGItem = new WeberRPGItem();
/*     */   }
/*     */   
/*     */   public void setModels(HashMap<String, WeberBuffModel> models) {
/*  45 */     this.models = models;
/*     */   }
/*     */   
/*     */   public CommandController(WeberBuff plugin) {
/*  49 */     this.plugin = plugin;
/*     */   }
/*     */   
/*     */   public boolean response() {
/*  53 */     if (this.command.getName().equalsIgnoreCase("wrbCheck") && this.sender instanceof Player) {
/*  54 */       WeberBuffConfig.debugMsg("此命令由玩家发出");
/*  55 */       String name = ((Player)this.sender).getDisplayName();
/*  56 */       String msg = "已激活: ";
/*     */       
/*  58 */       for (String k : ((HashMap)WeberBuffCombatConfig.combatInfo.get(name)).keySet()) {
/*  59 */         WeberBuffEntity e = (WeberBuffEntity)((HashMap)WeberBuffCombatConfig.combatInfo.get(name)).get(k);
/*     */         try {
/*  61 */           if (e.isActive()) {
/*  62 */             msg = msg + String.format(" %s ", new Object[] { e.weberBuffModel.name });
/*     */           }
/*  64 */         } catch (Exception ex) {
/*  65 */           WeberBuffConfig.debugMsg(String.format("出现异常，玩家名 %s buff对象 %s buff时间 %d", new Object[] { name, e.weberBuffModel, e.timer }));
/*  66 */           ex.printStackTrace();
/*     */         } 
/*     */       } 
/*  69 */       this.sender.sendMessage(msg);
/*  70 */       return true;
/*     */     } 
/*  72 */     if (this.command.getName().equalsIgnoreCase("wrbAdd") && this.sender instanceof Player) {
/*  73 */       WeberBuffConfig.debugMsg("此命令由玩家发出");
/*  74 */       String name = ((Player)this.sender).getDisplayName();
/*  75 */       WeberBuffEntity wbe = new WeberBuffEntity();
/*     */       
/*  77 */       wbe.timer = Long.valueOf(System.currentTimeMillis());
/*     */       
/*  79 */       if (WeberBuffConfig.models.get(this.args[0]) == null) {
/*  80 */         this.sender.sendMessage("没有此buff");
/*  81 */         return false;
/*     */       } 
/*  83 */       wbe.weberBuffModel = (WeberBuffModel)WeberBuffConfig.models.get(this.args[0]);
/*     */ 
/*     */       
/*     */       try {
/*  87 */         if (WeberBuffCombatConfig.combatInfo.containsKey(name)) {
/*  88 */           HashMap<String, WeberBuffEntity> arrayList = (HashMap<String, WeberBuffEntity>)WeberBuffCombatConfig.combatInfo.get(name);
/*  89 */           arrayList.put(wbe.weberBuffModel.id, wbe);
/*  90 */           WeberBuffCombatConfig.combatInfo.put(name, arrayList);
/*     */         } else {
/*  92 */           HashMap<String, WeberBuffEntity> wbM = new HashMap<>();
/*     */           
/*  94 */           wbM.put(wbe.weberBuffModel.id, wbe);
/*  95 */           WeberBuffCombatConfig.combatInfo.put(name, wbM);
/*     */         } 
/*  97 */       } catch (Exception ex) {
/*  98 */         ex.printStackTrace();
/*     */       } 
/* 100 */       WeberBuffConfig.debugMsg(String.format("已成功向 %s 添加buff %s", new Object[] { name, wbe.weberBuffModel.name }));
/* 101 */       return true;
/*     */     } 
/*     */     
/* 104 */     if (this.command.getName().equalsIgnoreCase("wrbSet")) {
/* 105 */       String playerName = this.args[0];
/*     */       try {
/* 107 */         addBuff(playerName, this.args[1], false);
/* 108 */       } catch (Exception e) {
/* 109 */         WeberBuffConfig.debugMsg("[警告]指令异常，请检查指令格式");
/*     */       } 
/*     */     } 
/*     */     
/* 113 */     if (this.command.getName().equalsIgnoreCase("wrbGive")) {
/* 114 */       if (!(this.sender instanceof Player)) {
/* 115 */         WeberBuffConfig.showPublicMsg("wrbGive命令执行者必须为玩家");
/* 116 */         return false;
/*     */       } 
/* 118 */       Integer itemID = Integer.valueOf(0);
/* 119 */       String buffID = this.args[0];
/*     */       try {
/* 121 */         itemID = Integer.valueOf(Integer.parseInt(this.args[1]));
/* 122 */       } catch (Exception e) {
/* 123 */         WeberBuffConfig.debugMsg("物品ID错误");
/* 124 */         this.sender.sendMessage("物品ID错误");
/* 125 */         return false;
/*     */       } 
/* 127 */       if (itemID.intValue() == 0) {
/* 128 */         WeberBuffConfig.debugMsg("物品ID不能为0");
/* 129 */         this.sender.sendMessage("物品ID不能为0");
/* 130 */         return false;
/*     */       } 
/* 132 */       if (WeberBuffConfig.models.get(buffID) == null) {
/* 133 */         this.sender.sendMessage("Buff不存在");
/* 134 */         return false;
/*     */       } 
/* 136 */       ItemStack im = new ItemStack(itemID.intValue());
/* 137 */       WeberNBTEditor.setItemNBTStringValue(im, "weber_buff_id", buffID);
/* 138 */       ((Player)this.sender).getInventory().addItem(new ItemStack[] { im });
/* 139 */       return true;
/*     */     } 
/*     */     
/* 142 */     if (this.command.getName().equalsIgnoreCase("wrbFace")) {
/* 143 */       Entity e = getTargetEntity(this.sender);
/* 144 */       if (e == null) {
/* 145 */         this.sender.sendMessage("面前无实体！");
/* 146 */         return true;
/*     */       } 
/* 148 */       this.sender.sendMessage(String.format("你面前的实体id为 %d", new Object[] { Integer.valueOf(e.getEntityId()) }));
/*     */ 
/*     */       
/* 151 */       Location senderL = ((LivingEntity)this.sender).getLocation();
/*     */       
/* 153 */       Location eyeLocation = ((LivingEntity)this.sender).getEyeLocation();
/*     */       
/* 155 */       Vector sight = eyeLocation.getDirection();
/* 156 */       sight = sight.normalize();
/* 157 */       ((LivingEntity)e).setVelocity(sight.multiply(Double.parseDouble(this.args[1])));
/* 158 */       final Double h = Double.valueOf(Double.parseDouble(this.args[0]));
/* 159 */       final LivingEntity user = (LivingEntity)e;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 165 */       BukkitTask task = (new BukkitRunnable() { public void run() { user.setVelocity(user.getVelocity().add(new Vector(0.0D, h.doubleValue(), 0.0D))); } }).runTaskLater((Plugin)WeberBuff.plugin, 1L);
/* 166 */       return true;
/*     */     } 
/*     */     
/* 169 */     return false;
/*     */   }
/*     */   public void addBuff(String name, String buffID, boolean Inf) {
/* 172 */     WeberBuffEntity wbe = new WeberBuffEntity();
/*     */     
/* 174 */     wbe.timer = Long.valueOf(System.currentTimeMillis());
/* 175 */     if (Inf) {
/* 176 */       wbe.timer = new Long(-1L);
/*     */     }
/*     */     
/* 179 */     if (WeberBuffConfig.models.get(buffID) == null) {
/* 180 */       WeberBuffConfig.debugMsg("Buff不存在");
/*     */       return;
/*     */     } 
/* 183 */     wbe.weberBuffModel = (WeberBuffModel)WeberBuffConfig.models.get(buffID);
/*     */ 
/*     */     
/*     */     try {
/* 187 */       if (WeberBuffCombatConfig.combatInfo.containsKey(name)) {
/* 188 */         HashMap<String, WeberBuffEntity> arrayList = (HashMap<String, WeberBuffEntity>)WeberBuffCombatConfig.combatInfo.get(name);
/* 189 */         arrayList.put(wbe.weberBuffModel.id, wbe);
/* 190 */         WeberBuffCombatConfig.combatInfo.put(name, arrayList);
/*     */       } else {
/* 192 */         HashMap<String, WeberBuffEntity> wbM = new HashMap<>();
/*     */         
/* 194 */         wbM.put(wbe.weberBuffModel.id, wbe);
/* 195 */         WeberBuffCombatConfig.combatInfo.put(name, wbM);
/*     */       } 
/* 197 */     } catch (Exception ex) {
/* 198 */       ex.printStackTrace();
/*     */     } 
/* 200 */     WeberBuffConfig.debugMsg(String.format("已成功向 %s 添加buff %s", new Object[] { name, wbe.weberBuffModel.name }));
/*     */   }
/*     */ 
/*     */   
/*     */   public Entity getTargetEntity(CommandSender sender) {
/* 205 */     if (!(sender instanceof Player)) {
/* 206 */       return null;
/*     */     }
/*     */     
/* 209 */     Location senderL = ((LivingEntity)sender).getLocation();
/*     */     
/* 211 */     Location eyeLocation = ((LivingEntity)sender).getEyeLocation();
/*     */     
/* 213 */     Vector sight = eyeLocation.getDirection();
/*     */     
/* 215 */     List<Entity> entities = ((LivingEntity)sender).getNearbyEntities(20.0D, 20.0D, 20.0D);
/*     */     
/* 217 */     ArrayList<Entity> nearSightEntities = new ArrayList<>();
/*     */     
/* 219 */     for (Entity e : entities) {
/*     */       
/* 221 */       if (!((LivingEntity)sender).hasLineOfSight(e)) {
/*     */         continue;
/*     */       }
/*     */       
/* 225 */       Vector dir = sight.normalize();
/*     */       
/* 227 */       Location eL = e.getLocation();
/*     */       
/* 229 */       Vector v = eL.subtract(senderL).toVector();
/*     */       
/* 231 */       sender.sendMessage(String.format("你们之间夹角的弧度值为%.2f", new Object[] { Float.valueOf(v.angle(sight)) }));
/*     */       
/* 233 */       if (Math.toDegrees(v.angle(sight)) <= 90.0D) {
/* 234 */         sender.sendMessage(String.format("寻获夹角为%.2f度的实体", new Object[] { Double.valueOf(Math.toDegrees(v.angle(sight))) }));
/*     */ 
/*     */ 
/*     */         
/* 238 */         Double[] F = { Double.valueOf(dir.getX()), Double.valueOf(dir.getY()), Double.valueOf(dir.getZ()), Double.valueOf(-(dir.getX() * e.getLocation().getX() + dir.getY() * e.getLocation().getY() + dir.getZ() * e.getLocation().getZ())) };
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
/* 249 */         double t = -(F[0].doubleValue() * senderL.getX() + F[1].doubleValue() * senderL.getY() + F[2].doubleValue() * senderL.getZ() + F[3].doubleValue()) / (F[0].doubleValue() * dir.getX() + F[1].doubleValue() * dir.getY() + F[2].doubleValue() * dir.getZ());
/*     */         
/* 251 */         Location P = new Location(e.getWorld(), dir.getX() * t + senderL.getX(), dir.getY() * t + senderL.getY(), dir.getZ() * t + senderL.getZ());
/*     */         
/* 253 */         sender.sendMessage(String.format("该实体与你的视线的直线距离为%.2f", new Object[] { Double.valueOf(e.getLocation().distance(P)) }));
/* 254 */         if (e.getLocation().distance(P) <= 0.4D) {
/* 255 */           nearSightEntities.add(e);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 260 */     if (nearSightEntities.size() < 1) {
/* 261 */       return null;
/*     */     }
/*     */     
/* 264 */     Entity result = null;
/*     */     
/* 266 */     for (Entity e : nearSightEntities) {
/* 267 */       Location eL = e.getLocation();
/* 268 */       if (result == null) {
/* 269 */         result = e;
/*     */         
/*     */         continue;
/*     */       } 
/* 273 */       if (eL.distance(senderL) < result.getLocation().distance(senderL) && eL.distance(senderL) != 0.0D) {
/* 274 */         result = e;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 279 */     return result;
/*     */   }
/*     */ 
/*     */   
/*     */   public void formCube(Location a, Location b, Material m) {
/* 284 */     int startX = (a.getX() < b.getX()) ? (int)a.getX() : (int)b.getX();
/* 285 */     int endX = (a.getX() > b.getX()) ? (int)a.getX() : (int)b.getX();
/* 286 */     int startY = (a.getY() < b.getY()) ? (int)a.getY() : (int)b.getY();
/* 287 */     int endY = (a.getY() > b.getY()) ? (int)a.getY() : (int)b.getY();
/* 288 */     int startZ = (a.getZ() < b.getZ()) ? (int)a.getZ() : (int)b.getZ();
/* 289 */     int endZ = (a.getZ() > b.getZ()) ? (int)a.getZ() : (int)b.getZ();
/* 290 */     for (int x = startX; x <= endX; x++) {
/* 291 */       for (int y = startY; y <= endY; y++) {
/* 292 */         for (int z = startZ; z <= endZ; z++) {
/* 293 */           Location block = new Location(a.getWorld(), x, y, z);
/* 294 */           block.getBlock().setType(m);
/*     */         } 
/*     */       } 
/*     */     } 
/*     */   }
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void solution(Vector CamForward, Vector Ground) {
/* 323 */     Double z0 = Double.valueOf((-CamForward.getY() * Ground.getX() + Ground.getY() * CamForward.getX()) / (-Ground.getY() * CamForward.getZ() + Ground.getZ() * CamForward.getY()));
/*     */     
/* 325 */     Double y0 = Double.valueOf((-CamForward.getZ() * z0.doubleValue() - CamForward.getX()) / CamForward.getY());
/*     */     
/* 327 */     Double x0 = new Double(1.0D);
/*     */     
/* 329 */     Vector CFG = new Vector(x0.doubleValue(), y0.doubleValue(), z0.doubleValue());
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
/*     */     
/* 341 */     Double z = Double.valueOf((-CamForward.getY() * CFG.getX() + CFG.getY() * CamForward.getX()) / (-CFG.getY() * CamForward.getZ() + CFG.getZ() * CamForward.getY()));
/*     */     
/* 343 */     Double y = Double.valueOf((-CamForward.getZ() * z.doubleValue() - CamForward.getX()) / CamForward.getY());
/*     */     
/* 345 */     Double x = new Double(1.0D);
/*     */     
/* 347 */     Vector LF = new Vector(x.doubleValue(), y.doubleValue(), z.doubleValue());
/*     */ 
/*     */     
/* 350 */     if (LF.angle(Ground) > 1.5707963267948966D)
/* 351 */       LF = new Vector(-x.doubleValue(), -y.doubleValue(), -z.doubleValue()); 
/*     */   }
/*     */ }


/* Location:              D:\Code_Collection\Code_Collection\Minecraft_Development\webersrpgitem\webersBuff.jar!\com\weberpyth\WeberBuffController\CommandController.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */