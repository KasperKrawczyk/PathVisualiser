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

    private Cell[][] grid;
    private Cell startCell;
    private Cell goalCell;

    private AlgorithmThread algorithmThread;

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

        for(int i = 0; i < numRows; i++){
            for(int j = 0; j < numCols; j++){
                createEdges(i, j);
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

    private void createEdges(int i, int j){
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
        this.algorithmThread.setChosenAlgorithm(chosenAlgorithm);
        this.algorithmThread.setThreadStopped(false);
        this.algorithmThread.start();
    }

    public void stopThread(){
        this.algorithmThread.stopThread();
        createGrid();
        this.algorithmThread = new AlgorithmThread(this);

        update();
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

        Point curMousePosition = new Point(mouseEvent.getX(), mouseEvent.getY());
        Cell curCell = grid[curMousePosition.x / cellWidth][curMousePosition.y / cellHeight];

        if (mouseEvent.isControlDown()
                && mouseEvent.getButton() == MouseEvent.BUTTON1
                && curCell.getCellType() == CellType.REGULAR) {

            curCell.setCellType(CellType.WALL);

        } else if (!mouseEvent.isControlDown() && mouseEvent.getButton() == MouseEvent.BUTTON1) {

            startCell.setCellType(CellType.REGULAR);
            startCell = grid[curMousePosition.x / cellWidth][curMousePosition.y / cellHeight];
            this.algorithmThread.setStartCell(startCell);
            curCell.setCellType(CellType.START);

        } else if (mouseEvent.isControlDown()
                && mouseEvent.getButton() == MouseEvent.BUTTON1
                && curCell.getCellType() == CellType.WALL) {

            curCell.setCellType(CellType.REGULAR);

        }
        if (mouseEvent.getButton() == MouseEvent.BUTTON3) {
            goalCell.setCellType(CellType.REGULAR);
            goalCell = grid[curMousePosition.x / cellWidth][curMousePosition.y / cellHeight];
            curCell.setCellType(CellType.GOAL);
            algorithmThread.setGoalCell(goalCell);
        }

        update();
    }


    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
}
