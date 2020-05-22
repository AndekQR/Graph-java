package GraphApp.model;

import GraphApp.model.entities.Edge;
import GraphApp.model.entities.Graph;
import GraphApp.model.entities.GraphPart;
import GraphApp.model.entities.Node;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public class GraphModel {

    public Graph saveGraph(Graph graph) {
        try (Connection connection=DAO.getInstance().getConnection()) {
            Graph insertedGraph=insertGraph(graph, connection);
            graph.setId(insertedGraph.getId());
            for (GraphPart graphPart : graph.getGraphParts()) {
                if (graphPart.getNode().getId() == 0) {
                    graphPart.setNode(insertNode(graphPart.getNode(), connection));
                }
                for (Edge edge : graphPart.getEdges()) {
                    if (edge.getDestination().getId() == 0) {
                        Node insertedNode=insertNode(edge.getDestination(), connection);
                        edge.setDestination(insertedNode);
                    }
                }
            }
            for (GraphPart graphPart : graph.getGraphParts()) {
                GraphPart insertedGraphpart=insertGraphPart(graphPart, graphPart.getNode(), insertedGraph.getId(), connection);
                graphPart.setId(insertedGraphpart.getId());
            }
            for (GraphPart graphPart : graph.getGraphParts()) {
                for (Edge edge : graphPart.getEdges()) {
                    Edge insertedEdge=insertEdge(edge, edge.getDestination(), graphPart.getId(), connection);
                    edge.setId(insertedEdge.getId());
                    edge.setGraphPartId(insertedEdge.getGraphPartId());
                }
            }
            return graph;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return graph;
    }


    private Node insertNode(Node node, Connection connection) throws SQLException {
        String query="INSERT INTO NODE (label) values (?)";
        try (PreparedStatement preparedStatement=connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, node.getLabel());
            int affectedRows=preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating node failed, no rows affected");
            }

            ResultSet generatedKeys=preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                node.setId(generatedKeys.getInt(1));
                return node;
            } else {
                throw new SQLException("Creating user failed, no ID obtained.");
            }
        }
    }

    private GraphPart insertGraphPart(GraphPart graphPart, Node node, int graphid, Connection connection) throws SQLException {
        String query="INSERT INTO GRAPHPART (nodeId, graphId) VALUES (?,?)";
        try (PreparedStatement preparedStatement=connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, node.getId());
            preparedStatement.setInt(2, graphid);
            int affectedRows=preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating graphPart failed, no rows affected");
            }

            ResultSet generatedKeys=preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                graphPart.setId(generatedKeys.getInt(1));
                graphPart.setNode(node);
                graphPart.setGraphId(graphid);
                return graphPart;
            } else {
                throw new SQLException("Creating graphPart failed, no ID obtained.");
            }
        }
    }

    private Edge insertEdge(Edge edge, Node node, int graphpartId, Connection connection) throws SQLException {
        String query="INSERT INTO edge (nodeId, graphPartId, weight) VALUES (?,?,?)";
        try (PreparedStatement preparedStatement=connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, node.getId());
            preparedStatement.setInt(2, graphpartId);
            preparedStatement.setDouble(3, edge.getWeight());
            int affectedRows=preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating edge failed, no rows affected");
            }

            ResultSet generatedKeys=preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                edge.setId(generatedKeys.getInt(1));
                edge.setDestination(node);
                edge.setGraphPartId(graphpartId);
                return edge;
            } else {
                throw new SQLException("Creating edge failed, no ID obtained.");
            }
        }
    }

    private Graph insertGraph(Graph graph, Connection connection) throws SQLException {
        String query="INSERT INTO GRAPH (directed, name) VALUES (?,?)";
        try (PreparedStatement preparedStatement=connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setBoolean(1, graph.isDirected());
            preparedStatement.setString(2, graph.getName());
            int affectedRows=preparedStatement.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating Graph failed, no rows affected");
            }

            ResultSet generatedKeys=preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                graph.setId(generatedKeys.getInt(1));
                return graph;
            } else {
                throw new SQLException("Creating Graph failed, no ID obtained.");
            }
        }
    }

    public List<Graph> getAllGraphs() {
        List<Graph> methodResult = new ArrayList<>();
        String query="SELECT * FROM graphpart\n" +
                "INNER JOIN graph ON graphpart.graphId=graph.id\n" +
                "INNER JOIN edge on graphpart.id = edge.graphPartId\n" +
                "INNER JOIN node edgeNode on edge.nodeId = edgeNode.id\n" +
                "INNER JOIN node gpNode ON graphpart.nodeId = gpNode.id";
        try (Connection connection=DAO.getInstance().getConnection();
             Statement statement=connection.createStatement();
             ResultSet resultSet=statement.executeQuery(query);
        ) {
            while (resultSet.next()) {

                boolean directed = resultSet.getBoolean("directed");
                String graphName = resultSet.getString("name");
                Date created = new java.util.Date(resultSet.getTimestamp("created").getTime());
                int graphId = resultSet.getInt("graph.id");

                // dodawanie grafu
                if (methodResult.stream().noneMatch(graph -> graph.getId() == graphId)) {
                    Graph graph = new Graph(graphName);
                    graph.setDirected(directed);
                    graph.setCreated(created);
                    graph.setId(graphId);
                    methodResult.add(graph);
                }

                int graphPartId = resultSet.getInt("graphpart.id");
                int edgeId = resultSet.getInt("edge.id");
                double edgeWeight = resultSet.getDouble("weight");
                int graphPartNodeId = resultSet.getInt("gpNode.id");
                String gpNodeLabel = resultSet.getString("gpNode.label");
                int edgeNodeId = resultSet.getInt("edgeNode.id");
                String edgeNodeLabel = resultSet.getString("edgeNode.label");

                //dodawanie graphPart do graph
                methodResult.stream().filter(graph -> graph.getId() == graphId).findFirst().ifPresent(graph -> {
                    if(graph.getGraphParts().stream().noneMatch(graphPart -> graphPart.getId() == graphPartId)) {
                        GraphPart graphPart = new GraphPart();
                        graphPart.setId(graphPartId);
                        graphPart.setGraphId(graphId);
                        graph.getGraphParts().add(graphPart);
                    }

                    //dodawanie edge do graphPart
                    graph.getGraphParts().stream().filter(graphPart -> graphPart.getId() == graphId).findFirst().ifPresent(graphPart -> {
                        if(graphPart.getEdges().stream().noneMatch(edge -> edge.getId() == edgeId)) {
                            Edge edge = new Edge();
                            edge.setId(edgeId);
                            edge.setWeight(edgeWeight);
                            graphPart.getEdges().add(edge);
                        }

                        //dodawanie node do edge
                        graphPart.getEdges().stream().filter(edge -> edge.getId() == edgeId).findFirst().ifPresent(edge -> {
                            //ten node może się już znajdować w innym graphPart
                            Node node = null;
                            for(GraphPart graphPart1 : graph.getGraphParts()) {
                                if(graphPart1.getNode() != null && graphPart1.getNode().getId() == edgeNodeId) {
                                    node = graphPart1.getNode();
                                }
                            }
                            if (node == null) {
                                node = new Node();
                                node.setId(edgeNodeId);
                                node.setLabel(edgeNodeLabel);
                            }
                            edge.setDestination(node);
                        });

                        //dodawanie node do graphPart
                        if(graphPart.getNode() == null) {
                            //ten node może się już znajdować w krawędzi innych graphPartów
                            Node node = null;
                            for(GraphPart graphPart1 : graph.getGraphParts()) {
                                Optional<Edge> edgeWithCorrectNode=graphPart1.getEdges().stream().filter(edge -> {
                                    if (edge.getDestination() != null) {
                                        if (edge.getDestination().getId() == graphPartNodeId) {
                                            return true;
                                        }
                                    }
                                    return false;
                                }).findFirst();
                                if(edgeWithCorrectNode.isPresent()) {
                                    node = edgeWithCorrectNode.get().getDestination();
                                }
                                if (node == null) {
                                    node=new Node();
                                    node.setId(graphPartNodeId);
                                    node.setLabel(gpNodeLabel);
                                }
                                graphPart.setNode(node); //można tak bo java do listy dodaje referencje do obiektu
                            }
                        }
                    });
                });
            }
        } catch (SQLException throwables) {
            throwables.getMessage();
        }
        return methodResult;
    }
}
