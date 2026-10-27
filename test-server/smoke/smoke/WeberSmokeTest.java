package smoke;

import com.google.gson.JsonObject;
import com.weberpyth.config.WeberBuffConfig;
import com.weberpyth.config.WeberLoreConfig;
import com.weberpyth.config.weberConfig;
import com.weberpyth.controller.CombatController;
import com.weberpyth.nbtEditor.WeberNBTEditor;
import com.weberpyth.rpgitemloader.rpgitemloader;
import com.weberpyth.util.LegacyItemUtil;
import com.weberpyth.util.WeberItemLoreUtil;
import com.weberpyth.util.WeberItemRenameUtil;
import com.weberpyth.util.WeberText;
import java.util.ArrayList;
import java.util.List;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Zombie;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Throw-away integration test that runs inside the real Paper 1.21.11 server.
 * It verifies the paths that broke during the 1.12 -> 1.21 migration plus the features added in
 * 1.8.0: generated attribute lore, /wrl list, tab completion and legacy item conversion.
 */
public class WeberSmokeTest extends JavaPlugin {

    private int passed;
    private int failed;

    @Override
    public void onEnable() {
        Bukkit.getScheduler().runTaskLater(this, this::runAll, 20L);
        // Failsafe: never leave a test server running if the assertions could not start.
        Bukkit.getScheduler().runTaskLater(this, () -> {
            getLogger().severe("SMOKE TIMEOUT - shutting the test server down");
            Bukkit.shutdown();
        }, 20L * 120);
    }

    private void check(String name, boolean ok, String detail) {
        if (ok) {
            passed++;
            getLogger().info("SMOKE PASS " + name + (detail.isEmpty() ? "" : " (" + detail + ")"));
        } else {
            failed++;
            getLogger().severe("SMOKE FAIL " + name + (detail.isEmpty() ? "" : " (" + detail + ")"));
        }
    }

    private void runAll() {
        try {
            testItemTags();
            testDisplayTemplate();
            testRegistry();
            testLoreGeneration();
            testLegacyConversion();
            testTabCompletion();
            testListCommand();
            testCombatPipeline();
            testReloadCommands();
            testCombatPipeline();
        } catch (Throwable t) {
            failed++;
            getLogger().severe("SMOKE FAIL exception: " + t);
            t.printStackTrace();
        }
        getLogger().info("SMOKE RESULT " + (failed == 0 ? "PASS" : "FAIL")
                + " passed=" + passed + " failed=" + failed);
        Bukkit.getScheduler().runTaskLater(this, Bukkit::shutdown, 20L);
    }

    private void testItemTags() {
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        WeberNBTEditor.setItemNBTStringValue(sword, "weber_id", "test1");
        check("tag-string", "test1".equals(WeberNBTEditor.getItemNBTStringValue(sword, "weber_id")),
                "value=" + WeberNBTEditor.getItemNBTStringValue(sword, "weber_id"));
        check("tag-isolated", WeberNBTEditor.getItemNBTStringValue(sword, "weber_info_id") == null
                && WeberNBTEditor.getItemNBTStringValue(sword, "weber_buff_id") == null, "");
        WeberNBTEditor.setItemNBTDoubleValue(sword, "weber_num", 3.5D);
        check("tag-double", Math.abs(WeberNBTEditor.getItemNBTDoubleValue(sword, "weber_num") - 3.5D) < 1e-9,
                "value=" + WeberNBTEditor.getItemNBTDoubleValue(sword, "weber_num"));
        check("tag-air-safe", WeberNBTEditor.getItemNBTStringValue(new ItemStack(Material.AIR), "weber_id") == null, "");
        check("tag-null-safe", WeberNBTEditor.getItemNBTStringValue(null, "weber_id") == null, "");
        check("tag-meta-persisted", sword.getItemMeta().getPersistentDataContainer().getKeys().size() >= 2,
                "keys=" + sword.getItemMeta().getPersistentDataContainer().getKeys());
    }

    private void testDisplayTemplate() {
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        WeberItemRenameUtil.setWeberRPGItemInfo(item, rpgitemloader.plugin.getItemInfo().get("test1"));
        String name = WeberItemRenameUtil.getDisplayName(item);
        check("display-name-applied", name.contains("破败王者之剑"), "name=" + name);
        List<Component> lore = item.getItemMeta().lore();
        check("display-lore-applied", lore != null && lore.size() == 8,
                "loreLines=" + (lore == null ? 0 : lore.size()));
    }

    private void testRegistry() {
        rpgitemloader loader = rpgitemloader.plugin;
        check("loader-plugin-instance", loader != null, "");
        check("items-loaded", loader != null && loader.getItems() != null && loader.getItems().containsKey("test1"),
                loader == null ? "no plugin" : "ids=" + loader.getItems().keySet());
        check("info-loaded", loader != null && loader.getItemInfo() != null && loader.getItemInfo().containsKey("test1"), "");
        check("lore-config-loaded", loader != null && loader.getLoreConfig() != null, "");
        check("buff-models-loaded", WeberBuffConfig.models != null && WeberBuffConfig.models.containsKey("test1"),
                WeberBuffConfig.models == null ? "null" : "ids=" + WeberBuffConfig.models.keySet());
        check("buff-combat-hook", CombatController.beforeCountModsLink.size() >= 1,
                "size=" + CombatController.beforeCountModsLink.size());
    }

    private void testLoreGeneration() {
        WeberLoreConfig lore = rpgitemloader.plugin.getLoreConfig();
        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);
        WeberItemLoreUtil.apply(item, rpgitemloader.plugin.getItems().get("test1"), lore);
        List<String> lines = loreStrings(item);
        // test1 has damage 15, attackHeal 2, crit 0.05, critMul 1, repel 1 -> five attribute
        // lines plus the separator at the start of the block
        check("lore-lines-generated", lines.size() == 6, "lines=" + lines);
        check("lore-damage-line", lines.stream().anyMatch(l -> l.contains("伤害") && l.contains("+15")),
                "lines=" + lines);
        check("lore-crit-percent", lines.stream().anyMatch(l -> l.contains("暴击率") && l.contains("+5%")),
                "lines=" + lines);
        WeberItemLoreUtil.apply(item, rpgitemloader.plugin.getItems().get("test1"), lore);
        check("lore-idempotent", loreStrings(item).size() == lines.size(), "after=" + loreStrings(item).size());
    }

    private void testLegacyConversion() {
        ItemStack legacy;
        try {
            legacy = createLegacyStack();
        } catch (Throwable t) {
            check("legacy-item-created", false, "cannot create legacy stack: " + t);
            return;
        }
        check("legacy-item-created", legacy != null && legacy.getType() == Material.DIAMOND_SWORD, "");
        if (legacy == null) {
            return;
        }
        LegacyItemUtil.LegacyTags tags = LegacyItemUtil.read(legacy);
        check("legacy-detected", tags != null && "test1".equals(tags.id()), "tags=" + tags);
        if (tags == null) {
            return;
        }
        // Simulate the hand-written name/lore that every legacy item already carries.
        List<String> legacyLoreBefore = loreStrings(legacy);
        String legacyNameBefore = WeberItemRenameUtil.getDisplayName(legacy);
        LegacyItemUtil.ConvertStatus status = LegacyItemUtil.convert(legacy, rpgitemloader.plugin.getItems());
        check("legacy-converted", status == LegacyItemUtil.ConvertStatus.CONVERTED, "status=" + status);
        check("legacy-new-tag", "test1".equals(WeberNBTEditor.getItemNBTStringValue(legacy, "weber_id")), "");
        check("legacy-detection-stops", LegacyItemUtil.read(legacy) == null, "");
        // Legacy items already carry hand-written lore: conversion must not touch name or lore.
        check("legacy-lore-untouched", legacyLoreBefore.equals(loreStrings(legacy)),
                "before=" + legacyLoreBefore + " after=" + loreStrings(legacy));
        check("legacy-name-untouched", legacyNameBefore.equals(WeberItemRenameUtil.getDisplayName(legacy)),
                "before=" + legacyNameBefore + " after=" + WeberItemRenameUtil.getDisplayName(legacy));
    }

    private void testTabCompletion() {
        Command wrl = rpgitemloader.plugin.getCommand("WRL");
        List<String> subcommands = rpgitemloader.plugin.onTabComplete(Bukkit.getConsoleSender(), wrl, "wrl", new String[]{""});
        check("tab-wrl-subcommands", subcommands.contains("list") && subcommands.contains("convert"),
                "subs=" + subcommands);
        Command lbi = rpgitemloader.plugin.getCommand("wrlLBI");
        List<String> itemIds = rpgitemloader.plugin.onTabComplete(Bukkit.getConsoleSender(), lbi, "wrlLBI", new String[]{""});
        check("tab-item-ids", itemIds.contains("test1"), "ids=" + itemIds);
        List<String> filtered = rpgitemloader.plugin.onTabComplete(Bukkit.getConsoleSender(), lbi, "wrlLBI", new String[]{"W2"});
        check("tab-item-filter", filtered.size() == 3 && !filtered.contains("test1"), "ids=" + filtered);
        Command wrbAdd = com.weberpyth.weberBuff.WeberBuff.plugin.getCommand("wrbAdd");
        List<String> buffIds = com.weberpyth.weberBuff.WeberBuff.plugin.onTabComplete(Bukkit.getConsoleSender(), wrbAdd, "wrbAdd", new String[]{""});
        check("tab-buff-ids", buffIds.contains("test1"), "ids=" + buffIds);
    }

    private void testListCommand() {
        Command wrl = rpgitemloader.plugin.getCommand("WRL");
        check("wrl-list", wrl.execute(Bukkit.getConsoleSender(), "WRL", new String[]{"list"}), "");
        check("wrl-list-detail", wrl.execute(Bukkit.getConsoleSender(), "WRL", new String[]{"list", "test1"}), "");
        check("wrl-list-unknown", wrl.execute(Bukkit.getConsoleSender(), "WRL", new String[]{"list", "does-not-exist"}), "");
        check("wrl-help", wrl.execute(Bukkit.getConsoleSender(), "WRL", new String[]{"help"}), "");
        check("wrl-convert-console-guard", wrl.execute(Bukkit.getConsoleSender(), "WRL", new String[]{"convert"}), "");
    }

    /**
     * Builds an item that looks exactly like a pre-1.21.11 stack after the 1.20.5 data upgrade:
     * the old root tag has been moved into {@code components."minecraft:custom_data"}.
     *
     * <p>The base JSON comes from the server itself so fields such as {@code DataVersion} are
     * present; only the custom_data component is injected.
     */
    private ItemStack createLegacyStack() {
        JsonObject template = Bukkit.getUnsafe().serializeItemAsJson(new ItemStack(Material.DIAMOND_SWORD));
        JsonObject components = template.getAsJsonObject("components");
        if (components == null) {
            components = new JsonObject();
            template.add("components", components);
        }
        JsonObject customData = new JsonObject();
        customData.addProperty("weber_id", "test1");
        customData.addProperty("weber_info_id", "test1");
        components.add("minecraft:custom_data", customData);
        ItemStack legacy = Bukkit.getUnsafe().deserializeItemFromJson(template);
        // Every legacy item already has a hand-written name and lore.
        org.bukkit.inventory.meta.ItemMeta meta = legacy.getItemMeta();
        meta.displayName(Component.text("手工命名的旧武器"));
        meta.lore(List.of(Component.text("手工 Lore 第一行"), Component.text("手工 Lore 第二行")));
        legacy.setItemMeta(meta);
        getLogger().info("SMOKE INFO legacy-json=" + Bukkit.getUnsafe().serializeItemAsJson(legacy));
        return legacy;
    }

    private List<String> loreStrings(ItemStack item) {
        List<String> lines = new ArrayList<>();
        List<Component> lore = item.getItemMeta().lore();
        if (lore != null) {
            for (Component component : lore) {
                lines.add(WeberText.serialize(component));
            }
        }
        return lines;
    }

    private void testCombatPipeline() {
        World world = Bukkit.getWorlds().get(0);
        Location spawn = world.getSpawnLocation();
        Zombie attacker = (Zombie) world.spawnEntity(spawn, EntityType.ZOMBIE);
        Zombie victim = (Zombie) world.spawnEntity(spawn.clone().add(3.0D, 0.0D, 0.0D), EntityType.ZOMBIE);
        attacker.setAI(false);
        victim.setAI(false);
        victim.setHealth(20.0D);

        ItemStack weapon = new ItemStack(Material.DIAMOND_SWORD);
        WeberNBTEditor.setItemNBTStringValue(weapon, "weber_id", "test1");
        attacker.getEquipment().setItemInMainHand(weapon);

        // The default config is player-only; allow any living entity for this test.
        weberConfig.playerOnly = false;
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(attacker, victim,
                EntityDamageEvent.DamageCause.ENTITY_ATTACK, 10.0D);
        Bukkit.getPluginManager().callEvent(event);

        // items.yml test1: damage 15, crit 0.05, critMul 1, attackHeal 2.
        double damage = event.getDamage();
        check("combat-damage-applied", damage > 10.0D && damage <= 25.5D, "damage=" + damage);

        ItemStack plain = new ItemStack(Material.DIAMOND_SWORD);
        attacker.getEquipment().setItemInMainHand(plain);
        EntityDamageByEntityEvent plainEvent = new EntityDamageByEntityEvent(attacker, victim,
                EntityDamageEvent.DamageCause.ENTITY_ATTACK, 10.0D);
        Bukkit.getPluginManager().callEvent(plainEvent);
        check("combat-plain-item-unchanged", Math.abs(plainEvent.getDamage() - 10.0D) < 1e-6D,
                "damage=" + plainEvent.getDamage());

        attacker.remove();
        victim.remove();
    }

    private void testReloadCommands() {
        // Commands declared in plugin.yml are registered in the Bukkit CommandMap. Executing the
        // PluginCommand is exactly what happens for a real "/wrlReloadConf"; Server#dispatchCommand
        // is avoided here because Paper resolves it through Brigadier, whose plugin nodes are only
        // populated once a player has logged in (there is no player in this headless test).
        org.bukkit.command.PluginCommand loaderCommand = rpgitemloader.plugin.getCommand("wrlReloadConf");
        check("command-lookup-wrl", loaderCommand != null, "");
        if (loaderCommand != null) {
            check("command-permission-wrl", loaderCommand.testPermission(Bukkit.getConsoleSender()),
                    "permission=" + loaderCommand.getPermission());
            boolean executed = loaderCommand.execute(Bukkit.getConsoleSender(), "wrlReloadConf", new String[0]);
            check("command-execute-wrlReloadConf", executed, "");
        }
        org.bukkit.command.PluginCommand buffCommand = com.weberpyth.weberBuff.WeberBuff.plugin.getCommand("wrbReload");
        check("command-lookup-wrb", buffCommand != null, "");
        if (buffCommand != null) {
            boolean executed = buffCommand.execute(Bukkit.getConsoleSender(), "wrbReload", new String[0]);
            check("command-execute-wrbReload", executed, "");
        }
        check("reload-keeps-items", rpgitemloader.plugin.getItems() != null
                && rpgitemloader.plugin.getItems().containsKey("test1"), "");
        check("reload-keeps-buff-models", WeberBuffConfig.models != null
                && WeberBuffConfig.models.containsKey("test1"), "");
        check("reload-keeps-buff-hook", CombatController.beforeCountModsLink.size() >= 1,
                "size=" + CombatController.beforeCountModsLink.size());
        check("reload-keeps-lore", rpgitemloader.plugin.getLoreConfig() != null, "");
    }
}
