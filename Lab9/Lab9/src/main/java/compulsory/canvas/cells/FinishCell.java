package compulsory.canvas.cells;

import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;

public class FinishCell extends StackPane implements Cell {
    private final int row, col;
    public FinishCell(int row, int col) {
        this.row = row;
        this.col = col;
        setStyle("-fx-background-color: green;");
    }
    @Override
    public int getRow() {
        return this.row;
    }
    @Override
    public int getCol(){
        return this.col;
    }
}