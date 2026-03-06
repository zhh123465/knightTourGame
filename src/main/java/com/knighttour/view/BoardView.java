package com.knighttour.view;

import com.knighttour.model.Position;
import com.knighttour.util.ColorScheme;
import com.knighttour.util.Constants;
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
    }

    private void initializeBoard() {
        this.setAlignment(Pos.CENTER);
        
        // Load knight image
        try {
            // Placeholder for knight image, in real app should load from resources
            // For now we can use a simple shape or text if image not found, but let's try to load
            // knightNode = new ImageView(new Image(getClass().getResourceAsStream("/images/knight.png")));
            // Since we don't have the image resource yet, we will initialize it later or use a placeholder if needed
            // For this task, I'll just declare it.
            knightNode = new ImageView();
            knightNode.setFitWidth(CELL_SIZE * 0.8);
            knightNode.setFitHeight(CELL_SIZE * 0.8);
        } catch (Exception e) {
            // Log error or ignore
        }

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                StackPane stackPane = new StackPane();
                
                // Create cell rectangle
                Rectangle cell = new Rectangle(CELL_SIZE, CELL_SIZE);
                // Set default chessboard pattern
                Color defaultColor = (row + col) % 2 == 0 ? Color.WHITE : Color.LIGHTGRAY;
                cell.setFill(defaultColor);
                cell.setStroke(Color.BLACK);
                
                // Create sequence text
                Text text = new Text("");
                text.setFont(Font.font(20));
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
            Color defaultColor = (row + col) % 2 == 0 ? Color.WHITE : Color.LIGHTGRAY;
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
            cellNodes[pos.getRow()][pos.getCol()].setFill(Color.YELLOW); // Or use a highlight color from ColorScheme
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
}
