package GraphApp.view;

import GraphApp.controllers.MainController;
import GraphApp.model.GraphModel;
import GraphApp.model.entities.Graph;
import com.brunomnsilva.smartgraph.graph.DigraphEdgeList;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartRandomPlacementStrategy;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainView {

    private static final int HEIGHT=600;
    private static final int WIDTH=800;
    private static final int CONTROL_PANEL_HEIGHT=30;
    private static final int LIST_VIEW_WIDTH=150;

    private final MainController mainController;
    private final boolean initialized=false;
    private final GraphModel graphModel;

    private BorderPane container;
    private ListView<Graph> graphListView;
    private ObservableList<Graph> graphsList;
    private com.brunomnsilva.smartgraph.graph.Graph<String, String> graphVisualization;
    private HBox controlPane;
    private SmartGraphPanel<String, String> graphView;

    public MainView(MainController mainController, GraphModel graphModel) {
        this.mainController=mainController;
        this.graphModel=graphModel;
        this.graphsList=FXCollections.observableArrayList();
        if (!initialized) initialize();
    }

    private void initialize() {
        this.container=new BorderPane();
        container.setPrefSize(WIDTH, HEIGHT);
        this.initControlPanel();
        container.setBottom(this.controlPane);
        container.setLeft(this.initGraphView());
        this.initListView();
        container.setRight(this.graphListView);

        Scene scene=new Scene(this.container);
        Stage stage=new Stage(StageStyle.DECORATED);
        stage.setTitle("Graph");
        stage.setScene(scene);

        stage.show();
        this.graphView.init();
        List<Graph> allGraphs=graphModel.getAllGraphs();
        this.graphsList.setAll(allGraphs);
        Optional<Graph> first=allGraphs.stream().findFirst();
        first.ifPresent(graph -> {
            Optional<com.brunomnsilva.smartgraph.graph.Graph<String, String>> graphView=this.mainController.getGraphView(first.get().getId());
            graphView.ifPresent(stringStringGraph -> {
                this.changeGraphVisualization(stringStringGraph);
                this.graphView.update();
            });
        });
    }

    // nie można po prostu przypisać bo graphView gubi referencje do grafu
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
        this.graphListView=new ListView<>(this.graphsList);
        this.graphListView.setPrefWidth(LIST_VIEW_WIDTH);
        ExecutorService executorService=Executors.newCachedThreadPool();
        this.graphListView.setCellFactory(graphListView1 -> {
            ListCell<Graph> cell = new ListCell<>(){
                @Override
                protected void updateItem(Graph item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) setText(null);
                    else setText(item.toString());
                }
            };
            cell.setOnMouseClicked(mouseEvent -> {
                executorService.submit(() -> {
                    if(cell.getItem() != null) {
                        Optional<com.brunomnsilva.smartgraph.graph.Graph<String, String>> graphView=mainController.getGraphView(cell.getItem().getId());
                        graphView.ifPresent(stringStringGraph -> {
                            try{
                                this.changeGraphVisualization(stringStringGraph);
                                this.graphView.update();
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        });
                    }
                });
            });
            return cell;
        });
    }

    private HBox initGraphView() {
        HBox hBox=new HBox();
        VBox vBox=new VBox();
        hBox.getChildren().add(vBox);
        hBox.setAlignment(Pos.CENTER);
        vBox.setAlignment(Pos.CENTER);

        this.graphVisualization=new GraphEdgeList<>();
        SmartPlacementStrategy strategy=new SmartRandomPlacementStrategy();
        this.graphView=new SmartGraphPanel<>(this.graphVisualization, strategy);
        this.graphView.prefWidthProperty().bind(this.container.widthProperty().subtract(LIST_VIEW_WIDTH));
        this.graphView.prefHeightProperty().bind(this.container.heightProperty().subtract(this.controlPane.heightProperty()));
        vBox.getChildren().add(this.graphView);
        return hBox;
    }

    private void initControlPanel() {
        HBox hBox=new HBox();
        hBox.setPrefSize(this.container.getWidth(), CONTROL_PANEL_HEIGHT);
        this.controlPane=hBox;
    }
}
