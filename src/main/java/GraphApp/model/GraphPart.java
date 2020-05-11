package GraphApp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GraphPart {

    private final String uid;
    private Node node;
    private ArrayList<Edge> edges;

    public GraphPart(Node node, Edge... edges) {
        this.node=node;
        this.edges=new ArrayList<>(List.of(edges));
        this.uid=UUID.randomUUID().toString();
    }

    public GraphPart() {
        this.uid=UUID.randomUUID().toString();
        this.edges=new ArrayList<>();
    }

    public Node getNode() {
        return node;
    }

    public void setNode(Node node) {
        this.node=node;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(ArrayList<Edge> edges) {
        this.edges=edges;
    }

    public String getUid() {
        return uid;
    }

    @Override
    public String toString() {
        return "GraphPart{" +
                "node=" + node +
                ", edges=" + edges +
                ", uid='" + uid + '\'' +
                '}';
    }
}
