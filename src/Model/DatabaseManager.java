package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// TODO: DatabaseManager
public class DatabaseManager {

    private Connection connection;
    private String url;
    private String account;
    private String password;

    DatabaseManager() {
        // Connect Database using JDBC API
        try {
            // Class.forName("com.mysql.jdbc.Driver");
            // Loading class `com.mysql.jdbc.Driver'.
            // This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.
            connection = DriverManager.getConnection("", "", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void disconnect() {}

    void executeUpdate() {}

    void executeQuery() {}
}
