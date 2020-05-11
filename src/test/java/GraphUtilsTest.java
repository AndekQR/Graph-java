import org.junit.BeforeClass;
import org.junit.Test;
import GraphApp.GraphUtils;
import GraphApp.model.Edge;
import GraphApp.model.Graph;
import GraphApp.model.GraphPart;
import GraphApp.model.Node;

import static org.junit.Assert.*;

public class GraphUtilsTest {

    private static GraphUtils graphUtils;

    @BeforeClass
    public static void setup() {
        graphUtils = new GraphUtils();
    }

    @Test
    public void newGraphTest() {
        Graph graph = graphUtils.newGraph(true);
        assertNotNull(graph);
        assertNotNull(graph.getUid());
        assertNotNull(graph.getGraphParts());
    }

    @Test
    public void addNodeTest() {
        Graph graph = graphUtils.newGraph(true);
        Node nodeA = graphUtils.addNode(graph, "nodeA");
        Node nodeB = graphUtils.addNode(graph, "nodeB");

        assertEquals(2, graph.getGraphParts().size());
        graph.getGraphParts().forEach(graphPart -> {
            assertTrue(graphPart.getNode().getUid().equals(nodeA.getUid()) ||
                    graphPart.getNode().getUid().equals(nodeB.getUid()));
            assertNotNull(graphPart.getEdges());
            assertNotNull(graphPart.getUid());

        });
    }

    @Test
    public void addEdgeTest() throws Exception {
        Graph graph = graphUtils.newGraph(true);
        Node nodeA = graphUtils.addNode(graph, "nodeA");
        Node nodeB = graphUtils.addNode(graph, "nodeB");
        Node nodeC = graphUtils.addNode(graph, "nodeC");

        graphUtils.addEdge(nodeA, nodeB, graph, 10D);
        GraphPart graphPart = graph.getGraphParts().stream().filter(x -> x.getNode().getUid().equals(nodeA.getUid()))
                .findFirst()
                .orElse(null);
        assertNotNull(graphPart);
        Edge edgeFromAToB = graphPart.getEdges().stream().filter(x-> x.getDestination().getUid().equals(nodeB.getUid()))
                .findFirst()
                .orElse(null);
        assertNotNull(edgeFromAToB);
        assertNotNull(edgeFromAToB.getWeight());
        assertNotNull(edgeFromAToB.getUid());
        assertEquals(10D, edgeFromAToB.getWeight(), 0.0);

        graphUtils.addEdge(nodeA, nodeB, graph, 5D);
        GraphPart graphPart1 = graph.getGraphParts().stream().filter(x -> x.getNode().getUid().equals(nodeA.getUid()))
                .findFirst()
                .orElse(null);
        assertNotNull(graphPart1);
        assertEquals(graphPart1.getEdges().size(), 1);
        Edge edgeFromAToB1 = graphPart.getEdges().stream().filter(x-> x.getDestination().getUid().equals(nodeB.getUid()))
                .findFirst()
                .orElse(null);
        assertNotNull(edgeFromAToB1);
        assertNotNull(edgeFromAToB1.getWeight());
        assertNotNull(edgeFromAToB1.getUid());
        assertEquals(5D, edgeFromAToB1.getWeight(), 0.0);
        assertEquals(edgeFromAToB.getUid(), edgeFromAToB1.getUid());

    }

    @Test
    public void getEdgeTest() throws Exception {
        Graph graph = graphUtils.newGraph(true);
        Node nodeA = graphUtils.addNode(graph, "nodeA");
        Node nodeB = graphUtils.addNode(graph, "nodeB");
        Node nodeC = graphUtils.addNode(graph, "nodeC");

        graphUtils.addEdge(nodeA, nodeB, graph, 5D);
        Edge edge = graphUtils.getEdge(nodeA, nodeB, graph).orElse(null);
        assertNotNull(edge);
        assertEquals(edge.getDestination().getUid(), nodeB.getUid());
        assertEquals(5D, edge.getWeight(), 0.0D);

    }

    @Test
    public void removeEdgeTest() throws Exception {
        Graph graph = graphUtils.newGraph(true);
        Node nodeA = graphUtils.addNode(graph, "nodeA");
        Node nodeB = graphUtils.addNode(graph, "nodeB");
        graphUtils.addEdge(nodeA, nodeB, graph, 5D);

        GraphPart graphPart = graph.getGraphParts().stream().filter(x -> x.getNode().getUid().equals(nodeA.getUid()))
                .findFirst()
                .orElse(null);
        assertNotNull(graphPart);
        assertEquals(1, graphPart.getEdges().size());

        graphUtils.removeEdge(nodeA, nodeB, graph);
        GraphPart graphPart1 = graph.getGraphParts().stream().filter(x -> x.getNode().getUid().equals(nodeA.getUid()))
                .findFirst()
                .orElse(null);
        assertNotNull(graphPart1);
        assertEquals(0, graphPart1.getEdges().size());
        assertTrue(graphUtils.isInGraph(graph, nodeA));
        assertTrue(graphUtils.isInGraph(graph, nodeB));

    }
}
