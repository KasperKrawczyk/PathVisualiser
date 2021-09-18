/**
 * Copyright © 2021 Kasper Krawczyk
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Icons by Icons8 (https://icons8.com)
 */

import java.awt.*;
import java.util.*;

public class AlgorithmThread extends Thread {

    public static final int DIJKSTRA = 0;
    public static final int A_STAR = 1;
    public static final int BFS = 2;

    int chosenAlgorithm;

    protected final Set<Cell> visitedCellsSet;
    protected PriorityQueue<Cell> priorityQueue;
    protected Queue<Cell> queue;
    protected final Grid grid;

    protected Cell startCell;
    protected Cell goalCell;

    protected boolean isStartChosen = true;
    protected boolean isEndChosen = true;
    protected boolean isThreadStopped = true;

    /**
     * Constructs the search algorithm thread with all necessary data structures.
     * @param grid The grid object for the search to be performed on
     * @param chosenAlgorithm int indicating what algorithm to perform (0 == DIJKSTRA, 1 == A_STAR, 2 == BFS)
     */
    public AlgorithmThread(Grid grid, int chosenAlgorithm){
        System.out.println("AlgorithmThread created");
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

    /**
     * Attempts to find the goalCell object with the Breadth First Search algorithm.
     * @param startCell Cell object, start of the search
     * @param goalCell Cell object, goal of the search
     */
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

    /**
     * Finds the shortest path for the Dijkstra and A* algorithms.
     * @param startCell Cell object, start of the search
     * @param goalCell Cell object, goal of the search
     * @param algorithm int indicating the algorithm (0 == DIJKSTRA, 1 == A_STAR)
     */
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

    /**
     * Calculates the Manhattan distance between two points
     * @param source Point object
     * @param destination Point object
     * @return double, Manhattan distance between two points
     */
    protected double getHeuristic(Point source, Point destination){
        return Math.abs(source.getX() - destination.getX()) + Math.abs(source.getY() - destination.getY());
    }

    /**
     * Processes a neighbour cell of curCell for the Dijkstra algorithm (8-directional, 8 edges)
     * @param curCell current cell
     * @param edge currently processed edge
     */
    protected void processNeighbourDijkstra(Cell curCell, Edge edge){
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

    /**
     * Processes a neighbour cell of curCell for the A* algorithm (8-directional, 8 edges)
     * @param curCell current cell
     * @param edge currently processed edge
     */
    protected void processNeighbourAStar(Cell curCell, Edge edge) {
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

    /**
     * Processes a neighbour cell of curCell for the Breadth First Search algorithm (4-directional, 4 edges)
     * @param edge currently processed edge
     */
    protected void processNeighbourBFS(Edge edge){
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

    /**
     * Iterates over the linked list of getPrev() of every cell in the path, and adds them to the path ArrayList
     * @param goalCell the end cell of the path
     * @return ArrayList<Cell> constituting the path
     */
    protected ArrayList<Cell> buildPath(Cell goalCell){
        ArrayList<Cell> path = new ArrayList<>();
        Cell curCell = goalCell;
        path.add(curCell);

        while(curCell.getPrev() != null){
            path.add(curCell.getPrev());
            curCell = curCell.getPrev();
        }
        return path;
    }

    /**
     * Iterates over an ArrayList<Cell> of cells constituting the reversed path (Start -> Goal)
     * Setting each cell (but for Start and Goal) as CellType.PATH, and updating the grid with each iteration
     * @param path
     * @param startCell
     * @param goalCell
     */
    protected void animatePath(ArrayList<Cell> path, Cell startCell, Cell goalCell){
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
