package GraphApp.services;

import GraphApp.model.entities.Edge;
import GraphApp.model.entities.Graph;
import GraphApp.model.entities.GraphPart;
import GraphApp.model.entities.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DijkstraAlgorythm {

    private static final Double NO_PATH=99999.99999;

    private final Graph graph;
    private final Node startNode;


    //lista do przetowrzenia
    List<GraphPart> toProcess;
    //lista przetworzona
    List<GraphPart> processed;
    //minimalny koszt dościa do poszczegołnego wierzchołka
    List<Double> d;
    //grappart poprzednik na scieżce
    List<GraphPart> p;

    public DijkstraAlgorythm(Graph graph, Node startNode) {
        this.graph=graph;
        this.startNode=startNode;

        dikstraMinimalPath();
    }

    private void dikstraMinimalPath() {
        toProcess=new ArrayList<>(graph.getGraphParts());
        processed=new ArrayList<>();
        d=new ArrayList<>(Collections.nCopies(graph.getGraphParts().size(), NO_PATH));
        Optional<GraphPart> graphPartOfStartNode=graph.getGraphParts().stream().filter(part -> part.getNode().getLabel().equals(startNode.getLabel())).findFirst();
        if (graphPartOfStartNode.isPresent()) {
            d.set(graph.getGraphParts().indexOf(graphPartOfStartNode.get()), 0.0);
        } else throw new IllegalArgumentException("Start node doesn'y exists in graph");
        //pierwszy wyraz to null - wierzchołek startowy
        p=new ArrayList<>(Collections.nCopies(graph.getGraphParts().size(), null));


        for (int i=0; i < graph.getGraphParts().size(); i++) {
            if (i == 0) {
                GraphPart graphPartByNode=this.findGraphPartByNode(startNode, toProcess);
                if (graphPartByNode != null) {
                    this.moveToProcessedList(graphPartByNode);
                    this.processAllDestinations(graphPartByNode);
                }
            } else {
                if (!toProcess.isEmpty()) {
                    GraphPart minimalDestination=getMinimalDestination(processed.get(processed.size() - 1));
                    if (minimalDestination != null) {
                        moveToProcessedList(minimalDestination);
                        processAllDestinations(minimalDestination);
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private void processAllDestinations(GraphPart part) {
        for (Edge edge : part.getEdges()) {
            GraphPart edgeDestinationPart=findGraphPartByNode(edge.getDestination(), toProcess);
            if (edgeDestinationPart == null) continue;
            if (d.get(graph.getGraphParts().indexOf(edgeDestinationPart)) > (d.get(graph.getGraphParts().indexOf(part)) + edge.getWeight())) {
                d.set(graph.getGraphParts().indexOf(edgeDestinationPart), d.get(graph.getGraphParts().indexOf(part)) + edge.getWeight());
                p.set(graph.getGraphParts().indexOf(edgeDestinationPart), part);
            }
        }
    }

    private void moveToProcessedList(GraphPart graphPart) {
        toProcess.remove(graphPart);
        processed.add(graphPart);
    }

    private GraphPart findGraphPartByNode(Node node, List<GraphPart> parts) {
        for (GraphPart graphPart : parts) {
            if (graphPart != null && graphPart.getNode().getLabel().equals(node.getLabel()))
                return graphPart;
        }
        return null;
    }

    private GraphPart getMinimalDestination(GraphPart part) {
        if (part.getEdges().isEmpty()) return null;

        Edge min=part.getEdges().get(0);
        for (Edge edge : part.getEdges()) {
            if (edge.getWeight() < min.getWeight()) {
                min=edge;
            }
        }
        return findGraphPartByNode(min.getDestination(), toProcess);
    }

    public Optional<Double> getRoadWeightToNode(Node end) {
        Optional<GraphPart> partOfEndNode=this.graph.getGraphParts().stream().filter(part -> part.getNode().getLabel().equals(end.getLabel())).findFirst();
        if (partOfEndNode.isEmpty()) return Optional.empty();

        int indexOfEndGraphPart=this.graph.getGraphParts().indexOf(partOfEndNode.get());
        if (indexOfEndGraphPart == -1) throw new IllegalArgumentException("Node not found");
        Double aDouble=this.d.get(indexOfEndGraphPart);
        if (aDouble.equals(NO_PATH)) {
            return Optional.empty();
        } else {
            return Optional.of(aDouble);
        }

    }

    //na początku listy jest node startowy na końcu końcowy
    public List<Node> getNodesInTheRouteTo(Node end) {

        List<Node> result=new ArrayList<>();
        GraphPart part = this.findGraphPartByNode(end, this.graph.getGraphParts());
        if (part == null) return Collections.emptyList();
        do {
            result.add(0, part.getNode());
            int indexOfNextNode=this.graph.getGraphParts().indexOf(part);
            part=this.p.get(indexOfNextNode);
        }while (part != null);

        return result;
    }

    public void printResult(Node end) {
        for (int i=0; i < getd().size(); i++) {
            System.out.print(graph.getGraphParts().get(i).getNode().getLabel() + "  ");
        }
        System.out.println();
        for (int i=0; i < getd().size(); i++) {
            System.out.print(getd().get(i) + "  ");
        }
        System.out.println();
        for (int i=0; i < getp().size(); i++) {
            if (getp().get(i) == null) {
                System.out.print("null  ");
            } else {
                System.out.print(getp().get(i).getNode().getLabel() + "  ");
            }
        }
        System.out.println();
        Optional<Double> roadWeightToNode=getRoadWeightToNode(end);
        if (roadWeightToNode.isPresent()) {
            System.out.println("Weight to " + startNode.getLabel() + " : " + roadWeightToNode.get());

        } else {
            System.out.println("No path");
        }
        List<Node> nodesInTheRouteTo=getNodesInTheRouteTo(end);
        nodesInTheRouteTo.forEach(node -> {
            System.out.print(node.getLabel());
            System.out.print("->");
        });

    }


    public List<Double> getd() {
        return d;
    }

    public List<GraphPart> getp() {
        return p;
    }
}
