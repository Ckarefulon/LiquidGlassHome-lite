# 第三方代码与素材声明

本仓库从 [xjtu-toolbox-android](https://github.com/yeliqin666/xjtu-toolbox-android)（GPL-3.0）提取主页 UI 相关代码，整体继承 GPL-3.0。以下是其中并入的第三方内容。

## vendored 代码

### kyant backdrop（`thirdparty/backdrop/`）

- **来源**：[Kyant0/AndroidLiquidGlass](https://github.com/Kyant0/AndroidLiquidGlass) 的 backdrop 库，上游版本 2.0.1
- **许可证**：Apache-2.0（全文见 `thirdparty/backdrop/LICENSE`）
- **改动**：给 `drawBackdrop` 加 `sampling` 开关（纯色 / 渐变背景下关闭离屏采样但保留高光投影），其余视觉代码与上游一致。详见 `thirdparty/backdrop/README.md`。

## Maven 依赖（未并入源码，按各自许可证分发）

| 用途 | 来源 | 许可证 |
| --- | --- | --- |
| MIUIX KMP 组件（主题 / 导航栏 / 弹层 / 导航栈 / 超椭圆圆角） | [miuix-kmp](https://github.com/miuix-kmp/miuix)（top.yukonga.miuix.kmp） | Apache-2.0 |
| Compose Multiplatform / AndroidX Compose | JetBrains / AndroidX | Apache-2.0 |
| kotlinx.coroutines / serialization / datetime | JetBrains | Apache-2.0 |
| zxing（二维码生成，android/jvm actual） | [zxing/zxing](https://github.com/zxing/zxing) | Apache-2.0 |

## 素材边界

上游应用图标中的「交小西」形象版权归交小西制作组所有，其授权**不允许下游 fork 沿用**。本仓库不包含该形象，应用图标为自绘简易图形。