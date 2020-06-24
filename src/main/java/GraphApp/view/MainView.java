package GraphApp.view;

import GraphApp.controllers.MainController;
import GraphApp.model.GraphModelInterface;
import GraphApp.model.entities.Edge;
import GraphApp.model.entities.Graph;
import GraphApp.model.entities.GraphPart;
import GraphApp.model.entities.Node;
import GraphApp.services.DijkstraAlgorythm;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainView {

    private static final int HEIGHT=768;
    private static final int WIDTH=1024;
    private static final int CONTROL_PANEL_HEIGHT=30;
    private static final int LIST_VIEW_WIDTH=150;
    private static final int GRAPH_INFORMATION_HEIGHT=100;

    private final MainController mainController;
    private final GraphModelInterface graphModel;
    private final ExecutorService executorService;
    private BorderPane container;
    private ListView<Graph> graphListView;
    private com.brunomnsilva.smartgraph.graph.Graph<String, String> graphVisualization;
    private HBox controlPane;
    private FlowPane graphInformation;
    private VBox allBottom;
    private SmartGraphPanel<String, String> graphView;
    private final List<String> colorizedEdges;

    public MainView(MainController mainController, GraphModelInterface graphModel) {
        this.mainController=mainController;
        this.graphModel=graphModel;
        this.executorService=Executors.newCachedThreadPool();
        colorizedEdges=new ArrayList<>();
        initialize();
    }

    private void initialize() {
        this.container=new BorderPane();
        this.container.setPrefSize(WIDTH, HEIGHT);
        initGraphInformationPanel();
        initListView();
        initControlPanel();
        allBottom=new VBox();
        allBottom.getChildren().addAll(this.controlPane, this.graphInformation);
        this.container.setLeft(this.graphListView);
        this.container.setBottom(allBottom);
        this.container.setRight(initGraphView());

        Scene scene=new Scene(this.container);
        Stage stage=new Stage(StageStyle.DECORATED);
        stage.setTitle("Graph");
        stage.setScene(scene);

        stage.show();
        this.graphView.init();
        this.graphView.update();
    }

    private void changeGraphVisualization(com.brunomnsilva.smartgraph.graph.Graph<String, String> newVisualization) {
        this.graphVisualization.edges().forEach(stringStringEdge -> {
            this.graphVisualization.removeEdge(stringStringEdge);
        });
        this.graphVisualization.vertices().forEach(stringVertex -> {
            this.graphVisualization.removeVertex(stringVertex);
        });
        newVisualization.vertices().forEach(stringVertex -> {
            this.graphVisualization.insertVertex(stringVertex.element());
        });
        newVisualization.edges().forEach(stringStringEdge -> {
            this.graphVisualization.insertEdge(stringStringEdge.vertices()[0].element(), stringStringEdge.vertices()[1].element(), stringStringEdge.element());
        });
    }

    private void initListView() {
        ObservableList<Graph> graphsList=mainController.getGraphsList();
        this.graphListView=new ListView<>(graphsList);
        if (!graphsList.isEmpty()) this.changeGraphInformation(graphsList.get(0));
        this.graphListView.setPrefWidth(LIST_VIEW_WIDTH);

        //określenie sposobu wyświetlania grafów na liście i akcji po naciśnięciu na element
        this.graphListView.setCellFactory(graphListView1 -> {
            ListCell<Graph> cell=new ListCell<>() {
                @Override
                protected void updateItem(Graph item, boolean empty) {
                    super.updateItem(item, empty);
                    Platform.runLater(() -> {
                        if (empty) setText(null);
                        else setText(item.toString());
                    });
                }
            };
            cell.setOnMouseClicked(mouseEvent -> {
                this.executorService.submit(() -> {

                    if ((cell.getItem() != null)) {
                        Optional<Graph> graph=mainController.getGraph(cell.getItem().getId());
                        graph.ifPresent(stringStringGraph -> {
                            try {
                                com.brunomnsilva.smartgraph.graph.Graph<String, String> graphView=mainController.convertGraphToVisualization(stringStringGraph);
                                this.changeGraphVisualization(graphView);
                                Platform.runLater(() -> this.changeGraphInformation(stringStringGraph));
                                this.graphView.update();
                            } catch (Exception e) {
                                System.out.println(e.getMessage() + " initListView");
                            }
                        });
                    }
                });
            });
            return cell;
        });
    }

    private void changeGraphInformation(Graph graph) {
        this.graphInformation.getChildren().clear();
        this.graphInformation.setHgap(5);
        this.graphInformation.setHgap(5);
        this.graphInformation.getChildren().addAll(this.getNumberOfVerticesHbox(graph), this.getNumberOfEdgesHbox(graph), this.getMinimalRoadFromAToBHBox(graph));

    }

    private HBox getNumberOfVerticesHbox(Graph graph) {
        HBox hBox=new HBox();
        Text text=new Text();
        text.setText("Number of vertices: " + graph.getGraphParts().size());
        hBox.getChildren().add(text);
        return hBox;
    }

    private HBox getNumberOfEdgesHbox(Graph graph) {
        HBox hBox=new HBox();
        int numberOfEdges=0;
        for (GraphPart graphPart : graph.getGraphParts()) {
            for (Edge ignored : graphPart.getEdges()) {
                numberOfEdges++;
            }
        }
        Text text=new Text("Number of edges: " + numberOfEdges);
        hBox.getChildren().add(text);
        return hBox;
    }

    private VBox getMinimalRoadFromAToBHBox(Graph graph) {
        HBox hBox=new HBox();
        Text text=new Text("Set the minimum road from A to B node ");
        TextField node_a=new TextField("node A");
        TextField node_b=new TextField("node B");
        Button button=new Button("Calculate");
        hBox.getChildren().addAll(text, node_a, node_b, button);
        HBox bottomHbox=new HBox();
        Text roadWeightText=new Text();
        bottomHbox.getChildren().add(roadWeightText);

        VBox vBox=new VBox();
        button.setOnMouseClicked(mouseEvent -> {
            Optional<Node> nodea=mainController.findNodeByName(graph, node_a.getText());
            Optional<Node> nodeb=mainController.findNodeByName(graph, node_b.getText());
            if (nodea.isPresent() && nodeb.isPresent()) {
                DijkstraAlgorythm dijkstraAlgorythm=this.mainController.initDijkstraAlgorythm(graph, nodea.get());
                Optional<Double> roadWeightToNode=dijkstraAlgorythm.getRoadWeightToNode(nodeb.get());
                if (roadWeightToNode.isPresent()) {
                    roadWeightText.setText("Road weight: " + roadWeightToNode.get());
                    this.makePathInTheVisualization(dijkstraAlgorythm.getNodesInTheRouteTo(nodeb.get()));
                } else {
                    roadWeightText.setText("No path!");
                }
            }
        });

        vBox.getChildren().addAll(hBox, bottomHbox);

        return vBox;
    }

    private void makePathInTheVisualization(List<Node> nodes) {
        deletePath();
        Collection<com.brunomnsilva.smartgraph.graph.Edge<String, String>> edges=graphVisualization.edges();
        if (nodes.size() > 1) {
            int j=1;
            for (int i=0; i < nodes.size(); i++) {
                if (j < nodes.size()) {
                    Optional<com.brunomnsilva.smartgraph.graph.Edge<String, String>> indexOfEdgeFromVisualization=this.getIndexOfEdgeFromVisualization(edges, nodes.get(i), nodes.get(j));
                    indexOfEdgeFromVisualization.ifPresent(stringStringEdge -> {
                        this.graphView.getStylableEdge(stringStringEdge.element()).setStyle("-fx-stroke: #d90000");
                        this.colorizedEdges.add(stringStringEdge.element());
                    });
                    j++;
                } else {
                    break;
                }
            }
            this.graphView.update();
        }
    }

    private void deletePath() {
        this.colorizedEdges.forEach(s -> {
            this.graphView.getStylableEdge(s).setStyle("-fx-stroke: #0e0909");
        });
        this.colorizedEdges.clear();
    }

    private Optional<com.brunomnsilva.smartgraph.graph.Edge<String, String>> getIndexOfEdgeFromVisualization(Collection<com.brunomnsilva.smartgraph.graph.Edge<String, String>> edges, Node a, Node b) {
        List<com.brunomnsilva.smartgraph.graph.Edge<String, String>> edgelist=new ArrayList<>(edges);
        return edgelist.stream()
                .filter(edge -> edge.vertices()[0].element().equals(a.getLabel()) && edge.vertices()[1].element().equals(b.getLabel())).findFirst();
    }


    private ScrollPane initGraphView() {

        this.graphVisualization=new GraphEdgeList<>();
        SmartPlacementStrategy strategy=new SmartCircularSortedPlacementStrategy();
        this.graphView=new SmartGraphPanel<>(this.graphVisualization, strategy);
        this.graphView.setAutomaticLayout(true);

        StackPane stackPane=new StackPane();
        stackPane.setPrefSize(WIDTH * 2, HEIGHT * 2);
        stackPane.getChildren().add(this.graphView);

        ZoomableScrollPane scrollPane=new ZoomableScrollPane(stackPane);
        scrollPane.prefWidthProperty().bind(this.container.widthProperty().subtract(LIST_VIEW_WIDTH));
        scrollPane.prefHeightProperty().bind(this.container.heightProperty().subtract(CONTROL_PANEL_HEIGHT).subtract(GRAPH_INFORMATION_HEIGHT));

        Optional<Graph> first=mainController.getGraphsList().stream().findFirst();
        first.ifPresent(graph -> {
            com.brunomnsilva.smartgraph.graph.Graph<String, String> randomGraph=mainController.convertGraphToVisualization(first.get());
            this.changeGraphVisualization(randomGraph);
        });

        return scrollPane;
    }

    private void initGraphInformationPanel() {
        this.graphInformation=new FlowPane();
        this.graphInformation.setPrefSize(WIDTH, GRAPH_INFORMATION_HEIGHT);
        graphInformation.setStyle("-fx-background-color: #fcfbfb");
        graphInformation.setOrientation(Orientation.VERTICAL);
        graphInformation.setVgap(3);
        graphInformation.setHgap(3);
    }

    private void initControlPanel() {
        HBox hBox=new HBox();
        hBox.setStyle("-fx-background-color: #f3f3f3");
        Button addButton=new Button("Add random graph");
        addButton.setOnMouseClicked(mouseEvent -> new NewGraphWindow(mainController::addRandomGraph));
        Button removeButton=new Button("remove selected graph");
        removeButton.setOnMouseClicked(mouseEvent -> {
            this.changeGraphVisualization(new GraphEdgeList<>());
            Graph selectedItem=this.graphListView.getSelectionModel().getSelectedItem();
            this.executorService.submit(() -> this.mainController.removeGraph(selectedItem));
            this.graphListView.refresh();
            this.graphView.update();

        });

        Button exit=new Button("Exit");
        exit.setOnMouseClicked(mouseEvent -> {
            this.executorService.shutdownNow();
            Platform.exit();
        });
        hBox.getChildren().addAll(addButton, removeButton, exit);
        hBox.setPrefSize(WIDTH, CONTROL_PANEL_HEIGHT);
        this.controlPane=hBox;
    }

}
