package GraphApp.controllers;

import GraphApp.GraphUtils;
import GraphApp.model.GraphModel;
import GraphApp.model.entities.Edge;
import GraphApp.model.entities.GraphPart;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

public class MainController {


    private final GraphModel graphModel;
    private GraphUtils graphUtils;

    // nie może zostać przypisana inna lista bo graphVisualization gubi referencje
    private final ObservableList<GraphApp.model.entities.Graph> graphsList;

    public MainController(GraphModel graphModel) {
        this.graphModel=graphModel;
        this.graphsList =FXCollections.observableArrayList(graphModel.getAllGraphs());
        this.graphUtils = new GraphUtils();
    }

    public Optional<Graph<String, String>> getGraphView(int id) {
        Optional<GraphApp.model.entities.Graph> optionalGraph=graphModel.getGraph(id);
        if (optionalGraph.isEmpty()) return Optional.empty();
        return Optional.of(this.convertToGraphView(optionalGraph.get()));
    }

    public Graph<String, String> convertToGraphView(GraphApp.model.entities.Graph graph) {
        Graph<String, String> result = new GraphEdgeList<>();

        for(GraphPart graphPart : graph.getGraphParts()) {
            result.insertVertex(graphPart.getNode().getLabel());
        }
        for(GraphPart graphPart : graph.getGraphParts()) {
            for(Edge edge : graphPart.getEdges()) {
                result.insertEdge(graphPart.getNode().getLabel(), edge.getDestination().getLabel(), String.valueOf(edge.getWeight()));
            }
        }
        return result;
    }

    public Digraph<String, String> getDirectedGraphView(int id) {
        return null;
    }

    public ObservableList<GraphApp.model.entities.Graph> getGraphsList() {
        return graphsList;
    }

    public Optional<GraphApp.model.entities.Graph> addRandomGraph(int vertices, boolean directed, String graphname) {
        GraphApp.model.entities.Graph randomGraph = null;
        try {
            randomGraph=this.graphUtils.getRandomGraph(vertices, directed, graphname);
            GraphApp.model.entities.Graph graph=this.graphModel.saveGraph(randomGraph);
            this.graphsList.add(graph);
            return Optional.of(graph);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return Optional.empty();
    }

    public Graph<String, String> getRandomGraphView(int vertices, boolean directed, String graphname) {
        Graph<String, String> result = new GraphEdgeList<>();
        try {
            GraphApp.model.entities.Graph random=this.graphUtils.getRandomGraph(vertices, directed, graphname);

            System.out.println("get random graph vertices: "+random.getGraphParts().size());
            for(GraphPart graphPart : random.getGraphParts()) {
                result.insertVertex(graphPart.getNode().getLabel());
            }
            System.out.println("result vertices: "+result.vertices().size());
            for(GraphPart graphPart : random.getGraphParts()) {
                for(Edge edge : graphPart.getEdges()) {
                    result.insertEdge(graphPart.getNode().getLabel(), edge.getDestination().getLabel(), String.valueOf(edge.getWeight()));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}
