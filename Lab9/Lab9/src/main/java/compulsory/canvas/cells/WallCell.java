package compulsory.canvas.cells;

import javafx.scene.layout.StackPane;

public class WallCell extends StackPane implements Cell {
    private final int row, col;
    public WallCell(int row, int col) {
        this.row = row;
        this.col = col;
        setStyle("-fx-background-color: black");
    }
    @Override
    public int getRow() {
        return this.row;
    }
    @Override
    public int getCol() {
        return this.col;
    }
}
