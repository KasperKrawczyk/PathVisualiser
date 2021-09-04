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
    boolean isStartChosen = true;
    boolean isEndChosen = true;
    boolean isCleared = true;
    public static Color REG_CELL_COLOR = new Color(218, 200, 140);
    public static Color START_COLOR = new Color(40, 45, 148);
    public static Color GOAL_COLOR = Color.RED;
    public static Color EXPLORED_COLOR = new Color(60, 134, 83);
    public static Color TO_EXPLORE_COLOR = new Color(86, 229, 109);

    public AlgorithmThread(Grid grid){
        this.grid = grid;
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
        }while(!isCleared);
    }

    public void findPath(Cell startCell, Cell goalCell, int algorithm){
        startCell.setDistanceFromStart(0.0);
        if(algorithm == DIJKSTRA) startCell.setCost(0.0);

        priorityQueue.add(startCell);

        while(!priorityQueue.isEmpty()){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.grid.update();

            Cell curCell = priorityQueue.poll();
            if(curCell.equals(startCell)){
                startCell.setColor(START_COLOR);
            } else if(curCell.equals(goalCell)){
                goalCell.setColor(GOAL_COLOR);
                this.grid.update();
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
                        neighbourCell.setPredecessor(curCell);
                        priorityQueue.add(neighbourCell);
                    }
                }

            }
        }
        ArrayList<Cell> path = new ArrayList<>();
        Cell curCell = goalCell;
        path.add(curCell);

        while(curCell.getPredecessor() != null){
            path.add(curCell.getPredecessor());
            curCell = curCell.getPredecessor();
            if(curCell.equals(startCell)){
                curCell.setColor(START_COLOR);
            }
        }
        this.grid.update();
        Collections.reverse(path);
        //animate the path
    }

    private boolean canComputeAlgorithm(){
        return isStartChosen && isEndChosen && !isCleared;
    }

    public void clear(){
        this.isCleared = true;
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

    public boolean isCleared() {
        return isCleared;
    }

    public void setCleared(boolean isCleared) {
        System.out.println("am I cleared? " + isCleared);
        this.isCleared = isCleared;
    }
}
