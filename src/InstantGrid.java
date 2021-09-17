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
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class InstantGrid extends Grid {

    public static double SWAMP_WEIGHT_PENALTY = 4.5;

    protected int width;
    protected int height;

    protected int cellHeight;
    protected int cellWidth;

    protected int numRows;
    protected int numCols;

    protected boolean isPainting = true;

    protected Cell[][] grid;
    protected Cell startCell;
    protected Cell goalCell;

    protected InstantAlgorithmThread instantAlgorithmThread;
    protected PainterThread painterThread;

    public InstantGrid(int height, int width, int numRows, int numCols){
        super(height, width, numRows, numCols);
        this.width = width;
        this.height = height;
        this.numRows = numRows;
        this.numCols = numCols;

        this.cellHeight = height / numRows;
        this.cellWidth = width / numCols;


        this.createGrid();
        addMouseListener(this);
        this.setPreferredSize(new Dimension(width, height));
        //startInstantAlgorithmThread();
    }

    public void startInstantAlgorithmThread(){

        InstantGrid instantGrid = this;
        System.out.println("here?");

        SwingWorker swingWorker = new SwingWorker<Void, Void>(){
            protected Void doInBackground() {

                instantGrid.createEdges();
                instantAlgorithmThread = new InstantAlgorithmThread(instantGrid, instantAlgorithmThread.getChosenAlgorithm());
                instantAlgorithmThread.setStartCell(startCell);
                instantAlgorithmThread.setGoalCell(goalCell);
                setAlgorithmThread(instantAlgorithmThread);

                start();


                return null;
            }
        };
        swingWorker.run();

    }

    /**
     * Populates the Grid with new Cell objects
     * Places the Start and the Goal cells in their default positions
     */
    protected void createGrid(){
        this.grid = new Cell[numRows][numCols];
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                this.grid[i][j] = new Cell(new Point(i * cellWidth, j * cellHeight), cellWidth, cellHeight);
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
     * and preserving WALL and SWAMP cells, as well as the positions of the Start and Goal cells
     */
    public void clearExploredAfterRun(){
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                Cell curCell = grid[i][j];
                boolean wasSwamp = curCell.isSwamp();
                CellType typeToRecreate = curCell.getCellType();
                grid[i][j] = new Cell(new Point(i * cellWidth, j * cellHeight), cellWidth, cellHeight);
                if(curCell == startCell) startCell = grid[i][j];
                if(curCell == goalCell) goalCell = grid[i][j];
                if(typeToRecreate == CellType.WALL ||
                        typeToRecreate == CellType.START ||
                        typeToRecreate == CellType.GOAL){
                    grid[i][j].setCellType(typeToRecreate);
                }
                if(wasSwamp){
                    grid[i][j].setCellType(CellType.SWAMP);
                }
            }
        }
        this.update();
    }

    /**
     * Repaints this component
     * @param graphics a graphics object
     */
    public void paintComponent(Graphics graphics){
        graphics.setColor(Color.BLACK);
        graphics.drawRect(0, 0, width, height);
        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);
        graphics.setColor(Color.BLACK);

        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                grid[i][j].draw(graphics);
            }
        }
    }

    /**
     * Creates a set of edges for each Cell in the grid
     */
    public void createEdges(){
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                createEdgesUtil(i, j);
            }
        }
    }

    /**
     * Creates a set of edges (4-directional edges are a subset of the 8-directional set)
     * for the currently processed Cell in the createEdges() function
     * For diagonal edges, cost is multiplied by a factor of 1.4. Additional multiplier applies for Swamp cells
     * @param i row coordinate of the current Cell
     * @param j j column coordinate of the current Cell
     */
    protected void createEdgesUtil(int i, int j){
        Cell curCell = grid[i][j];
        CellType curCellType = curCell.getCellType();

        double weightFactor = 1;
        if(curCellType == CellType.SWAMP) weightFactor = SWAMP_WEIGHT_PENALTY;
        if(i + 1 < numRows){
            Edge newEdge = new Edge((int) (cellWidth * weightFactor), grid[i + 1][j]);
            curCell.addEdgeEightDir(newEdge);
            curCell.addEdgeFourDir(newEdge);
        }
        if(j + 1 < numCols){
            Edge newEdge = new Edge((int) (cellWidth * weightFactor), grid[i][j + 1]);
            curCell.addEdgeEightDir(newEdge);
            curCell.addEdgeFourDir(newEdge);
        }
        if(i - 1 >= 0){
            Edge newEdge = new Edge((int) (cellWidth * weightFactor), grid[i - 1][j]);
            curCell.addEdgeEightDir(newEdge);
            curCell.addEdgeFourDir(newEdge);
        }
        if(j - 1 >= 0){
            Edge newEdge = new Edge((int) (cellWidth * weightFactor), grid[i][j - 1]);
            curCell.addEdgeEightDir(newEdge);
            curCell.addEdgeFourDir(newEdge);
        }
        if(i + 1 < numRows && j + 1 < numCols){
            Edge newEdge = new Edge((int)(cellHeight * 1.4 * weightFactor), grid[i + 1][j + 1]);
            curCell.addEdgeEightDir(newEdge);
        }
        if(i - 1 >= 0 && j - 1 >= 0){
            Edge newEdge = new Edge((int)(cellHeight * 1.4 * weightFactor), grid[i - 1][j - 1]);
            curCell.addEdgeEightDir(newEdge);
        }
        if(i + 1 < numRows && j - 1 >= 0){
            Edge newEdge = new Edge((int)(cellHeight * 1.4 * weightFactor), grid[i + 1][j - 1]);
            curCell.addEdgeEightDir(newEdge);
        }
        if(i - 1 >= 0 && j + 1 < numCols){
            Edge newEdge = new Edge((int)(cellHeight * 1.4 * weightFactor), grid[i - 1][j + 1]);
            curCell.addEdgeEightDir(newEdge);
        }
    }

    public void update(){
        this.repaint();
    }

    /**
     * Runs the search algorithm thread object with new Start and Goal cells
     */
    public void start(){
        this.instantAlgorithmThread.setStartCell(startCell);
        this.instantAlgorithmThread.setGoalCell(goalCell);
        this.instantAlgorithmThread.setThreadStopped(false);
        this.instantAlgorithmThread.start();

    }

    /**
     * Stops the currently running search algorithm thread
     * creates a new grid and updates the Grid component
     */
    public void stopThreadAndCreateGrid(){
        if(this.instantAlgorithmThread != null){
            this.instantAlgorithmThread.setThreadStopped(true);

        }
        createGrid();
        update();
    }

    /**
     * Stops the currently running search algorithm thread
     */
    public void stopThread(){
        if(this.instantAlgorithmThread != null){
            this.instantAlgorithmThread.setThreadStopped(true);

        }
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

    public AlgorithmThread getInstantAlgorithmThread() {
        return instantAlgorithmThread;
    }

    public void setAlgorithmThread(InstantAlgorithmThread instantAlgorithmThread) {
        this.instantAlgorithmThread = instantAlgorithmThread;
    }

    /**
     * Sets the Start or Goal cell object in the clicked cell
     * ctrl + LMB for Start
     * alt + RMB for Goal
     * @param mouseEvent
     */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

        int x = (int) getMousePosition().getX();
        int y = (int) getMousePosition().getY();
        Cell curCell = grid[x / getCellWidth()][y / getCellHeight()];

        if (!mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON1) {

            startCell.setCellType(CellType.REGULAR);
            startCell = grid[x / cellWidth][y / cellHeight];
            curCell.setCellType(CellType.START);
            clearExploredAfterRun();
            startInstantAlgorithmThread();

        }
        if (!mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON3) {

            goalCell.setCellType(CellType.REGULAR);
            goalCell = grid[x / cellWidth][y / cellHeight];
            curCell.setCellType(CellType.GOAL);
            clearExploredAfterRun();
            startInstantAlgorithmThread();

        }

        update();
    }

    protected void setIsMousePressed(boolean isMousePressed){

    }


    /**
     * Creates and runs the painterThread object, with the isPainting flag passed
     * (true for setting walls / swamps, false for clearing them)
     * @param mouseEvent
     */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {

        InstantGrid instantGrid = this;

        if (mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON1) {

            SwingWorker swingWorker = new SwingWorker<Void,Void>(){
                protected Void doInBackground(){

                    painterThread = new PainterThread(instantGrid, instantGrid.isPainting, mouseEvent);
                    painterThread.setThreadStopped(false);
                    painterThread.start();

                    return null;
                }
            };
            swingWorker.run();
            System.out.println("PRESSED");
        } else if (mouseEvent.isAltDown() && mouseEvent.getButton() == MouseEvent.BUTTON3){

            SwingWorker swingWorker = new SwingWorker<Void,Void>(){
                protected Void doInBackground(){

                    painterThread = new PainterThread(instantGrid, instantGrid.isPainting, mouseEvent);
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
     * @param mouseEvent
     */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

        if (mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON1) {
            this.painterThread.setThreadStopped(true);
            this.painterThread = null;
            System.out.println("RELEASED");
            isPainting = !isPainting;
        }

        if (mouseEvent.isAltDown() && mouseEvent.getButton() == MouseEvent.BUTTON3) {
            this.painterThread.setThreadStopped(true);
            this.painterThread = null;
            System.out.println("RELEASED");
            isPainting = !isPainting;
        }


    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
