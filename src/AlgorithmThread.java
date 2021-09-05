import java.awt.*;
import java.util.*;

public class AlgorithmThread extends Thread {

    public static final int DIJKSTRA = 0;
    public static final int A_STAR = 1;

    private Set<Cell> visitedCellsSet;
    private PriorityQueue<Cell> priorityQueue;
    private Grid grid;

    Cell startCell;
    Cell goalCell;

    boolean isStartChosen = true;
    boolean isEndChosen = true;
    boolean isThreadStopped = true;
    boolean isComputing = true;

    public AlgorithmThread(Grid grid){
        this.visitedCellsSet = new HashSet<>();
        this.priorityQueue = new PriorityQueue<>();
        this.grid = grid;
        this.startCell = grid.getStartCell();
        this.goalCell = grid.getGoalCell();

    }

    public void run(){
        do{
            findPath(this.grid.getStartCell(), this.grid.getGoalCell(), 1);
        }while(!isThreadStopped());
    }

    public void findPath(Cell startCell, Cell goalCell, int algorithm){
        startCell.setDistanceFromStart(0.0);
        if(algorithm == DIJKSTRA) {
            startCell.setCost(0.0);
        } else if (algorithm == A_STAR){
            startCell.setCost(getHeuristic(startCell.getPosition(), goalCell.getPosition()));
        }

        priorityQueue.add(startCell);

        while(!priorityQueue.isEmpty() && !isThreadStopped()){
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.grid.update();

            Cell curCell = priorityQueue.poll();
            this.visitedCellsSet.add(curCell);

            curCell.setCellType(CellType.EXPLORED);

            if(curCell == startCell){
                startCell.setCellType(CellType.START);
            }
            if(curCell == goalCell){
                goalCell.setCellType(CellType.GOAL);
                this.grid.update();
                break;
            }

            for(Edge edge : curCell.getEdges()){

                if(algorithm == DIJKSTRA){
                    this.processNeighbourDijkstra(curCell, edge);

                } else if(algorithm == A_STAR){
                    this.processNeighbourAStar(curCell, edge, goalCell);

                }

            }
        }
        ArrayList<Cell> path = new ArrayList<>();
        Cell curCell = goalCell;
        path.add(curCell);

        while(curCell.getPrev() != null){
            System.out.println("WTF");
            path.add(curCell.getPrev());
            curCell = curCell.getPrev();
        }

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


    public void stopThread(){
        this.isComputing = false;
        this.isThreadStopped = true;
    }


    private double getHeuristic(Point source, Point destination){
        return Math.abs(source.getX() - destination.getX()) + Math.abs(source.getY() - destination.getY());
    }

    private void processNeighbourDijkstra(Cell curCell, Edge edge){
        Cell neighbourCell = edge.getDestination();
        if(neighbourCell.getCellType() == CellType.WALL){
            return;
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

    private void processNeighbourAStar(Cell curCell, Edge edge, Cell goalCell){
        Cell neighbourCell = edge.getDestination();
        if(neighbourCell.getCellType() == CellType.WALL){
            return;
        }
        if(visitedCellsSet.contains(neighbourCell)){
            return;
        }

        double startDist = curCell.getDistanceFromStart() + edge.getCost();
        double goalDist = getHeuristic(neighbourCell.getPosition(), goalCell.getPosition());
        double approxCost = startDist + goalDist;

        if(neighbourCell.getCellType() != CellType.EXPLORED
                && neighbourCell.getCellType() != CellType.START){
            neighbourCell.setCellType(CellType.TO_EXPLORE);
        }

        if(!priorityQueue.contains(neighbourCell) || startDist < neighbourCell.getDistanceFromStart()){
            neighbourCell.setDistanceFromStart(startDist);
            neighbourCell.setCost(approxCost);
            neighbourCell.setPrev(curCell);
            priorityQueue.add(neighbourCell);
        }
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
