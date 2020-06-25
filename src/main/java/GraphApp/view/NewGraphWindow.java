package GraphApp.view;

import GraphApp.AddRandomGraphToDb;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;


public class NewGraphWindow {

    private int vertices;
    private String graphName;

    public NewGraphWindow(AddRandomGraphToDb addRandomGraphToDb) {
        Stage stage=new Stage();
        VBox vBox=new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(10);
        vBox.getChildren().addAll(getInputPane(), getControls(addRandomGraphToDb, stage));

        stage.setTitle("Creating new graph");
        stage.setScene(new Scene(vBox, 160, 200));
        stage.show();
    }

    private TilePane getInputPane() {

        Label verticesLabel=new Label("Vertices");
        TextField vertices=new TextField();
        Label graphNameLabel=new Label("Graph name");
        TextField graphName=new TextField();

        vertices.textProperty().addListener((observableValue, oldValue, newValue) -> {
            Pattern pattern=Pattern.compile("\\d+");
            if (pattern.matcher(newValue).matches()) {
                this.vertices=Integer.parseInt(newValue);
            } else {
                vertices.setText(oldValue);
            }
        });


        graphName.textProperty().addListener((observableValue, s, t1) -> {
            this.graphName=t1;
        });

        TilePane tilePane=new TilePane();
        tilePane.setPrefColumns(1);
        tilePane.setHgap(10);
        tilePane.setVgap(5);
        tilePane.setAlignment(Pos.CENTER);
        tilePane.getChildren().addAll(verticesLabel, vertices, graphNameLabel, graphName);
        return tilePane;
    }

    private HBox getControls(AddRandomGraphToDb addRandomGraphToDb, Stage stage) {
        Button okButton=new Button("OK");
        Button cancelButton=new Button("Cancel");
        ExecutorService executorService=Executors.newSingleThreadExecutor();

        okButton.setOnAction(actionEvent -> {
            if (this.graphName != null && !this.graphName.isEmpty() && this.vertices > 0) {
                executorService.submit(() -> {
                    addRandomGraphToDb.addToDB(this.vertices, true, this.graphName);
                });
                executorService.shutdown();
                stage.close();
            } else {
                Alert alert=new Alert(AlertType.ERROR, "Error in form", ButtonType.OK);
                alert.showAndWait();
            }
        });

        cancelButton.setOnAction(actionEvent -> {
            executorService.shutdown();
            stage.close();
        });

        HBox hBox=new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(5);
        hBox.getChildren().addAll(okButton, cancelButton);
        return hBox;
    }
}
