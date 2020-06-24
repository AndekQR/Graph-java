package GraphApp;

import GraphApp.controllers.MainController;
import GraphApp.model.GraphModel;
import GraphApp.model.GraphModelInterface;
import GraphApp.view.MainView;

public class ViewManager {

    public void showMainPanel() {
        GraphModelInterface graphModel=new GraphModel();
        MainController mainController=new MainController(graphModel);
        new MainView(mainController, graphModel);
    }
}
