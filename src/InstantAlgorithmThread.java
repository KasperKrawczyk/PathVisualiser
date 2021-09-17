import java.util.ArrayList;
import java.util.Collections;

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


public class InstantAlgorithmThread extends AlgorithmThread {

    /**
     * Creates a InstantAlgorithmThread object
     * @param instantGrid the InstantGrid object to paint on
     */
    public InstantAlgorithmThread(InstantGrid instantGrid, int selectedAlgorithm){
        super(instantGrid, selectedAlgorithm);

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

        //draw the path
        this.drawPath(path, startCell, goalCell);
        this.setThreadStopped(true);
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
     * Iterates over an ArrayList<Cell> of cells constituting the reversed path (Start -> Goal)
     * Setting each cell (but for Start and Goal) as CellType.PATH, and updating the grid with each iteration
     * Draws the path instantly
     * @param path
     * @param startCell
     * @param goalCell
     */
    protected void drawPath(ArrayList<Cell> path, Cell startCell, Cell goalCell){
        for(Cell cell : path){
            if(cell != startCell && cell != goalCell){
                cell.setCellType(CellType.PATH);
                this.grid.update();
            }
        }

    }
}
