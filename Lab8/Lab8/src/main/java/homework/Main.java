package homework;

import homework.canvas.MazePane;
import homework.canvas.MazeState;
import homework.panel.ConfigPanel;
import homework.panel.ControlPanel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.embed.swing.SwingFXUtils;
import javafx.stage.Stage;


import javax.imageio.ImageIO;
import java.io.*;


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
        controlPanel.getValidateButton().setOnMouseClicked(event->{
            mazePane.validate();
        });
        controlPanel.getSaveButton().setOnMouseClicked(event->{
            saveMaze(mazePane);
        });
        controlPanel.getLoadButton().setOnMouseClicked(event->{
            loadSavedState(mazePane);
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

    private void saveMazeAsPng(MazePane mazePane) throws IOException {
        WritableImage image = mazePane.snapshot(null, null);
        File file = new File("maze_lab8.png");
        ImageIO.write(SwingFXUtils.fromFXImage(image, null),"png",file);
    }

    private void saveMazeState(MazePane mazePane) throws IOException{
        MazeState mazeState=new MazeState(mazePane);
        var fileWriter=new ObjectOutputStream(new FileOutputStream("maze_state.ser"));
        fileWriter.writeObject(mazeState);
    }

    private void saveMaze(MazePane mazePane){
        try{
            this.saveMazeAsPng(mazePane);
            this.saveMazeState(mazePane);
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validare Save");
            alert.setHeaderText("Succes!");
            alert.setContentText("Maze-ul s-a salvat cu succes!");
            alert.showAndWait();
        } catch (IOException exception){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validare Save");
            alert.setHeaderText("Eroare!");
            alert.setContentText("Maze-ul nu s-a salvat...");
            alert.showAndWait();
        }
    }

    private void loadSavedState(MazePane mazePane) {
        File file=new File("maze_state.ser");
        if(file==null){
            return;
        }

        try(var fileReader=new ObjectInputStream(new FileInputStream("maze_state.ser"))){
            MazeState state =(MazeState) fileReader.readObject();
            mazePane.loadMaze(state);
            Alert alert=new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validare Load");
            alert.setHeaderText("Succes!");
            alert.setContentText("Maze-ul s-a incarcat cu succes!");
            alert.showAndWait();
        } catch (Exception exception){
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validare Load");
            alert.setHeaderText("Eroare!");
            alert.setContentText("Maze-ul nu s-a putut incarca...");
            alert.showAndWait();
        }
    }
    public static void main(String[] args) {
       launch(args);
    }
}