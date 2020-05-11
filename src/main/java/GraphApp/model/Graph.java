package GraphApp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Graph {

    private final String uid;
    private Boolean directed;
    private ArrayList<GraphPart> graphParts;

    public Graph(Boolean directed, GraphPart... graphParts) {
        this.directed=directed;
        this.uid=UUID.randomUUID().toString();
        this.graphParts=new ArrayList<>(List.of(graphParts));
    }

    public Graph() {
        this.uid=UUID.randomUUID().toString();
        this.graphParts=new ArrayList<>();
    }

    public Boolean getDirected() {
        return directed;
    }

    public void setDirected(Boolean directed) {
        this.directed=directed;
    }

    public String getUid() {
        return uid;
    }

    public List<GraphPart> getGraphParts() {
        return graphParts;
    }

    public void setGraphParts(ArrayList<GraphPart> graphParts) {
        this.graphParts=graphParts;
    }

    @Override
    public String toString() {
        return "Graph{" +
                "directed=" + directed +
                ", uid='" + uid + '\'' +
                ", graphParts=" + graphParts +
                '}';
    }
}
