package com.weberpyth.rpgitemloader;

import com.weberpyth.config.WeberInfoConfig;
import com.weberpyth.config.WeberItemConfig;
import com.weberpyth.config.WeberLoreConfig;
import com.weberpyth.config.weberConfig;
import com.weberpyth.controller.CombatController;
import com.weberpyth.controller.CommandController;
import com.weberpyth.controller.DotController;
import com.weberpyth.controller.ProjectileController;
import com.weberpyth.item.WeberRPGItem;
import com.weberpyth.item.WeberRPGItemInfo;
import com.weberpyth.nbtEditor.WeberNBTEditor;
import com.weberpyth.util.LegacyItemUtil;
import com.weberpyth.util.WeberText;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Weber's RPG Item Loader entry point.
 *
 * <p>Note about 1.20.5+: the server no longer exposes a versioned CraftBukkit package
 * ({@code org.bukkit.craftbukkit.v1_12_R1}), so the old {@code getPackage().getName()} probe
 * threw {@link IllegalStateException} during enable. The plugin is now written against the
 * stable Bukkit API only and no longer needs to know the server version.
 */
public class rpgitemloader extends JavaPlugin implements Listener {

    private static final String DEFAULT_LEGACY_MESSAGE = "&e检测到旧版 Weber 物品（&f%id%&e），请转换后再使用。";

    private CommandController commandController;
    private CombatController combatController;
    private HashMap<String, WeberRPGItem> items;
    private HashMap<String, WeberRPGItemInfo> itemInfo;
    private DotController dotController;
    private WeberLoreConfig loreConfig;

    private final Map<UUID, Long> legacyNotices = new HashMap<>();

    public static rpgitemloader plugin;

    @Override
    public void onEnable() {
        plugin = this;
        // Must happen before getConfig(): on a fresh install there is no config.yml yet and
        // getConfig() would otherwise hand back an empty configuration (all switches defaulted).
        saveDefaultConfig();
        reloadConfig();

        new weberConfig(getConfig());
        weberConfig.showPublicMsg("WRL插件正在启动");

        Bukkit.getPluginManager().registerEvents(this, this);

        weberConfig.showPublicMsg("WRL插件正在加载指令处理器");
        this.commandController = new CommandController(this);

        weberConfig.showPublicMsg("WRL插件正在加载战斗系统");
        this.combatController = new CombatController();

        loadAll();

        // PDC based item tags need no external plugin; the hook stays for API compatibility.
        try {
            WeberNBTEditor.initWeberNBTEditor();
        } catch (Throwable t) {
            weberConfig.showPublicMsg("[警告]前置API未正常工作");
            weberConfig.noNBTAPI = true;
        }

        weberConfig.showPublicMsg("WRL插件正在载入DOT系统");
        this.dotController = new DotController();

        weberConfig.showPublicMsg("WRL插件加载完成!");
        weberConfig.HyperionDebugMSG();
    }

    private void loadAll() {
        weberConfig.showPublicMsg("WRL插件正在加载WRL物品模板数据");
        loadItemTemplates();
        weberConfig.showPublicMsg("WRL插件正在载入显示信息模板数据");
        loadInfoTemplates();
        weberConfig.showPublicMsg("WRL插件正在载入属性 Lore 规则");
        loadLoreConfig();
        CombatController.initCombatController();
    }

    private void loadItemTemplates() {
        WeberItemConfig itemConfig = new WeberItemConfig();
        itemConfig.setPlugin(this);
        itemConfig.setFileName();
        itemConfig.getCustomConfig();
        this.items = itemConfig.itemsParser();
        itemConfig.saveDefaultConfig();
        if (this.combatController != null) {
            this.combatController.setItems(this.items);
        }
        if (this.commandController != null) {
            this.commandController.setItems(this.items);
        }
    }

    private void loadInfoTemplates() {
        WeberInfoConfig infoConfig = new WeberInfoConfig();
        infoConfig.setPlugin(this);
        infoConfig.setFileName();
        infoConfig.getCustomConfig();
        this.itemInfo = infoConfig.itemsParser();
        infoConfig.saveDefaultConfig();
        if (this.commandController != null) {
            this.commandController.setItemInfo(this.itemInfo);
        }
    }

    private void loadLoreConfig() {
        this.loreConfig = new WeberLoreConfig();
        this.loreConfig.setPlugin(this);
        this.loreConfig.setFileName();
        this.loreConfig.getCustomConfig();
        this.loreConfig.saveDefaultConfig();
        if (this.commandController != null) {
            this.commandController.setLoreConfig(this.loreConfig);
        }
    }

    /**
     * Reloads configuration, templates and the lore rules. Used by {@code /wrl reload} and
     * {@code /wrlReloadConf}.
     */
    public boolean reloadPlugin() {
        try {
            reloadConfig();
            new weberConfig(getConfig());
            loadAll();
            WeberNBTEditor.initWeberNBTEditor();
            weberConfig.showPublicMsg("WRL插件重新加载完成!");
            return true;
        } catch (Throwable t) {
            weberConfig.showPublicMsg("[警告]重载失败");
            t.printStackTrace();
            return false;
        }
    }

    @Override
    public void onDisable() {
        this.combatController = null;
        this.commandController = null;
        this.itemInfo = null;
        this.items = null;
        this.dotController = null;
        this.loreConfig = null;
        this.legacyNotices.clear();
        plugin = null;
    }

    /** Item templates loaded from {@code items.yml}; exposed for extensions and integration tests. */
    public HashMap<String, WeberRPGItem> getItems() {
        return this.items;
    }

    /** Display templates loaded from {@code info.yml}. */
    public HashMap<String, WeberRPGItemInfo> getItemInfo() {
        return this.itemInfo;
    }

    public WeberLoreConfig getLoreConfig() {
        return this.loreConfig;
    }

    public CombatController getCombatController() {
        return this.combatController;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (this.commandController == null) {
            sender.sendMessage("§c插件尚未完成初始化，请稍后再试");
            return true;
        }
        this.commandController.setCommand(sender, command, label, args, this);
        return this.commandController.response();
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (this.commandController == null) {
            return Collections.emptyList();
        }
        return this.commandController.tabComplete(sender, command, args);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onBattleDamage(EntityDamageByEntityEvent entityDamageByEntityEvent) {
        if (entityDamageByEntityEvent.isCancelled() || this.combatController == null) {
            return;
        }
        this.combatController.setEntityDamageByEntityEvent(entityDamageByEntityEvent);
        try {
            this.combatController.response();
        } catch (Exception e) {
            weberConfig.showPublicMsg("[警告]战斗处理系统异常");
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDotDamage(EntityDamageEvent entityDamageEvent) {
        if (entityDamageEvent.isCancelled() || this.dotController == null) {
            return;
        }
        this.dotController.setEntityDamageEvent(entityDamageEvent);
        try {
            this.dotController.response();
        } catch (Exception e) {
            weberConfig.showPublicMsg("[警告]DOT系统异常");
            e.printStackTrace();
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onShoot(EntityShootBowEvent entityShootBowEvent) {
        if (entityShootBowEvent.isCancelled()) {
            return;
        }
        if (entityShootBowEvent.getProjectile() instanceof Projectile
                && entityShootBowEvent.getEntity() instanceof org.bukkit.entity.LivingEntity) {
            ProjectileController.debugProjectileInfo((Projectile) entityShootBowEvent.getProjectile(),
                    (org.bukkit.entity.LivingEntity) entityShootBowEvent.getEntity());
        }
    }

    // ------------------------------------------------------------------ legacy item detection

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        notifyLegacy(event.getPlayer(), event.getItem());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemHeld(PlayerItemHeldEvent event) {
        notifyLegacy(event.getPlayer(), event.getPlayer().getInventory().getItem(event.getNewSlot()));
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(this, () -> notifyLegacy(player, player.getInventory().getItemInMainHand()), 40L);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onQuit(PlayerQuitEvent event) {
        this.legacyNotices.remove(event.getPlayer().getUniqueId());
    }

    private void notifyLegacy(Player player, ItemStack item) {
        if (!weberConfig.legacyDetect || player == null || item == null || item.getType().isAir()) {
            return;
        }
        LegacyItemUtil.LegacyTags tags;
        try {
            tags = LegacyItemUtil.read(item);
        } catch (Throwable t) {
            return;
        }
        if (tags == null) {
            return;
        }
        long now = System.currentTimeMillis();
        Long last = this.legacyNotices.get(player.getUniqueId());
        long cooldown = Math.max(0, weberConfig.legacyDetectCooldown) * 1000L;
        if (last != null && now - last < cooldown) {
            return;
        }
        this.legacyNotices.put(player.getUniqueId(), now);

        String id = tags.id() != null ? tags.id() : (tags.infoId() != null ? tags.infoId() : "?");
        String template = weberConfig.legacyMessage == null || weberConfig.legacyMessage.isEmpty()
                ? DEFAULT_LEGACY_MESSAGE
                : weberConfig.legacyMessage;
        Component message = WeberText.deserialize(template.replace("%id%", id));
        Component button = Component.text(" [点击转换]", NamedTextColor.AQUA)
                .clickEvent(ClickEvent.runCommand("/wrl convert"))
                .hoverEvent(HoverEvent.showText(Component.text("把手中的旧版物品转换为新版")));
        player.sendMessage(message.append(button));
    }
}
