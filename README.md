# 马踏棋盘游戏 (Knight's Tour Game)

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com/zhh123465/knightTourGame)
[![License](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

## 项目简介

**马踏棋盘游戏** 是一个基于 JavaFX 的桌面应用程序，旨在演示经典的“骑士周游问题”（Knight's Tour Problem）的求解过程。该项目不仅是一个游戏，更是一个用于教学和演示回溯算法（Backtracking Algorithm）及其优化策略（Warnsdorff 规则）的工具。

### 主要功能

*   **可视化求解**: 实时展示骑士在 8x8 棋盘上的移动路径和回溯过程。
*   **交互控制**: 支持开始、暂停、重置和单步执行操作。
*   **任意起始点**: 用户可以指定棋盘上任意位置作为起始点。
*   **性能优异**: 采用 Warnsdorff 启发式算法，能在毫秒级内找到解。
*   **统计信息**: 实时显示已访问格数、总步数、回溯次数和耗时。
*   **导出功能**: 支持将找到的解矩阵导出为文本文件。
*   **自定义设置**: 可调整动画速度、颜色方案等。

## 技术栈

*   **编程语言**: Java 11+
*   **GUI 框架**: JavaFX 11
*   **构建工具**: Maven
*   **测试框架**: JUnit 5, AssertJ, jqwik (属性测试), Mockito

## 安装与运行

### 前置条件

*   JDK 11 或更高版本
*   Maven 3.6+

### 构建项目

```bash
git clone https://github.com/zhh123465/knightTourGame.git
cd knight-tour-game
mvn clean package
```

### 运行应用

**方式 1: 使用 Windows 安装程序 (推荐)**

1.  下载最新版本的安装包: [Releases](https://github.com/zhh123465/knightTourGame/releases)
2.  下载 `KnightTourGame_Setup_x.x.x.exe`。
3.  双击运行安装程序，按照向导完成安装。
4.  安装完成后，可通过桌面快捷方式或开始菜单启动游戏。
    *   **注意**: 此方式无需预先安装 Java 环境，程序已内置运行时。

**方式 2: 使用免安装绿色版**

1.  在 [Releases](https://github.com/zhh123465/knightTourGame/releases) 页面下载 `KnightTourGame-Windows.zip`。
2.  解压 ZIP 文件到任意目录。
3.  进入解压后的文件夹，双击 `KnightTourGame.exe` 即可运行。

**方式 3: 使用 Maven 插件运行 (开发者)**

```bash
mvn javafx:run
```

**方式 4: 运行打包后的 JAR**

```bash
java -jar target/knight-tour-game-1.0.0.jar
```

*注意: 方式 3 和 4 需要您的环境已正确配置 JavaFX 运行时，或者使用包含 JavaFX 的 JDK。*

## 使用说明

1.  **启动应用**: 运行程序后，将看到主窗口。
2.  **设置起始点**: 在控制面板左侧输入起始坐标（格式：行,列，例如 `0,0`），或直接点击棋盘上的方格。
3.  **开始求解**: 点击“开始”按钮，骑士将开始移动。
4.  **控制过程**:
    *   **暂停**: 点击“暂停”按钮暂停动画。
    *   **单步**: 在暂停状态下，点击“单步”按钮执行一步移动。
    *   **速度**: 拖动下方滑块调整动画速度。
5.  **重置**: 点击“重置”按钮清空棋盘，准备下一次求解。
6.  **导出**: 找到解后，可通过菜单栏 `文件 -> 导出解矩阵` 保存结果。

## 算法原理

本项目使用 **深度优先搜索 (DFS)** 结合 **Warnsdorff 启发式规则**。

*   **基本回溯**: 尝试每一个可能的移动，如果走不通则回退。
*   **Warnsdorff 规则**: 在选择下一步移动时，优先选择那些“后续可行移动数最少”的格子。这种贪心策略极大地减少了回溯次数，使得算法在大多数情况下能线性时间找到解。

## 项目结构

```
src/main/java/com/knighttour/
├── algorithm/      # 核心算法实现 (KnightTourSolver, MoveGenerator)
├── controller/     # 控制器层 (GameController, SolverController)
├── model/          # 数据模型 (Board, Position, Solution)
├── util/           # 工具类 (ConfigManager, Validator)
├── view/           # JavaFX 视图组件 (MainWindow, BoardView)
└── Main.java       # 应用程序入口
```

## 贡献

欢迎提交 Issue 或 Pull Request 来改进本项目！

## 许可证

本项目采用 MIT 许可证。详见 [LICENSE](LICENSE) 文件。
