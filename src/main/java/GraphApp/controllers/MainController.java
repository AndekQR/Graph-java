package GraphApp.controllers;

import GraphApp.GraphUtils;
import GraphApp.model.GraphModel;
import GraphApp.model.entities.Edge;
import GraphApp.model.entities.GraphPart;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;

import java.util.Optional;

public class MainController {


    private final GraphModel graphModel;
    private GraphUtils graphUtils;

    public MainController(GraphModel graphModel) {
        this.graphModel=graphModel;
    }

    public Optional<Graph<String, String>> getGraphView(int id) {
        Optional<GraphApp.model.entities.Graph> optionalGraph=graphModel.getGraph(id);
        if (optionalGraph.isEmpty()) return Optional.empty();
        Graph<String, String> result = new GraphEdgeList<>();
        GraphApp.model.entities.Graph graph = optionalGraph.get();

        for(GraphPart graphPart : graph.getGraphParts()) {
            result.insertVertex(graphPart.getNode().getLabel());
        }
        for(GraphPart graphPart : graph.getGraphParts()) {
            for(Edge edge : graphPart.getEdges()) {
                result.insertEdge(graphPart.getNode().getLabel(), edge.getDestination().getLabel(), String.valueOf(edge.getWeight()));
            }
        }
        return Optional.of(result);
    }

    public Digraph<String, String> getDirectedGraphView(int id) {
        return null;
    }


    public com.brunomnsilva.smartgraph.graph.Graph<String, String> getGraph(com.brunomnsilva.smartgraph.graph.Graph<String, String> g) {
        g=new GraphEdgeList<>();
        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");
        g.insertVertex("F");
        g.insertVertex("G");

        g.insertEdge("A", "B", "1");
        g.insertEdge("A", "C", "2");
        g.insertEdge("A", "D", "3");
        g.insertEdge("A", "E", "4");
        g.insertEdge("A", "F", "5");
        g.insertEdge("A", "G", "6");

        g.insertVertex("H");
        g.insertVertex("I");
        g.insertVertex("J");
        g.insertVertex("K");
        g.insertVertex("L");
        g.insertVertex("M");
        g.insertVertex("N");

        g.insertEdge("H", "I", "7");
        g.insertEdge("H", "J", "8");
        g.insertEdge("H", "K", "9");
        g.insertEdge("H", "L", "10");
        g.insertEdge("H", "M", "11");

        g.insertEdge("A", "H", "0");
        return g;
    }
}
