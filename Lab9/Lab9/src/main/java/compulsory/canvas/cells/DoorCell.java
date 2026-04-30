package compulsory.canvas.cells;

import javafx.scene.layout.StackPane;

public class DoorCell extends StackPane implements Cell {
    private final int row, col;
    private boolean closed = true;

    public DoorCell(int row, int col) {
        this.row = row;
        this.col = col;
        applyStyle();
        //setOnMouseClicked(e -> updateAppearance());
    }

    @Override public int getRow() {
        return this.row;
    }
    @Override public int getCol() {
        return this.col;
    }
    @Override
    public boolean isClosed() {
        return this.closed;
    }

    @Override
    public void updateAppearance() {
        closed = !closed;
        applyStyle();
    }

    private void applyStyle() {
        setStyle("-fx-background-color: " + (closed ? "brown" : "bisque") + ";");
    }
}
