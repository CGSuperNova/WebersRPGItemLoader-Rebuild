# Weber's RPG Item System

# 前言

本项目为基于WebersRPGItemLoader和WebersBuff两个我的世界服务器插件的二次开发版本，不属于原创插件，请注意甄别。

我主要是将本插件向高版本进行了兼容并修复了一些零碎的问题，同时添加了一些比较方便的功能（如list子命令）。插件所提供的核心功能和作用基本不变。

原作者：[隔壁富贵我表哥](https://space.bilibili.com/27238021)

原插件wiki页面: [[MC插件][娱乐|RPG|免费] Webers RPGItem Loader 系列教程 (一)](https://www.bilibili.com/opus/907416402296569858)

> 作者没有对原插件进行开源，本重制版是直接对插件本体进行反编译的

> 注意，项目根目录下的rpgItemLoader1.6.9Beta.jar与webersBuff.jar是原版的插件（即没有重置的）。重制版放置于dist目录下

# 插件详情
两个协同工作的 Paper 插件，为服务器提供一套 RPG 武器/护甲属性系统：

| 插件 | 作用 | 主类 |
| --- | --- | --- |
| **WeberRpgitemLoader** | 物品模板、战斗结算管线、属性事件、属性 Lore、旧版物品转换 | `com.weberpyth.rpgitemloader.rpgitemloader` |
| **WebersBuff** | 限时属性 Buff、Buff 道具 | `com.weberpyth.weberBuff.WeberBuff` |

---

## 1. 运行环境

- **服务端**：Paper 1.21.11（API `1.21.11-R0.1-SNAPSHOT`）
- **Java**：21+
- **依赖**：`WebersBuff` 需要 `WeberRpgitemLoader` 同时安装

## 2. 构建

### 方式一：离线脚本（无需 Maven）

```powershell
.\build.ps1
```

产物在 `dist/`：

```
dist/WeberRpgitemLoader-1.9.0.jar
dist/WebersBuff-1.9.0.jar
```

脚本会自动：

1. 定位 JDK 21（`JAVA_HOME` 或常见安装目录）；
2. 若 `libs/` 为空，用 Node 运行 `tools/fetch-deps.mjs` 从 PaperMC 仓库下载 `paper-api` 与 Adventure API；
3. 依次编译两个模块、复制资源并打包。

首次构建需要 Node.js 与网络；之后 `libs/` 已缓存即可完全离线。

### 方式二：Maven

```bash
mvn -q package
```

两个模块分别产出可部署 jar（`rpgitem-loader/target/WeberRpgitemLoader-1.9.0.jar`、`webers-buff/target/WebersBuff-1.9.0.jar`）。`webers-buff` 对 `rpgitem-loader` 的依赖是 `provided`，不会被打进 jar。

### 本地冒烟测试（可选）

```powershell
.\test-server\run-smoke.ps1
```

脚本会下载 Paper 1.21.11、重新构建两个插件、编译 `test-server/smoke` 中的一次性测试插件并启动服务器。测试插件会在真实服务端里跑全部断言（物品标签、显示名/Lore、属性 Lore、旧版物品检测与转换、Tab 补全、`/wrl list`、战斗结算、配置重载及重载后的再次结算），随后自动关服；结果以 `SMOKE ` 前缀输出。

## 3. 安装

1. 把两个 jar 放进 `plugins/`；
2. 启动服务器（`WeberRpgitemLoader` 会先于 `WebersBuff` 加载，无需手动调整顺序）；
3. 首次启动会生成：
   - `plugins/WeberRpgitemLoader/`：`config.yml`、`items.yml`、`info.yml`、`lore.yml`
   - `plugins/WebersBuff/`：`config.yml`、`buff.yml`、`combat.yml`、`echo.yml`

## 4. 命令

| 命令 | 说明 | 权限 |
| --- | --- | --- |
| `/wrl` | 显示命令帮助 | 无 |
| `/wrl list` | 只列出全部物品模板的名称（不展开详情） | `WeberRpgitemLoader.admin` |
| `/wrl info <物品ID>` | 查看模板的内部名称、显示模板、全部属性 | `WeberRpgitemLoader.admin` |
| `/wrl create <物品ID>` | 新建一个全属性为 0 的空模板（写入 `items.yml`） | `WeberRpgitemLoader.admin` |
| `/wrl delete <物品ID>` | 删除模板（已打标签的物品会失去属性） | `WeberRpgitemLoader.admin` |
| `/wrl edit <物品ID> addAttribute <属性> <数值>` | 设置模板属性 | `WeberRpgitemLoader.admin` |
| `/wrl edit <物品ID> removeAttribute <属性>` | 移除模板属性（数值归零） | `WeberRpgitemLoader.admin` |
| `/wrl convert [all]` | 把手中的旧版物品转换为新版；`all` 转换整个背包 | `WeberRpgitemLoader.convert`（默认所有玩家） |
| `/wrl reload` | 重载全部配置（等同 `/wrlReloadConf`） | `WeberRpgitemLoader.admin` |
| `/wrl help` | 显示命令帮助 | 无 |
| `/wrlLBI <物品模板ID>` | 给手持物品打上战斗属性，并自动生成属性 Lore | `WeberRpgitemLoader.admin` |
| `/wrlLII <显示模板ID>` | 给手持物品应用名称与 Lore（保留属性 Lore） | `WeberRpgitemLoader.admin` |
| `/wrlCheck` | 查看手持物品的战斗属性 | `WeberRpgitemLoader.admin` |
| `/wrlReloadConf` | 重载本插件全部配置 | `WeberRpgitemLoader.admin` |
| `/wrlJump <高度>` | 调试用跳跃 | `WeberRpgitemLoader.admin` |
| `/wrbGive <buffID> <物品>` | 获得一个 Buff 道具（物品可写材质名如 `PAPER`，也兼容旧数字 ID） | `WeberBuff.admin` |
| `/wrbAdd <buffID>` | 给自己添加 Buff | `WeberBuff.admin` |
| `/wrbSet <玩家> <buffID>` | 给指定玩家添加 Buff | `WeberBuff.admin` |
| `/wrbCheck` | 查看自己当前生效的 Buff | `WeberBuff.wrbCheck` |
| `/wrbReload` | 重载 Buff 插件配置 | `WeberBuff.admin` |
| `/wrbFace <垂直> <水平>` | 推动视线正前方的生物（调试） | `WeberBuff.admin` |

## 5. 配置

### items.yml / buff.yml 属性键

`items.yml` 的每个节点是一个物品模板，`buff.yml` 的每个节点是"模板 + `name` + `length`"：

```yaml
test1:
  damage: 15          # 固定伤害修正
  crit: 0.05          # 暴击几率（每 1.0 视为一级暴击）
  critMul: 1          # 每级暴击的额外倍率
  attackHeal: 2       # 攻击回复
  stealHealth: 0      # 生命偷取比例
  outputDamage: 0     # 输出增伤比例
  maxEntityHealthDamage: 0   # 目标最大生命百分比伤害
  currentEntityHealthDamage: 0
  appendDamage: 0
  repel: 1            # 击退强度
  # ... 完整键位见 WeberItemConfig.ATTRIBUTE_KEYS
```

所有数值键都可省略（缺省为 0）。`innerName` 用于 `loreCheck` 模式。

### lore.yml（属性 Lore）

`/wrlLBI`、`/wrlLII` 会根据模板属性自动生成中文 Lore。显示规则全部可配置：

```yaml
separator: "&8&m--------------------"   # 属性块与普通 Lore 的分隔线
showZero: false                        # 是否显示数值为 0 的属性

order:                                 # 显示顺序
  - damage
  - crit
  # ...

entries:
  damage:
    format: "&c伤害 &f+%value%"        # %value% 会替换为格式化后的数值
  crit:
    percent: true                      # 0.05 -> 5
    format: "&6暴击率 &f+%value%%"
  critMul:
    percent: true
    decimals: 1                        # 可选：固定小数位
    format: "&6暴击伤害 &f+%value%%"
```

- `%value%`：数值；`%key%`：原始属性键。
- `percent: true`：把 0~1 的比例显示为百分比（内部乘以 100）。
- 未在 `entries` 中出现的属性会回退为 `属性键 +数值`，不会静默消失。
- 同一个物品重复应用属性时，旧的属性块会被替换而不是叠加；`/wrlLII` 更换显示模板后也会自动补回属性块。

### config.yml（Loader）

| 键 | 默认 | 说明 |
| --- | --- | --- |
| `isDebug` | false | 输出调试日志 |
| `playerOnly` | true | 只在事件涉及玩家时结算 |
| `battleEcho` | false | 战斗信息回显给攻击者 |
| `lightBattleEcho` | false | 使用精简版回显 |
| `noOriginDamage` | false | 把原版伤害清零，只保留插件伤害 |
| `noImmutableTick` | false | 关闭受击无敌帧 |
| `noNBTAPI` | false | 关闭基于物品标签的属性结算 |
| `loreCheck` | false | 额外按 Lore 文本匹配模板 |
| `legacyDetect` | true | 检测旧版物品并提示玩家转换 |
| `legacyDetectCooldown` | 15 | 同一玩家两次提示的最小间隔（秒） |
| `legacyMessage` | 见文件 | 提示文本，`%id%` 替换为模板 ID |

### combat.yml（Buff）

```yaml
<玩家名>:
  buff:
    <buffID>: <获得时的时间戳毫秒>
```

## 6. 自动属性 Lore

`/wrlLBI <物品ID>` 给物品写入战斗模板后，会同时把该模板**所有非零属性**逐行写入 Lore：

```
§5§l破败王者之剑
§8§m--------------------
§c伤害 +15
§6暴击率 +5%
§6暴击伤害 +100%
§a攻击回复 +2
§b击退 +1
```

- 中文名称、颜色、百分比/小数规则、显示顺序都在 `lore.yml` 中自定义。
- 属性块上方保留 `info.yml` 的展示 Lore，下方以 `separator` 分隔。
- 再次应用同一模板会替换属性块，不会产生重复行。

## 7. 旧版物品检测与转换

旧版插件把 `weber_id` 写在物品根 NBT 上；升级到 1.20.5+ 后，Minecraft 的数据升级器会把它搬到
`components.minecraft:custom_data.weber_id`，新版本读不到，因此需要转换。

**检测**：玩家切换手持物品、右键使用物品、或登录后首次手持时，插件会检查 `custom_data` 中的旧版标签，
发现有旧物品就发送一条带 `[点击转换]` 按钮的提示（提示文本与冷却时间可在 `config.yml` 调整）。

**转换**：

- 点击提示按钮或输入 `/wrl convert`：转换主手物品；
- `/wrl convert all`：转换整个背包（含护甲与副手）；
- 转换只写入新的 `weberrpg:weber_id` / `weberrpg:weber_info_id` 标签，**不会修改物品名称与 Lore**（旧物品通常已有手写 Lore）；确实需要自动属性 Lore 时，转换后再执行一次 `/wrlLBI <ID>`；
- 旧物品的模板在 `items.yml` 中不存在时不会转换，并给出提示；
- 转换是幂等的：已经转换过的物品不会再次被识别为旧版。

> 旧版 `custom_data` 中的键不会被删除（对游戏无影响），因为 Bukkit API 只允许写自己的
> `PublicBukkitValues` 子树；新标签优先，检测也会忽略已转换物品。

## 8. 1.21.11 适配要点

完整的变更与验证记录见 [`docs/MIGRATION-1.21.11.md`](docs/MIGRATION-1.21.11.md)，代码结构见 [`docs/ARCHITECTURE.md`](docs/ARCHITECTURE.md)。

最重要的三点：

1. **物品 NBT → PersistentDataContainer**：旧的 NBT-API 2.7.1 在 1.20.5+（物品组件化）后无法工作，且它的缺失导致 `WeberNBTEditor` 无法加载，正是日志里 `ClassNotFoundException: com.weberpyth.nbtEditor.WeberNBTEditor` 的根因。现已改用 Bukkit 官方 PDC，标签命名空间为 `weberrpg`。
2. **移除对服务端内部包的探测**：`Bukkit.getServer().getClass().getPackage().getName()` 在 1.20.5+ 不再包含 `v1_xx_Rx`，原代码会抛 `IllegalStateException` 导致插件启用失败。
3. **1.21 API 迁移**：`Attribute.GENERIC_MAX_HEALTH` → `Attribute.MAX_HEALTH`、`getItemInHand()` 移除、空装备槽返回 `AIR` 而非 `null`、`new ItemStack(int)` 移除、`getDisplayName()` 不再适合作为玩家唯一标识等。

## 9. 数据迁移提示

- 旧版物品请用 `/wrl convert` 转换；1.20.5 之前生成的旧物品无法通过 PDC 读取，必须走转换流程。
- `combat.yml` 旧数据以玩家**显示名**为键。新版本改用 `Player#getName()`，若服务器使用昵称插件导致两者不同，旧键不会自动匹配，可手动改写该文件。
- 升级到 1.9.0 后，已有物品的 Lore 不会自动补全，请重新执行一次 `/wrlLBI <ID>`（或转换旧物品）以生成属性块。

## 10. 目录结构

```
.
├─ pom.xml                     # Maven 聚合工程
├─ build.ps1                   # Windows 离线构建脚本
├─ libs/                       # 编译期依赖（paper-api、Adventure）
├─ rpgitem-loader/             # 插件一
│  ├─ src/main/java/com/weberpyth/...
│  └─ src/main/resources/{plugin.yml,config.yml,items.yml,info.yml,lore.yml}
├─ webers-buff/                # 插件二
│  ├─ src/main/java/com/weberpyth/...
│  └─ src/main/resources/{plugin.yml,config.yml,buff.yml,combat.yml,echo.yml}
├─ tools/                      # 依赖下载、源码清理、编译日志分析脚本
├─ test-server/                # 本地 1.21.11 冒烟测试服（不参与打包）
├─ docs/                       # 架构与迁移文档
├─ *.jar.src/                  # 原始反编译产物（保留作对照）
└─ dist/                       # 构建产物
```

---

# 附录 A：属性手册（整理自原作者 Wiki）

> 以下内容整理自原作者发布的 Wiki 页面，**版权归原作者所有**；本重制版只做排版、补全配置键名与少量勘误，不改变原意。
>
> 单位说明：以下“1 点伤害”等于原版 Minecraft 中的半颗心。

## A.1 装备栏机制

插件只检测角色的 **主手、副手、头盔、胸甲、护腿、靴子** 六个装备栏。

与部分 RPG 插件不同，本插件的战斗属性对**角色整体**生效：六个装备栏被视作类似 MOBA 的装备栏，任意一格提供的属性都会作用于角色的所有攻击，包括空手攻击和使用其他插件/模组武器时的攻击。

例如：头盔提供 `+0.1 stealHealth`，即使主手是其他插件的武器，攻击也会享受 10% 生命偷取。

## A.2 属性总表

| 配置键 | 名称 | 类型 | 说明 |
| --- | --- | --- | --- |
| `damage` | 伤害修正 | 数值 | 每次攻击的固定伤害增减，正负皆可，可暴击 |
| `appendDamage` | 附加伤害 | 数值 | 与 `damage` 独立叠加，不可暴击 |
| `crit` | 暴击几率 | 比例 | `0.2` = 20% 概率暴击；只统计本插件属性 |
| `critMul` | 暴击倍率 | 比例 | 触发暴击时的额外伤害，`0.25` = 额外 25% |
| `attackHeal` | 攻击治疗 | 数值 | 每次攻击固定回复，与造成伤害无关，不可暴击 |
| `currentEntityHealthDamage` | 目标当前生命百分比伤害 | 比例 | 附加“目标当前生命 × 比例”的伤害，不可暴击 |
| `maxEntityHealthDamage` | 目标最大生命百分比伤害 | 比例 | 附加“目标最大生命 × 比例”的伤害，不可暴击 |
| `stealHealth` | 生命偷取 | 比例 | 在所有伤害结算后按伤害吸血；吸血量不可暴击，但会计算暴击后的伤害 |
| `outputDamage` | 最终输出百分比 | 比例 | 在暴击后结算，对最终伤害（含暴击）加成，正负皆可 |
| `block` | 固定格挡 | 数值 | 在暴击后、生命偷取前结算，正负皆可，影响最终吸血量 |
| `backstab` | 背刺伤害 | 比例 | 从目标背面 120° 内攻击时加成，正负皆可 |
| `angerDamage` | 狂怒伤害 | 比例 | 攻击者每损失 1% 生命，伤害提升 `value×100%`（`0.1` → 每 1% 提升 10%，成长极高，慎用） |
| `executeDamage` | 斩杀伤害 | 比例 | 目标每损失 1% 生命，本次伤害提升 `value×100%`（数值过高会秒杀低血量目标） |
| `regurgitationCurrentHealthDamage` | 伤害反噬 | 比例 | 每次造成伤害时，自身受到“当前生命 × 比例”的反噬，正负皆可 |
| `giantDamage` | 巨人之力 | 比例 | 攻击者生命越高伤害越高；`0.005` = 每 1% 生命提高 0.5% 伤害 |
| `meleeDamage` | 近战输出 | 比例 | `0.1` = +10% 近战输出 |
| `meleePower` | 近战伤害 | 数值 | `10` = +10 点近战伤害，可暴击 |
| `meleeAppendDamage` | 近战附伤 | 数值 | `5` = +5 点近战伤害，不可暴击 |
| `rangedDamage` | 远程输出 | 比例 | `0.1` = +10% 远程输出，可作用于模组枪械 |
| `rangedPower` | 远程伤害 | 数值 | `10` = +10 点远程伤害，可暴击，可作用于模组枪械 |
| `rangedAppendDamage` | 远程附伤 | 数值 | `5` = +5 点远程伤害，不可暴击，可作用于模组枪械 |
| `repel` | 击退力度 | 数值 | `1` = 额外 1 倍击退距离 |
| `annihilation` | 处决 | 比例 | 目标生命低于该比例时直接处决；建议不超过 `1` |
| `playerKiller` | 玩家杀手 | 比例 | 对玩家造成 `value×100%` 额外伤害 |
| `tenacity` | 坚忍不屈 | 比例 | 每损失 1% 生命，减少 `value×100%` 承受伤害（`0.005` → 每 1% 减伤 0.5%） |
| `increasesDamageOnTheGround` | 地面攻击增益 | 比例 | 攻击者站在地面时额外造成 `value×100%` 伤害 |
| `increaseDefenseOnTheGround` | 地面防守增益 | 比例 | 受害者站在地面时减少 `value×100%` 承受伤害 |
| `armor` | 护甲 | 数值 | 正值为减伤、负值为增伤；100 护甲约 50% 减伤。负值减少的是**装备者**的护甲，不是穿透 |
| `armorIncrease` | 护甲提升 | 比例 | 提升 `armor` 的百分比，可为负；同样不是护甲穿透 |
| `armorSub` | 固定护甲穿透 | 数值 | 攻击时无视目标一定数值的护甲 |
| `armorDecrease` | 百分比护甲穿透 | 比例 | 攻击时无视目标一定比例的护甲 |
| `injuryReduction` | 伤害减免 | 比例 | `0.1` = 减少 10% 所受伤害，建议取正值 |
| `blow` | 击飞高度 | 数值 | 命中时把目标向上击飞的高度，建议取正值 |
| `innerName` | 内名 | 文本 | 仅在 `loreCheck: true` 时生效：物品 Lore 包含该文本时套用此模板属性；同一角色每个内名只生效一次 |

## A.3 机制详解（整理自 Wiki）

### damage / appendDamage

- `damage` 是固定伤害修正，可正可负；六格装备的数值会累加后作用于角色整体。例如头盔 `+10 damage`、护腿 `-7 damage`，角色整体为 `+3`，空手攻击也 +3。
- `appendDamage` 与 `damage` 互不冲突，同样六格累加。例如靴子 `+2`、副手盾牌 `+3`，则每次攻击额外 +5。
- `damage` 可暴击，`appendDamage` 不可暴击。

### crit / critMul

- `crit` 决定暴击概率，`critMul` 决定暴击后的额外伤害。
- 例：`+0.2 crit` 与 `+0.25 critMul` → 20% 概率暴击，暴击时额外造成 25% 伤害。
- 本插件独立计算自己的暴击，不与其他插件暴击互相影响；但开启原版伤害时，本插件暴击的倍率会作用到其他插件/模组加成后的伤害上。

### attackHeal

- 固定治疗量，与造成的伤害无关；没有造成伤害的攻击也会触发。
- 例：`+2 attackHeal` → 每次攻击回复 2 点生命；不可暴击。

### currentEntityHealthDamage

- 附加“目标当前生命 × 比例”的伤害，不可暴击。例：`+0.1` → 附加目标当前生命 10% 的伤害。

### stealHealth

- 在所有其他伤害属性结算完成后，最后结算生命偷取。
- 吸血治疗量不可暴击，但计算时会包含暴击产生的额外伤害。
- 例：`+0.1 stealHealth` → 任何攻击（包括空手）回复所造成伤害 10% 的生命。
