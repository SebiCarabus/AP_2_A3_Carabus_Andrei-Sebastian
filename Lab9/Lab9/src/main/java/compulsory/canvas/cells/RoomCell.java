package compulsory.canvas.cells;

import javafx.scene.layout.StackPane;

public class RoomCell extends StackPane implements Cell {
    private final int row, col;
    public RoomCell(int row, int col) {
        this.row = row;
        this.col = col;
        setStyle("-fx-background-color: grey");
    }
    @Override public int getRow() {
        return this.row;
    }
    @Override public int getCol() {
        return this.col;
    }

}
