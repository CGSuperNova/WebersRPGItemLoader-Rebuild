package com.weberpyth.WeberBuffController;

import com.weberpyth.config.WeberBuffCombatConfig;
import com.weberpyth.config.WeberBuffConfig;
import com.weberpyth.nbtEditor.WeberNBTEditor;
import com.weberpyth.weberBuff.WeberBuff;
import com.weberpyth.weberBuffData.WeberBuffEntity;
import com.weberpyth.weberBuffData.WeberBuffModel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeSet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class CommandController {

    /**
     * Legacy numeric item ids used by {@code /wrbGive <buff> <itemId>} before 1.13.
     * Modern servers identify items by name; numeric ids are still accepted for existing
     * command macros and mapped through this table.
     */
    private static final Map<Integer, Material> LEGACY_ITEM_IDS = Map.ofEntries(
            Map.entry(260, Material.APPLE),
            Map.entry(261, Material.BOW),
            Map.entry(263, Material.COAL),
            Map.entry(264, Material.DIAMOND),
            Map.entry(265, Material.IRON_INGOT),
            Map.entry(266, Material.GOLD_INGOT),
            Map.entry(280, Material.STICK),
            Map.entry(288, Material.FEATHER),
            Map.entry(289, Material.GUNPOWDER),
            Map.entry(322, Material.GOLDEN_APPLE),
            Map.entry(331, Material.REDSTONE),
            Map.entry(339, Material.PAPER),
            Map.entry(340, Material.BOOK),
            Map.entry(341, Material.SLIME_BALL),
            Map.entry(348, Material.GLOWSTONE_DUST),
            Map.entry(352, Material.BONE),
            Map.entry(353, Material.SUGAR),
            Map.entry(368, Material.ENDER_PEARL),
            Map.entry(369, Material.BLAZE_ROD),
            Map.entry(370, Material.GHAST_TEAR),
            Map.entry(371, Material.GOLD_NUGGET),
            Map.entry(373, Material.POTION),
            Map.entry(375, Material.SPIDER_EYE),
            Map.entry(376, Material.FERMENTED_SPIDER_EYE),
            Map.entry(377, Material.BLAZE_POWDER),
            Map.entry(378, Material.MAGMA_CREAM),
            Map.entry(388, Material.EMERALD),
            Map.entry(399, Material.NETHER_STAR),
            Map.entry(403, Material.ENCHANTED_BOOK)
    );

    public CommandSender sender;
    public Command command;
    public String label;
    public String[] args;
    public WeberBuff plugin;

    public CommandController(WeberBuff plugin) {
        this.plugin = plugin;
    }

    public void setCommand(CommandSender sender, Command command, String label, String[] args, WeberBuff plugin) {
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args;
        this.plugin = plugin;
    }

    private boolean hasArg(int index) {
        return this.args != null && this.args.length > index;
    }

    private String arg(int index) {
        return this.args[index].trim();
    }

    public List<String> tabComplete(CommandSender sender, Command command, String[] args) {
        String name = command.getName().toLowerCase(Locale.ROOT);
        switch (name) {
            case "wrbadd":
                return args.length == 1 ? filter(buffIds(), args[0]) : Collections.emptyList();
            case "wrbset":
                if (args.length == 1) {
                    return filter(playerNames(), args[0]);
                }
                if (args.length == 2) {
                    return filter(buffIds(), args[1]);
                }
                return Collections.emptyList();
            case "wrbgive":
                if (args.length == 1) {
                    return filter(buffIds(), args[0]);
                }
                if (args.length == 2) {
                    return filter(materialNames(), args[1]);
                }
                return Collections.emptyList();
            case "wrbface":
                if (args.length <= 2) {
                    return filter(List.of("0.5", "1", "2", "3"), args[args.length - 1]);
                }
                return Collections.emptyList();
            default:
                return Collections.emptyList();
        }
    }

    private Collection<String> buffIds() {
        if (WeberBuffConfig.models == null) {
            return Collections.emptySet();
        }
        return new TreeSet<>(WeberBuffConfig.models.keySet());
    }

    private Collection<String> playerNames() {
        List<String> names = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            names.add(player.getName());
        }
        return names;
    }

    private Collection<String> materialNames() {
        List<String> names = new ArrayList<>();
        for (Material material : Material.values()) {
            if (material.isItem() && !material.isAir() && !material.name().startsWith("LEGACY_")) {
                names.add(material.name().toLowerCase(Locale.ROOT));
            }
        }
        Collections.sort(names);
        return names;
    }

    static List<String> filter(Collection<String> options, String prefix) {
        String lower = prefix == null ? "" : prefix.toLowerCase(Locale.ROOT);
        List<String> result = new ArrayList<>();
        for (String option : options) {
            if (option != null && option.toLowerCase(Locale.ROOT).startsWith(lower)) {
                result.add(option);
                if (result.size() >= 50) {
                    break;
                }
            }
        }
        return result;
    }

    public boolean response() {
        String name = this.command.getName();
        if (name.equalsIgnoreCase("wrbCheck")) {
            return check();
        }
        if (name.equalsIgnoreCase("wrbAdd")) {
            return add();
        }
        if (name.equalsIgnoreCase("wrbSet")) {
            return set();
        }
        if (name.equalsIgnoreCase("wrbGive")) {
            return give();
        }
        if (name.equalsIgnoreCase("wrbFace")) {
            return face();
        }
        return false;
    }

    private boolean check() {
        if (!(this.sender instanceof Player)) {
            this.sender.sendMessage("该命令只能由玩家执行");
            return true;
        }
        Player player = (Player) this.sender;
        HashMap<String, WeberBuffEntity> buffs = WeberBuffCombatConfig.combatInfo.get(player.getName());
        if (buffs == null || buffs.isEmpty()) {
            player.sendMessage("当前没有激活的 Buff");
            return true;
        }
        StringBuilder msg = new StringBuilder("已激活: ");
        for (WeberBuffEntity entity : buffs.values()) {
            if (entity == null || entity.weberBuffModel == null) {
                continue;
            }
            if (entity.isActive()) {
                msg.append(String.format(" %s ", entity.weberBuffModel.name));
            }
        }
        player.sendMessage(msg.toString());
        return true;
    }

    private boolean add() {
        if (!(this.sender instanceof Player)) {
            this.sender.sendMessage("该命令只能由玩家执行");
            return true;
        }
        if (!hasArg(0)) {
            this.sender.sendMessage("用法: /wrbAdd <buffID>");
            return true;
        }
        addBuff(((Player) this.sender).getName(), arg(0), false);
        return true;
    }

    private boolean set() {
        if (!hasArg(1)) {
            this.sender.sendMessage("用法: /wrbSet <玩家> <buffID>");
            return true;
        }
        addBuff(arg(0), arg(1), false);
        return true;
    }

    private boolean give() {
        if (!(this.sender instanceof Player)) {
            WeberBuffConfig.showPublicMsg("wrbGive命令执行者必须为玩家");
            return true;
        }
        if (!hasArg(1)) {
            this.sender.sendMessage("用法: /wrbGive <buffID> <物品ID或材质名>");
            return true;
        }
        String buffId = arg(0);
        if (WeberBuffConfig.models == null || !WeberBuffConfig.models.containsKey(buffId)) {
            this.sender.sendMessage("Buff不存在: " + buffId);
            return true;
        }
        Material material = resolveMaterial(arg(1));
        if (material == null || material.isAir()) {
            this.sender.sendMessage("物品ID错误，请使用材质名（如 PAPER）或旧版数字ID");
            return true;
        }
        ItemStack item = new ItemStack(material);
        WeberNBTEditor.setItemNBTStringValue(item, "weber_buff_id", buffId);
        ((Player) this.sender).getInventory().addItem(item);
        WeberBuffConfig.debugMsg("已发放 Buff 道具 " + buffId + " (" + material + ")");
        return true;
    }

    /** Accepts a modern material name (optionally namespaced) or a legacy numeric item id. */
    private Material resolveMaterial(String value) {
        Material material = Material.matchMaterial(value);
        if (material != null) {
            return material;
        }
        try {
            int id = Integer.parseInt(value);
            return LEGACY_ITEM_IDS.get(id);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private boolean face() {
        if (!(this.sender instanceof LivingEntity)) {
            this.sender.sendMessage("该命令只能由生物执行");
            return true;
        }
        if (!hasArg(1)) {
            this.sender.sendMessage("用法: /wrbFace <垂直力度> <水平力度>");
            return true;
        }
        Entity target = getTargetEntity(this.sender);
        if (target == null) {
            this.sender.sendMessage("面前无实体！");
            return true;
        }
        if (!(target instanceof LivingEntity)) {
            this.sender.sendMessage("面前的目标不是生物，无法推动");
            return true;
        }
        double horizontal;
        double vertical;
        try {
            vertical = Double.parseDouble(arg(0));
            horizontal = Double.parseDouble(arg(1));
        } catch (NumberFormatException e) {
            this.sender.sendMessage("力度必须是数字");
            return true;
        }
        LivingEntity living = (LivingEntity) this.sender;
        Vector sight = living.getEyeLocation().getDirection().normalize();
        final LivingEntity targetLiving = (LivingEntity) target;
        targetLiving.setVelocity(sight.multiply(horizontal));
        final double up = vertical;
        new BukkitRunnable() {
            @Override
            public void run() {
                if (targetLiving.isValid()) {
                    targetLiving.setVelocity(targetLiving.getVelocity().add(new Vector(0.0D, up, 0.0D)));
                }
            }
        }.runTaskLater(this.plugin, 1L);
        return true;
    }

    public void addBuff(String name, String buffId, boolean permanent) {
        if (WeberBuffConfig.models == null) {
            return;
        }
        WeberBuffModel model = WeberBuffConfig.models.get(buffId);
        if (model == null) {
            WeberBuffConfig.debugMsg("Buff不存在: " + buffId);
            return;
        }
        WeberBuffEntity entity = new WeberBuffEntity();
        entity.timer = permanent ? -1L : System.currentTimeMillis();
        entity.weberBuffModel = model;
        if (WeberBuffCombatConfig.combatInfo == null) {
            WeberBuffCombatConfig.combatInfo = new HashMap<>();
        }
        WeberBuffCombatConfig.combatInfo.computeIfAbsent(name, key -> new HashMap<>()).put(model.id, entity);
        WeberBuffConfig.debugMsg(String.format("已成功向 %s 添加buff %s", name, model.name));
    }

    public Entity getTargetEntity(CommandSender sender) {
        if (!(sender instanceof Player)) {
            return null;
        }
        LivingEntity living = (LivingEntity) sender;
        Location senderLocation = living.getLocation();
        Vector sight = living.getEyeLocation().getDirection().normalize();
        Collection<Entity> entities = living.getNearbyEntities(20.0D, 20.0D, 20.0D);
        ArrayList<Entity> candidates = new ArrayList<>();
        for (Entity entity : entities) {
            if (entity.getWorld() != senderLocation.getWorld()) {
                continue;
            }
            if (!living.hasLineOfSight(entity)) {
                continue;
            }
            Vector direction = entity.getLocation().toVector().subtract(senderLocation.toVector());
            double angle = direction.angle(sight);
            if (Double.isNaN(angle) || Math.toDegrees(angle) > 90.0D) {
                continue;
            }
            // Perpendicular distance from the entity to the player's line of sight.
            Vector offset = entity.getLocation().toVector().subtract(senderLocation.toVector());
            double projection = offset.dot(sight);
            Location closest = senderLocation.clone().add(sight.clone().multiply(projection));
            if (entity.getLocation().distance(closest) <= 0.4D) {
                candidates.add(entity);
            }
        }
        Entity result = null;
        double best = Double.MAX_VALUE;
        for (Entity entity : candidates) {
            double distance = entity.getLocation().distance(senderLocation);
            if (distance != 0.0D && distance < best) {
                best = distance;
                result = entity;
            }
        }
        return result;
    }
}
