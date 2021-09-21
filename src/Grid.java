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
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Grid extends JPanel implements MouseListener {

    public static double SWAMP_WEIGHT_PENALTY = 4.5;

    protected Cell[][] grid;
    protected Cell startCell;
    protected Cell goalCell;

    protected AlgorithmThread algorithmThread;
    protected PainterThread painterThread;

    protected int width;
    protected int height;

    protected int cellHeight;
    protected int cellWidth;

    protected int numRows;
    protected int numCols;

    protected boolean isPainting;


    /**
     * Creates a Grid object with its width, height, the number of rows and the number of columns
     * Populates the grid with cells via createGrid()
     *
     * @param height  int with height of the Grid
     * @param width   int with width of the Grid
     * @param numRows number of rows
     * @param numCols number of columns
     */
    public Grid(int height, int width, int numRows, int numCols) {
        this.width = width;
        this.height = height;
        this.numRows = numRows;
        this.numCols = numCols;

        this.cellHeight = height / numRows;
        this.cellWidth = width / numCols;

        this.isPainting = true;

        createGrid();
        addMouseListener(this);
        setPreferredSize(new Dimension(width, height));
    }

    /**
     * Populates the Grid with new Cell objects
     * Places the Start and the Goal cells in their default positions
     */
    protected void createGrid() {
        this.grid = new Cell[numRows][numCols];
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                grid[i][j] = new Cell(new Point(i * cellWidth, j * cellHeight), cellWidth, cellHeight);
            }
        }

        startCell = grid[2][2];
        startCell.setCellType(CellType.START);
        goalCell = grid[numRows - 3][numCols - 3];
        goalCell.setCellType(CellType.GOAL);
        this.update();

    }

    /**
     * Clears the Grid from the EXPLORED and TO_EXPLORE CellTypes,
     * and preserving Wall and Swamp cells, as well as the positions of the Start and Goal cells
     */
    public void clearExploredAfterRun() {
        System.out.println("clearExploredAfterRun()");
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                Cell curCell = grid[i][j];
                boolean wasSwamp = curCell.isSwamp();
                CellType typeToRecreate = curCell.getCellType();
                grid[i][j] = new Cell(new Point(i * cellWidth, j * cellHeight), cellWidth, cellHeight);
                if (curCell == startCell) startCell = grid[i][j];
                if (curCell == goalCell) goalCell = grid[i][j];
                if (typeToRecreate == CellType.WALL ||
                        typeToRecreate == CellType.START ||
                        typeToRecreate == CellType.GOAL) {
                    grid[i][j].setCellType(typeToRecreate);
                }
                if (wasSwamp) {
                    grid[i][j].setCellType(CellType.SWAMP);
                }
            }
        }
        this.update();
    }

    /**
     * Repaints this component
     *
     * @param graphics a graphics object
     */
    public void paintComponent(Graphics graphics) {
        graphics.setColor(Color.BLACK);
        graphics.drawRect(0, 0, width, height);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                grid[i][j].draw(graphics);
            }
        }
    }

    /**
     * Creates a set of edges for each Cell in the grid
     */
    public void createEdges() {
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                createEdgesUtil(i, j);
            }
        }
    }

    /**
     * Creates a set of edges (4-directional edges are a subset of the 8-directional set)
     * for the currently processed Cell in the createEdges() function
     * For diagonal edges, cost is multiplied by a factor of 1.4. Additional multiplier applies for Swamp cells
     *
     * @param i row coordinate of the current Cell
     * @param j j column coordinate of the current Cell
     */
    private void createEdgesUtil(int i, int j) {
        Cell curCell = grid[i][j];
        CellType curCellType = curCell.getCellType();

        double weightFactor = 1;
        if (curCellType == CellType.SWAMP) weightFactor = SWAMP_WEIGHT_PENALTY;
        if (i + 1 < numRows) {
            Edge newEdge = new Edge((int) (cellWidth * weightFactor), grid[i + 1][j]);
            curCell.addEdgeEightDir(newEdge);
            curCell.addEdgeFourDir(newEdge);
        }
        if (j + 1 < numCols) {
            Edge newEdge = new Edge((int) (cellWidth * weightFactor), grid[i][j + 1]);
            curCell.addEdgeEightDir(newEdge);
            curCell.addEdgeFourDir(newEdge);
        }
        if (i - 1 >= 0) {
            Edge newEdge = new Edge((int) (cellWidth * weightFactor), grid[i - 1][j]);
            curCell.addEdgeEightDir(newEdge);
            curCell.addEdgeFourDir(newEdge);
        }
        if (j - 1 >= 0) {
            Edge newEdge = new Edge((int) (cellWidth * weightFactor), grid[i][j - 1]);
            curCell.addEdgeEightDir(newEdge);
            curCell.addEdgeFourDir(newEdge);
        }
        if (i + 1 < numRows && j + 1 < numCols) {
            Edge newEdge = new Edge((int) (cellHeight * 1.4 * weightFactor), grid[i + 1][j + 1]);
            curCell.addEdgeEightDir(newEdge);
        }
        if (i - 1 >= 0 && j - 1 >= 0) {
            Edge newEdge = new Edge((int) (cellHeight * 1.4 * weightFactor), grid[i - 1][j - 1]);
            curCell.addEdgeEightDir(newEdge);
        }
        if (i + 1 < numRows && j - 1 >= 0) {
            Edge newEdge = new Edge((int) (cellHeight * 1.4 * weightFactor), grid[i + 1][j - 1]);
            curCell.addEdgeEightDir(newEdge);
        }
        if (i - 1 >= 0 && j + 1 < numCols) {
            Edge newEdge = new Edge((int) (cellHeight * 1.4 * weightFactor), grid[i - 1][j + 1]);
            curCell.addEdgeEightDir(newEdge);
        }
    }

    /**
     * Repaints the object
     */
    public void update() {
        this.repaint();
    }

    /**
     * Runs the search algorithm thread object with new Start and Goal cells
     */
    public void start() {
        System.out.println("start() in " + this.getClass());
        this.algorithmThread.setStartCell(startCell);
        this.algorithmThread.setGoalCell(goalCell);
        this.algorithmThread.setThreadStopped(false);
        this.algorithmThread.start();
    }

    /**
     * Stops the currently running search algorithm thread
     * creates a new grid and updates the Grid component
     */
    public void stopThreadAndCreateGrid() {
        if (this.algorithmThread != null) {
            this.algorithmThread.setThreadStopped(true);

        }
        createGrid();
        update();
    }

    /**
     * Stops the currently running search algorithm thread
     */
    public void stopThread() {
        if (this.algorithmThread != null) {
            this.algorithmThread.setThreadStopped(true);

        }
    }

    /**
     * Sets the Start or Goal cell object in the clicked cell
     * ctrl + LMB for Start
     * alt + RMB for Goal
     *
     * @param mouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        System.out.println("mouseClicked() in Grid");
        mouseWasClicked(mouseEvent);
    }

    protected void mouseWasClicked(MouseEvent mouseEvent) {
        int x = (int) getMousePosition().getX();
        int y = (int) getMousePosition().getY();
        Cell curCell = grid[x / getCellWidth()][y / getCellHeight()];

        if (!mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON1) {
            System.out.println("mouseWasClicked() in Grid");
            startCell.setCellType(CellType.REGULAR);
            startCell = grid[x / cellWidth][y / cellHeight];
            curCell.setCellType(CellType.START);

        }
        if (!mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON3) {
            System.out.println("mouseWasClicked() in Grid");
            goalCell.setCellType(CellType.REGULAR);
            goalCell = grid[x / cellWidth][y / cellHeight];
            curCell.setCellType(CellType.GOAL);

        }

        update();
    }

    private void setIsMousePressed(boolean isMousePressed) {

    }


    /**
     * Creates and runs the painterThread object, with the isPainting flag passed
     * (true for setting walls / swamps, false for clearing them)
     *
     * @param mouseEvent MouseEvent object
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        this.mouseWasPressed(mouseEvent);
    }

    /**
     * A utility method for mousePressed
     *
     * @param mouseEvent MouseEvent object
     */
    protected void mouseWasPressed(MouseEvent mouseEvent) {
        if (mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON1) {

            SwingWorker swingWorker = new SwingWorker<Void, Void>() {
                protected Void doInBackground() {
                    Grid grid = Grid.this;
                    painterThread = new PainterThread(grid, grid.isPainting, mouseEvent);
                    painterThread.setThreadStopped(false);
                    painterThread.start();

                    return null;
                }
            };
            swingWorker.run();
            System.out.println("PRESSED");
        } else if (mouseEvent.isAltDown() && mouseEvent.getButton() == MouseEvent.BUTTON3) {

            SwingWorker swingWorker = new SwingWorker<Void, Void>() {
                protected Void doInBackground() {
                    Grid grid = Grid.this;
                    painterThread = new PainterThread(grid, grid.isPainting, mouseEvent);
                    painterThread.setThreadStopped(false);
                    painterThread.start();

                    return null;
                }
            };
            swingWorker.run();
            System.out.println("PRESSED");
        }
    }

    /**
     * Stops the currently running painterThread, and sets the isPainting flag to its opposite
     * for the next painterThread to be called to do the opposite (if the current one is setting
     * walls / swamps, it's successor will clear them)
     *
     * @param mouseEvent MouseEvent object which prompts action
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        this.mouseWasReleased(mouseEvent);
    }

    /**
     * A utility method for mouseReleased
     *
     * @param mouseEvent MouseEvent object which prompts action
     */
    protected void mouseWasReleased(MouseEvent mouseEvent) {
        if (mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON1) {
            this.painterThread.setThreadStopped(true);
            this.painterThread = null;
            isPainting = !isPainting;
        }

        if (mouseEvent.isAltDown() && mouseEvent.getButton() == MouseEvent.BUTTON3) {
            this.painterThread.setThreadStopped(true);
            this.painterThread = null;
            isPainting = !isPainting;
        }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }

    public boolean isPainting() {
        return isPainting;
    }

    public void setPainting(boolean painting) {
        isPainting = painting;
    }

    public Cell[][] getGrid() {
        return grid;
    }

    public void setGrid(Cell[][] grid) {
        this.grid = grid;
    }

    public int getCellHeight() {
        return cellHeight;
    }

    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }

    public int getCellWidth() {
        return cellWidth;
    }

    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
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

    public AlgorithmThread getAlgorithmThread() {
        return algorithmThread;
    }

    public void setAlgorithmThread(AlgorithmThread algorithmThread) {
        this.algorithmThread = algorithmThread;
    }
}
