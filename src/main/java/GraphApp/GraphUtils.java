package GraphApp;

import GraphApp.model.Edge;
import GraphApp.model.Graph;
import GraphApp.model.GraphPart;
import GraphApp.model.Node;

import java.util.Optional;

public class GraphUtils {

    public GraphUtils() {

    }

    public Graph newGraph(Boolean directed) {
        Graph graph=new Graph();
        graph.setDirected(directed);
        return graph;
    }

    public Node addNode(Graph graph, String label) {
        Node node=new Node(label);
        GraphPart graphPart=new GraphPart(node);
        graph.getGraphParts().add(graphPart);
        return node;
    }

    public void addEdge(Node source, Node destination, Graph graph, Double weight) throws Exception {
        if (isInGraph(graph, source) && isInGraph(graph, destination)) {
            Optional<Edge> edgeToCheck=getEdge(source, destination, graph);
            if (edgeToCheck.isPresent()) {
                this.changeEdgeData(edgeToCheck.get(), new Edge(destination, weight));
            } else {
                Edge edge=new Edge(destination, weight);
                Optional<GraphPart> graphPart=graph.getGraphParts().stream().filter(x -> x.getNode().getUid().equals(source.getUid()))
                        .findFirst();
                graphPart.ifPresent(part -> part.getEdges().add(edge));

            }

            if (!graph.getDirected()) {
                Optional<Edge> edge=getEdge(destination, source, graph);
                if (edge.isPresent()) {
                    changeEdgeData(edge.get(), new Edge(source, weight));
                } else {
                    Edge directedEdge=new Edge(source, weight);
                    Optional<GraphPart> first=graph.getGraphParts().stream().filter(x -> x.getNode().getUid().equals(destination.getUid()))
                            .findFirst();
                    first.ifPresent(graphPart -> graphPart.getEdges().add(directedEdge));
                }
            }
        } else {
            throw new Exception("Node not in graph");
        }
    }

    public Boolean isInGraph(final Graph graph, final Node node) {
        return graph.getGraphParts().stream().anyMatch(x -> x.getNode().getUid().equals(node.getUid()));
    }

    public Optional<Edge> getEdge(final Node source, final Node destination, final Graph graph) {
        Optional<GraphPart> graphPart=graph.getGraphParts().stream().filter(graphPart1 -> graphPart1.getNode().getUid().equals(source.getUid()))
                .findFirst();
        if (graphPart.isEmpty()) return Optional.empty();
        Optional<Edge> edge=graphPart.get().getEdges().stream().filter(edge1 -> edge1.getDestination().getUid().equals(destination.getUid()))
                .findFirst();
        if (edge.isEmpty()) return Optional.empty();
        return edge;
    }

    private void changeEdgeData(Edge oldEdge, final Edge newEdge) {
        oldEdge.setDestination(newEdge.getDestination());
        oldEdge.setWeight(newEdge.getWeight());
    }

    public void removeEdge(Node source, Node destination, Graph graph) {
        graph.getGraphParts().forEach(x -> {
            if (x.getNode().getUid().equals(source.getUid())) {
                x.getEdges().removeIf(e -> e.getDestination().getUid().equals(destination.getUid()));
            }
        });
    }
}
