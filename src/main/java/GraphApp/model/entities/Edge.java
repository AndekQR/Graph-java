package GraphApp.model.entities;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

public class Edge {

    private int id;
    private Node destination;
    private DoubleProperty weight;
    private int graphPartId;

    public Edge(Node destination, Double weight) {
        this.destination=destination;
        this.weight = new SimpleDoubleProperty(weight);
    }

    public Edge(int id, Node destination, Double weight, int graphPartId) {
        this.id= id;
        this.destination=destination;
        this.weight= new SimpleDoubleProperty(weight);
        this.graphPartId = graphPartId;
    }

    public Edge() {
        this.weight = new SimpleDoubleProperty();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public Node getDestination() {
        return destination;
    }

    public void setDestination(Node destination) {
        this.destination=destination;
    }

    public double getWeight() {
        return weight.get();
    }

    public DoubleProperty weightProperty() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight.set(weight);
    }

    @Override
    public String toString() {
        return "Edge{" +
                "id=" + id +
                ", destination=" + destination +
                ", weight=" + weight +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;

        Edge edge=(Edge) o;

        if (!destination.equals(edge.destination)) return false;
        return weight.equals(edge.weight);
    }

    @Override
    public int hashCode() {
        int result=destination.hashCode();
        result=31 * result + weight.hashCode();
        return result;
    }

    public int getGraphPartId() {
        return graphPartId;
    }

    public void setGraphPartId(int graphPartId) {
        this.graphPartId=graphPartId;
    }
}
