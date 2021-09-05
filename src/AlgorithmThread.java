import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.PriorityQueue;

public class AlgorithmThread extends Thread {
    public static final int DIJKSTRA = 0;
    //cells we have already looked at
    //private boolean[][] visited;
    //cells we need to look at
    private PriorityQueue<Cell> priorityQueue;
    private Grid grid;
    Cell startCell;
    Cell goalCell;
    boolean isStartChosen = true;
    boolean isEndChosen = true;
    boolean isThreadStopped = true;
    boolean isComputing = true;


    public AlgorithmThread(Grid grid){
        this.grid = grid;
        this.startCell = grid.getStartCell();
        this.goalCell = grid.getGoalCell();
        //visited = new boolean[grid.getNumRows()][grid.getNumCols()];
        priorityQueue = new PriorityQueue<>();
    }

    public void run(){
        do{
            if(canComputeAlgorithm()){
                findPath(this.grid.getStartCell(), this.grid.getGoalCell(), 0);
            }
        }while(!isThreadStopped());
    }

    public void findPath(Cell startCell, Cell goalCell, int algorithm){
        startCell.setDistanceFromStart(0.0);
        if(algorithm == DIJKSTRA) startCell.setCost(0.0);

        priorityQueue.add(startCell);

        while(!priorityQueue.isEmpty() && isComputing){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.grid.update();

            Cell curCell = priorityQueue.poll();

            if(curCell.getCellType() == CellType.WALL){
                continue;
            }
            curCell.setCellType(CellType.EXPLORED);

            if(curCell == startCell){
                startCell.setCellType(CellType.START);
            }
            if(curCell == goalCell){
                goalCell.setCellType(CellType.GOAL);
                this.grid.update();
                System.out.println("breaking!");
                break;
            }

            for(Edge edge : curCell.getEdges()){
                if(algorithm == DIJKSTRA){
                    Cell neighbourCell = edge.getDestination();
                    if(neighbourCell.getCellType() == CellType.WALL){
                        continue;
                    }

                    double distanceFromStartCell = curCell.getDistanceFromStart() + edge.getCost();

                    if(neighbourCell.getCellType() != CellType.EXPLORED
                            && neighbourCell.getCellType() != CellType.START
                            && neighbourCell.getCellType() != CellType.GOAL){
                        neighbourCell.setCellType(CellType.TO_EXPLORE);
                    }

                    if(distanceFromStartCell < neighbourCell.getDistanceFromStart()){
                        priorityQueue.remove(neighbourCell);
                        neighbourCell.setDistanceFromStart(distanceFromStartCell);
                        neighbourCell.setCost(distanceFromStartCell);
                        neighbourCell.setPrev(curCell);
                        priorityQueue.add(neighbourCell);
                    }
                }

            }
        }
        ArrayList<Cell> path = new ArrayList<>();
        Cell curCell = goalCell;
        path.add(curCell);

        while(curCell.getPrev() != null){
            path.add(curCell.getPrev());
            curCell = curCell.getPrev();
            if(curCell.equals(startCell)){
                curCell.setCellType(CellType.START);
            }
        }
        this.grid.update();
        Collections.reverse(path);
        //animate the path
        for(Cell cell : path){
            if(cell == startCell) System.out.println("startCell = " + cell);
            if(cell == goalCell) System.out.println("goalCell = " + cell);


            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(cell != startCell && cell != goalCell){
                System.out.println("animating the path " + cell);
                cell.setCellType(CellType.PATH);
                this.grid.update();
            }
        }
        stopThread();
    }

    private boolean canComputeAlgorithm(){
        return isStartChosen && isEndChosen && !isThreadStopped;
    }

    public void stopThread(){
        this.isComputing = false;
        this.isThreadStopped = true;
    }


    private double getManhattanDistance(Point source, Point destination){
        return Math.abs(source.x - destination.x) + Math.abs(source.y - destination.y);
    }

    public boolean isStartChosen() {
        return isStartChosen;
    }

    public void setStartChosen(boolean startChosen) {
        isStartChosen = startChosen;
    }

    public boolean isEndChosen() {
        return isEndChosen;
    }

    public void setEndChosen(boolean endChosen) {
        isEndChosen = endChosen;
    }

    public boolean isThreadStopped() {
        return isThreadStopped;
    }

    public void setThreadStopped(boolean isThreadStopped) {
        System.out.println("am I cleared? " + isThreadStopped);
        this.isThreadStopped = isThreadStopped;
    }
}
