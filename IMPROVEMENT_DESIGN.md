# 骑士巡游游戏改进方案技术设计文档 (IMPROVEMENT_DESIGN.md)

本文档基于 `DESIGN_DOC.md` 中的改进建议，详细描述了技术实现方案。待确认无误后，将按照此文档进行代码开发。

## 1. 算法架构重构 (Strategy Pattern)

为了支持多种求解算法并在运行时切换，我们将引入 **策略模式 (Strategy Pattern)**。

### 1.1 接口定义
创建一个通用的求解策略接口 `KnightTourAlgorithm`，将具体的寻路逻辑从 `KnightTourSolver` 中解耦。

```java
package com.knighttour.algorithm;

import com.knighttour.model.Board;
import com.knighttour.model.Position;
import java.util.List;

public interface KnightTourAlgorithm {
    /**
     * 获取当前位置的下一步可能移动列表
     * @param board 当前棋盘状态
     * @param currentPos 当前位置
     * @return 排序后的合法移动列表（排序规则由具体算法决定）
     */
    List<Position> findNextMoves(Board board, Position currentPos);
    
    /**
     * 获取算法名称（用于 UI 显示）
     */
    String getName();
}
```

### 1.2 算法实现类
*   `WarnsdorffAlgorithm`: 现有的启发式逻辑（优先选择度数最小的节点）。
*   `DfsAlgorithm`: 纯深度优先搜索（按照固定顺序，如顺时针方向尝试移动）。
*   `BfsAlgorithm` (可选): 广度优先搜索（通常用于寻找最短路径，但对于骑士巡游这种全覆盖问题，可能需要配合状态压缩，实现较复杂，初期可作为探索性功能）。

### 1.3 Solver 改造
`KnightTourSolver` 将不再直接包含 `MoveGenerator` 的逻辑，而是持有一个 `KnightTourAlgorithm` 实例。

```java
public class KnightTourSolver {
    private KnightTourAlgorithm algorithm;

    public void setAlgorithm(KnightTourAlgorithm algorithm) {
        this.algorithm = algorithm;
    }
    
    // 在求解循环中调用:
    // List<Position> nextMoves = algorithm.findNextMoves(board, currentPos);
}
```

## 2. 主题与资源系统

### 2.1 资源管理 (`ResourceManager`)
为了统一管理图片和音效资源，避免路径硬编码，创建一个单例 `ResourceManager`。

*   **图片资源**:
    *   为了兼容性和避免编码问题，建议将 `src/main/resources/images/画线条小马.png` 重命名为 `knight_sketch.png`。
    *   加载逻辑：`Image knightImage = new Image(getClass().getResourceAsStream("/images/knight_sketch.png"));`
*   **音效资源 (预留)**:
    *   定义 `SoundType` 枚举 (`MOVE`, `SUCCESS`, `FAIL`)。
    *   提供 `playSound(SoundType type)` 方法（当前为空实现，后续添加 `.wav` 文件后实现）。

### 2.2 主题系统 (`ThemeManager`)
引入主题切换功能，支持“深色模式”和“木纹风格”。

```java
public enum Theme {
    CLASSIC("经典", Color.WHITE, Color.LIGHTGRAY, Color.BLACK),
    DARK("深色模式", Color.rgb(50, 50, 50), Color.rgb(100, 100, 100), Color.WHITE),
    WOOD("木纹风格", Color.rgb(222, 184, 135), Color.rgb(139, 69, 19), Color.BLACK);
    
    // 包含浅色格、深色格、线条/文字颜色等属性
}
```

`BoardView` 将订阅 `ThemeManager` 的变更事件，并在主题改变时重绘棋盘。

## 3. 响应式布局 (Responsive Layout)

优化 `MainWindow` 和 `BoardView`，使其能够适应窗口大小的变化。

### 3.1 布局调整
*   **BoardView**:
    *   将 `BoardView` 的 `GridPane` 放置在一个 `StackPane` 或 `BorderPane` 的中心。
    *   利用 JavaFX 的 `Bindings` 机制，将棋盘单元格大小 (`cellWidth`, `cellHeight`) 绑定到父容器的宽度和高度的最小值。
    *   `cellSize = min(parent.width, parent.height) / BOARD_SIZE`。
*   **ControlPanel**:
    *   使用 `VBox` 或 `FlowPane`，确保在窗口变窄时控件不会重叠或消失。

## 4. 代码质量与工程化

### 4.1 异常处理
*   **全局异常捕获**: 在 `Main` 类中设置 `Thread.setDefaultUncaughtExceptionHandler`，捕获未处理的运行时异常并弹窗提示。
*   **输入验证**: 在 `Validator` 类中增强对坐标输入的校验（格式、范围），并返回友好的错误信息，而不是直接抛出异常。

### 4.2 单元测试 (JUnit 5)
*   增加 `SolverControllerTest`: 使用 `Mockito` 模拟 `KnightTourSolver` 和 `BoardView`，验证控制逻辑（开始、暂停、重置）是否正确触发。
*   增加 `BoardViewTest` (TestFX): 如果条件允许，引入 TestFX 进行 UI 自动化测试；或者至少对 `BoardView` 的状态更新逻辑进行单元测试。

### 4.3 模块化 (`module-info.java`)
为了符合现代 Java 标准并支持 `jlink`/`jpackage`，将项目模块化。

```java
module com.knighttour {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    
    exports com.knighttour;
    exports com.knighttour.controller;
    exports com.knighttour.model;
    exports com.knighttour.view;
    // ... 其他包导出
}
```

### 4.4 构建脚本 (`jpackage`)
在 `pom.xml` 或独立的脚本中添加 `jpackage` 命令，用于生成原生安装包。

**Windows 示例脚本 (`package_windows.bat`)**:
```bat
jpackage ^
  --name "KnightTourGame" ^
  --input target/ ^
  --main-jar knight-tour-game-1.0.0.jar ^
  --main-class com.knighttour.Main ^
  --type app-image ^
  --icon src/main/resources/images/icon.ico ^
  --win-dir-chooser ^
  --win-menu ^
  --win-shortcut
```
*(注：需要确保 JDK 版本支持 jpackage，且已安装 WiX Toolset)*

## 5. 开发计划

1.  **Phase 1 (资源与重构)**:
    *   重命名图片资源。
    *   创建 `ResourceManager`。
    *   实现 `ThemeManager` 并应用到 `BoardView`。
2.  **Phase 2 (算法扩展)**:
    *   提取 `KnightTourAlgorithm` 接口。
    *   实现 `WarnsdorffAlgorithm` 和 `DfsAlgorithm`。
    *   在 UI 添加算法选择器。
3.  **Phase 3 (响应式与优化)**:
    *   实现响应式布局绑定。
    *   增强异常处理和单元测试。
4.  **Phase 4 (工程化)**:
    *   添加 `module-info.java`。
    *   配置构建脚本。
