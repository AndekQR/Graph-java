package GraphApp.services;

import GraphApp.model.entities.Edge;
import GraphApp.model.entities.GraphPart;
import com.brunomnsilva.smartgraph.graph.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;

public class VisualizationAdapter {

    public Graph<String, String> convertToGraphView(GraphApp.model.entities.Graph graph) {
        Graph<String, String> result=new GraphEdgeList<>();

        for (GraphPart graphPart : graph.getGraphParts()) {
            result.insertVertex(graphPart.getNode().getLabel());
        }
        for (GraphPart graphPart : graph.getGraphParts()) {
            for (Edge edge : graphPart.getEdges()) {
                result.insertEdge(graphPart.getNode().getLabel(), edge.getDestination().getLabel(), String.valueOf(edge.getWeight()));
            }
        }
        return result;
    }

    public Graph<String, String> getRandomGraphView(int vertices, boolean directed, String graphname) {
        GraphUtils graphUtils=new GraphUtils();
        Graph<String, String> result=new GraphEdgeList<>();
        try {
            GraphApp.model.entities.Graph random=graphUtils.getRandomGraph(vertices, directed, graphname);

            System.out.println("get random graph vertices: " + random.getGraphParts().size());
            for (GraphPart graphPart : random.getGraphParts()) {
                result.insertVertex(graphPart.getNode().getLabel());
            }
            System.out.println("result vertices: " + result.vertices().size());
            for (GraphPart graphPart : random.getGraphParts()) {
                for (Edge edge : graphPart.getEdges()) {
                    result.insertEdge(graphPart.getNode().getLabel(), edge.getDestination().getLabel(), String.valueOf(edge.getWeight()));
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }
}
