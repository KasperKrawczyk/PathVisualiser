import java.awt.*;
import java.util.*;

public class AlgorithmThread extends Thread {

    public static final int DIJKSTRA = 0;
    public static final int A_STAR = 1;
    public static final int BFS = 2;

    private final Set<Cell> visitedCellsSet;
    private PriorityQueue<Cell> priorityQueue;
    private Queue<Cell> queue;
    private final Grid grid;

    private Cell startCell;
    private Cell goalCell;

    int chosenAlgorithm;

    private boolean isStartChosen = true;
    private boolean isEndChosen = true;
    private boolean isThreadStopped = true;

    public AlgorithmThread(Grid grid, int chosenAlgorithm){
        this.chosenAlgorithm = chosenAlgorithm;
        if(chosenAlgorithm == BFS){
            this.queue = new LinkedList<>();
        } else {
            this.priorityQueue = new PriorityQueue<>();
        }
        this.visitedCellsSet = new HashSet<>();
        this.grid = grid;
        this.startCell = grid.getStartCell();
        this.goalCell = grid.getGoalCell();

    }

    public void run(){
        if(this.chosenAlgorithm == BFS){
            do{
                findGoalBFS(this.grid.getStartCell(), this.grid.getGoalCell());
            }while(!isThreadStopped());
        } else {
            do{
                findPath(this.grid.getStartCell(), this.grid.getGoalCell(), chosenAlgorithm);
            }while(!isThreadStopped());
        }
    }

    public void findGoalBFS(Cell startCell, Cell goalCell){
        queue.add(startCell);
        visitedCellsSet.add(startCell);

        while(!queue.isEmpty() && !isThreadStopped()){
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.grid.update();

            Cell curCell = queue.poll();
            this.visitedCellsSet.add(curCell);
            System.out.println("curCell = " + curCell);

            curCell.setCellType(CellType.EXPLORED);

            if(curCell == startCell){
                startCell.setCellType(CellType.START);
            }
            if(curCell == goalCell){
                goalCell.setCellType(CellType.GOAL);
                this.grid.update();
                break;
            }

            for(Edge edge : curCell.getEdgesFourDir()){
                this.processNeighbourBFS(edge);
            }
            //queue.remove(curCell);
            //visitedCellsSet.add(curCell);
        }

        this.setThreadStopped(true);
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

            for(Edge edge : curCell.getEdgesEightDir()){
                if(algorithm == DIJKSTRA){
                    this.processNeighbourDijkstra(curCell, edge);
                } else if(algorithm == A_STAR){
                    this.processNeighbourAStar(curCell, edge);
                }
            }
            priorityQueue.remove(curCell);
            visitedCellsSet.add(curCell);
        }

        ArrayList<Cell> path = this.buildPath(goalCell);

        Collections.reverse(path);

        //animate the path
        this.animatePath(path, startCell, goalCell);
        this.setThreadStopped(true);
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

    private void processNeighbourAStar(Cell curCell, Edge edge) {
        Cell neighbourCell = edge.getDestination();
        if (neighbourCell.getCellType() == CellType.WALL || visitedCellsSet.contains(neighbourCell)) {
            return;
        }

        double startDist = curCell.getDistanceFromStart() + edge.getCost();
        double goalDist = getHeuristic(neighbourCell.getPosition(), this.goalCell.getPosition());
        double approxCost = startDist + goalDist;

        if (neighbourCell.getCellType() != CellType.EXPLORED
                && neighbourCell.getCellType() != CellType.START) {
            neighbourCell.setCellType(CellType.TO_EXPLORE);
        }


        if (!priorityQueue.contains(neighbourCell)) {
            neighbourCell.setDistanceFromStart(startDist);
            neighbourCell.setCost(approxCost);
            neighbourCell.setPrev(curCell);
            priorityQueue.add(neighbourCell);
        } else if (startDist < neighbourCell.getDistanceFromStart()) {
            neighbourCell.setDistanceFromStart(startDist);
            neighbourCell.setCost(approxCost);
            neighbourCell.setPrev(curCell);

            if (visitedCellsSet.contains(neighbourCell)) {
                visitedCellsSet.remove(neighbourCell);
                priorityQueue.add(neighbourCell);
            }
        }
    }

    private void processNeighbourBFS(Edge edge){
        Cell neighbourCell = edge.getDestination();
        if(neighbourCell.getCellType() == CellType.WALL || visitedCellsSet.contains(neighbourCell)){
            return;
        }

        if(neighbourCell.getCellType() != CellType.EXPLORED
                && neighbourCell.getCellType() != CellType.START
                && neighbourCell.getCellType() != CellType.GOAL){
            neighbourCell.setCellType(CellType.TO_EXPLORE);
        }


            queue.add(neighbourCell);
            visitedCellsSet.add(neighbourCell);
    }

    private ArrayList<Cell> buildPath(Cell goalCell){
        ArrayList<Cell> path = new ArrayList<>();
        Cell curCell = goalCell;
        path.add(curCell);

        while(curCell.getPrev() != null){
            System.out.println("curCell = " + curCell + " || prev = " + curCell.getPrev());
            path.add(curCell.getPrev());
            curCell = curCell.getPrev();
        }
        return path;
    }

    private void animatePath(ArrayList<Cell> path, Cell startCell, Cell goalCell){
        for(Cell cell : path){
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(cell != startCell && cell != goalCell){
                cell.setCellType(CellType.PATH);
                this.grid.update();
            }
        }
    }

    public int getChosenAlgorithm() {
        return chosenAlgorithm;
    }

    public void setChosenAlgorithm(int chosenAlgorithm) {
        this.chosenAlgorithm = chosenAlgorithm;
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

    public Cell getStartCell() {
        return startCell;
    }

    public void setStartCell(Cell startCell) {
        this.startCell = startCell;
    }

    public Cell getGoalCell() {
        return goalCell;
    }

    public void setGoalCell(Cell goalCell) {
        this.goalCell = goalCell;
    }

    public boolean isThreadStopped() {
        return isThreadStopped;
    }

    public void setThreadStopped(boolean isThreadStopped) {
        System.out.println(this + " stopped? =" + isThreadStopped);
        this.isThreadStopped = isThreadStopped;
    }
}
