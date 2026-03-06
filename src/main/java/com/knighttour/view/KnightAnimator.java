package com.knighttour.view;

import com.knighttour.model.Position;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 * 骑士动画控制器
 * 
 * 负责在棋盘上执行平滑的骑士移动和回溯动画。
 */
public class KnightAnimator {

    private final BoardView boardView;
    private int animationDelayMs = 100;

    public KnightAnimator(BoardView boardView) {
        this.boardView = boardView;
    }

    public void setAnimationDelay(int delayMs) {
        this.animationDelayMs = delayMs;
    }

    public void animateMove(Position from, Position to, Runnable onComplete) {
        // For simplicity in this phase, we might just update the position directly if delay is 0
        if (animationDelayMs <= 0) {
            boardView.placeKnight(to);
            if (onComplete != null) onComplete.run();
            return;
        }

        // Calculate translation
        // Actually, BoardView uses GridPane which places nodes in cells.
        // Moving a node between cells in GridPane is not directly supported by TranslateTransition unless we use a floating node.
        // BoardView.placeKnight removes the knight from old cell and adds to new cell.
        // To animate, we need to:
        // 1. Get coordinates of from and to cells relative to scene or parent.
        // 2. Create a transition.
        // 3. On finish, actually move the node in the scene graph.
        
        // However, a simpler approach for now:
        // Just use a pause if we don't implement full smooth movement yet, or
        // implement smooth movement by using a layout translation.
        
        // Since BoardView puts knight in a StackPane inside a grid cell, 
        // we can try to animate the translation properties of the knight node *before* moving it in the scene graph?
        // No, once it's in the new parent, translation is relative to new parent.
        
        // Let's implement a simple fade out/in or just direct placement with delay for now if smooth movement is complex.
        // Task says "Use TranslateTransition for smooth movement".
        
        // To do this properly:
        // We need the knight to be in a layer above the grid, using absolute coordinates.
        // But BoardView design puts it inside the cell.
        
        // Workaround: 
        // 1. Place knight in 'to' cell.
        // 2. Set translation to appear as if it's at 'from' cell.
        // 3. Animate translation to (0,0).
        
        // Get layout bounds is tricky if scene not shown.
        // Assuming cells are fixed size 60.
        double deltaX = (from.getCol() - to.getCol()) * 60.0;
        double deltaY = (from.getRow() - to.getRow()) * 60.0;
        
        boardView.placeKnight(to);
        ImageView knight = boardView.getKnightNode();
        
        knight.setTranslateX(deltaX);
        knight.setTranslateY(deltaY);
        
        TranslateTransition tt = new TranslateTransition(Duration.millis(animationDelayMs), knight);
        tt.setToX(0);
        tt.setToY(0);
        tt.setOnFinished(e -> {
            if (onComplete != null) onComplete.run();
        });
        tt.play();
    }

    public void animateBacktrack(Position pos, Runnable onComplete) {
        // Fade out at current position (which is 'pos' usually, or we are moving *from* pos back to previous)
        // Wait, backtrack means we are undoing a move. 
        // Usually we just remove the knight from current and place at previous.
        // Or if we just want to visualize the backtrack action (disappearing from current cell).
        
        // Task says: "Use FadeTransition for fade in/out effect".
        
        ImageView knight = boardView.getKnightNode();
        // Assuming knight is currently at 'pos' (the one being backtracked FROM)
        // But in Solver, we backtrack by popping, so knight should move to previous.
        
        // Let's assume this method is called when we want to show the knight "leaving" 'pos'.
        // But actually the knight should move to the previous position.
        
        // Implementation:
        // 1. Fade out at 'pos'.
        // 2. On finish, place at previous (handled by caller? or we need 'to' position?)
        
        // If we only have 'pos' (the bad move), we might not know where to go back to unless passed.
        // But the solver handles the logic. The animator just visualizes.
        
        // If we want to visualize "undo", we might want to move back.
        // But the interface only has `animateBacktrack(Position pos, Runnable onComplete)`.
        // This implies we just fade out the "bad" move visualization?
        // Or maybe 'pos' is where we are backtracking TO?
        
        // Let's look at the usage context. 
        // Solver calls `onBacktrack(Move move)`. `move.to` is the position we are retreating FROM. `move.from` is where we go back TO.
        // So we probably want to move from `move.to` to `move.from`.
        
        // But here we only have `pos`.
        // If `pos` is the position we are leaving (the dead end).
        
        // Let's implement a simple fade out.
        FadeTransition ft = new FadeTransition(Duration.millis(animationDelayMs / 2.0), knight);
        ft.setFromValue(1.0);
        ft.setToValue(0.0);
        ft.setOnFinished(e -> {
            knight.setOpacity(1.0); // Reset for next use
            // The actual placement update should happen after or during?
            // If we assume the knight is ALREADY at 'pos' (the dead end), we fade it out.
            // Then the caller (Controller) will place it at the previous position?
            if (onComplete != null) onComplete.run();
        });
        ft.play();
    }
}
