package GraphApp.view;

import GraphApp.controllers.MainController;
import GraphApp.model.GraphModel;
import GraphApp.model.entities.Graph;
import com.brunomnsilva.smartgraph.graph.GraphEdgeList;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainView {

    private static final int HEIGHT=768;
    private static final int WIDTH=1024;
    private static final int CONTROL_PANEL_HEIGHT=150;
    private static final int LIST_VIEW_WIDTH=150;

    private final MainController mainController;
    private final GraphModel graphModel;

    private BorderPane container;
    private ListView<Graph> graphListView;
    private com.brunomnsilva.smartgraph.graph.Graph<String, String> graphVisualization;
    private HBox controlPane;
    private SmartGraphPanel<String, String> graphView;

    public MainView(MainController mainController, GraphModel graphModel) {
        this.mainController=mainController;
        this.graphModel=graphModel;
        initialize();
    }

    private void initialize() {
        this.container=new BorderPane();
        this.container.setPrefSize(WIDTH, HEIGHT);
        initListView();
        initControlPanel();
        this.container.setLeft(this.graphListView);
        this.container.setBottom(this.controlPane);
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
        this.graphListView=new ListView<>(mainController.getGraphsList());
        this.graphListView.setPrefWidth(LIST_VIEW_WIDTH);

        //określenie sposobu wyświetlania grafów na liście i akcji po naciśnięciu na element
        ExecutorService executorService=Executors.newCachedThreadPool();
        this.graphListView.setCellFactory(graphListView1 -> {
            ListCell<Graph> cell=new ListCell<>() {
                @Override
                protected void updateItem(Graph item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) setText(null);
                    else setText(item.toString());
                }
            };
            cell.setOnMouseClicked(mouseEvent -> {
                executorService.submit(() -> {
                    if (cell.getItem() != null) {
                        Optional<com.brunomnsilva.smartgraph.graph.Graph<String, String>> graphView=mainController.getGraphView(cell.getItem().getId());
                        graphView.ifPresent(stringStringGraph -> {
                            try {
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
        scrollPane.prefHeightProperty().bind(this.container.heightProperty().subtract(CONTROL_PANEL_HEIGHT));


        Optional<Graph> first=mainController.getGraphsList().stream().findFirst();
        first.ifPresent(graph -> {
            com.brunomnsilva.smartgraph.graph.Graph<String, String> randomGraph= mainController.convertToGraphView(first.get());
            this.changeGraphVisualization(randomGraph);
        });

        return scrollPane;
    }

    private void initControlPanel() {
        HBox hBox=new HBox();
        hBox.setStyle("-fx-background-color: #f3f3f3");
        Button button=new Button("Add random graph");
        button.setOnMouseClicked(mouseEvent -> new NewGraphWindow(mainController::addRandomGraph));
        Button button1=new Button("random button 1");
        hBox.getChildren().add(button);
        hBox.getChildren().add(button1);
        hBox.setPrefSize(WIDTH, CONTROL_PANEL_HEIGHT);
        this.controlPane=hBox;
    }
}
