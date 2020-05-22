package GraphApp;

import GraphApp.controllers.MainController;
import GraphApp.model.GraphModel;
import GraphApp.model.entities.Graph;
import GraphApp.model.entities.Node;
import GraphApp.view.MainView;

import java.util.List;

public class ViewManager {

    public void showMainPanel() {
        GraphModel graphModel = new GraphModel();

        GraphUtils graphUtils = new GraphUtils();
        Graph graph = graphUtils.newGraph(true, "testowy");
        Node nodeA=null, nodeB=null, nodeC=null;
        try {
            nodeA = graphUtils.addNode(graph, "A");
            nodeB = graphUtils.addNode(graph, "B");
            nodeC = graphUtils.addNode(graph, "C");

            graphUtils.addEdge(nodeA, nodeB, graph, 10D);
            graphUtils.addEdge(nodeA, nodeC, graph, 5D);
            graphUtils.addEdge(nodeC, nodeA, graph, 15D);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
//        graphModel.saveGraph(graph);

        List<Graph> graphs = graphModel.getAllGraphs();

        MainController mainController=new MainController(graphModel);
        new MainView(mainController, graphModel);
    }
}
