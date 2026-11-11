# 第三方代码与素材声明

本仓库的主页 UI 代码由仓库作者本人编写，以 MIT 授权（见根目录 `LICENSE`）。
以下并入了第三方内容，各自按其许可分发。

## vendored 代码

### kyant backdrop（`thirdparty/backdrop/`）

- **来源**：`io.github.kyant0:backdrop:2.0.1`（仓库 [Kyant0/backdrop](https://github.com/Kyant0/backdrop)，作者 Kyant）
- **许可证**：Apache-2.0（全文见 `thirdparty/backdrop/LICENSE`）
- **改动**：加了 `sampling` 开关（纯色 / 渐变背景时关闭离屏采样但保留高光与投影）、
  内容单遍渲染、`BlurMaskFilter` 复用、删掉折射色散。其余视觉代码与上游一致，详见
  `thirdparty/backdrop/README.md`。

## Maven 依赖（未并入源码，按各自许可证分发）

| 用途 | 来源 | 许可证 |
| --- | --- | --- |
| MIUIX KMP 组件（主题 / 导航栏 / 弹层 / 超椭圆圆角） | [miuix-kmp](https://github.com/miuix-kmp/miuix)（`top.yukonga.miuix.kmp`） | Apache-2.0 |
| Compose Multiplatform / AndroidX Compose | JetBrains / AndroidX | Apache-2.0 |
| kotlinx.coroutines / serialization / datetime | JetBrains | Apache-2.0 |
| kyant shapes（vendored backdrop 的 Lens 采样形状） | [Kyant0/shapes](https://github.com/Kyant0/shapes) | Apache-2.0 |
| androidx graphics-shapes（超椭圆圆角） | AndroidX | Apache-2.0 |
| zxing（二维码生成，android actual） | [zxing/zxing](https://github.com/zxing/zxing) | Apache-2.0 |

## 素材边界

原工具箱应用图标中的「占位头像」形象版权归占位头像制作组所有，其授权 **不允许下游 fork 沿用**。
本仓库不包含该形象，应用图标为自绘简易图形。
