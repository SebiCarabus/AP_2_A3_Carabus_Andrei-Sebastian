package compulsory.canvas;

import compulsory.canvas.cells.Cell;
import compulsory.canvas.cells.DoorCell;
import compulsory.canvas.cells.RoomCell;
import compulsory.canvas.cells.WallCell;
import compulsory.maze.Coordonates;
import compulsory.maze.Maze;
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
import java.util.Map;
import java.util.Random;

@Getter
@Setter
public class MazePane extends GridPane {

    private int size;
    private Maze maze;

    public MazePane(Maze maze) {
        this.setMinSize(600,600);
        this.setMaxSize(600,600);
        this.loadMaze(maze);
    }

    public void randomMaze() {
        this.loadMaze(new Maze(10,2,2));
    }

    public void loadMaze(Maze maze) {
        this.maze  = maze;
        this.size  = maze.getSize();
        prepareMaze();
        mazeDraw(maze);
    }

    public void reDraw() {
        List<Cell> openDoors = new ArrayList<>();
        for (Node node : this.getChildren()) {
            Cell cell = (Cell) node;
            if (!cell.isClosed()) {
                openDoors.add(cell);
            }
        }

        prepareMaze();
        if (this.maze != null) {
            mazeDraw(this.maze);
        } else {
            defaultMazeDraw();
            for (Cell openDoor : openDoors) {
                Cell found = getCell(openDoor.getRow(), openDoor.getCol());
                if (found != null) found.updateAppearance();
            }
        }
    }

    private void prepareMaze() {
        this.getChildren().clear();
        this.getColumnConstraints().clear();
        this.getRowConstraints().clear();
        int gridSize = this.size * 2 + 1;

        for (int i = 0; i < gridSize; i++) {
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

    private void mazeDraw(Maze maze) {
        int gridSize = this.size * 2 + 1;
        Map<String, Coordonates> bunnies = maze.getBunnies();
        Coordonates[] robots = maze.getRobots();
        Coordonates finish = maze.getFinish();
        boolean[][] doors = maze.getClosedDoors();

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Cell cell;

                boolean isCornerOrBorder = (i == 0 || j == 0 || i == gridSize - 1 || j == gridSize - 1);
                boolean isIntersection = (i % 2 == 0 && j % 2 == 0);
                boolean isWall = isCornerOrBorder || isIntersection;
                boolean isDoor = !isWall && (i % 2 == 0 || j % 2 == 0);

                if (isWall) {
                    cell = new WallCell(i, j);
                } else if (isDoor) {
                    DoorCell door = new DoorCell(i, j);
                    if (!doors[i][j]) {
                        door.updateAppearance();
                    }
                    cell = door;
                } else {
                    String bunnyName  = getBunnyAt(i, j, bunnies);
                    int robotNumber   = getRobotAt(i, j, robots);
                    boolean isFinish  = finish != null && finish.getRow() == i && finish.getCol() == j;

                    cell = Cell.createContextCell(i, j, false, false, isFinish, bunnyName, robotNumber);
                }

                this.add((Node) cell, j, i);
            }
        }
    }

    private void defaultMazeDraw() {
        int gridSize = this.size * 2 + 1;
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Cell cell = buildDefaultCell(i, j);
                this.add((Node) cell, j, i);
            }
        }
    }

    private Cell buildDefaultCell(int i, int j) {
        int gridSize = this.size * 2 + 1;
        boolean isCornerOrBorder = (i == 0 || j == 0 || i == gridSize - 1 || j == gridSize - 1);
        boolean isIntersection = (i % 2 == 0 && j % 2 == 0);
        boolean isWall = isCornerOrBorder || isIntersection;
        boolean isDoor = !isWall && (i % 2 == 0 || j % 2 == 0);

        if (isWall) {
            return new WallCell(i, j);
        }
        if (isDoor) {
            return new DoorCell(i, j);
        }
        RoomCell room = new RoomCell(i, j);
        return room;
    }

    private Cell getCell(int row, int col) {
        for (Node node : this.getChildren()) {
            if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col) {
                return (Cell) node;
            }
        }
        return null;
    }

    private static String getBunnyAt(int row, int col, Map<String, Coordonates> bunnies) {
        for (Map.Entry<String, Coordonates> entry : bunnies.entrySet()) {
            if (entry.getValue().getRow() == row && entry.getValue().getCol() == col) {
                return entry.getKey();
            }
        }
        return null;
    }

    private static int getRobotAt(int row, int col, Coordonates[] robots) {
        for (int i=0; i<robots.length;i++) {
            if (robots[i].getRow()==row && robots[i].getCol()==col) {
                return i+1;
            }
        }
        return 0;
    }
}
