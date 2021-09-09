import java.awt.event.MouseEvent;

public class PainterThread extends Thread {
    Grid grid;
    boolean isThreadStopped = true;
    CellType cellType;
    boolean isPainting;

    public PainterThread(Grid grid, boolean isPainting, MouseEvent mouseEvent){
        this.setCellType(mouseEvent);
        this.grid = grid;
        this.isPainting = isPainting;
    }

    public void run(){

        if(!isThreadStopped){
            System.out.println("isPainting = " + isPainting);
            do{
                int x = (int) grid.getMousePosition().getX();
                int y = (int) grid.getMousePosition().getY();
                Cell curCell = grid.getGrid()[x / grid.getCellWidth()][y / grid.getCellHeight()];
                if(curCell.getCellType() == CellType.START || curCell.getCellType() == CellType.GOAL){
                    continue;
                } else if(this.cellType == curCell.getCellType() && !isPainting){
                        curCell.setCellType(CellType.REGULAR);
                } else if(this.cellType != curCell.getCellType() && isPainting){
                        curCell.setCellType(this.cellType);
                }



                this.grid.update();
            }while(!isThreadStopped);
        }

    }

    public void setThreadStopped(boolean isThreadStopped) {
        System.out.println(this + " (PaintWorker) stopped? =" + isThreadStopped);
        this.isThreadStopped = isThreadStopped;
    }

    private void setCellType(MouseEvent mouseEvent){
        if(mouseEvent.getButton() == MouseEvent.BUTTON1){
            this.cellType = CellType.WALL;
        } else if(mouseEvent.getButton() == MouseEvent.BUTTON3){
            this.cellType = CellType.SWAMP;
        }
    }

}
