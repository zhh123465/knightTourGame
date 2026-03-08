package com.knighttour.view;

import com.knighttour.model.Position;
import com.knighttour.util.Constants;
import com.knighttour.util.ResourceManager;
import com.knighttour.util.Theme;
import com.knighttour.util.ThemeManager;
import javafx.beans.binding.Bindings;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.function.Consumer;

/**
 * 棋盘视图组件
 * 
 * 负责渲染8x8的棋盘网格，显示骑士位置和移动顺序。
 */
public class BoardView extends StackPane {

    private static final int CELL_SIZE = 60;
    private static final int BOARD_SIZE = Constants.BOARD_SIZE;

    private final GridPane gridPane;
    private final Rectangle[][] cellNodes = new Rectangle[BOARD_SIZE][BOARD_SIZE];
    private final Text[][] sequenceLabels = new Text[BOARD_SIZE][BOARD_SIZE];
    private final StackPane[][] cellPanes = new StackPane[BOARD_SIZE][BOARD_SIZE];
    private ImageView knightNode;
    private Consumer<Position> onCellClickedHandler;

    public BoardView() {
        this.gridPane = new GridPane();
        this.getChildren().add(gridPane);
        initializeBoard();
        ThemeManager.getInstance().addThemeChangeListener(this::updateTheme);
    }

    private void initializeBoard() {
        gridPane.setAlignment(Pos.CENTER);
        
        // Load knight image
        try {
            Image knightImage = ResourceManager.getInstance().getKnightImage();
            knightNode = new ImageView();
            if (knightImage != null) {
                knightNode.setImage(knightImage);
            }
            // Bind knight size to cell size
            // We will handle resizing in layout logic
        } catch (Exception e) {
            // Log error or ignore
        }

        Theme currentTheme = ThemeManager.getInstance().getCurrentTheme();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                StackPane stackPane = new StackPane();
                
                // Create cell rectangle
                Rectangle cell = new Rectangle();
                // Bind cell size to grid pane size
                // We want the grid to be square and fit within the parent
                // But individual cells in GridPane are usually sized by content or constraints.
                // A better approach for responsive grid:
                // Bind rectangle width/height to a property that depends on parent size.
                
                // Initial size
                cell.setWidth(CELL_SIZE);
                cell.setHeight(CELL_SIZE);
                
                // Set default chessboard pattern
                Color defaultColor = (row + col) % 2 == 0 ? currentTheme.getLightCellColor() : currentTheme.getDarkCellColor();
                cell.setFill(defaultColor);
                cell.setStroke(Color.BLACK);
                
                // Create sequence text
                Text text = new Text("");
                text.setFont(Font.font(20));
                text.setFill(currentTheme.getTextColor());
                text.setVisible(false);
                
                stackPane.getChildren().addAll(cell, text);
                
                // Store references
                cellNodes[row][col] = cell;
                sequenceLabels[row][col] = text;
                cellPanes[row][col] = stackPane;
                
                // Add click handler
                final int r = row;
                final int c = col;
                stackPane.setOnMouseClicked(e -> {
                    if (onCellClickedHandler != null) {
                        onCellClickedHandler.accept(new Position(r, c));
                    }
                });

                // Add hover effect
                stackPane.setOnMouseEntered(e -> {
                    if (cell.getFill().equals(defaultColor)) {
                         cell.setOpacity(0.8);
                    }
                });
                stackPane.setOnMouseExited(e -> {
                    cell.setOpacity(1.0);
                });

                gridPane.add(stackPane, col, row);
            }
        }
        
        // Responsive Layout Logic
        this.widthProperty().addListener((obs, oldVal, newVal) -> updateLayout());
        this.heightProperty().addListener((obs, oldVal, newVal) -> updateLayout());
    }

    public void setCellColor(Position pos, Color color) {
        if (pos == null) return;
        Rectangle cell = cellNodes[pos.getRow()][pos.getCol()];
        cell.setFill(color);
    }
    
    public void highlightCell(Position pos) {
        if (pos == null) return;
        
        // 使用叠加层实现深紫色闪烁
        StackPane pane = cellPanes[pos.getRow()][pos.getCol()];
        Rectangle highlightRect = new Rectangle();
        highlightRect.setFill(Color.web("#800080")); // 深紫色
        highlightRect.setOpacity(0.0);
        highlightRect.setMouseTransparent(true); // 确保不阻挡点击
        
        // 绑定大小
        highlightRect.widthProperty().bind(pane.widthProperty());
        highlightRect.heightProperty().bind(pane.heightProperty());
        
        pane.getChildren().add(highlightRect); // 添加到最上层
        
        javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(200), highlightRect);
        ft.setFromValue(0.0);
        ft.setToValue(0.8);
        ft.setCycleCount(6); // 3次闪烁 (亮-暗-亮-暗-亮-暗)
        ft.setAutoReverse(true);
        ft.setOnFinished(e -> pane.getChildren().remove(highlightRect));
        ft.play();
    }

    private void updateLayout() {
        double w = getWidth();
        double h = getHeight();
        
        if (w == 0 || h == 0) return;
        
        // Keep square aspect ratio
        double size = Math.min(w, h) * 0.9; // 90% of available space
        double cellSize = size / BOARD_SIZE;
        
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Rectangle cell = cellNodes[row][col];
                cell.setWidth(cellSize);
                cell.setHeight(cellSize);
                
                Text text = sequenceLabels[row][col];
                text.setFont(Font.font(cellSize * 0.4)); // Scale font
            }
        }
        
        if (knightNode != null) {
            knightNode.setFitWidth(cellSize * 0.8);
            knightNode.setFitHeight(cellSize * 0.8);
        }
    }

    public void updateCellSequence(Position pos, int sequence) {
        if (isValidPosition(pos)) {
            Text text = sequenceLabels[pos.getRow()][pos.getCol()];
            text.setText(String.valueOf(sequence));
            text.setVisible(true);
        }
    }

    public void clearCellSequence(Position pos) {
        if (isValidPosition(pos)) {
            Text text = sequenceLabels[pos.getRow()][pos.getCol()];
            text.setText("");
            text.setVisible(false);
            
            // Restore default color
            resetCellColor(pos);
        }
    }
    
    public void resetCellColor(Position pos) {
        if (isValidPosition(pos)) {
            int row = pos.getRow();
            int col = pos.getCol();
            Theme currentTheme = ThemeManager.getInstance().getCurrentTheme();
            Color defaultColor = (row + col) % 2 == 0 ? currentTheme.getLightCellColor() : currentTheme.getDarkCellColor();
            cellNodes[row][col].setFill(defaultColor);
        }
    }
    
    public void flashRedX(Position pos) {
        if (pos == null) return;
        
        StackPane pane = cellPanes[pos.getRow()][pos.getCol()];
        Text xMark = new Text("X");
        xMark.setFill(Color.RED);
        xMark.setFont(Font.font("Arial", javafx.scene.text.FontWeight.BOLD, 40));
        xMark.setMouseTransparent(true);
        xMark.setOpacity(0.0);
        
        pane.getChildren().add(xMark);
        
        javafx.animation.FadeTransition ft = new javafx.animation.FadeTransition(javafx.util.Duration.millis(200), xMark);
        ft.setFromValue(0.0);
        ft.setToValue(1.0);
        ft.setCycleCount(6);
        ft.setAutoReverse(true);
        ft.setOnFinished(e -> pane.getChildren().remove(xMark));
        ft.play();
    }

    public void markInvalidCell(Position pos) {
        if (isValidPosition(pos)) {
            // 叠加红色半透明层
            StackPane pane = cellPanes[pos.getRow()][pos.getCol()];
            
            // 检查是否已经标记过，避免重复叠加
            boolean alreadyMarked = pane.getChildren().stream()
                .filter(node -> node instanceof Rectangle)
                .map(node -> (Rectangle)node)
                .anyMatch(rect -> Color.rgb(255, 0, 0, 0.3).equals(rect.getFill()));
                
            if (alreadyMarked) return;

            Rectangle invalidRect = new Rectangle();
            invalidRect.setFill(Color.rgb(255, 0, 0, 0.3));
            invalidRect.setMouseTransparent(true);
            invalidRect.widthProperty().bind(pane.widthProperty());
            invalidRect.heightProperty().bind(pane.heightProperty());
            
            pane.getChildren().add(invalidRect);
        }
    }
    
    public void clearOverlays() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                StackPane pane = cellPanes[row][col];
                
                // Lambda required final variables
                final int r = row;
                final int c = col;
                
                // 移除所有临时的叠加层
                pane.getChildren().removeIf(node -> {
                    if (node == cellNodes[r][c]) return false;
                    if (node == sequenceLabels[r][c]) return false;
                    if (node == knightNode) return false;
                    return true;
                });
            }
        }
    }

    public void setOnCellClicked(Consumer<Position> handler) {
        this.onCellClickedHandler = handler;
    }

    public ImageView getKnightNode() {
        return knightNode;
    }
    
    private boolean isValidPosition(Position pos) {
        return pos != null && pos.isValid();
    }
    
    public void resetBoard() {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Position pos = new Position(row, col);
                clearCellSequence(pos);
                resetCellColor(pos);
            }
        }
    }
    
    public void placeKnight(Position pos) {
        // Remove knight from previous parent if any
        if (knightNode.getParent() != null) {
            ((StackPane)knightNode.getParent()).getChildren().remove(knightNode);
        }
        
        if (isValidPosition(pos)) {
            cellPanes[pos.getRow()][pos.getCol()].getChildren().add(knightNode);
        }
    }

    private void updateTheme(Theme theme) {
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                Color defaultColor = (row + col) % 2 == 0 ? theme.getLightCellColor() : theme.getDarkCellColor();
                
                // Update cell color if it's not currently highlighted or special
                // Simple logic: just reset to default color for now, or check if it matches old default
                // Ideally we should track state better, but resetCellColor will do the job for unvisited cells
                // For visited cells (handled by external controller/solver), they might have specific colors?
                // Actually SolverController uses setCellColor. 
                // For now, let's just refresh the base board colors.
                // If a cell is "active" (visited), its color might need to be preserved or re-applied.
                // But SolverController usually sets color based on sequence.
                // Let's just update the "default" look.
                
                // A safer approach for now: Reset everything or just update properties that don't affect game state visualization too much.
                // But changing theme mid-game might be tricky if we don't know which cells are visited.
                // However, the text color should definitely be updated.
                sequenceLabels[row][col].setFill(theme.getTextColor());
                
                // If the cell is NOT visited (sequence 0), update its background
                if (sequenceLabels[row][col].getText().isEmpty()) {
                    cellNodes[row][col].setFill(defaultColor);
                }
            }
        }
    }
}
