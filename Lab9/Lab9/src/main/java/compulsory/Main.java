package compulsory;

import compulsory.canvas.MazePane;
import compulsory.maze.Maze;
import compulsory.panels.*;
import compulsory.threads.BunnyTask;
import compulsory.threads.CowntDownTask;
import compulsory.threads.ExecutingController;
import compulsory.threads.RobotTask;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {
    @Override
    public void start(Stage stage){
        Maze maze=new Maze(10,2,2);
        BorderPane root = new BorderPane();

        ExecutingController executingController=new ExecutingController();
        BunniesPanel bunniesPanel= new BunniesPanel(maze,executingController);
        root.setLeft(bunniesPanel);

        RobotsPanel robotsPanel=new RobotsPanel(maze,executingController);
        root.setRight(robotsPanel);

        MazePane mazePane=new MazePane(maze);
        root.setCenter(mazePane);

        for(var child : bunniesPanel.getChildren()){
            if (child instanceof BunnyPanel) {
                BunnyPanel bunnyPanel = (BunnyPanel) child;
                Thread thread = new Thread(new BunnyTask(bunnyPanel.getName(), executingController, mazePane, bunnyPanel,bunniesPanel,robotsPanel));
                executingController.addThread(thread);
            }
        }
        int index=0;
        for(var child : robotsPanel.getChildren()){
            if (child instanceof RobotPanel) {
                RobotPanel robotPanel = (RobotPanel) child;
                Thread thread = new Thread(new RobotTask(index, executingController, mazePane, robotPanel,bunniesPanel,robotsPanel));
                executingController.addThread(thread);
                index++;
            }
        }

        ControlPanel controlPanel=new ControlPanel();
        controlPanel.getStartButton().setOnMouseClicked(event->{
            bunniesPanel.enableMovement();
            robotsPanel.enableMovement();
            controlPanel.getStartButton().setDisable(true);
            controlPanel.getStopButton().setDisable(false);
            synchronized (executingController.getPauseLock()) {
                executingController.getPauseLock().notifyAll();
            }
            controlPanel.getCountDown().setCounting(true);
        });
        controlPanel.getStopButton().setOnMouseClicked(event->{
            bunniesPanel.disableMovement();
            robotsPanel.disableMovement();
            controlPanel.getStartButton().setDisable(false);
            controlPanel.getStopButton().setDisable(true);
            controlPanel.getCountDown().setCounting(false);
        });
        root.setBottom(controlPanel);

        Thread timerThread = new Thread(new CowntDownTask(executingController, mazePane, bunniesPanel, robotsPanel, controlPanel.getCountDown()));
        executingController.addThread(timerThread);
        executingController.startThreads();

        stage.setOnCloseRequest(event -> {
            executingController.killThreads();
            Platform.exit();
            System.exit(0);
        });
        stage.setScene(new Scene(root,950,800));
        stage.setTitle("Bunny Maze Escape");
        stage.setResizable(false);
        stage.show();
    }

    public static void main(String[] args) {
        /*
        |  |  |  |  |  |
        +--+--+--+--+--+
         🐰|R1|R2|R3|🤖|
        +--+--+--+--+--+
        |✅|
        +--+--+
        */
        launch(args);
    }
}