package homework.canvas;

import javafx.scene.Node;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class MazeState implements Serializable {
    private int size;
    private int rowStart;
    private int colStart;
    private int colStop;
    private int rowStop;
    private boolean[][] doorClosed;
    public MazeState(MazePane mazePane){
        this.size=mazePane.getSize();
        this.rowStart=mazePane.getRowStart();
        this.colStart= mazePane.getColStart();
        this.rowStop=mazePane.getRowStop();
        this.colStop= mazePane.getColStop();
        int mazeSize=this.size*2+1;
        doorClosed = new boolean[mazeSize][mazeSize];
        for(Node node:mazePane.getChildren()){
            Cell currCell=(Cell) node;
            int row = currCell.getRow();
            int col = currCell.getCol();
            doorClosed[row][col] = true;
            if(currCell.getType() == CellType.DOOR){
                doorClosed[row][col] = currCell.isClosed();
            }
        }
    }
}
