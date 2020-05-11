package GraphApp.model;

import java.util.UUID;

public class Edge {

    private final String uid;
    private Node destination;
    private Double weight;

    public Edge(Node destination, Double weight) {
        this.destination=destination;
        this.weight=weight;
        this.uid=UUID.randomUUID().toString();
    }

    public Edge() {
        this.uid=UUID.randomUUID().toString();
    }

    public Node getDestination() {
        return destination;
    }

    public void setDestination(Node destination) {
        this.destination=destination;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight=weight;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return "Edge{" +
                "destination=" + destination +
                ", weight=" + weight +
                ", uid='" + uid + '\'' +
                '}';
    }
}
