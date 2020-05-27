package GraphApp;

import GraphApp.model.entities.Graph;

public interface AddRandomGraphToDb {
    public void addToDB(int vertices, boolean directed, String graphName);
}
