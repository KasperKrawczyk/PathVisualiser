import java.awt.*;

public class WallWorkerThread extends Thread {
    Grid grid;
    boolean isThreadStopped = true;
    boolean isSettingWalls;

    public WallWorkerThread(Grid grid, boolean isSettingWalls){
        this.grid = grid;
        this.isSettingWalls = isSettingWalls;
    }

    public void run(){

        if(!isThreadStopped){
            System.out.println("isSettingWalls = "+isSettingWalls);
            do{
                int x = (int) grid.getMousePosition().getX();
                int y = (int) grid.getMousePosition().getY();
                Point curMousePosition = new Point(x, y);
                Cell curCell = grid.getGrid()[curMousePosition.x / grid.getCellWidth()][curMousePosition.y / grid.getCellHeight()];
                if(curCell.getCellType() == CellType.WALL && !isSettingWalls){
                    curCell.setCellType(CellType.REGULAR);
                } else if(curCell.getCellType() == CellType.REGULAR && isSettingWalls){
                    curCell.setCellType(CellType.WALL);
                }

                this.grid.update();
            }while(!isThreadStopped);
        }

    }

    public void setThreadStopped(boolean isThreadStopped) {
        System.out.println(this + " (WallWorker) stopped? =" + isThreadStopped);
        this.isThreadStopped = isThreadStopped;
    }

}
