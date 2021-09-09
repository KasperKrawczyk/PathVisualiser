import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class Cell implements Comparable<Cell> {
    private int width;
    private int height;
    private CellType cellType;
    private Color color;
    private Point position;
    private double distanceFromStart;
    private double cost;
    private Cell prev;
    private boolean isSwamp = false;
    private ArrayList<Edge> edgesEightDir;
    private ArrayList<Edge> edgesFourDir;




    public Cell(Point position, int width, int height) {
        this.width = width;
        this.height = height;
        this.cellType = CellType.REGULAR;
        this.color = cellType.color;
        this.position = position;
        this.distanceFromStart = Double.POSITIVE_INFINITY;
        this.edgesEightDir = new ArrayList<>();
        this.edgesFourDir = new ArrayList<>();
    }

    public void draw(Graphics graphics){
        graphics.setColor(this.color);
        graphics.fill3DRect(
                this.position.x,
                this.position.y,
                this.width,
                this.height,
                true
        );
        graphics.setColor(Color.lightGray);
        graphics.drawRect(
                this.position.x,
                this.position.y,
                this.width,
                this.height
        );

    }

    public void addEdgeEightDir(Edge edge){
        edgesEightDir.add(edge);
    }

    public void addEdgeFourDir(Edge edge){
        edgesFourDir.add(edge);
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

    public CellType getCellType() {
        return cellType;
    }

    public void setCellType(CellType cellType) {
        CellType oldCellType = this.cellType;
        this.cellType = cellType;
        this.color = cellType.color;
        if(cellType == CellType.SWAMP) setSwamp(true);
        setCellTypeUtil(oldCellType);
    }

    /**
     * Determines if this cell should be saved as Swamp when recreating the grid after a search or when painting
     * @param oldCellType previous CellType of this cell
     */
    private void setCellTypeUtil(CellType oldCellType){
        if(oldCellType == CellType.SWAMP &&
                (getCellType() == CellType.EXPLORED || getCellType() == CellType.TO_EXPLORE)) {
            setSwamp(true);
        } else if(oldCellType == CellType.SWAMP
                || getCellType() == CellType.REGULAR
                || getCellType() == CellType.WALL){
            setSwamp(false);
        }
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public double getDistanceFromStart() {
        return distanceFromStart;
    }

    public void setDistanceFromStart(double distanceFromStart) {
        this.distanceFromStart = distanceFromStart;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public Cell getPrev() {
        return prev;
    }

    public void setPrev(Cell prev) {
        this.prev = prev;
    }

    public boolean isSwamp() {
        return isSwamp;
    }

    public void setSwamp(boolean swamp) {
        isSwamp = swamp;
    }

    public ArrayList<Edge> getEdgesEightDir() {
        return edgesEightDir;
    }

    public void setEdgesEightDir(ArrayList<Edge> edges) {
        this.edgesEightDir = edges;
    }

    public ArrayList<Edge> getEdgesFourDir() {
        return edgesFourDir;
    }

    public void setEdgesFourDir(ArrayList<Edge> edgesFourDir) {
        this.edgesFourDir = edgesFourDir;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return cellType.equals(cell.cellType) && position.equals(cell.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(cellType, position);
    }

    @Override
    public int compareTo(Cell otherCell) {
        return Double.compare(this.cost, otherCell.cost);
    }

    @Override
    public String toString(){
        return "Cell " + position.getX() + ", " + position.getY() + ", " + this.getCellType();
    }
}
