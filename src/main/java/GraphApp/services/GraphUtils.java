package GraphApp.services;

import GraphApp.model.entities.Edge;
import GraphApp.model.entities.Graph;
import GraphApp.model.entities.GraphPart;
import GraphApp.model.entities.Node;
import com.github.javafaker.Faker;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GraphUtils {

    public GraphUtils() {

    }

    public Graph newGraph(Boolean directed, String graphName) {
        Graph graph=new Graph(graphName);
        graph.setDirected(directed);
        return graph;
    }

    public Optional<Node> addNode(Graph graph, String label) throws Exception {
        if (this.isLabelGood(graph, label)) {
            Node node=new Node(label);
            GraphPart graphPart=new GraphPart(node);
            graph.getGraphParts().add(graphPart);
            return Optional.of(node);
        } else {
            return Optional.empty();
        }
    }

    private Boolean isLabelGood(final Graph graph, String label) {
        for (GraphPart graphPart : graph.getGraphParts()) {
            if (graphPart.getNode().getLabel().equals(label)) return false;
            for (Edge edge : graphPart.getEdges()) {
                if (edge.getDestination().getLabel().equals(label)) return false;
            }
        }
        return true;
    }

    public void addEdge(Node source, Node destination, Graph graph, Double weight) throws Exception {
        if (source == null || destination == null || graph == null) {
            throw new Exception("null during adding edge");
        }
        if (isInGraph(graph, source) && isInGraph(graph, destination)) {
            Optional<Edge> edgeToCheck=getEdge(source, destination, graph);
            if (edgeToCheck.isPresent()) {
                this.changeEdgeData(edgeToCheck.get(), new Edge(destination, weight));
            } else {
                Edge edge=new Edge(destination, weight);
                Optional<GraphPart> graphPart=graph.getGraphParts().stream().filter(x -> x.getNode().getLabel().equals(source.getLabel()))
                        .findFirst();
                graphPart.ifPresent(part -> part.getEdges().add(edge));

            }

            if (!graph.isDirected()) {
                Optional<Edge> edge=getEdge(destination, source, graph);
                if (edge.isPresent()) {
                    changeEdgeData(edge.get(), new Edge(source, weight));
                } else {
                    Edge directedEdge=new Edge(source, weight);
                    Optional<GraphPart> first=graph.getGraphParts().stream().filter(x -> x.getNode().getLabel().equals(destination.getLabel()))
                            .findFirst();
                    first.ifPresent(graphPart -> graphPart.getEdges().add(directedEdge));
                }
            }
        } else {
            throw new Exception("Node not in graph");
        }
    }

    public Boolean isInGraph(final Graph graph, final Node node) {
        return graph.getGraphParts().stream().anyMatch(x -> x.getNode().getLabel().equals(node.getLabel()));
    }

    public Optional<Edge> getEdge(final Node source, final Node destination, final Graph graph) {
        Optional<GraphPart> graphPart=graph.getGraphParts().stream().filter(graphPart1 -> graphPart1.getNode().getLabel().equals(source.getLabel()))
                .findFirst();
        if (graphPart.isEmpty()) return Optional.empty();
        Optional<Edge> edge=graphPart.get().getEdges().stream().filter(edge1 -> edge1.getDestination().getLabel().equals(destination.getLabel()))
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
            if (x.getNode().getLabel().equals(source.getLabel())) {
                x.getEdges().removeIf(e -> e.getDestination().getLabel().equals(destination.getLabel()));
            }
        });
    }

    public Optional<Node> findNodeByName(Graph graph, String name) {
        for (GraphPart graphPart : graph.getGraphParts()) {
            if (graphPart.getNode().getLabel().equals(name)) return Optional.of(graphPart.getNode());
        }
        return Optional.empty();
    }

    public Graph getRandomGraph(int vertices, boolean directed, String name) throws Exception {
        Faker faker=new Faker();
        Graph result=this.newGraph(directed, name);
        List<Node> createdNodes=new ArrayList<>();
        List<Integer> createdWeights=new ArrayList<>();
        for (int i=0; i < vertices; i++) {
            this.addNode(result, faker.name().firstName()).ifPresent(createdNodes::add);
        }
        createdNodes.forEach(node -> {
            if (createdNodes.size() > 1)
                for (int i=0; i < faker.number().numberBetween(1, 3); i++) {
                    Node dest=createdNodes.get(faker.number().numberBetween(1, createdNodes.size()));
                    try {
                        if (this.getEdge(node, dest, result).isEmpty()) {
                            Integer weight=faker.number().numberBetween(1, vertices * 2);
                            while (createdWeights.contains(weight)) {
                                weight=faker.number().numberBetween(1, vertices * 2);
                            }
                            createdWeights.add(weight);
                            if (node != dest) {
                                this.addEdge(node, dest, result, weight.doubleValue());
                            }
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }
        });
        return result;
    }


}
