import org.junit.BeforeClass;
import org.junit.Test;
import GraphApp.GraphUtils;
import GraphApp.model.entities.Edge;
import GraphApp.model.entities.Graph;
import GraphApp.model.entities.GraphPart;
import GraphApp.model.entities.Node;

import static org.junit.Assert.*;

public class GraphUtilsTest {

    private static GraphUtils graphUtils;

    @BeforeClass
    public static void setup() {
        graphUtils = new GraphUtils();
    }

    @Test
    public void newGraphTest() {
        Graph graph = graphUtils.newGraph(true, "test");
        assertNotNull(graph);
        assertNotNull(graph.getGraphParts());
    }

    @Test
    public void addNodeTest() throws Exception {
        Graph graph = graphUtils.newGraph(true, "test");
        Node nodeA = graphUtils.addNode(graph, "nodeA");
        Node nodeB = graphUtils.addNode(graph, "nodeB");

        assertEquals(2, graph.getGraphParts().size());
        graph.getGraphParts().forEach(graphPart -> {
            assertTrue(graphPart.getNode().getLabel().equals(nodeA.getLabel()) ||
                    graphPart.getNode().getLabel().equals(nodeB.getLabel()));
            assertNotNull(graphPart.getEdges());

        });
    }

    @Test
    public void addEdgeTest() throws Exception {
        Graph graph = graphUtils.newGraph(true, "test");
        Node nodeA = graphUtils.addNode(graph, "nodeA");
        Node nodeB = graphUtils.addNode(graph, "nodeB");
        Node nodeC = graphUtils.addNode(graph, "nodeC");

        graphUtils.addEdge(nodeA, nodeB, graph, 10D);
        GraphPart graphPart = graph.getGraphParts().stream().filter(x -> x.getNode().getLabel().equals(nodeA.getLabel()))
                .findFirst()
                .orElse(null);
        assertNotNull(graphPart);
        Edge edgeFromAToB = graphPart.getEdges().stream().filter(x-> x.getDestination().getLabel().equals(nodeB.getLabel()))
                .findFirst()
                .orElse(null);
        assertNotNull(edgeFromAToB);
        assertNotNull(edgeFromAToB.getWeight());
        assertEquals(10D, edgeFromAToB.getWeight(), 0.0);

        graphUtils.addEdge(nodeA, nodeB, graph, 5D);
        GraphPart graphPart1 = graph.getGraphParts().stream().filter(x -> x.getNode().getLabel().equals(nodeA.getLabel()))
                .findFirst()
                .orElse(null);
        assertNotNull(graphPart1);
        assertEquals(graphPart1.getEdges().size(), 1);
        Edge edgeFromAToB1 = graphPart.getEdges().stream().filter(x-> x.getDestination().getLabel().equals(nodeB.getLabel()))
                .findFirst()
                .orElse(null);
        assertNotNull(edgeFromAToB1);
        assertNotNull(edgeFromAToB1.getWeight());
        assertEquals(5D, edgeFromAToB1.getWeight(), 0.0);

    }

    @Test
    public void getEdgeTest() throws Exception {
        Graph graph = graphUtils.newGraph(true, "test");
        Node nodeA = graphUtils.addNode(graph, "nodeA");
        Node nodeB = graphUtils.addNode(graph, "nodeB");
        Node nodeC = graphUtils.addNode(graph, "nodeC");

        graphUtils.addEdge(nodeA, nodeB, graph, 5D);
        Edge edge = graphUtils.getEdge(nodeA, nodeB, graph).orElse(null);
        assertNotNull(edge);
        assertEquals(edge.getDestination().getLabel(), nodeB.getLabel());
        assertEquals(5D, edge.getWeight(), 0.0D);

    }

    @Test
    public void removeEdgeTest() throws Exception {
        Graph graph = graphUtils.newGraph(true, "test");
        Node nodeA = graphUtils.addNode(graph, "nodeA");
        Node nodeB = graphUtils.addNode(graph, "nodeB");
        graphUtils.addEdge(nodeA, nodeB, graph, 5D);

        GraphPart graphPart = graph.getGraphParts().stream().filter(x -> x.getNode().getLabel().equals(nodeA.getLabel()))
                .findFirst()
                .orElse(null);
        assertNotNull(graphPart);
        assertEquals(1, graphPart.getEdges().size());

        graphUtils.removeEdge(nodeA, nodeB, graph);
        GraphPart graphPart1 = graph.getGraphParts().stream().filter(x -> x.getNode().getLabel().equals(nodeA.getLabel()))
                .findFirst()
                .orElse(null);
        assertNotNull(graphPart1);
        assertEquals(0, graphPart1.getEdges().size());
        assertTrue(graphUtils.isInGraph(graph, nodeA));
        assertTrue(graphUtils.isInGraph(graph, nodeB));

    }
}
