package com.weberpyth.controller;

import com.weberpyth.config.WeberItemConfig;
import com.weberpyth.config.WeberLoreConfig;
import com.weberpyth.config.weberConfig;
import com.weberpyth.item.WeberRPGItem;
import com.weberpyth.item.WeberRPGItemInfo;
import com.weberpyth.nbtEditor.WeberNBTEditor;
import com.weberpyth.rpgitemloader.rpgitemloader;
import com.weberpyth.util.LegacyItemUtil;
import com.weberpyth.util.WeberEquipmentUtil;
import com.weberpyth.util.WeberItemLoreUtil;
import com.weberpyth.util.WeberItemRenameUtil;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CommandController {

    private static final String ADMIN_PERMISSION = "WeberRpgitemLoader.admin";
    private static final String CONVERT_PERMISSION = "WeberRpgitemLoader.convert";

    public CommandSender sender;
    public Command command;
    public String label;
    public String[] args;
    public rpgitemloader plugin;

    private HashMap<String, WeberRPGItem> items;
    private HashMap<String, WeberRPGItemInfo> itemInfo;
    private WeberLoreConfig loreConfig;

    public CommandController(rpgitemloader plugin) {
        this.plugin = plugin;
    }

    public HashMap<String, WeberRPGItem> getItems() {
        return this.items;
    }

    public void setItems(HashMap<String, WeberRPGItem> items) {
        this.items = items;
    }

    public HashMap<String, WeberRPGItemInfo> getItemInfo() {
        return this.itemInfo;
    }

    public void setItemInfo(HashMap<String, WeberRPGItemInfo> itemInfo) {
        this.itemInfo = itemInfo;
    }

    public WeberLoreConfig getLoreConfig() {
        return this.loreConfig;
    }

    public void setLoreConfig(WeberLoreConfig loreConfig) {
        this.loreConfig = loreConfig;
    }

    public void setCommand(CommandSender sender, Command command, String label, String[] args, rpgitemloader plugin) {
        this.sender = sender;
        this.command = command;
        this.label = label;
        this.args = args == null ? new String[0] : args;
        this.plugin = plugin;
    }

    private boolean hasArg(int index) {
        return this.args != null && this.args.length > index;
    }

    private String arg(int index) {
        return this.args[index];
    }

    private ItemStack mainHand(Player player) {
        EntityEquipment equipment = player.getEquipment();
        return equipment == null ? null : WeberEquipmentUtil.getItemInMainHand(equipment);
    }

    // ------------------------------------------------------------------ routing

    public boolean response() {
        String name = this.command.getName();
        if (name.equalsIgnoreCase("WRL")) {
            return root();
        }
        if (name.equalsIgnoreCase("wrlLBI") && this.sender instanceof Player) {
            return applyItemTemplate((Player) this.sender);
        }
        if (name.equalsIgnoreCase("wrlLII") && this.sender instanceof Player) {
            return applyInfoTemplate((Player) this.sender);
        }
        if (name.equalsIgnoreCase("wrlCheck") && this.sender instanceof Player) {
            return check();
        }
        if (name.equalsIgnoreCase("wrlJump") && this.sender instanceof Player) {
            return jump((Player) this.sender);
        }
        if (name.equalsIgnoreCase("wrlReloadConf")) {
            if (!this.sender.hasPermission(ADMIN_PERMISSION)) {
                this.sender.sendMessage("§c你没有权限执行该命令");
                return true;
            }
            return reload();
        }
        this.sender.sendMessage("§c未知命令，使用 /wrl help 查看帮助");
        return true;
    }

    /** {@code /WRL ...} root command. */
    private boolean root() {
        if (!hasArg(0)) {
            sendHelp();
            return true;
        }
        String sub = arg(0).toLowerCase(Locale.ROOT);
        switch (sub) {
            case "list":
                return list();
            case "info":
                return info();
            case "create":
                return create();
            case "delete":
                return delete();
            case "edit":
                return edit();
            case "convert":
                return convert();
            case "reload":
                if (!this.sender.hasPermission(ADMIN_PERMISSION)) {
                    this.sender.sendMessage("§c你没有权限执行该命令");
                    return true;
                }
                return reload();
            case "help":
            default:
                sendHelp();
                return true;
        }
    }

    private void sendHelp() {
        this.sender.sendMessage("§6§lWeber's RPGItem Loader §7v" + this.plugin.getPluginMeta().getVersion());
        this.sender.sendMessage("§e/wrl list §7- 列出全部物品模板名称");
        this.sender.sendMessage("§e/wrl info <物品ID> §7- 查看模板的详细属性");
        this.sender.sendMessage("§e/wrl create <物品ID> §7- 新建空模板");
        this.sender.sendMessage("§e/wrl delete <物品ID> §7- 删除模板");
        this.sender.sendMessage("§e/wrl edit <物品ID> addAttribute <属性> <数值> §7- 设置属性");
        this.sender.sendMessage("§e/wrl edit <物品ID> removeAttribute <属性> §7- 移除属性");
        this.sender.sendMessage("§e/wrl convert §8[all] §7- 把旧版物品转换为新版");
        this.sender.sendMessage("§e/wrl reload §7- 重载全部配置");
        this.sender.sendMessage("§e/wrlLBI <物品ID> §7- 给手持物品打上战斗属性");
        this.sender.sendMessage("§e/wrlLII <显示ID> §7- 给手持物品应用名称与 Lore");
        this.sender.sendMessage("§e/wrlCheck §7- 查看手持物品的战斗属性");
        this.sender.sendMessage("§e/wrlJump <高度> §7- 调试用跳跃");
    }

    private boolean reload() {
        boolean ok = this.plugin.reloadPlugin();
        this.sender.sendMessage(ok ? "§aWeber's RPGItem Loader 已重载" : "§c重载失败，请查看控制台");
        return true;
    }

    // ------------------------------------------------------------------ list

    private boolean list() {
        if (!this.sender.hasPermission(ADMIN_PERMISSION)) {
            this.sender.sendMessage("§c你没有权限查看物品列表");
            return true;
        }
        if (this.items == null || this.items.isEmpty()) {
            this.sender.sendMessage("§e当前没有配置任何物品模板 (items.yml)");
            return true;
        }
        this.sender.sendMessage("§6§m--------§r §6Weber 物品模板 §7(" + this.items.size() + ") §6§m--------");
        for (String id : new TreeSet<>(this.items.keySet())) {
            this.sender.sendMessage("§e- §f" + id);
        }
        this.sender.sendMessage("§7使用 /wrl info <物品ID> 查看详细信息");
        return true;
    }

    private boolean info() {
        if (!this.sender.hasPermission(ADMIN_PERMISSION)) {
            this.sender.sendMessage("§c你没有权限查看物品信息");
            return true;
        }
        if (!hasArg(1)) {
            this.sender.sendMessage("§c用法: /wrl info <物品ID>");
            return true;
        }
        return infoDetail(arg(1));
    }

    private boolean infoDetail(String id) {
        WeberRPGItem item = this.items.get(id);
        if (item == null) {
            this.sender.sendMessage("§c没有此物品模板: §f" + id);
            return true;
        }
        this.sender.sendMessage("§6§m------§r §6物品 §f" + id + " §6§m------");
        if (item.innerName != null && !item.innerName.isEmpty()) {
            this.sender.sendMessage("§7内部名称: §f" + item.innerName);
        }
        WeberRPGItemInfo info = this.itemInfo == null ? null : this.itemInfo.get(id);
        if (info != null) {
            if (info.name != null) {
                this.sender.sendMessage("§7显示名称: §f" + info.name);
            }
            if (info.lores != null && !info.lores.isEmpty()) {
                this.sender.sendMessage("§7显示 Lore:");
                for (String line : info.lores) {
                    this.sender.sendMessage("  §8| §r" + line);
                }
            }
        } else {
            this.sender.sendMessage("§8(info.yml 中没有对应的显示模板)");
        }
        this.sender.sendMessage("§7战斗属性:");
        int shown = 0;
        for (String key : orderedAttributeKeys(item)) {
            Double value = item.attributes.get(key);
            if (value == null) {
                continue;
            }
            if (this.loreConfig != null && !this.loreConfig.isShowZero() && Math.abs(value) < 1.0E-9D) {
                continue;
            }
            this.sender.sendMessage("  §8| §f" + (this.loreConfig == null
                    ? key + ": " + value
                    : this.loreConfig.describe(key, value)));
            shown++;
        }
        if (shown == 0) {
            this.sender.sendMessage("  §8| §7(全部为 0)");
        }
        this.sender.sendMessage(Component.text("使用 /wrlLBI " + id + " 应用到手持物品", NamedTextColor.GRAY));
        return true;
    }

    private List<String> orderedAttributeKeys(WeberRPGItem item) {
        Set<String> keys = new LinkedHashSet<>();
        if (this.loreConfig != null) {
            keys.addAll(this.loreConfig.getOrderedKeys());
        }
        keys.addAll(item.attributes.keySet());
        return new ArrayList<>(keys);
    }

    // ------------------------------------------------------------------ template editing

    private static final java.util.regex.Pattern TEMPLATE_ID = java.util.regex.Pattern.compile("[A-Za-z0-9_-]{1,32}");

    private boolean create() {
        if (!this.sender.hasPermission(ADMIN_PERMISSION)) {
            this.sender.sendMessage("§c你没有权限创建物品模板");
            return true;
        }
        if (!hasArg(1)) {
            this.sender.sendMessage("§c用法: /wrl create <物品ID>");
            return true;
        }
        String id = arg(1).trim();
        if (!TEMPLATE_ID.matcher(id).matches()) {
            this.sender.sendMessage("§c物品ID只能包含字母、数字、下划线和短横线，长度 1-32");
            return true;
        }
        if (this.items != null && this.items.containsKey(id)) {
            this.sender.sendMessage("§c物品模板已存在: §f" + id);
            return true;
        }
        if (!this.plugin.createItemTemplate(id)) {
            this.sender.sendMessage("§c创建失败，请查看控制台");
            return true;
        }
        this.sender.sendMessage("§a已创建物品模板 §f" + id + " §7(所有属性为 0)");
        this.sender.sendMessage("§7使用 §f/wrl edit " + id + " addAttribute <属性> <数值> §7配置属性");
        return true;
    }

    private boolean delete() {
        if (!this.sender.hasPermission(ADMIN_PERMISSION)) {
            this.sender.sendMessage("§c你没有权限删除物品模板");
            return true;
        }
        if (!hasArg(1)) {
            this.sender.sendMessage("§c用法: /wrl delete <物品ID>");
            return true;
        }
        String id = arg(1).trim();
        if (this.items == null || !this.items.containsKey(id)) {
            this.sender.sendMessage("§c没有此物品模板: §f" + id);
            return true;
        }
        if (!this.plugin.deleteItemTemplate(id)) {
            this.sender.sendMessage("§c删除失败，请查看控制台");
            return true;
        }
        this.sender.sendMessage("§a已删除物品模板 §f" + id);
        this.sender.sendMessage("§7注意：已经带有该模板标签的物品会失去属性效果");
        return true;
    }

    private boolean edit() {
        if (!this.sender.hasPermission(ADMIN_PERMISSION)) {
            this.sender.sendMessage("§c你没有权限修改物品模板");
            return true;
        }
        if (!hasArg(3)) {
            this.sender.sendMessage("§c用法: /wrl edit <物品ID> <addAttribute|removeAttribute> <属性> [数值]");
            return true;
        }
        String id = arg(1).trim();
        if (this.items == null || !this.items.containsKey(id)) {
            this.sender.sendMessage("§c没有此物品模板: §f" + id);
            return true;
        }
        String operation = arg(2).toLowerCase(Locale.ROOT);
        String attribute = WeberItemConfig.canonicalAttribute(arg(3));
        if (attribute == null) {
            this.sender.sendMessage("§c未知属性: §f" + arg(3));
            this.sender.sendMessage("§7可用属性: §f" + String.join("§7, §f", WeberItemConfig.ATTRIBUTE_KEYS));
            return true;
        }
        if (operation.equals("addattribute")) {
            if (!hasArg(4)) {
                this.sender.sendMessage("§c用法: /wrl edit <物品ID> addAttribute <属性> <数值>");
                return true;
            }
            double value;
            try {
                value = Double.parseDouble(arg(4));
            } catch (NumberFormatException e) {
                this.sender.sendMessage("§c数值无效: §f" + arg(4));
                return true;
            }
            if (!Double.isFinite(value)) {
                this.sender.sendMessage("§c数值无效: §f" + arg(4));
                return true;
            }
            if (!this.plugin.setItemAttribute(id, attribute, value)) {
                this.sender.sendMessage("§c修改失败，请查看控制台");
                return true;
            }
            this.sender.sendMessage("§a已设置 §f" + id + " §7的 §f" + attribute + " §7= §f"
                    + WeberLoreConfig.formatNumber(value, -1));
            return true;
        }
        if (operation.equals("removeattribute")) {
            if (!this.plugin.removeItemAttribute(id, attribute)) {
                this.sender.sendMessage("§c修改失败，请查看控制台");
                return true;
            }
            this.sender.sendMessage("§a已移除 §f" + id + " §7的 §f" + attribute + " §7(数值归零)");
            return true;
        }
        this.sender.sendMessage("§c未知操作: §f" + arg(2));
        this.sender.sendMessage("§7可用操作: §faddAttribute§7, §fremoveAttribute");
        return true;
    }

    // ------------------------------------------------------------------ convert

    private boolean convert() {
        if (!(this.sender instanceof Player)) {
            this.sender.sendMessage("§c该命令只能由玩家执行");
            return true;
        }
        if (!this.sender.hasPermission(CONVERT_PERMISSION)) {
            this.sender.sendMessage("§c你没有权限转换物品");
            return true;
        }
        Player player = (Player) this.sender;
        boolean all = hasArg(1) && arg(1).equalsIgnoreCase("all");
        if (!all) {
            ItemStack hand = mainHand(player);
            if (hand == null) {
                this.sender.sendMessage("§c请手持一件物品");
                return true;
            }
            LegacyItemUtil.ConvertStatus result = LegacyItemUtil.convert(hand, this.items);
            player.getInventory().setItemInMainHand(hand);
            reportConvertResult(result, 1);
            return true;
        }
        int converted = 0;
        int unsupported = 0;
        ItemStack[] contents = player.getInventory().getContents();
        for (int slot = 0; slot < contents.length; slot++) {
            ItemStack item = contents[slot];
            if (item == null || item.getType().isAir()) {
                continue;
            }
            LegacyItemUtil.ConvertStatus result = LegacyItemUtil.convert(item, this.items);
            if (result == LegacyItemUtil.ConvertStatus.CONVERTED) {
                converted++;
                player.getInventory().setItem(slot, item);
            } else if (result == LegacyItemUtil.ConvertStatus.UNKNOWN_TEMPLATE) {
                unsupported++;
            }
        }
        if (converted > 0) {
            reportConvertResult(LegacyItemUtil.ConvertStatus.CONVERTED, converted);
        } else {
            reportConvertResult(LegacyItemUtil.ConvertStatus.NOT_LEGACY, 0);
        }
        if (unsupported > 0) {
            this.sender.sendMessage("§e另有 §f" + unsupported + " §e件旧版物品的模板已不存在，未转换");
        }
        return true;
    }

    private void reportConvertResult(LegacyItemUtil.ConvertStatus result, int count) {
        switch (result) {
            case CONVERTED:
                this.sender.sendMessage("§a已转换 §f" + count + " §a件物品为新版 Weber 物品");
                break;
            case UNKNOWN_TEMPLATE:
                this.sender.sendMessage("§c旧版物品的模板已不存在，无法转换（请先补回 items.yml）");
                break;
            case INVALID:
                this.sender.sendMessage("§c物品数据异常，无法转换");
                break;
            case NOT_LEGACY:
            default:
                this.sender.sendMessage("§e该物品不是旧版 Weber 物品，无需转换");
                break;
        }
    }

    // ------------------------------------------------------------------ item commands

    private boolean applyItemTemplate(Player player) {
        if (!hasArg(0)) {
            this.sender.sendMessage("§c用法: /wrlLBI <物品ID>");
            return true;
        }
        if (weberConfig.noNBTAPI) {
            this.sender.sendMessage("§c物品标签系统不可用，请检查控制台");
            return true;
        }
        String id = arg(0);
        if (this.items == null || !this.items.containsKey(id)) {
            this.sender.sendMessage("§c没有此物品模板: §f" + id);
            return true;
        }
        ItemStack hand = mainHand(player);
        if (hand == null) {
            this.sender.sendMessage("§c请手持一件物品");
            return true;
        }
        WeberNBTEditor.setItemNBTStringValue(hand, "weber_id", id);
        WeberItemLoreUtil.apply(hand, this.items.get(id), this.loreConfig);
        player.getInventory().setItemInMainHand(hand);
        this.sender.sendMessage("§a已为手持物品应用战斗属性: §f" + id);
        return true;
    }

    private boolean applyInfoTemplate(Player player) {
        if (!hasArg(0)) {
            this.sender.sendMessage("§c用法: /wrlLII <显示ID>");
            return true;
        }
        String id = arg(0);
        WeberRPGItemInfo info = this.itemInfo == null ? null : this.itemInfo.get(id);
        if (info == null) {
            this.sender.sendMessage("§c没有此显示模板: §f" + id);
            return true;
        }
        ItemStack hand = mainHand(player);
        if (hand == null) {
            this.sender.sendMessage("§c请手持一件物品");
            return true;
        }
        if (!weberConfig.noNBTAPI) {
            WeberNBTEditor.setItemNBTStringValue(hand, "weber_info_id", id);
        }
        WeberItemRenameUtil.setWeberRPGItemInfo(hand, info);
        // Re-apply the attribute block so changing the display template does not erase it.
        String itemId = WeberNBTEditor.getItemNBTStringValue(hand, "weber_id");
        if (itemId != null && this.items != null && this.items.containsKey(itemId)) {
            WeberItemLoreUtil.apply(hand, this.items.get(itemId), this.loreConfig);
        }
        player.getInventory().setItemInMainHand(hand);
        this.sender.sendMessage("§a已为手持物品应用显示模板: §f" + id);
        return true;
    }

    private boolean check() {
        Player player = (Player) this.sender;
        ItemStack hand = mainHand(player);
        if (hand == null) {
            this.sender.sendMessage("§c请手持一件物品");
            return true;
        }
        String id = WeberNBTEditor.getItemNBTStringValue(hand, "weber_id");
        if (id == null) {
            LegacyItemUtil.LegacyTags legacy = LegacyItemUtil.read(hand);
            if (legacy != null) {
                this.sender.sendMessage("§e检测到旧版 Weber 物品，请使用 §b/wrl convert §e转换");
            } else {
                this.sender.sendMessage("§7该物品不是 Weber 物品");
            }
            return true;
        }
        showItemMessage(id);
        return true;
    }

    public void showItemMessage(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType().isAir()) {
            return;
        }
        String itemId = WeberNBTEditor.getItemNBTStringValue(itemStack, "weber_id");
        if (itemId != null && !itemId.isEmpty()) {
            showItemMessage(itemId);
        }
    }

    private void showItemMessage(String id) {
        if (this.items == null || id == null) {
            this.sender.sendMessage("§c物品模板尚未加载完成");
            return;
        }
        WeberRPGItem item = this.items.get(id);
        if (item == null) {
            this.sender.sendMessage("§c没有此物品模板: §f" + id);
            return;
        }
        this.sender.sendMessage("§6该物品战斗附加ID: §f" + item.id);
        for (String key : orderedAttributeKeys(item)) {
            Double value = item.attributes.get(key);
            if (value == null) {
                continue;
            }
            if (this.loreConfig != null && !this.loreConfig.isShowZero() && Math.abs(value) < 1.0E-9D) {
                continue;
            }
            this.sender.sendMessage("§8| §f" + (this.loreConfig == null
                    ? key + ": " + value
                    : this.loreConfig.describe(key, value)));
        }
    }

    private boolean jump(Player player) {
        if (!hasArg(0)) {
            this.sender.sendMessage("§c用法: /wrlJump <高度>");
            return true;
        }
        try {
            double height = Double.parseDouble(arg(0));
            player.setVelocity(new Vector(0.0D, height, 0.0D));
        } catch (NumberFormatException e) {
            this.sender.sendMessage("§c高度必须是一个数字");
        }
        return true;
    }

    // ------------------------------------------------------------------ tab completion

    public List<String> tabComplete(CommandSender sender, Command command, String[] args) {
        String name = command.getName().toLowerCase(Locale.ROOT);
        switch (name) {
            case "wrl": {
                if (args.length == 1) {
                    return filter(List.of("list", "info", "create", "delete", "edit",
                            "convert", "reload", "help"), args[0]);
                }
                String sub = args[0].toLowerCase(Locale.ROOT);
                if (args.length == 2) {
                    switch (sub) {
                        case "info":
                        case "delete":
                        case "edit":
                            return filter(itemIds(), args[1]);
                        case "convert":
                            return filter(List.of("all"), args[1]);
                        default:
                            return Collections.emptyList();
                    }
                }
                if (args.length == 3 && sub.equals("edit")) {
                    return filter(List.of("addAttribute", "removeAttribute"), args[2]);
                }
                if (args.length == 4 && sub.equals("edit")) {
                    return filter(attributeKeys(), args[3]);
                }
                if (args.length == 5 && sub.equals("edit") && args[2].equalsIgnoreCase("addAttribute")) {
                    return filter(List.of("0", "1", "5", "10", "100"), args[4]);
                }
                return Collections.emptyList();
            }
            case "wrllbi":
                if (!sender.hasPermission(ADMIN_PERMISSION)) {
                    return Collections.emptyList();
                }
                return args.length == 1 ? filter(itemIds(), args[0]) : Collections.emptyList();
            case "wrllii":
                if (!sender.hasPermission(ADMIN_PERMISSION)) {
                    return Collections.emptyList();
                }
                return args.length == 1 ? filter(infoIds(), args[0]) : Collections.emptyList();
            case "wrljump":
                return args.length == 1 ? filter(List.of("1", "2", "3", "5", "10"), args[0]) : Collections.emptyList();
            default:
                return Collections.emptyList();
        }
    }

    private Set<String> itemIds() {
        return this.items == null ? Collections.emptySet() : new TreeSet<>(this.items.keySet());
    }

    private Set<String> infoIds() {
        return this.itemInfo == null ? Collections.emptySet() : new TreeSet<>(this.itemInfo.keySet());
    }

    private List<String> attributeKeys() {
        return List.of(WeberItemConfig.ATTRIBUTE_KEYS);
    }

    static List<String> filter(Collection<String> options, String prefix) {
        String lower = prefix == null ? "" : prefix.toLowerCase(Locale.ROOT);
        List<String> result = new ArrayList<>();
        for (String option : options) {
            if (option != null && option.toLowerCase(Locale.ROOT).startsWith(lower)) {
                result.add(option);
            }
        }
        Collections.sort(result);
        return result;
    }
}
