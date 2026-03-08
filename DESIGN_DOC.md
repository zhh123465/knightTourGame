# 骑士巡游游戏 (Knight's Tour Game) 设计与分析文档

## 1. 项目概览

本项目是一个基于 JavaFX 的桌面应用程序，旨在可视化演示“骑士周游问题”的求解过程。项目采用经典的回溯算法（Backtracking）结合 Warnsdorff 启发式规则，能够高效地在 8x8 棋盘上找到解，并提供实时的动画演示。

### 技术栈
- **编程语言**: Java 8+
- **GUI 框架**: JavaFX
- **构建工具**: Maven
- **测试框架**: JUnit 5, Mockito
- **并发模型**: Java Util Concurrent (ExecutorService)

## 2. 架构设计

项目采用经典的 **MVC (Model-View-Controller)** 架构模式，确保关注点分离，易于维护和扩展。

### 2.1 模块划分

*   **Model (`com.knighttour.model`)**:
    *   `Board`: 维护棋盘状态（单元格访问情况、访问顺序）。
    *   `Position`: 封装坐标 (row, col)。
    *   `Solution`: 存储求解结果（路径矩阵、耗时、步数）。
    *   `Move`: 记录单步移动信息。

*   **View (`com.knighttour.view`)**:
    *   `MainWindow`: 主窗口容器，整合各个面板。
    *   `BoardView`: 核心视图，使用 `GridPane` 渲染棋盘，负责骑士移动动画和路径显示。
    *   `ControlPanel`: 提供开始、暂停、重置、速度调节等控制组件。
    *   `StatisticsPanel`: 实时显示步数、回溯次数等统计信息。

*   **Controller (`com.knighttour.controller`)**:
    *   `GameController`: 主控制器，协调 View 和 SolverController，处理用户输入和菜单事件。
    *   `SolverController`: 算法适配器，管理 `KnightTourSolver` 的生命周期（启动、暂停、停止），并处理线程调度，将算法事件桥接到 UI 线程。

*   **Algorithm (`com.knighttour.algorithm`)**:
    *   `KnightTourSolver`: 核心求解器，实现非递归回溯算法。
    *   `MoveGenerator`: 生成合法移动，实现了 Warnsdorff 启发式规则（优先选择后续可行移动最少的格子）。

### 2.2 核心流程

1.  **启动**: `Main` 类启动 JavaFX 应用，初始化 `MainWindow` 和 `GameController`。
2.  **交互**: 用户在 `BoardView` 点击设置起始点，或在 `ControlPanel` 输入坐标。
3.  **求解**:
    *   用户点击“开始”。
    *   `GameController` 调用 `SolverController.solve()`。
    *   `SolverController` 在后台线程 (`ExecutorService`) 中启动 `KnightTourSolver`。
4.  **更新**:
    *   `KnightTourSolver` 每执行一步，触发 `SolverListener`。
    *   `SolverController` 捕获事件，通过 `Platform.runLater` 将更新任务提交到 JavaFX Application Thread。
    *   `BoardView` 更新 UI，`KnightAnimator` 执行平滑移动动画。
5.  **结束**: 找到解或无解时，弹出对话框提示，并可导出结果。

## 3. 代码分析

### 3.1 核心算法 (`KnightTourSolver`)
*   **实现**: 采用非递归 DFS（使用 `Stack<MoveStackEntry>`），避免了深度递归导致的 `StackOverflowError`。
*   **优化**: `MoveGenerator` 实现了 Warnsdorff 规则，极大地减少了回溯次数，使得算法在 8x8 棋盘上几乎能线性时间求解。
*   **并发**: 算法内部通过 `wait/notify` 机制支持暂停和单步执行，设计巧妙。

### 3.2 视图渲染 (`BoardView`)
*   使用 `GridPane` 布局，每个格子是一个 `StackPane`（包含 `Rectangle` 和 `Text`）。
*   骑士图标使用 `ImageView`，目前代码中使用了占位符，建议完善资源加载。

### 3.3 线程安全
*   耗时的计算任务严格在后台线程执行。
*   UI 更新严格在 JavaFX 线程执行。
*   状态共享通过 `volatile` 或 `synchronized` 保证可见性。

## 4. 改进建议与计划

经过扫描与分析，识别出以下可完善的部分：

### 4.1 功能增强 (High Priority)
1.  **自定义棋盘大小**: 目前 `Board.SIZE` 硬编码为 8。建议改为动态配置，支持 5x5 到 12x12 等不同规格。
2.  **算法选择**: 目前仅支持 Warnsdorff。可增加“纯 DFS”、“BFS”或“神经网络”等算法供用户对比学习。
3.  **导出格式**: 目前仅支持文本导出。可增加图片导出 (PNG/JPEG) 或 PDF 报告。

### 4.2 UI/UX 体验 (Medium Priority)
1.  **国际化 (i18n)**: 目前界面文本硬编码为中文。应提取到 `messages.properties`，支持中英切换。
2.  **主题切换**: 提供“深色模式”、“木纹风格”等多种棋盘皮肤。
3.  **资源完善**: 添加高质量的骑士图标和音效（移动声、胜利声）。
4.  **响应式布局**: 优化 `MainWindow` 在不同分辨率下的显示效果。

### 4.3 代码质量与工程化 (Low Priority)
1.  **异常处理**: 增强 `GameController` 中的输入验证和异常捕获，避免抛出原始 `RuntimeException`。
2.  **单元测试**: 增加 `SolverController` 和 `BoardView` 的单元测试覆盖率。
3.  **模块化**: 考虑升级到 Java 11+ 模块化系统 (`module-info.java`)。
4.  **构建脚本**: 增加 `jpackage` 脚本，自动生成 Windows/macOS/Linux 原生安装包。

## 5. 结论

该项目架构清晰，核心算法实现高效，具备良好的教学演示价值。通过实施上述改进计划，特别是支持自定义棋盘大小和国际化，可以显著提升项目的实用性和用户体验。
