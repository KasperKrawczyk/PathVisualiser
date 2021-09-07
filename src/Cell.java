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
    private ArrayList<Edge> edges;



    public Cell(Point position, int width, int height) {
        this.width = width;
        this.height = height;
        this.cellType = CellType.REGULAR;
        this.color = cellType.color;
        this.position = position;
        this.distanceFromStart = Double.POSITIVE_INFINITY;
        this.edges = new ArrayList<>();
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

//    private void updateEdgeCosts(CellType newCellType){
//        for(Edge edge : this.edges){
//            edge.setCost();
//        }
//    }

    public void addEdge(Edge edge){
        edges.add(edge);
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
        this.cellType = cellType;
        this.color = cellType.color;
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

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
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
