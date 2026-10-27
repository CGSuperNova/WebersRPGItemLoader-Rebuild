# 架构说明

本文描述两个插件在 1.8.0（Paper 1.21.11）下的代码结构与运行机制，供后续维护参考。

## 1. 模块与职责

```
rpgitem-loader (WeberRpgitemLoader)
├─ com.weberpyth.rpgitemloader.rpgitemloader   插件入口：事件注册、配置加载、/wrl* 命令、Tab 补全、旧版物品检测
├─ com.weberpyth.config
│  ├─ SimpleYamlConfig        用户配置文件基类（jar 内默认值 + 首次导出）
│  ├─ weberConfig             全局开关（静态）
│  ├─ WeberItemConfig         items.yml  → HashMap<String, WeberRPGItem>
│  ├─ WeberInfoConfig         info.yml   → HashMap<String, WeberRPGItemInfo>
│  └─ WeberLoreConfig         lore.yml   → 属性 Lore 显示规则
├─ com.weberpyth.item
│  ├─ WeberRPGItem            模板：id / innerName / attributes（33 个数值键）
│  └─ WeberRPGItemInfo        模板：name / lores
├─ com.weberpyth.nbtEditor
│  └─ WeberNBTEditor          物品标签读写（PersistentDataContainer，命名空间 weberrpg）
├─ com.weberpyth.controller
│  ├─ CombatController        战斗结算核心（属性汇总 + 事件链 + 伤害应用）
│  ├─ CommandController       /wrl 子命令、/wrlLBI /wrlLII /wrlCheck /wrlJump、Tab 补全
│  ├─ DotController           DOT 占位实现（当前空转）
│  └─ ProjectileController    投掷物占位实现
├─ com.weberpyth.weberEvents  28 个属性事件 + 基类 weberAttributesEvent
├─ com.weberpyth.weberInterface
│  ├─ AttrModFunction         外部属性注入接口（Buff 插件实现它）
│  └─ WebersVirtualEntity     虚构实体标记接口
└─ com.weberpyth.util
   ├─ WeberEntityUtil          实体/射手解析、最大生命
   ├─ WeberEquipmentUtil       装备槽读取（AIR 归一化为 null）
   ├─ WeberItemRenameUtil      显示名/Lore 模板应用（Adventure）
   ├─ WeberItemLoreUtil        属性 Lore 块生成与幂等替换
   ├─ LegacyItemUtil           旧版 custom_data 标签检测与转换
   └─ WeberText                两套颜色语法（§ / &）与 Component 互转

webers-buff (WebersBuff)
├─ com.weberpyth.weberBuff.WeberBuff        入口：Buff 道具右键、/wrbReload、Tab 补全
├─ com.weberpyth.WeberBuffController
│  ├─ BuffCombatController  实现 AttrModFunction，把 Buff 属性并入 attrs
│  └─ CommandController     /wrbAdd /wrbSet /wrbGive /wrbCheck /wrbFace、Tab 补全
├─ com.weberpyth.config      buff.yml / combat.yml / echo.yml 加载器（复用 SimpleYamlConfig）
└─ com.weberpyth.weberBuffData
   ├─ WeberBuffModel        extends WeberRPGItem，多一个 name
   └─ WeberBuffEntity       Buff 实例：模板 + 获得时间
```

`WebersBuff` 在编译期依赖 `rpgitem-loader` 的类（`WeberRPGItem`、`CombatController`、`AttrModFunction`、`WeberNBTEditor`、`SimpleYamlConfig`）。运行时通过 `plugin.yml` 的 `depend: [WeberRpgitemLoader]` 保证加载顺序并让 Paper 把两个插件放进同一个 class loader group；`WebersBuff` 的 jar **不包含**这些类。

## 2. 启动流程

`WeberRpgitemLoader#onEnable`：

1. `saveDefaultConfig()` + `reloadConfig()`（顺序很关键，见迁移文档）；
2. `new weberConfig(getConfig())` 读取全局开关；
3. 注册监听器；
4. `CommandController` → `CombatController`；
5. `loadAll()`：`items.yml` → `info.yml` → `lore.yml` → `CombatController.initCombatController()`；
6. `WeberNBTEditor.initWeberNBTEditor()`（现为幂等空操作，保留 ABI）；
7. `DotController`。

`WebersBuff#onEnable`：

1. 配置文件与模型载入（`buff.yml` → `WeberBuffModel`）；
2. `WeberBuffCombatConfig.loadCombatInfo()` 读取 `combat.yml`；
3. `echo.yml`；
4. 把 `new BuffCombatController()` 加进 `CombatController.beforeCountModsLink`（静态，带重复注册保护）。

## 3. 战斗管线

监听入口：`rpgitemloader#onBattleDamage`（`EntityDamageByEntityEvent`，`HIGHEST`）。

```
EntityDamageByEntityEvent
        │
        ▼
CombatController.response()
  ├─ 清空 attrs；过滤 FIRE_TICK / POISON / WITHER（交给 DotController，当前空转）
  ├─ 投掷物 → 解析射手（WeberEntityUtil.findShooter）
  ├─ 收集攻击方 6 格装备：主手、副手、4 件护甲
  ├─ 收集受击方 6 格装备
  ├─ beforeCountMods()  ← AttrModFunction 链（Buff 属性在这里并入）
  ├─ countMods()        ← 按 weber_id 标签查模板，把属性并入 attrs
  │                        攻击方键：attrEventsDamager
  │                        受击方键：attrEventsEntity
  ├─ countLore()        ← 可选，按 Lore 是否包含 innerName 匹配
  ├─ afterCountMods()
  ├─ 若双方所有格子都没有插件属性且没有 AttrModFunction，提前结束
  ├─ setNoOriginDamage()（可把原版伤害清零）
  └─ finalCount()
       ├─ beforeFinalDamageCount()   trigger == 0 的事件链
       ├─ finalDamageCount()         应用 modDamage / modInjury 到事件伤害
       ├─ beforeFinalHealthCount()   trigger == 1 的事件链（治疗/吸血）
       ├─ finalHealthCount()         对攻击者 setHealth（上限用 MAX_HEALTH 属性）
       └─ showBattleInfo()           可选战斗回显
```

### 属性事件

`weberAttributesEvent` 是具体基类，字段 `name`、`value`、`trigger`，并暴露：

```java
Double countAttack(Double modDamage, EntityDamageByEntityEvent event);
Double countDefence(Double modInjury, Double modDamage, EntityDamageByEntityEvent event);
Double countHealth(Double modHeal, EntityDamageByEntityEvent event);
int getTrigger();
void setValue(HashMap<String, Double> attrs);
String setNumInfo(String msg);
String setNoNumInfo(String msg);
```

- `trigger == 0`：伤害阶段，在 `setDamage` 之前执行；
- `trigger == 1`：治疗阶段，在伤害确定之后执行。

事件实例在 `CombatController.initAttrEventsLink()` 中一次性 `new` 出来并放进静态 `attrEvents`，执行顺序由 `attrEventsOrder` 字符串数组硬编码决定（29 个键）。新增属性事件需要同时改注册表与 `attrEventsOrder`。

`@attrEventsLink` 注解目前没有被读取，属于历史遗留；注册完全靠上面的硬编码表。

### 跨插件扩展点

`CombatController.beforeCountModsLink` / `afterCountModsLink` 是 `ArrayList<AttrModFunction>`，`AttrModFunction.filter(attrs, event)` 返回（或原地修改）属性表。`WebersBuff` 用它把玩家身上所有生效 Buff 的属性并入 `attrs`，从而与武器/护甲属性走同一套事件链。

## 4. 物品标签

`WeberNBTEditor` 统一使用 `ItemMeta#getPersistentDataContainer()`：

| 键 | 含义 | 写入方 |
| --- | --- | --- |
| `weberrpg:weber_id` | 战斗属性模板 ID | `/wrlLBI`、`/wrl convert` |
| `weberrpg:weber_info_id` | 显示模板 ID | `/wrlLII`、`/wrl convert` |
| `weberrpg:weber_buff_id` | Buff 模板 ID | `/wrbGive` |

空物品（`AIR`、`null`、数量 ≤ 0）一律安全返回 `null`/空操作。旧的根 NBT 标签不再读取。

## 5. 属性 Lore

`WeberLoreConfig` 从 `lore.yml` 读取一个有序规则表（`order` + `entries`）：

- 每条规则包含 `format`、`percent`、`decimals`；
- `%value%` 替换为格式化后的数值，`percent: true` 时先乘以 100；
- 未配置的属性回退为 `<key> +<value>`；
- `showZero` 控制是否输出 0 值。

`WeberItemLoreUtil.apply(item, template, config)`：

1. 读取物品现有 Lore（Adventure `meta.lore()`，逐行序列化为 `§` 形式）；
2. 若发现 `separator` 行，则截断到该行之前（保留展示 Lore，去掉旧属性块）；
3. 追加 `separator` + 新属性行；
4. 写回。

因此重复应用是幂等的；`/wrlLII` 更换展示模板后会重新调用一次 `apply`，属性块不会丢失。

## 6. 旧版物品检测与转换

旧版插件把 `weber_id` 写在物品根 NBT；1.20.5+ 的数据升级器把未知根标签搬进
`components.minecraft:custom_data`。Bukkit 的 PDC 只暴露 `PublicBukkitValues` 子树，因此：

- `LegacyItemUtil.read(item)` 先检查新 PDC 标签（有则直接判定为新版），再用
  `Bukkit.getUnsafe().serializeItemAsJson(item)` 取得整件物品的 JSON 视图；
- 从 `components → minecraft:custom_data` 读取 `weber_id` / `weber_info_id`；
- JSON 结构在不同版本可能变化，解析器带有深度受限的递归回退（查找直接包含 `weber_id` 的对象）；
- `LegacyItemUtil.convert(item, items)` 只写入新 PDC 标签；名称与 Lore 保持不变（旧物品已有手写 Lore），需要属性 Lore 时由玩家/管理员再执行 `/wrlLBI`；
- 旧 `custom_data` 键不删除：Bukkit 无法安全改写别人的 `custom_data`，而新标签优先、检测也会忽略已转换物品。

检测触发点（`rpgitemloader`，`MONITOR` 优先级）：`PlayerItemHeldEvent`、`PlayerInteractEvent`、
`PlayerJoinEvent`（延迟 2 秒）。每个玩家有冷却时间，提示带 `[点击转换]` 可点击组件。

## 7. 命令与 Tab 补全

`/wrl` 是子命令根：`list` / `convert` / `reload` / `help`；旧的顶层命令（`/wrlLBI` 等）保留。
权限在代码中按子命令检查：`WeberRpgitemLoader.admin`（管理/列表/重载）、
`WeberRpgitemLoader.convert`（默认所有玩家）。

Tab 补全由 `JavaPlugin#onTabComplete` 实现（`JavaPlugin` 本身是 `TabExecutor`，Bukkit 注册
plugin.yml 命令时会自动把它设为 completer），并委托给各自的 `CommandController#tabComplete`：

- Loader：`/wrl` 子命令、`list` 的模板 ID、`convert` 的 `all`、`/wrlLBI` 物品 ID、`/wrlLII` 显示 ID、
  `/wrlJump` 高度；无权限时不返回管理类补全。
- Buff：`wrbAdd` Buff ID、`wrbSet` 在线玩家 + Buff ID、`wrbGive` Buff ID + 材质名、`wrbFace` 力度。

## 8. 已知遗留

以下问题是原插件设计层面的，**未在本次迁移中改动**（改动会改变战斗数值或需要重新设计，建议后续单独评估）：

1. **DOT 系统空转**：`DotController.countMods()` / `initAttrsEventsLink()` 为空，`weberDotEvent` 不是 `weberAttributesEvent` 子类且从未注册。FIRE_TICK / POISON / WITHER 伤害既不走战斗管线也没有 DOT 加成。
2. **投掷物系统未接线**：`ProjectileController` 只有 `debugProjectileInfo` 被调用，事件列表永远为空；远程判定实际由事件类的 `getDamager() instanceof Projectile` 完成，且命中时读取的是射手**当前**装备（箭飞行中换武器会影响伤害）。
3. **乘区叠加**：多个事件以 `event.getDamage() + modDamage` 为基数继续相乘/相加，结果依赖 `attrEventsOrder` 顺序，属于原设计。
4. **静态事件实例**：每个属性事件只实例化一次并复用，`opd`/`rank`/`oph` 等字段跨事件共享。服务端单线程执行，目前安全，但不是可重入设计。
5. **玩家键**：Buff 数据以 `Player#getName()` 为键（旧版用显示名）。
6. **旧 custom_data 残留**：转换后旧键仍留在物品上，无法通过 Bukkit API 安全移除。
