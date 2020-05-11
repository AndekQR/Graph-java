package GraphApp;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class ViewManager {

    private static final String MAIN_PANEL="/panels/mainPanel.fxml";

    private final Stage stage;

    public ViewManager(Stage primaryStage) {
        this.stage=primaryStage;
    }

    public void showMainPanel() {
        FXMLLoader fxmlLoader=this.getLoader(MAIN_PANEL);
        try {
            AnchorPane root=fxmlLoader.load();
            Scene scene=new Scene(root, 300, 300);
            stage.setTitle("Hello World");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            System.out.println("load error: " + e.getMessage());
        }

    }

    private FXMLLoader getLoader(String fxmlPath) {
        FXMLLoader loader;
        loader=new FXMLLoader(this.getClass().getResource(fxmlPath));
        return loader;
    }
}
