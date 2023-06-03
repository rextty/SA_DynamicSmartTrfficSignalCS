package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCConnector {

    private Connection connection;
    private AppConfig config;

    public JDBCConnector() {
        try {
            config = new AppConfig();
            // TODO:
            // Class.forName("com.mysql.jdbc.Driver");
            // Loading class `com.mysql.jdbc.Driver'.
            // This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.
            connection = DriverManager.getConnection(config.getDbUrl(), config.getDbUsername(), config.getDbPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    void disconnect() throws SQLException {
        connection.close();
    }

    public static void main(String[] args) {
        JDBCConnector connector = new JDBCConnector();
    }
}
