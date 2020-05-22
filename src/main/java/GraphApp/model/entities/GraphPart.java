package GraphApp.model.entities;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class GraphPart {

    private int id;
    private Node node;
    private ListProperty<Edge> edges;
    private int graphId;

    public GraphPart(Node node, Edge... edges) {
        this.node=node;
        ObservableList<Edge> observableList =FXCollections.observableArrayList(edges);
        this.edges = new SimpleListProperty<>(observableList);
    }

    public GraphPart(int id, Node node, List<Edge> edges, int grapId) {
        this.id= id;
        this.node=node;
        ObservableList<Edge> observableList = FXCollections.observableArrayList(edges);
        this.edges= new SimpleListProperty<>(observableList);
        this.graphId = grapId;
    }

    public GraphPart() {
        ObservableList<Edge> observableList = FXCollections.observableArrayList();
        this.edges = new SimpleListProperty<>(observableList);
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node=node;
    }


    public ObservableList<Edge> getEdges() {
        return edges.get();
    }

    public ListProperty<Edge> edgesProperty() {
        return edges;
    }

    public void setEdges(ObservableList<Edge> edges) {
        this.edges.set(edges);
    }

    @Override
    public String toString() {
        return "GraphPart{" +
                "id=" + id +
                ", node=" + node +
                ", edges=" + edges.size() +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public int getGraphId() {
        return graphId;
    }

    public void setGraphId(int graphId) {
        this.graphId=graphId;
    }
}
