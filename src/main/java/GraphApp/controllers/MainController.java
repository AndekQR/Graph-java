package GraphApp.controllers;

import GraphApp.GraphUtils;
import GraphApp.model.GraphModel;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;

public class MainController {


    private final GraphModel graphModel;
    private GraphUtils graphUtils;

    public MainController(GraphModel graphModel) {
        this.graphModel = graphModel;
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
        g.insertEdge("H", "N", "12");

        g.insertEdge("A", "H", "0");
        return g;
    }
}
