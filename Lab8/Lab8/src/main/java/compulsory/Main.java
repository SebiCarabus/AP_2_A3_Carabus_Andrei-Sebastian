package compulsory;

import compulsory.canvas.MazePane;
import compulsory.panel.ConfigPanel;
import compulsory.panel.ControlPanel;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import javax.swing.*;
import java.util.function.UnaryOperator;


//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main extends Application {
    @Override
    public void start(Stage stage){
        BorderPane root = new BorderPane();

        MazePane mazePane=new MazePane(5);
        ConfigPanel configPanel=new ConfigPanel();
        configPanel.getDrawButton().setOnMouseClicked(event -> {
            int size = configPanel.getDimension();
            mazePane.resizeMaze(size);
        });
        ControlPanel controlPanel=new ControlPanel();
        controlPanel.getResetButton().setOnMouseClicked(event->{
            int size = mazePane.getSize();
            mazePane.resizeMaze(size);
        });
        controlPanel.getCreateButton().setOnMouseClicked(event->{
            mazePane.randomMaze();
        });

        root.setCenter(mazePane);
        root.setTop(configPanel);
        root.setBottom(controlPanel);

        stage.setScene(new Scene(root,800,800));
        stage.setTitle("Maze Editor");
        stage.setMaxHeight(800);
        stage.setMinHeight(750);
        stage.setMaxWidth(1000);
        stage.setMinWidth(650);
        stage.show();
    }
    public static void main(String[] args) {
       launch(args);
    }
}