package compulsory.panels;

import compulsory.maze.Maze;
import compulsory.threads.ExecutingController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RobotsPanel extends VBox {
    private volatile int score=0;
    private ExecutingController executingController;

    public void incrementScore(){
        this.score++;
        Integer robotsScore=this.score;
        Label scoreLabel = new Label("Robots Score: "+robotsScore.toString());
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD,16));
        this.getChildren().set(1,scoreLabel);
    }

    public void disableMovement(){
        for(var child:this.getChildren()){
            if(child instanceof RobotPanel){
                ((RobotPanel) child).disableMovement();
            }
        }
    }

    public void enableMovement(){
        for(var child:this.getChildren()){
            if(child instanceof RobotPanel){
                ((RobotPanel) child).enableMovement();
            }
        }
    }

    public RobotsPanel(Maze maze,ExecutingController executingController){
        this.executingController=executingController;
        this.setAlignment(Pos.TOP_CENTER);
        VBox paddingBox=new VBox();
        this.setPadding(new Insets(20));
        paddingBox.setMinHeight(60);
        paddingBox.setMaxHeight(60);
        this.setSpacing(20);
        this.getChildren().add(paddingBox);
        Integer robotsScore=this.score;
        Label scoreLabel = new Label("Robots Score: "+robotsScore.toString());
        scoreLabel.setFont(Font.font("Arial", FontWeight.BOLD,16));
        this.getChildren().add(scoreLabel);
        for(Integer i=1;i<=maze.getRobots().length;i++){
            this.getChildren().add(new RobotPanel(new String("R"+i.toString()),this.executingController));
        }
    }
}
