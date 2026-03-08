package com.knighttour.view;

import com.knighttour.model.Position;
import com.knighttour.util.Constants;
import com.knighttour.util.ResourceManager;
import com.knighttour.util.Theme;
import com.knighttour.util.ThemeManager;
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
public class BoardView extends GridPane {

    private static final int CELL_SIZE = 60;
    private static final int BOARD_SIZE = Constants.BOARD_SIZE;

    private final Rectangle[][] cellNodes = new Rectangle[BOARD_SIZE][BOARD_SIZE];
    private final Text[][] sequenceLabels = new Text[BOARD_SIZE][BOARD_SIZE];
    private final StackPane[][] cellPanes = new StackPane[BOARD_SIZE][BOARD_SIZE];
    private ImageView knightNode;
    private Consumer<Position> onCellClickedHandler;

    public BoardView() {
        initializeBoard();
        ThemeManager.getInstance().addThemeChangeListener(this::updateTheme);
    }

    private void initializeBoard() {
        this.setAlignment(Pos.CENTER);
        
        // Load knight image
        try {
            Image knightImage = ResourceManager.getInstance().getKnightImage();
            knightNode = new ImageView();
            if (knightImage != null) {
                knightNode.setImage(knightImage);
            }
            knightNode.setFitWidth(CELL_SIZE * 0.8);
            knightNode.setFitHeight(CELL_SIZE * 0.8);
        } catch (Exception e) {
            // Log error or ignore
        }

        Theme currentTheme = ThemeManager.getInstance().getCurrentTheme();

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                StackPane stackPane = new StackPane();
                
                // Create cell rectangle
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
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

                this.add(stackPane, col, row);
            }
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

    public void setCellColor(Position pos, Color color) {
        if (isValidPosition(pos)) {
            cellNodes[pos.getRow()][pos.getCol()].setFill(color);
        }
    }

    public void highlightCell(Position pos) {
        if (isValidPosition(pos)) {
            cellNodes[pos.getRow()][pos.getCol()].setFill(ThemeManager.getInstance().getCurrentTheme().getHighlightColor());
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
