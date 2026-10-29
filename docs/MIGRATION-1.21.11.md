# 1.21.11 迁移与修复记录

> 基线：`bukkit/spigot-api 1.12.2-R0.1-SNAPSHOT` + NBT-API 2.7.1（pom 遗留）
> 目标：Paper `1.21.11-R0.1-SNAPSHOT`，Java 21
> 结果：两个插件在真实 Paper 1.21.11 服务端成功启用，58 项运行时冒烟测试全部通过。

---

## 1. 报错根因：`ClassNotFoundException: com.weberpyth.nbtEditor.WeberNBTEditor`

原始堆栈：

```
[Yum] 异常名称: java.lang.ClassNotFoundException
[Yum] 异常说明: com.weberpyth.nbtEditor.WeberNBTEditor
[Yum]     位于 com.weberpyth.weberBuff.WeberBuff.onPlayerInteract(WeberBuff.java:141)
```

`WeberBuff.java:141` 调用的是 `WeberNBTEditor.getItemNBTStringValue(...)`——该类位于 **另一个插件** `WeberRpgitemLoader.jar` 中。

链条如下：

1. `WeberNBTEditor` 依赖 `de.tr7zw.nbtapi.NBTItem`（NBT-API 2.7.1）；
2. NBT-API 通过反射调用 NMS 内部类，在 1.20.5+（物品组件化）后已无法工作，服务器上也没有可用的 NBT-API；
3. `WeberRpgitemLoader#onEnable` 里 `WeberNBTEditor.initWeberNBTEditor()` 抛出的错误被 `catch (Throwable)` 吞掉，插件继续启用，但 `WeberNBTEditor` 这个类**从未成功加载**；
4. 事件触发时，Paper 的 class loader group 无法提供该类，最终以 `ClassNotFoundException: com.weberpyth.nbtEditor.WeberNBTEditor` 的形式抛出。

**修复**：删除 NBT-API 依赖，`WeberNBTEditor` 改为只使用 Bukkit 官方 `PersistentDataContainer` 重写。该类现在没有任何第三方依赖，可以正常加载；`WebersBuff` 的调用点无需再改动逻辑。

同时为 `WebersBuff` 增加 `depend: [WeberRpgitemLoader]`，显式声明两者是同一套系统的两个部分，保证加载顺序和 class loader group 的稳定。

---

## 2. 反编译缺陷修复

反编译产物（JD-Core 1.1.3）存在系统性问题，已全部处理：

| 问题 | 示例 | 处理 |
| --- | --- | --- |
| 每行前缀注释 | `/*  141 */ String buffID = ...` | 构建前由 `tools/clean-sources.mjs` 去除 |
| 文件尾部残缺注释 | ` * Java compiler version: 8 (52.0)` | 同上，整段截断 |
| 裸 `this;` 语句（`ALOAD 0`/`POP` 产物） | `this; attrs.clear();`、`this; this; attrs = ...` | 同上，脚本统一删除（约 300 处） |
| 丢失 import | `CombatController` 使用 `EntityEquipment` 但无 import；16 个事件类未 import | 补齐 |
| 错误 import | `ProjectileController` 导入 `javax.swing.text.html.parser.Entity` | 改为 `org.bukkit.entity.Entity` |
| 无意义的强制转换/冗余代码 | `(Plugin)this`、`new Object[]{...}` | 保留兼容写法，未做无风险重构 |
| 恒真条件 | `if (k != "damage" \|\| k != "")` | 改为 `!"damage".equals(k)` |
| 空指针路径 | `wae == null` 打印警告后继续 `wae.getTrigger()` | 补 `continue`；第二个阶段补判空 |
| 未初始化/共享状态 | 属性事件单例的 `opd`/`rank`/`oph` | 见 §5，未改变设计 |

> 说明：`*.jar.src/` 目录保留了原始反编译输出作为对照，实际参与构建的是 `rpgitem-loader/`、`webers-buff/` 下的清理版源码。

---

## 3. 1.21.11 API 断裂与修复

| # | 位置 | 旧代码 | 问题 | 新写法 |
| --- | --- | --- | --- | --- |
| 1 | `rpgitemloader#onEnable` | `Bukkit.getServer().getClass().getPackage().getName()` + `Pattern("v.*")` | 1.20.5+ 包名不再含 `v1_xx_Rx`，`m.group()` 抛 `IllegalStateException`，插件直接启用失败 | 删除版本探测，改用 `Bukkit.getBukkitVersion()` |
| 2 | `WeberEntityUtil#getMaxHealth` | `Attribute.GENERIC_MAX_HEALTH` | 1.21.3 起常量改名为 `MAX_HEALTH`，编译失败 | `Attribute.MAX_HEALTH`，并对 `AttributeInstance` 判空 |
| 3 | 同上 | `catch (NoClassDefFoundError) { entity.getMaxHealth(); }` | `getMaxHealth()` 已弃用；属性不存在时是 NPE 而非 NCDFE | 直接判空回退 20 |
| 4 | `WeberEquipmentUtil` | `getItemInHand()` 兜底 | 方法已移除；且 1.9+ 空手返回 `AIR` 不再返回 `null` | 只保留 `getItemInMainHand()`，并把 `AIR` 归一化为 `null` |
| 5 | `CombatController` | `im == null` 判断空槽 | 1.21 空槽是 `AIR`，会进入 NBT 读取并累计 `failTime` | 通过 `WeberEquipmentUtil.emptyToNull` 统一处理 |
| 6 | `WeberBuffController#wrbGive` | `new ItemStack(int)` | 数字物品 ID 在 1.13 移除 | 支持材质名（`PAPER`），并保留常用旧数字 ID 映射表 |
| 7 | `WeberBuff#onPlayerInteract` | `WeberNBTEditor` + `getDisplayName()` | 见 §1；显示名不唯一且可被昵称插件改写 | PDC 读取 + `Player#getName()` |
| 8 | `BuffCombatController` 等 | `(HashMap) x.get(...)` 原始类型 | Java 21 编译告警/类型不安全 | 全部改为泛型 |
| 9 | `WeberItemRenameUtil` | `ItemMeta#setDisplayName(String)` / `setLore(List<String>)` | 已弃用，1.20.5+ 组件化后语义不稳定 | Adventure `Component` + `LegacyComponentSerializer` |
| 10 | `plugin.yml` | 无 `api-version` | 1.20.5+ 会按旧版材质/注册表处理并产生警告 | `api-version: '1.21'` |

其余检查项（`setMaximumNoDamageTicks`、`EntityDamageEvent#getDamage/setDamage`、`getFinalDamage`、`ItemFlag`、`BukkitRunnable`、`EntityShootBowEvent#getProjectile`）在 1.21.11 仍然可用，未改动。

---

## 4. 其他缺陷修复

### 配置加载

- 原 `WeberItemConfig` / `WeberInfoConfig` / `WeberBuffLoadConfig` / `WeberBuffCombatConfig` / `WeberBuffEchoConfig` 是五份几乎相同的 70 行样板，且：
  - `reloadCustomConfig()` 首次运行会递归调用自身；
  - jar 内默认配置的 `InputStreamReader` 从不关闭；
  - 用平台默认字符集读取 UTF-8 资源；
  - 配置为空或节点类型不对时返回 `null`，调用方直接 NPE。
- 现统一为 `SimpleYamlConfig` 基类（try-with-resources、UTF-8、`getRoot()`、空配置返回空 Map）。
- `rpgitemloader#onEnable` 现在**先** `saveDefaultConfig()` 再 `getConfig()`：原顺序在全新安装时会读到空配置，导致所有开关都是默认 `false`。
- `items.yml` 的 33 个属性键统一由 `WeberItemConfig.ATTRIBUTE_KEYS` 驱动，缺省值 0，节点缺失时跳过并告警，不再整份配置加载失败。

### 命令与交互

- `/wrlLBI` `/wrlLII` `/wrlJump` `/wrbGive` `/wrbSet` `/wrbFace` 补参数个数校验，避免 `ArrayIndexOutOfBoundsException`。
- `/wrlLBI` 校验模板是否存在、主手是否为空。
- `/wrlCheck` 对未知模板 ID 给出提示而不是 NPE。
- `/wrbGive` 校验 buff 是否存在、材质是否合法。
- `/wrbFace` 校验目标为生物、同世界，移除对玩家刷屏的调试消息。
- Buff 道具右键：现在会取消交互（防止同时放置方块）、只在非创造模式扣减、并正确写回副手/主手。

### 其他

- `CombatController`：`attrs` 懒初始化；`EntityEquipment` 判空；`AttrModFunction` 返回 `null` 时保留原 map；属性合并抽成 `mergeAttributes`，缺键不再拆箱 NPE。
- `WeberBuffEntity#isActive()`：`timer` / 模板 / 属性判空。
- `WebersBuff#onDisable`：配置未初始化时不再 NPE。
- `WebersBuff#onEnable`：战斗钩子重复注册保护；接入失败只告警不阻断启用。

---

## 5. 属性事件层加固

对 `weberEvents` 下的 28 个事件类做了**不改变数值公式**的加固：

- 35 处 Java 21 标记为 removal 的 `new Double(...)` 装箱写法全部替换为基本类型 / `Double.valueOf`；
- `countAttack` / `countDefence` / `countHealth` 不再可能返回 `null`（原基类返回 `null`，调用方直接 `.doubleValue()`）；6 处 `getMaxHealth()` 返回值补判空；
- 5 处血量比例计算补零除保护（`currentHealth / maxHealth` 等原会产生 `NaN`/`Infinity`）；
- `weberCritEvent` 的 `while (value >= 1.0)` 增加 100 次上限与 NaN/∞ 防护，并改用局部变量，避免改写共享的 `value` 字段导致下次事件状态残留；
- `weberBlowEvent` 的延迟任务在目标失效/跨世界时跳过；
- `weberBackstabEvent` 仅在背刺属性非 0 时输出"失败"提示，避免每次命中刷屏。

事件注册顺序（`attrEventsOrder`）、触发阶段（`trigger`）与全部中文文案保持不变。加固后重新执行冒烟测试，13 项断言仍全部通过（`combat-damage-applied = 25.0`）。

---

## 6. 1.9.0 功能扩展

在完成 1.21.11 适配后，按需求补充了四项功能。

### 6.1 Tab 补全

- 两个插件的主类实现 `onTabComplete`（`JavaPlugin` 本身是 `TabExecutor`，Bukkit 注册 plugin.yml 命令时会自动把它设为 completer），委托给各自的 `CommandController#tabComplete`。
- Loader：`/wrl` 子命令（list/convert/reload/help）、`/wrl list` 的模板 ID、`/wrl convert` 的 `all`、`/wrlLBI` 的物品 ID、`/wrlLII` 的显示模板 ID、`/wrlJump` 的高度。
- Buff：`/wrbAdd` 的 Buff ID、`/wrbSet` 的在线玩家名 + Buff ID、`/wrbGive` 的 Buff ID + 材质名、`/wrbFace` 的力度。
- 结果按前缀过滤、排序，最多 50 条；没有管理权限时不返回管理类补全。

### 6.2 `/wrl list` 子命令

- `/wrl` 现在是子命令根：`list` / `convert` / `reload` / `help`，旧的顶层命令保持不变。
- `/wrl list` 列出全部模板（含 `info.yml` 的显示名），每条带可点击的 `[查看]`；
- `/wrl list <id>` 输出内部名称、显示名称、展示 Lore、全部非零属性（使用 `lore.yml` 的中文名称与格式）以及应用提示。

### 6.3 自动属性 Lore

- 新增 `lore.yml` + `WeberLoreConfig` + `WeberItemLoreUtil`。
- `/wrlLBI` 写入模板后自动生成属性块；`/wrlLII` 更换展示模板后自动补回属性块。`/wrl convert` **不生成 Lore**（旧物品已有手写 Lore）。
- 中文名称、颜色、顺序、百分比、小数位全部可配置；`damage: 15` 默认显示为 `伤害 +15`，`crit: 0.05` 显示为 `暴击率 +5%`。
- 属性块以 `separator` 与展示 Lore 分隔，重复应用会替换旧块（幂等），不会重复堆叠。

### 6.4 旧版物品检测与转换

- 旧版物品的 `weber_id` 位于 `components.minecraft:custom_data`（1.20.5 数据升级的结果），Bukkit PDC 读不到；`LegacyItemUtil` 通过 `UnsafeValues#serializeItemAsJson` 读取整件物品 JSON 并解析该组件。
- 触发点：切换手持、右键使用、登录后首次手持；提示带 `[点击转换]` 可点击按钮，文本与冷却可配置。
- `/wrl convert` 转换主手，`/wrl convert all` 转换整个背包；转换只写入新的 PDC 标签，**不修改名称与 Lore**（旧物品通常已有手写 Lore）。
- 转换幂等；模板不存在时拒绝并提示；旧 `custom_data` 键保留但不影响新逻辑。

### 6.5 模板管理命令（1.9.0）

- `/wrl list` 改为只输出模板 ID；详细属性移到 `/wrl info <ID>`。
- 新增 `/wrl create <ID>` / `/wrl delete <ID>`：直接修改 `items.yml` 并保存，同时更新内存模板表（与战斗管线共享同一 Map，立即生效）。
- 新增 `/wrl edit <ID> addAttribute <属性> <数值>` 与 `/wrl edit <ID> removeAttribute <属性>`：属性名大小写不敏感，`removeAttribute` 数值归零并从 YAML 删除该键。
- 模板 ID 限制为 `[A-Za-z0-9_-]{1,32}`；`create` 写入 `innerName: <ID>` 作为节点占位，避免空节点被 YAML 丢弃。
- Tab 补全同步支持新子命令、操作名、属性键与常用数值。

## 7. 构建体系

原项目只有反编译源码和一个 1.12.2 的 `pom.xml` 片段，无法复现构建。现提供：

- `pom.xml` + 两个子模块：`paper-api:1.21.11-R0.1-SNAPSHOT`、`release 21`、`webers-buff` 以 `provided` 依赖 loader；
- `build.ps1`：无需 Maven 的离线构建（JDK 21 + `javac`/`jar` + `libs/`），产物输出到 `dist/`；
- `tools/fetch-deps.mjs`：不依赖 Maven 的依赖下载器（PaperMC 仓库 → `libs/`）；
- `tools/clean-sources.mjs`：反编译产物清理（可重复执行，便于将来重新反编译后对比）。

---

## 8. 验证

### 静态

- `javac --release 21` 编译两个模块：**0 error / 0 warning**；
- `mvn -B -DskipTests package`：**BUILD SUCCESS**（Maven 3.9.9 + JDK 21.0.3），两个模块均产出 jar；
- 两个 jar 均包含 `plugin.yml` 与全部默认配置资源；`WebersBuff.jar` 不包含 loader 的任何类。

### 运行时（真实 Paper 1.21.11 build 132）

测试服：`test-server/`（Paper 1.21.11-132，Java 21.0.3），安装两个插件 + 一个一次性冒烟测试插件。

```
[WeberRpgitemLoader] Enabling WeberRpgitemLoader v1.9.0
[Weber's RPG Item Loader] WRL物品模板载入完成，共 7 项
[Weber's RPG Item Loader] WRL显示模板载入完成，共 7 项
[Weber's RPG Item Loader] WRL插件加载完成!
[WebersBuff] Enabling WebersBuff v1.9.0
[Weber's Buff] Buff模板载入完成，共 1 项
[Weber's Buff:DEBUG] 已注册 Buff 战斗判定钩子
[WebersBuff] WebersBuff 已启用
Done (17.892s)!
```

冒烟测试结果（`test-server/smoke/`，最终 58 项全部通过）：

```
SMOKE PASS tag-string / tag-isolated / tag-double / tag-air-safe / tag-null-safe / tag-meta-persisted
SMOKE PASS display-name-applied (name=§5§l破败王者之剑) / display-lore-applied (loreLines=8)
SMOKE PASS loader-plugin-instance / items-loaded / info-loaded / lore-config-loaded
SMOKE PASS buff-models-loaded / buff-combat-hook
SMOKE PASS lore-lines-generated (分隔线 + 伤害+15 / 暴击率+5% / 暴击伤害+100% / 攻击回复+2 / 击退+1)
SMOKE PASS lore-damage-line / lore-crit-percent / lore-idempotent (after=6)
SMOKE PASS legacy-item-created / legacy-detected (LegacyTags[id=test1, infoId=test1])
SMOKE PASS legacy-converted / legacy-new-tag / legacy-detection-stops
SMOKE PASS legacy-lore-untouched / legacy-name-untouched (转换不改动手写名称与 Lore)
SMOKE PASS tab-wrl-subcommands / tab-wrl-template-subcommands / tab-edit-operations / tab-edit-attributes
SMOKE PASS tab-item-ids / tab-item-filter / tab-buff-ids
SMOKE PASS wrl-list / wrl-list-ignores-extra-arg / wrl-info-missing / wrl-help / wrl-convert-console-guard
SMOKE PASS wrl-create / wrl-create-saved-to-disk
SMOKE PASS wrl-edit-add (damage=7.0) / wrl-edit-remove (damage=0.0)
SMOKE PASS wrl-info / wrl-list-names-only
SMOKE PASS wrl-delete / wrl-delete-saved-to-disk
SMOKE PASS combat-damage-applied (damage=25.0) / combat-plain-item-unchanged (damage=10.0)
SMOKE PASS command-lookup-wrl / command-permission-wrl / command-execute-wrlReloadConf
SMOKE PASS command-lookup-wrb / command-execute-wrbReload
SMOKE PASS reload-keeps-items / reload-keeps-buff-models / reload-keeps-buff-hook / reload-keeps-lore
SMOKE PASS combat-damage-applied (damage=25.0) / combat-plain-item-unchanged (damage=10.0)   ← 重载后复测
SMOKE RESULT PASS passed=58 failed=0
```

> 测试说明：命令断言使用 `PluginCommand#execute(...)`（等价于命令映射器对真实玩家/控制台的调用）。未使用
> `Bukkit.dispatchCommand(...)`，因为 Paper 1.21 的 `CraftServer#dispatchCommand` 直接走 Brigadier 派发器，
> 而插件命令节点是在玩家登录后才同步给客户端的；无玩家的无头测试里它必然返回 `false`，与被测插件无关。
> 旧版物品由测试插件用服务端自身的 `serializeItemAsJson` 结果注入 `minecraft:custom_data` 生成，与 1.20.5
> 数据升级后的真实旧物品结构一致（该组件在 JSON 视图中是 SNBT 字符串）。

覆盖内容：

1. PDC 标签写入/读取/隔离/`AIR`/`null` 安全；
2. Adventure 显示名/Lore（`info.yml` 的 `§` 颜色码，中文正常）；
3. 属性 Lore 生成：中文名称、百分比（`crit 0.05 → +5%`）、倍率（`critMul 1 → +100%`）、幂等替换；
4. 旧版物品：从 `custom_data` 识别、转换、写入新标签、**名称与 Lore 保持不变**、转换后不再被识别；
5. Tab 补全：`/wrl` 子命令、物品 ID、前缀过滤、Buff ID；
6. `/wrl list`、`/wrl list <id>`、未知 ID、`help`、控制台执行 `convert` 的守卫；
7. 两个插件的数据互相可见（跨 class loader）；
8. Buff 模型载入与战斗钩子注册；
9. 真实 `EntityDamageByEntityEvent` 触发完整战斗管线：`test1` 模板（damage 15）把 10.0 伤害结算为 **25.0**；
10. 无标签的同材质武器保持 **10.0**，确认不会误伤普通物品；
11. `/wrlReloadConf` 与 `/wrbReload` 执行成功，重载后模板、Buff 模型、Lore 规则、战斗钩子仍在，且战斗结算结果不变。

复现方式：

```powershell
node tools/get-paper-server.mjs          # 下载 Paper 1.21.11 到 test-server/
.\build.ps1                              # 构建两个插件
# 编译 test-server/smoke 并放入 test-server/plugins/，然后
java -Xmx2G -jar paper.jar --nogui
```

---

## 9. 升级注意

1. **旧物品需要重新打标**：1.20.5 起物品不再保留任意根 NBT，旧 `weber_id` 无法迁移。用 `/wrlLBI` / `/wrlLII` 重新标记，Buff 道具用 `/wrbGive` 重新发放。
2. **`combat.yml` 的玩家键**：旧数据以显示名为键，新版本写入时使用 `Player#getName()`。若服务器有昵称插件，请手动统一该文件的键。
3. **两插件必须同时安装**：`WebersBuff` 声明了硬依赖，缺少 loader 时 Paper 会拒绝加载它（这是预期行为，旧版本只是碰巧能部分工作）。
4. **调试开关**：`webers-buff/config.yml` 默认 `isDebug: true`，正式服建议改 `false`。

---

## 10. 未覆盖 / 后续建议

详见 `docs/ARCHITECTURE.md` §5：DOT 与投掷物子系统仍是空壳、乘区叠加与事件顺序强耦合、属性事件为静态单例。这些属于原设计范畴，修改会直接影响战斗数值，建议在明确数值预期后单独迭代。
