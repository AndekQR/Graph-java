package GraphApp.model;

import GraphApp.model.entities.Graph;

import java.util.List;
import java.util.Optional;

public interface GraphModelInterface {

    Graph saveGraph(Graph graph);

    Optional<Graph> getGraph(int id);

    List<Graph> getAllGraphs();

    Graph deleteGraph(Graph graph);
}
