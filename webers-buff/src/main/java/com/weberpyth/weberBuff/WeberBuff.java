package com.weberpyth.weberBuff;

import com.weberpyth.WeberBuffController.BuffCombatController;
import com.weberpyth.WeberBuffController.CommandController;
import com.weberpyth.config.WeberBuffCombatConfig;
import com.weberpyth.config.WeberBuffConfig;
import com.weberpyth.config.WeberBuffEchoConfig;
import com.weberpyth.config.WeberBuffLoadConfig;
import com.weberpyth.controller.CombatController;
import com.weberpyth.nbtEditor.WeberNBTEditor;
import com.weberpyth.weberBuffData.WeberBuffModel;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * WebersBuff - the buff half of the Weber RPG item system.
 *
 * <p>This plugin builds on the shared data model and the combat pipeline of
 * {@code WeberRpgitemLoader}, which is therefore declared as a plugin dependency in
 * {@code plugin.yml}. That dependency is also what puts both plugins into the same class loader
 * group, so the shared classes resolve reliably.
 */
public class WeberBuff extends JavaPlugin implements Listener {

    public static WeberBuff plugin;

    /** Guards against registering the combat hook twice when the plugin is reloaded. */
    private static boolean combatHookRegistered = false;

    public HashMap<String, WeberBuffModel> models;

    private WeberBuffLoadConfig weberBuffLoadConfig;
    private WeberBuffCombatConfig weberBuffCombatConfig;
    private WeberBuffEchoConfig weberBuffEchoConfig;
    private CommandController commandController;

    @Override
    public void onEnable() {
        plugin = this;
        saveDefaultConfig();
        reloadConfig();
        new WeberBuffConfig(getConfig());

        Bukkit.getPluginManager().registerEvents(this, this);
        this.commandController = new CommandController(this);
        loadConfigurations();
        registerCombatHook();

        WeberBuffConfig.showPublicMsg("WebersBuff 已启用");
    }

    private void registerCombatHook() {
        if (combatHookRegistered) {
            return;
        }
        try {
            CombatController.beforeCountModsLink.add(new BuffCombatController());
            combatHookRegistered = true;
            WeberBuffConfig.debugMsg("已注册 Buff 战斗判定钩子");
        } catch (Throwable t) {
            // Should not happen when WeberRpgitemLoader is installed, but never let the buff
            // plugin fail to enable because of it.
            WeberBuffConfig.showPublicMsg("[警告]无法接入 WeberRpgitemLoader 战斗系统: " + t);
        }
    }

    private void loadConfigurations() {
        this.weberBuffLoadConfig = new WeberBuffLoadConfig();
        this.weberBuffLoadConfig.setPlugin(this);
        this.weberBuffLoadConfig.setFileName();
        this.weberBuffLoadConfig.getCustomConfig();
        this.models = this.weberBuffLoadConfig.itemsParser();
        this.weberBuffLoadConfig.saveDefaultConfig();
        WeberBuffConfig.setModels(this.models);

        this.weberBuffCombatConfig = new WeberBuffCombatConfig();
        this.weberBuffCombatConfig.setPlugin(this);
        this.weberBuffCombatConfig.setFileName();
        this.weberBuffCombatConfig.getCustomConfig();
        this.weberBuffCombatConfig.saveDefaultConfig();
        this.weberBuffCombatConfig.loadCombatInfo();

        this.weberBuffEchoConfig = new WeberBuffEchoConfig();
        this.weberBuffEchoConfig.setPlugin(this);
        this.weberBuffEchoConfig.setFileName();
        this.weberBuffEchoConfig.getCustomConfig();
        this.weberBuffEchoConfig.echoParser();
        this.weberBuffEchoConfig.saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        if (this.weberBuffCombatConfig != null) {
            this.weberBuffCombatConfig.saveCombatInfo();
        }
        plugin = null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("wrbReload")) {
            // Persist buffs granted since the last save before throwing the in-memory state away.
            if (this.weberBuffCombatConfig != null) {
                this.weberBuffCombatConfig.saveCombatInfo();
            }
            reloadConfig();
            new WeberBuffConfig(getConfig());
            loadConfigurations();
            sender.sendMessage("§aWebersBuff 重载完成");
            return true;
        }
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

    /**
     * Consumes a buff item.
     *
     * <p>The old implementation read the tag through NBT-API (which is gone), keyed players by
     * {@code getDisplayName()} (which is not unique and may be changed by other plugins) and
     * decremented {@code event.getItem()} without writing it back or cancelling the interaction.
     */
    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_AIR && action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        Player player = event.getPlayer();
        ItemStack clicked = event.getItem();
        if (clicked == null || clicked.getType().isAir()) {
            return;
        }
        String buffId = WeberNBTEditor.getItemNBTStringValue(clicked, "weber_buff_id");
        if (buffId == null || buffId.isEmpty()) {
            return;
        }
        WeberBuffConfig.debugMsg("玩家右键事件: " + player.getName() + " 使用 Buff 道具 " + buffId);
        if (this.models == null || !this.models.containsKey(buffId)) {
            WeberBuffConfig.showPublicMsg("[警告]物品上的 Buff ID 不存在: " + buffId);
            return;
        }
        // Do not let the interaction also place a block / use the item.
        event.setCancelled(true);

        if (this.commandController == null) {
            return;
        }
        this.commandController.addBuff(player.getName(), buffId, false);
        consumeOne(player, event.getHand(), clicked);
        broadcastEcho(player, buffId);
    }

    private void consumeOne(Player player, EquipmentSlot hand, ItemStack clicked) {
        if (player.getGameMode() == GameMode.CREATIVE) {
            return;
        }
        if (hand == EquipmentSlot.OFF_HAND) {
            ItemStack offHand = player.getInventory().getItemInOffHand();
            offHand.setAmount(offHand.getAmount() - 1);
            player.getInventory().setItemInOffHand(offHand.getAmount() <= 0 ? null : offHand);
        } else {
            ItemStack mainHand = player.getInventory().getItemInMainHand();
            mainHand.setAmount(mainHand.getAmount() - 1);
            player.getInventory().setItemInMainHand(mainHand.getAmount() <= 0 ? null : mainHand);
        }
    }

    private void broadcastEcho(Player player, String buffId) {
        if (!WeberBuffConfig.publicEcho || WeberBuffEchoConfig.echo == null) {
            return;
        }
        String message = WeberBuffEchoConfig.echo.get(buffId);
        if (message == null) {
            return;
        }
        WeberBuffModel model = this.models.get(buffId);
        message = message.replace("{player}", player.getName());
        if (model != null && model.name != null) {
            message = message.replace("{buffID}", model.name);
        }
        echoMsg(message);
    }

    public void echoMsg(String msg) {
        for (Player online : Bukkit.getServer().getOnlinePlayers()) {
            online.sendMessage(msg);
        }
    }
}
