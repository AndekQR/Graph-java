package GraphApp;

@FunctionalInterface
public interface AddRandomGraphToDb {
    void addToDB(int vertices, boolean directed, String graphName);
}
