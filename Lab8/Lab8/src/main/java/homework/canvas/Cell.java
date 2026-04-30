package homework.canvas;

import javafx.scene.layout.StackPane;
import lombok.Getter;

@Getter
public class Cell extends StackPane {
    private CellType type;
    private boolean isClosed = true;
    private int row;
    private int col;
    public Cell(CellType type, int row, int col){
        this.type=type;
        this.row=row;
        this.col=col;
        if(this.type== CellType.ROOM){
            this.setStyle("-fx-background-color: white");
        } else if(this.type== CellType.WALL){
            this.setStyle("-fx-background-color: black");
        } else if(this.type== CellType.START){
            this.setStyle("-fx-background-color: green");
        } else if(this.type== CellType.STOP){
            this.setStyle("-fx-background-color: red");
        } else {
            this.setStyle("-fx-background-color: brown");
        }
        this.setOnMouseClicked(event->{
            this.updateAppearance();
        });
    }

    public void updateAppearance(){
        if (type == CellType.DOOR) {
            this.isClosed =!this.isClosed;
            String color = isClosed ? "brown" : "bisque";
            this.setStyle("-fx-background-color: " + color + ";");
        }
    }
}
