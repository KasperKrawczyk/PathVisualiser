import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Grid extends JPanel implements MouseListener {
    private int width;
    private int height;

    private int cellHeight;
    private int cellWidth;

    private int numRows;
    private int numCols;

    private boolean isSettingWalls = true;

    private Cell[][] grid;
    private Cell startCell;
    private Cell goalCell;

    private AlgorithmThread algorithmThread;
    private WallWorkerThread wallWorkerThread;

    public Grid(int height, int width, int numRows, int numCols){
        this.width = width;
        this.height = height;
        this.numRows = numRows;
        this.numCols = numCols;

        this.cellHeight = height / numRows;
        this.cellWidth = width / numCols;


        createGrid();
        addMouseListener(this);
        this.setPreferredSize(new Dimension(width, height));
    }

    private void createGrid(){
        this.grid = new Cell[numRows][numCols];
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                grid[i][j] = new Cell(new Point(i * cellWidth, j * cellHeight), cellWidth, cellHeight);
            }
        }

        startCell = grid[2][2];
        startCell.setCellType(CellType.START);
        goalCell = grid[numRows - 3][numCols - 3];
        goalCell.setCellType(CellType.GOAL);
        this.update();

    }

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

    public void createEdges(){
        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                createEdgesUtil(i, j);
            }
        }
    }

    private void createEdgesUtil(int i, int j){
        CellType curCellType = grid[i][j].getCellType();
        if(i + 1 < numRows){
            grid[i][j].addEdge(new Edge(cellWidth, grid[i + 1][j]));
        }
        if(j + 1 < numCols){
            grid[i][j].addEdge(new Edge(cellHeight, grid[i][j + 1]));
        }
        if(i - 1 >= 0){
            grid[i][j].addEdge(new Edge(cellWidth, grid[i - 1][j]));
        }
        if(j - 1 >= 0){
            grid[i][j].addEdge(new Edge(cellHeight, grid[i][j - 1]));
        }
        if(i + 1 < numRows && j + 1 < numCols){
            grid[i][j].addEdge(new Edge((int)(cellHeight * 1.4), grid[i + 1][j + 1]));
        }
        if(i - 1 >= 0 && j - 1 >= 0){
            grid[i][j].addEdge(new Edge((int)(cellHeight * 1.4), grid[i - 1][j - 1]));
        }
        if(i + 1 < numRows && j - 1 >= 0){
            grid[i][j].addEdge(new Edge((int)(cellHeight * 1.4), grid[i + 1][j - 1]));
        }
        if(i - 1 >= 0 && j + 1 < numCols){
            grid[i][j].addEdge(new Edge((int)(cellHeight * 1.4), grid[i - 1][j + 1]));
        }
    }

    public void update(){
        this.repaint();
    }

    public void start(int chosenAlgorithm){
        this.algorithmThread.setGoalCell(goalCell);
        this.algorithmThread.setStartCell(startCell);
        this.algorithmThread.setChosenAlgorithm(chosenAlgorithm);
        this.algorithmThread.setThreadStopped(false);
        this.algorithmThread.start();
    }

    public void stopThread(){
        this.algorithmThread.setThreadStopped(true);
        createGrid();
        //this.algorithmThread = new AlgorithmThread(this);

        update();
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

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

        int x = (int) getMousePosition().getX();
        int y = (int) getMousePosition().getY();
        Cell curCell = grid[x / getCellWidth()][y / getCellHeight()];

        if (!mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON1) {

            startCell.setCellType(CellType.REGULAR);
            startCell = grid[x / cellWidth][y / cellHeight];
            curCell.setCellType(CellType.START);

        }
        if (mouseEvent.getButton() == MouseEvent.BUTTON3) {

            goalCell.setCellType(CellType.REGULAR);
            goalCell = grid[x / cellWidth][y / cellHeight];
            curCell.setCellType(CellType.GOAL);

        }

        update();
    }

    private void setIsMousePressed(boolean isMousePressed){

    }


    @Override
    public void mousePressed(MouseEvent mouseEvent) {

        Grid grid = this;

        if (mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON1) {

            SwingWorker swingWorker = new SwingWorker<Void,Void>(){
                protected Void doInBackground(){
                    wallWorkerThread = new WallWorkerThread(grid, grid.isSettingWalls);
                    wallWorkerThread.setThreadStopped(false);
                    wallWorkerThread.start();

                    return null;
                }
            };
            swingWorker.run();
            System.out.println("PRESSED");
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

            if(mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON1){
                this.wallWorkerThread.setThreadStopped(true);
                this.wallWorkerThread = null;
                System.out.println("RELEASED");
                isSettingWalls = !isSettingWalls;

            }
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
