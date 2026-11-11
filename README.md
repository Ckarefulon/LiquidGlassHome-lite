# LiquidGlassHome-lite

西安交通大学工具箱（[xjtu-toolbox-android](https://github.com/yeliqin666/xjtu-toolbox-android)）**主页 UI 的独立展示仓库**。

原仓库里这套主页界面（卡片组 / 经典分类卡两套布局、液态玻璃底栏、拖拽编排、主页设置抽屉）经过多轮迭代后被移除；这里把主页相关的全部 UI 代码合在一起保存下来，**所有业务数据替换为占位内容**，可以直接编译安装查看效果。

## 包含什么

- **两套主页布局**：卡片组（默认，刊头 + 头卡 + 可编辑分组网格）与经典分类卡（回落），在「主页设置」里切换
- **编辑态**：长按拖拽移动 / 缩放卡片、组排序、添加卡片悬浮球、幽灵块落点预览
- **液态玻璃**：玻璃胶囊底栏（可拖动切 tab）、顶栏圆钮的模糊 / 活力 / 高光 / 折射采样，vendored [kyant backdrop](thirdparty/backdrop/README.md)（Apache-2.0，加了 sampling 开关）
- **主页设置抽屉**：布局 / 背景 / 壁纸 / 液态玻璃 / 网格 / 圆角 / 不透明度 / 布局导出导入
- **底栏五格**：首页 / 日程 / 小岱 / 学辅 / 我的 —— 除首页外全部是占位页；首页点开的任何功能入口也落在占位页
- 宽屏侧栏、经典底栏 / 悬浮胶囊等壳形态（共享壳 `:core` 按屏形状自动切换）

## 不包含什么

- 登录、网络请求、教务 / 校园卡 / 图书馆等一切真实业务（占位宿主 `LiteHomeHost` 喂假数据）
- 公告条（bulletin）与公告详情
- 「一镜到底」SharedViewKit（依赖私有 Nexus）：共享层几何与 Compose 放大转场保留，系统级转场槽位留空自动回落
- 原应用图标形象（交小西，授权不允许下游沿用）：本仓库用自绘简易图标

## 模块

| 模块 | 内容 |
| --- | --- |
| `:app` | 极简 Android 壳：占位宿主 + 底栏五格 + 占位页 |
| `:core` | 共享应用外壳（顶栏 + 底栏 / 侧栏 + 玻璃采样层） |
| `:feature:home` | 主页两套布局、拖拽、卡片模型 |
| `:feature:settings` | 主页设置抽屉三件 |
| `:shared:designsystem` | MIUIX 主题、玻璃组件、通用组件 |
| `:shared:navigation` | 路由表、服务目录、布局落盘 |
| `:shared:model` | 纯 Kotlin 模型与平台缝（整层保留，主页依赖它的课表 / 时间 / 设置等模型） |
| `:thirdparty:backdrop` | vendored kyant backdrop（Apache-2.0） |
| `build-logic` | KMP 约定插件（android + jvm + wasmJs 三端，交付物只构建 Android） |

## 构建

需要 JDK 21 与 Android SDK（compileSdk 37，minSdk 31）：

```bash
./gradlew :app:assembleDebug
```

CI（GitHub Actions）在每次推送时构建 APK 并跑 JVM 单测，产物见 Actions artifacts。

## 许可

GPL-3.0（继承上游仓库许可，见 [LICENSE](LICENSE)）。第三方代码与素材声明见 [THIRD_PARTY_NOTICES.md](THIRD_PARTY_NOTICES.md)。

主页交互需求文档随代码保留在 [docs/home-interaction-spec.md](docs/home-interaction-spec.md)。