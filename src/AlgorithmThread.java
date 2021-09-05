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
    public static Color REG_CELL_COLOR = new Color(218, 200, 140);
    public static Color START_COLOR = new Color(40, 45, 148);
    public static Color GOAL_COLOR = Color.RED;
    public static Color EXPLORED_COLOR = new Color(60, 134, 83);
    public static Color TO_EXPLORE_COLOR = new Color(86, 229, 109);
    public static Color PATH_COLOR = new Color(250, 100, 36);

    public AlgorithmThread(Grid grid){
        this.grid = grid;
        this.startCell = grid.getStartCell();
        this.goalCell = grid.getGoalCell();
        //visited = new boolean[grid.getNumRows()][grid.getNumCols()];
        priorityQueue = new PriorityQueue<>();
    }

    public void run(){
        do{
            System.out.println("he?");
            if(canComputeAlgorithm()){
                System.out.println("RUNNING");
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

            curCell.setColor(EXPLORED_COLOR);

            if(curCell == startCell){
                startCell.setColor(START_COLOR);
            }
            if(curCell == goalCell){
                goalCell.setColor(GOAL_COLOR);
                this.grid.update();
                System.out.println("breaking!");
                break;
            } else {
                curCell.setColor(EXPLORED_COLOR);
            }

            for(Edge edge : curCell.getEdges()){
                if(algorithm == DIJKSTRA){
                    Cell neighbourCell = edge.getDestination();

                    double distanceFromStartCell = curCell.getDistanceFromStart() + edge.getCost();

                    if(neighbourCell.getColor() != EXPLORED_COLOR && neighbourCell.getColor() != START_COLOR){
                        neighbourCell.setColor(TO_EXPLORE_COLOR);
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
                curCell.setColor(START_COLOR);
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
                cell.setColor(PATH_COLOR);
                this.grid.update();
            }
        }
        stopThread();
    }

    private boolean canComputeAlgorithm(){
        return isStartChosen && isEndChosen && !isThreadStopped();
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
