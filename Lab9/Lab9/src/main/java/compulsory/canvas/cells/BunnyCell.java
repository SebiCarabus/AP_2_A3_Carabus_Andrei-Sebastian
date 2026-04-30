package compulsory.canvas.cells;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.OverrunStyle;
import javafx.scene.layout.StackPane;

public class BunnyCell extends StackPane implements Cell {
    private final int row, col;
    private final String name;
    public BunnyCell(int row, int col, String name) {
        this.row = row;
        this.col = col;
        this.name = name;
        setStyle("-fx-background-color: white;");
        Label label = new Label(name);
        label.setMinWidth(0);
        label.setPrefWidth(0);
        label.setMaxWidth(Double.MAX_VALUE);
        label.setTextOverrun(OverrunStyle.ELLIPSIS);
        label.setAlignment(Pos.CENTER);
        this.getChildren().add(label);
    }
    @Override
    public int getRow() {
        return this.row;
    }


    @Override
    public int getCol() {
        return this.col;
    }

    public String getName() {
        return this.name;
    }
}