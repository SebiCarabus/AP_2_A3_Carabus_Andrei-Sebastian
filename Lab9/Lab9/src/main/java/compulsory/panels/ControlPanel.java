package compulsory.panels;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import lombok.Getter;

@Getter
public class ControlPanel extends HBox {
    private Button startButton;
    private Button stopButton;
    private CountDown countDown;
    public ControlPanel(){
        this.setAlignment(Pos.TOP_CENTER);
        this.setSpacing(10);
        this.setPadding(new Insets(10));
        this.countDown=new CountDown(300);
        this.getChildren().add(this.countDown);

        this.startButton=new Button("Start");
        /*startButton.setOnMouseClicked(event->{
            this.stopButton.setDisable(false);
            this.startButton.setDisable(true);
        });*/
        this.stopButton=new Button("Stop");
        /*this.stopButton.setOnMouseClicked(event->{
            this.stopButton.setDisable(true);
            this.startButton.setDisable(false);
        });*/
        stopButton.setDisable(true);

        this.getChildren().addAll(this.startButton,this.stopButton);
    }
}
