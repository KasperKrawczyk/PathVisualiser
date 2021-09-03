import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class Cell implements Comparable<Cell> {
    int width;
    int height;
    Color color;
    Point position;
    double distanceFromStart;
    double cost;
    Cell predecessor;
    ArrayList<Edge> edges;
    boolean isStart;
    boolean isGoal;

    public Cell(Point position, int width, int height) {
        this.width = width;
        this.height = height;
        this.color = AlgorithmThread.REG_CELL_COLOR;
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
                this.height);

    }

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

    public Cell getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(Cell predecessor) {
        this.predecessor = predecessor;
    }

    public ArrayList<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges = edges;
    }

    @Override
    public int compareTo(Cell otherCell) {
        return Double.compare(this.cost, otherCell.cost);
    }

    @Override
    public String toString(){
        return "Cell " + position.getX() + ", " + position.getY();
    }
}
