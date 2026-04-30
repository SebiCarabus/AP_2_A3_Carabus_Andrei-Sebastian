package compulsory.canvas.cells;

public interface Cell {

    int getRow();
    int getCol();

    default boolean isClosed() {
        return true;
    }

    default void updateAppearance() {}

    static Cell createContextCell(int row, int col, boolean isDoor, boolean isWall, boolean isFinish, String bunnyName, int robotNumber) {
        if (isWall){
            return new WallCell(row, col);
        }
        if (isDoor){
            return new DoorCell(row, col);
        }

        if (isFinish){
            return new FinishCell(row, col);
        }
        if (bunnyName != null && !bunnyName.isEmpty()){
            return new BunnyCell(row, col, bunnyName);
        }
        if (robotNumber > 0) {
            return new RobotCell(row, col, new String("R"+robotNumber));
        }
        return new RoomCell(row, col);
    }
}
