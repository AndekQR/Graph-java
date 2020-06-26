package GraphApp;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        ViewManager viewManager=new ViewManager();
        viewManager.showMainPanel();
    }
}
