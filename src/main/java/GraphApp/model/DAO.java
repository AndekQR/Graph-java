package GraphApp.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DAO {

    private static DAO instance;
    private final String url="jdbc:mysql://127.0.0.1:3306/graphTO";
    private final String user="root";
    private final String password="";
    private final Connection connection;

    private DAO() throws SQLException {
        this.connection=DriverManager.getConnection(url, user, password);
    }

    public static DAO getInstance() throws SQLException {
        if (instance == null) {
            instance=new DAO();
        } else if (instance.connection.isClosed()) {
            instance=new DAO();
        }
        return instance;
    }

    public Connection getConnection() {
        return this.connection;
    }
}
