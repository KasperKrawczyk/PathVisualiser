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

import java.awt.event.*;

public class InstantGrid extends Grid {

    int chosenAlgorithm;


    public InstantGrid(int height, int width, int numRows, int numCols) {
        super(height, width, numRows, numCols);
        startAlgorithmThread();
        System.out.println("InstantGrid created");
    }


    public void startAlgorithmThread(){
        System.out.println("startAlgorithmThread() in " + this.getClass());


                InstantGrid instantGrid = InstantGrid.this;
                instantGrid.createEdges();
                //System.out.println("startAlgorithmThread() in SwingWorker");
                instantGrid.setAlgorithmThread(new InstantAlgorithmThread(instantGrid, getChosenAlgorithm()));
                instantGrid.start();



    }

    public void setAlgorithmThread(InstantAlgorithmThread instantAlgorithmThread) {
        this.algorithmThread = instantAlgorithmThread;
    }

    /**
     * Sets the Start or Goal cell object in the clicked cell
     * ctrl + LMB for Start
     * alt + RMB for Goal
     * @param mouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        System.out.println("mouseClicked() in InstantGrid");
        this.mouseWasClicked(mouseEvent);
    }

    @Override
    protected void mouseWasClicked(MouseEvent mouseEvent){
        System.out.println("mouseWasClicked() in InstantGrid");
        int x = (int) getMousePosition().getX();
        int y = (int) getMousePosition().getY();
        Cell curCell = grid[x / getCellWidth()][y / getCellHeight()];

        if (!mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON1) {
            System.out.println("mouseWasClicked() in InstantGrid");
            startCell.setCellType(CellType.REGULAR);
            startCell = grid[x / cellWidth][y / cellHeight];
            curCell.setCellType(CellType.START);
            clearExploredAfterRun();
            startAlgorithmThread();

        }
        if (!mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON3) {
            System.out.println("mouseWasClicked() in InstantGrid");
            goalCell.setCellType(CellType.REGULAR);
            goalCell = grid[x / cellWidth][y / cellHeight];
            curCell.setCellType(CellType.GOAL);
            clearExploredAfterRun();
            startAlgorithmThread();

        }

        update();
    }

    public int getChosenAlgorithm() {
        return chosenAlgorithm;
    }

    public void setChosenAlgorithm(int chosenAlgorithm) {
        this.chosenAlgorithm = chosenAlgorithm;
    }

    //    public static void main(String[] args) {
//        Grid grid = new InstantGrid(5,5,5,5);
//        grid.mouseWasClicked(
//                new MouseEvent(
//                       new JPanel(), 1, 1, 1, 1, 1, 1, true, 1)
//        );
//    }
}
