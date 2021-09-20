/**
 * Copyright © 2021 Kasper Krawczyk
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * <p>
 * Icons by Icons8 (https://icons8.com)
 * Sounds by zapsplat (https://zapsplat.com)
 */

import javax.swing.*;
import java.awt.event.*;

public class InstantGrid extends Grid {

    private int chosenAlgorithm;

    /**
     * Creates an InstantGrid object with call to Grid()
     * Initialises the InstantGrid's InstantAlgorithmThread object
     *
     * @param height  int with height of the Grid
     * @param width   int with width of the Grid
     * @param numRows number of rows
     * @param numCols number of columns
     */
    public InstantGrid(int height, int width, int numRows, int numCols) {
        super(height, width, numRows, numCols);
        startAlgorithmThread();
    }


    /**
     * Creates this Grid's edges (for every Cell) and starts its InstantAlgorithmThread
     */
    public void startAlgorithmThread() {
        SwingWorker swingWorker = new SwingWorker<Void, Void>() {
            protected Void doInBackground() {
                InstantGrid instantGrid = InstantGrid.this;
                instantGrid.createEdges();
                instantGrid.setAlgorithmThread(new InstantAlgorithmThread(instantGrid, getChosenAlgorithm(), 0));
                instantGrid.start();

                return null;
            }
        };
        swingWorker.run();
    }

    /**
     * A utility method for mouseReleased
     *
     * @param mouseEvent MouseEvent object
     */
    @Override
    protected void mouseWasClicked(MouseEvent mouseEvent) {
        int x = (int) getMousePosition().getX();
        int y = (int) getMousePosition().getY();
        Cell curCell = grid[x / getCellWidth()][y / getCellHeight()];

        if (!mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON1) {
            startCell.setCellType(CellType.REGULAR);
            startCell = grid[x / cellWidth][y / cellHeight];
            curCell.setCellType(CellType.START);
            clearExploredAfterRun();
            startAlgorithmThread();

        }
        if (!mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON3) {
            goalCell.setCellType(CellType.REGULAR);
            goalCell = grid[x / cellWidth][y / cellHeight];
            curCell.setCellType(CellType.GOAL);
            clearExploredAfterRun();
            startAlgorithmThread();

        }

        update();
    }

    /**
     * A utility method for mousePressed
     *
     * @param mouseEvent MouseEvent object
     */
    @Override
    protected void mouseWasPressed(MouseEvent mouseEvent) {
        if (mouseEvent.isAltDown() && mouseEvent.getButton() == MouseEvent.BUTTON3 ||
                mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON1) {

            SwingWorker swingWorker = new SwingWorker<Void, Void>() {
                protected Void doInBackground() {
                    clearExploredAfterRun();
                    Grid grid = InstantGrid.this;
                    painterThread = new PainterThread(grid, grid.isPainting, mouseEvent);
                    painterThread.setThreadStopped(false);
                    painterThread.start();

                    return null;
                }
            };
            swingWorker.run();
        }
    }

    /**
     * A utility method for mouseReleased
     *
     * @param mouseEvent MouseEvent object which prompts action
     */
    @Override
    protected void mouseWasReleased(MouseEvent mouseEvent) {
        if (mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON1) {
            this.painterThread.setThreadStopped(true);
            this.painterThread = null;
            isPainting = !isPainting;
            this.startAlgorithmThread();
        }

        if (mouseEvent.isAltDown() && mouseEvent.getButton() == MouseEvent.BUTTON3) {
            this.painterThread.setThreadStopped(true);
            this.painterThread = null;
            isPainting = !isPainting;
            this.startAlgorithmThread();
        }
    }

    public void setAlgorithmThread(InstantAlgorithmThread instantAlgorithmThread) {
        this.algorithmThread = instantAlgorithmThread;
    }

    public int getChosenAlgorithm() {
        return chosenAlgorithm;
    }

    public void setChosenAlgorithm(int chosenAlgorithm) {
        this.chosenAlgorithm = chosenAlgorithm;
    }

}
