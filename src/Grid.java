import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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

    public Grid(int height, int width, int numRows, int numCols){
        this.width = width;
        this.height = height;
        this.numRows = numRows;
        this.numCols = numCols;

        this.cellHeight = height / numRows;
        this.cellWidth = width / numCols;

        buildGrid();
        addMouseListener(this);
        this.setPreferredSize(new Dimension(width, height));
    }

    private void buildGrid(){
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

        startCell = grid[1][1];
        startCell.setColor(AlgorithmThread.START_COLOR);
        goalCell = grid[numRows - 2][numCols - 2];
        goalCell.setColor(AlgorithmThread.GOAL_COLOR);
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
            this.grid[i][j].addEdge(new Edge(cellWidth, this.grid[i + 1][j]));
        }
        if(j + 1 < numCols){
            this.grid[i][j].addEdge(new Edge(cellHeight, this.grid[i][j + 1]));
        }
        if(i - 1 >= 0){
            this.grid[i][j].addEdge(new Edge(cellWidth, this.grid[i - 1][j]));
        }
        if(j - 1 >= 0){
            this.grid[i][j].addEdge(new Edge(cellHeight, this.grid[i][j - 1]));
        }
        if(i + 1 < numRows && j + 1 < numCols){
            this.grid[i][j].addEdge(new Edge((int)(cellHeight * 1.4), this.grid[i + 1][j + 1]));
        }
        if(i - 1 >= 0 && j - 1 >= 0){
            this.grid[i][j].addEdge(new Edge((int)(cellHeight * 1.4), this.grid[i - 1][j - 1]));
        }
        if(i + 1 < numRows && j - 1 >= 0){
            this.grid[i][j].addEdge(new Edge((int)(cellHeight * 1.4), this.grid[i + 1][j - 1]));
        }
        if(i - 1 >= 0 && j + 1 < numCols){
            this.grid[i][j].addEdge(new Edge((int)(cellHeight * 1.4), this.grid[i - 1][j + 1]));
        }
    }

    public void update(){
        this.repaint();
    }

    public void clear(){
        buildGrid();
        this.repaint();
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

    @Override
    public void mouseClicked(MouseEvent e) {

        Point mousePos = new Point(e.getX(), e.getY());

        if (startCell != null){
            startCell.setColor(AlgorithmThread.REG_CELL_COLOR);

        }
        startCell = grid[(int) (mousePos.x / cellWidth)][(int) (mousePos.y / cellHeight)];
        startCell.setColor(AlgorithmThread.START_COLOR);


        if (goalCell != null){
            goalCell.setColor(AlgorithmThread.REG_CELL_COLOR);

        }
        goalCell = grid[(int) (mousePos.x / cellWidth)][(int) (mousePos.y / cellHeight)];
        goalCell.setColor(AlgorithmThread.GOAL_COLOR);

        update();
    }


    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
