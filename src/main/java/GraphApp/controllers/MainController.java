package GraphApp.controllers;

import GraphApp.model.entities.Node;
import GraphApp.services.GraphUtils;
import GraphApp.model.GraphModelInterface;
import GraphApp.services.VisualizationAdapter;
import com.brunomnsilva.smartgraph.graph.Digraph;
import com.brunomnsilva.smartgraph.graph.Graph;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Optional;

public class MainController {


    private final GraphModelInterface graphModel;
    // nie może zostać przypisana inna lista bo graphVisualization gubi referencje
    private final ObservableList<GraphApp.model.entities.Graph> graphsList;
    private final GraphUtils graphUtils;

    public MainController(GraphModelInterface graphModel) {
        this.graphModel=graphModel;
        this.graphsList=FXCollections.observableArrayList(graphModel.getAllGraphs());
        this.graphUtils=new GraphUtils();
    }

    public Optional<Graph<String, String>> getGraphView(int id) {
        Optional<GraphApp.model.entities.Graph> optionalGraph=graphModel.getGraph(id);
        if (optionalGraph.isEmpty()) return Optional.empty();
        return Optional.of(this.convertGraphToVisualization(optionalGraph.get()));
    }

    public Graph<String, String> convertGraphToVisualization(GraphApp.model.entities.Graph graph) {
        VisualizationAdapter visualizationAdapter = new VisualizationAdapter();
        return visualizationAdapter.convertToGraphView(graph);
    }

    public Optional<GraphApp.model.entities.Graph> getGraph(int id) {
        return graphModel.getGraph(id);
    }

    public ObservableList<GraphApp.model.entities.Graph> getGraphsList() {
        return graphsList;
    }

    public Optional<GraphApp.model.entities.Graph> addRandomGraph(int vertices, boolean directed, String graphname) {
        GraphApp.model.entities.Graph randomGraph=null;
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

    public GraphApp.model.entities.Graph removeGraph(GraphApp.model.entities.Graph graphToDelete) {
        this.graphModel.deleteGraph(graphToDelete);
        this.graphsList.remove(graphToDelete);
        return graphToDelete;
    }

    public Optional<Node> findNodeByName(GraphApp.model.entities.Graph graph, String name) {
        return graphUtils.findNodeByName(graph, name);
    }
}
