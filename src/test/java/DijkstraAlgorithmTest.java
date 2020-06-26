import GraphApp.model.entities.Graph;
import GraphApp.model.entities.Node;
import GraphApp.services.DijkstraAlgorithm;
import GraphApp.services.GraphUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Optional;

public class DijkstraAlgorithmTest {

    private static DijkstraAlgorithm dijkstraAlgorithm;
    private static GraphUtils graphUtils;

    private static Node startNode;
    private static Node endNode;



    @BeforeClass
    public static void setup() throws Exception {
        graphUtils = new GraphUtils();
        Graph graph=getGraph();
        dijkstraAlgorithm= new DijkstraAlgorithm(graph, startNode);
    }

    public static Graph getGraph() throws Exception {
        Graph graph = graphUtils.newGraph(true, "test");
        startNode = graphUtils.addNode(graph, "nodeA").get();
        Node nodeB = graphUtils.addNode(graph, "nodeB").get();
        endNode = graphUtils.addNode(graph, "nodeC").get();
        Node nodeD = graphUtils.addNode(graph, "nodeD").get();
        graphUtils.addEdge(startNode, nodeB, graph, 5D);
        graphUtils.addEdge(startNode, endNode, graph, 3D);
        graphUtils.addEdge(startNode, nodeD, graph, 1D);
        graphUtils.addEdge(nodeB, endNode, graph, 2D);
        return graph;
    }

    @Test
    public void weightCalculateTest(){
        Optional<Double> roadWeightToNode=dijkstraAlgorithm.getRoadWeightToNode(endNode);
        assertTrue(roadWeightToNode.isPresent());
        assertEquals(3D, roadWeightToNode.get(), 0.0);
        assertNull(dijkstraAlgorithm.getp().get(0));
    }

    @Test
    public void roadNodesTest(){
        dijkstraAlgorithm.printResult(endNode);
        List<Node> nodesInTheRouteTo=dijkstraAlgorithm.getNodesInTheRouteTo(endNode);
        assertEquals(2, nodesInTheRouteTo.size());
        assertEquals(nodesInTheRouteTo.get(0), startNode);
        assertEquals(nodesInTheRouteTo.get(1), endNode);
    }

}
