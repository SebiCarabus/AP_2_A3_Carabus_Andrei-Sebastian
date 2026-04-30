package compulsory.panels;

import compulsory.threads.ExecutingController;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.layout.GridPane;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RobotPanel extends GridPane {
    private String name;
    private Spinner<Integer> speedSpinner;
    private Button startButton;
    private Button stopButton;
    private MovementStatus status= MovementStatus.RUNNING;
    private volatile int speed=3;
    private static int maxTimePerStep=600000;
    private volatile boolean running=true;
    private volatile boolean parentDisabled=true;
    private ExecutingController executingController;

    public void disableMovement(){
        this.parentDisabled=true;
        this.status= MovementStatus.STAYING;
        this.startButton.setDisable(true);
        this.stopButton.setDisable(true);
    }

    public void enableMovement(){
        this.parentDisabled=false;
        if(running==true)
        {
            this.status= MovementStatus.RUNNING;
            this.startButton.setDisable(true);
            this.stopButton.setDisable(false);
        } else {
            this.startButton.setDisable(false);
            this.stopButton.setDisable(true);
        }

    }

    public RobotPanel(String name,ExecutingController executingController){
        this.executingController=executingController;
        this.setHgap(5);
        this.setVgap(5);
        this.setPadding(new Insets(10));
        this.setStyle("-fx-border-color: black; -fx-border-width: 2px; -fx-border-style: solid;");
        this.name=name;
        Label labelName = new Label(this.name);
        this.speedSpinner= new Spinner<>(1,5,this.speed);
        this.speedSpinner.setMaxWidth(45);
        this.speedSpinner.valueProperty().addListener((observable,oldValue,newValue)->{
            this.speed=newValue;
        });
        this.add(new Label("Robot:"),1,1);
        this.add(labelName,2,1);
        this.add(new Label("Speed:"),1,2);
        this.add(this.speedSpinner,2,2);

        this.startButton=new Button("Start");
        startButton.setOnMouseClicked(event->{
            this.stopButton.setDisable(false);
            this.startButton.setDisable(true);
            this.running=true;
            this.status= MovementStatus.RUNNING;
            synchronized (executingController.getPauseLock()) {
                executingController.getPauseLock().notifyAll();
            }
            this.reDraw();
        });
        this.stopButton=new Button("Stop");
        this.stopButton.setOnMouseClicked(event->{
            this.stopButton.setDisable(true);
            this.startButton.setDisable(false);
            this.running=false;
            this.status= MovementStatus.STAYING;
            this.reDraw();
        });
        this.add(new Label("Status:"),1,3);
        this.add(new Label(this.status.toString()),2,3);
        this.add(this.startButton,1,4);
        this.add(this.stopButton,2,4);
        this.disableMovement();
    }

    public void reDraw(){
        this.getChildren().clear();
        this.speedSpinner= new Spinner<>(1,5,this.speed);
        this.speedSpinner.setMaxWidth(45);
        this.add(new Label("Robot:"),1,1);
        Label labelName=new Label(this.name);
        this.add(labelName,2,1);
        this.add(new Label("Speed:"),1,2);
        this.add(this.speedSpinner,2,2);
        this.add(new Label("Status:"),1,3);
        this.add(new Label(this.status.toString()),2,3);
        this.add(this.startButton,1,4);
        this.add(this.stopButton,2,4);
    }
}
