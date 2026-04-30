package compulsory.panels;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountDown extends HBox {
    private volatile boolean counting=false;
    private Integer numberSeconds;
    public CountDown(int seconds){
        this.numberSeconds=seconds;
        this.setSpacing(5);
        this.getChildren().add(new Label("Remaining Time:"));
        this.getChildren().add(new Label(this.numberSeconds.toString()+" seconds"));
    }

    public void reDraw(){
        this.getChildren().clear();
        this.getChildren().add(new Label("Remaining Time:"));
        this.getChildren().add(new Label(this.numberSeconds.toString()+" seconds"));
    }
}
