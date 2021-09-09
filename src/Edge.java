public class Edge {
    private int cost;
    private Cell destination;

    public Edge(int cost, Cell destination){
        this.cost = cost;
        this.destination = destination;
    }

    public int getCost(){
        return cost;
    }

    public Cell getDestination(){
        return destination;
    }

    public void setCost(int cost){
        this.cost = cost;
    }
}
