package GraphApp.view;

import GraphApp.controllers.MainController;
import GraphApp.model.GraphModel;
import GraphApp.model.entities.Graph;
import com.brunomnsilva.smartgraph.graphview.SmartCircularSortedPlacementStrategy;
import com.brunomnsilva.smartgraph.graphview.SmartGraphPanel;
import com.brunomnsilva.smartgraph.graphview.SmartPlacementStrategy;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class MainView {

    private static final int HEIGHT = 600;
    private static final int WIDTH = 800;
    private static final int CONTROL_PANEL_HEIGHT = 30;

    private final MainController mainController;
    private final boolean initialized=false;
    private final GraphModel graphModel;

    private BorderPane container;
    private ListView<Graph> graphList;
    private com.brunomnsilva.smartgraph.graph.Graph<String, String> graphVisualization;
    private HBox controlPane;
    private SmartGraphPanel<String, String> graphView;

    public MainView(MainController mainController, GraphModel graphModel) {
        this.mainController=mainController;
        this.graphModel = graphModel;
        if (!initialized) initialize();
    }

    private void initialize() {
        this.container=new BorderPane();
        container.setPrefSize(WIDTH, HEIGHT);
        this.initListView();
        this.initControlPanel();
        container.setBottom(this.controlPane);
        container.setRight(this.graphList);
        container.setLeft(this.initGraphView());

        Scene scene=new Scene(this.container);
        Stage stage=new Stage(StageStyle.DECORATED);
        stage.setTitle("Graph");
        stage.setScene(scene);

        stage.show();
        this.graphView.init();
    }

    private void initListView() {
        this.graphList=new ListView<>();

    }

    private HBox initGraphView() {
        HBox hBox=new HBox();
        VBox vBox=new VBox();
        hBox.getChildren().add(vBox);
        hBox.setAlignment(Pos.CENTER);
        vBox.setAlignment(Pos.CENTER);

        this.graphVisualization=this.mainController.getGraph(this.graphVisualization);
        SmartPlacementStrategy strategy=new SmartCircularSortedPlacementStrategy();
        this.graphView=new SmartGraphPanel<>(this.graphVisualization, strategy);
        this.graphView.prefWidthProperty().bind(this.container.widthProperty().subtract(this.graphList.widthProperty()));
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
