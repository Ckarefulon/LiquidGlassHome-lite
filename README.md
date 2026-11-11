# LiquidGlass Home

**可复用的「液态玻璃」主页 UI 模板**（Android / Compose Multiplatform，Kotlin）。

一套现成的液态玻璃主页界面：卡片组网格、玻璃胶囊底栏、顶栏圆钮的模糊与折射、长按拖拽编排、
主页设置抽屉。代码由仓库作者本人编写，业务数据全部是占位内容——拿来当自己 App 的主页骨架，
把 `HomeHost` 接到你的真实数据源即可。

## 包含什么

- **卡片组主页**：刊头 + 头卡 + 可编辑分组网格
- **编辑态**：长按拖拽移动 / 缩放卡片、组排序、添加卡片悬浮球、幽灵块落点预览
- **液态玻璃**：玻璃胶囊底栏（可拖动切 tab）、顶栏圆钮的模糊 / 活力 / 高光 / 折射采样，
  vendored [kyant backdrop](thirdparty/backdrop/README.md)（Apache-2.0，加了 sampling 开关）
- **主页设置抽屉**：背景 / 壁纸 / 液态玻璃 / 网格 / 圆角 / 不透明度 / 布局导出导入
- **底栏五格**：首页 / 日程 / 小岱 / 学辅 / 我的 —— 除首页外全部是占位页；首页点开的任何功能入口也落在占位页

## 不包含什么

- 登录、网络请求、教务 / 校园卡 / 图书馆等任何真实业务（占位宿主 `LiteHomeHost` 喂硬编码假数据）
- 公告条（bulletin）与公告详情
- 「一镜到底」SharedViewKit（依赖私有 Nexus）；共享层几何与 Compose 放大转场保留，系统级转场槽位留空
- 原应用图标形象（交小西，授权不允许下游沿用）：本仓库用自绘简易图标

## 模块

| 模块 | 内容 |
| --- | --- |
| `:app` | 极简 Android 壳：`Application` 初始化 + 占位宿主 + 底栏五格 + 占位页 |
| `:core` | 共享应用外壳（顶栏 + 底栏 / 侧栏 + 玻璃采样层） |
| `:feature:home` | 主页、拖拽、卡片模型 |
| `:feature:settings` | 主页设置抽屉三件 |
| `:shared:designsystem` | MIUIX 主题、玻璃组件、通用组件 |
| `:shared:navigation` | 路由表、服务目录、布局落盘 |
| `:shared:model` | 纯 Kotlin 模型与平台缝（主页依赖它的课表 / 时间 / 设置等模型） |
| `:thirdparty:backdrop` | vendored kyant backdrop（Apache-2.0） |
| `build-logic` | KMP 约定插件（**只构建 Android 一端**） |

## 构建

需要 JDK 21 与 Android SDK（compileSdk 37，minSdk 31）：

```bash
./gradlew :app:assembleRelease
```

发布变体开了 R8（代码 + 资源裁剪），APK 体积依赖裁剪才有意义；debug 变体只用于本机排查。
CI（GitHub Actions）在每次推送时构建 **已签名的 release APK** 并跑单元测试，产物见 Actions artifacts。

### 打包与签名

`release` 走 `signingConfigs`，两种方式任选：

- **CI**：配好四个 GitHub Secret —— `LIQUIDGLASS_KEYSTORE_BASE64`（keystore 的 base64）、
  `LIQUIDGLASS_KEYSTORE_PASSWORD`、`LIQUIDGLASS_KEY_ALIAS`、`LIQUIDGLASS_KEY_PASSWORD`。
  workflow 会在构建后用 `apksigner verify` 复核，未签名直接判失败。
- **本地**：在仓库根放一份 `keystore.properties`（已在 `.gitignore` 里）：

  ```properties
  storeFile=/绝对路径/your-release.jks
  storePassword=...
  keyAlias=...
  keyPassword=...
  ```

两处都没配时仍会产出 APK，但**未签名、装不上**，Gradle 会打印一行提示。

## 许可

本仓库的液态玻璃主页 UI 代码由仓库作者编写，以 **MIT** 授权（见 [LICENSE](LICENSE)）。
唯一的例外是 vendored 的第三方代码 `thirdparty/backdrop`（Apache-2.0，版权归 Kyant），
以及各 Maven 依赖按各自许可分发；详见 [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md)。

主页交互需求文档随代码保留在 [docs/home-interaction-spec.md](docs/home-interaction-spec.md)。
