package homework.canvas;

import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
@Setter
public class MazePane extends GridPane {
    private int size;
    private int rowStart;
    private int colStart;
    private int colStop;
    private int rowStop;
    private void prepareMaze(){

        this.getChildren().clear();
        this.getColumnConstraints().clear();
        this.getRowConstraints().clear();
        int gridSize=this.size*2+1;

        for(int i=0;i<gridSize;i++) {
            ColumnConstraints columnConstraints = new ColumnConstraints();
            RowConstraints rowConstraints = new RowConstraints();
            if (i % 2 == 0) {
                rowConstraints.setPrefHeight(10);
                columnConstraints.setPrefWidth(10);
            } else {
                rowConstraints.setVgrow(Priority.ALWAYS);
                columnConstraints.setHgrow(Priority.ALWAYS);
            }
            this.getColumnConstraints().add(columnConstraints);
            this.getRowConstraints().add(rowConstraints);
        }
    }
    private void defaultMazeDraw(){
        int gridSize=this.size*2+1;
        for(int i=0;i<gridSize;i++){
            for(int j=0;j<gridSize;j++){
                Cell cell=null;
                if(i==0||j==0||i==gridSize-1||j==gridSize-1||(i % 2 == 0 && j % 2 == 0)){
                    cell=new Cell(CellType.WALL,i,j);
                } else if (i % 2 == 0 || j % 2 == 0){
                    cell=new Cell(CellType.DOOR,i,j);
                } else {
                    if(i==this.rowStart && j==this.colStart){
                        cell=new Cell(CellType.START,i,j);
                    } else if(i==this.rowStop && j==this.colStop){
                        cell=new Cell(CellType.STOP,i,j);
                    } else {
                        cell=new Cell(CellType.ROOM,i,j);
                        final int currentRow = i;
                        final int currentCol = j;
                        cell.setOnMouseClicked(event ->{
                            if(event.getButton() == MouseButton.PRIMARY){
                                this.rowStart= currentRow;
                                this.colStart= currentCol;
                                this.reDraw();
                            } else if(event.getButton() == MouseButton.SECONDARY){
                                this.rowStop=currentRow;
                                this.colStop=currentCol;
                                this.reDraw();
                            }
                        });
                    }
                }

                this.add(cell,j,i);
            }
        }

    }
    public MazePane(int size){
        this.setMinSize(600,600);
        this.setMaxSize(600,600);
        this.size=size;

        int gridSize=this.size*2+1;
        this.rowStart=1;
        this.colStart=1;
        this.rowStop=gridSize-2;
        this.colStop=gridSize-2;

        this.prepareMaze();
        this.defaultMazeDraw();
    }

    public void resizeMaze(int newSize){
        int oldSize=this.size;
        this.size=newSize;
        int gridSize=this.size*2+1;
        if(oldSize!=this.size){
            this.rowStart=1;
            this.colStart=1;
            this.rowStop=gridSize-2;
            this.colStop=gridSize-2;
        }
        this.prepareMaze();
        this.defaultMazeDraw();
    }

    public void randomMaze(){
        this.prepareMaze();
        int gridSize=this.size*2+1;
        Random randomDoorClose=new Random();
        for(int i=0;i<gridSize;i++){
            for(int j=0;j<gridSize;j++){
                Cell cell=null;
                if(i==0||j==0||i==gridSize-1||j==gridSize-1||(i % 2 == 0 && j % 2 == 0)){
                    cell=new Cell(CellType.WALL,i,j);
                } else if (i % 2 == 0 || j % 2 == 0){
                    cell=new Cell(CellType.DOOR,i,j);
                    if(randomDoorClose.nextBoolean()){
                        cell.updateAppearance();
                    }
                } else {
                    if(i==this.rowStart && j==this.colStart){
                        cell=new Cell(CellType.START,i,j);
                    } else if(i==this.rowStop && j==this.colStop){
                        cell=new Cell(CellType.STOP,i,j);
                    } else {
                        cell=new Cell(CellType.ROOM,i,j);
                        final int currentRow = i;
                        final int currentCol = j;
                        cell.setOnMouseClicked(event ->{
                            if(event.getButton() == MouseButton.PRIMARY){
                                this.rowStart= currentRow;
                                this.colStart= currentCol;
                                this.reDraw();
                            } else if(event.getButton() == MouseButton.SECONDARY){
                                this.rowStop=currentRow;
                                this.colStop=currentCol;
                                this.reDraw();
                            }
                        });
                    }
                }
                this.add(cell,j,i);
            }
        }

    }

    private Cell getCell(int row, int col){
        for(Node node : this.getChildren()){
            int currRow =GridPane.getRowIndex(node);
            int currCol =GridPane.getColumnIndex(node);
            if(currCol==col && currRow==row){
                return (Cell) node;
            }
        }
        return null;
    }

    public void reDraw(){
        List<Cell>openDoors=new ArrayList<>();
        for(Node node:this.getChildren()){
            int currRow = GridPane.getRowIndex(node);
            int currCol = GridPane.getColumnIndex(node);
            openDoors.add(this.getCell(currRow,currCol));
        }
        this.prepareMaze();
        this.defaultMazeDraw();
        for(Cell openDoor: openDoors){
            if(!openDoor.isClosed()){
                this.getCell(openDoor.getRow(),openDoor.getCol()).updateAppearance();
            }
        }
    }

    private boolean validRoute(int rowCurr,int colCurr,boolean[][] visited){
        visited[rowCurr][colCurr]=true;
        boolean route1=false;
        boolean route2=false;
        boolean route3=false;
        boolean route4=false;
        if(rowCurr==this.rowStop && colCurr==this.colStop){
            return true;
        }
        if(this.getCell(rowCurr-1,colCurr).isClosed()==false){
            if(!visited[rowCurr-2][colCurr]){
                route1=this.validRoute(rowCurr-2,colCurr,visited);
            }
        }
        if(this.getCell(rowCurr+1,colCurr).isClosed()==false){
            if(!visited[rowCurr+2][colCurr]){
                route2=this.validRoute(rowCurr+2,colCurr,visited);
            }
        }
        if(this.getCell(rowCurr,colCurr-1).isClosed()==false){
            if(!visited[rowCurr][colCurr-2]){
                route3=this.validRoute(rowCurr,colCurr-2,visited);
            }
        }
        if(this.getCell(rowCurr,colCurr+1).isClosed()==false){
            if(!visited[rowCurr][colCurr+2]){
                route4=this.validRoute(rowCurr,colCurr+2,visited);
            }
        }
        return route1||route2||route3||route4;
    }

    public void validate(){
        int gridSize=2*this.size+1;
        boolean[][] visited=new boolean[gridSize][gridSize];
        for(int i=0;i<gridSize;i++){
            for(int j=0;j<gridSize;j++){
                visited[i][j]=false;
            }
        }

        if(this.validRoute(this.rowStart,this.colStart,visited)){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Validare Ruta");
            alert.setHeaderText("Succes!");
            alert.setContentText("Exista un drum valid de la START la STOP.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Validare Ruta");
            alert.setHeaderText("Ruta blocata!");
            alert.setContentText("Nu exista niciun drum posibil intre START și STOP.");
            alert.showAndWait();
        }
    }

    public void loadMaze(MazeState mazeState){
        this.size=mazeState.getSize();
        this.rowStart=mazeState.getRowStart();
        this.colStart= mazeState.getColStart();
        this.rowStop=mazeState.getRowStop();
        this.colStop= mazeState.getColStop();
        int mazeSize=this.size*2+1;
        boolean [][] savedDoorStatus=mazeState.getDoorClosed();
        this.prepareMaze();
        this.defaultMazeDraw();
        for(Node node: this.getChildren()){
            Cell cell=(Cell) node;
            if(cell.getType() == CellType.DOOR){
                boolean wasClosedInFile = savedDoorStatus[cell.getRow()][cell.getCol()];
                if (!wasClosedInFile) {
                    cell.updateAppearance();
                }
            }
        }

    }
}
