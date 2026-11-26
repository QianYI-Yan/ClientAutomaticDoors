[中文](./CHANGELOG.md)
[English](./CHANGELOG-en.md)
# 0.1.0
## 新增
- 自动开关门、活板门、栅栏门。
# 0.1.1
## 修复
- 与某些客户端模组可能的冲突
- 潜行时仍尝试开关门。
- 网站链接。
# 0.1.2
## 修复
- 自由相机会触发开门。
# 0.2.0
## 新增
- 添加了对《Dramatic Doors》的支持。
## 修复
- 某些拥有`open`方块状态但不是门的方块被视为门。
# 0.3.0
## 更改
- 如果开门会导致依附于门上的方块掉落，则不开门。
- 如果可以直接走到门上方，则不开门。([#1](https://github.com/Phoupraw/ClientAutomaticDoors/issues/1))
- 如果安装了《投影》，则手持木棍时不开关门。
- 如果安装了《创世神》，则手持木斧时不开关门。
## 修复
- 连续碰撞并开门可能导致最终方块状态与初始不同。
- 即使开门后仍无法通过也会开门。
- 即使玩家无碰撞箱也会开关门。
- 1刻内多次移动可能提前关门导致服务端玩家移动错误。
# 26.3-1
## 更改
- 移植到 Minecraft 26.3（非混淆版本，需要 Java 25）。
- 内置配置系统替代 TrilevelConfig，仅保留全局开关；不再支持按存档开关。
- 自定义方块注册 API 由 fabric `BlockApiLookup` 改为 `BlockShapeToggler.LOOKUP.registerFallback`（fabric 26.3 已移除 BlockApiLookup）。