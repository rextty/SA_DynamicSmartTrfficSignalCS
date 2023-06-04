package Model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

public class JDBCConnector {

    private Connection connection;

    public JDBCConnector() {
        try {
            AppConfig config = new AppConfig();
            // Class.forName("com.mysql.jdbc.Driver");
            // Loading class `com.mysql.jdbc.Driver'.
            // This is deprecated. The new driver class is `com.mysql.cj.jdbc.Driver'. The driver is automatically registered via the SPI and manual loading of the driver class is generally unnecessary.
            connection = DriverManager.getConnection(config.getDbUrl(), config.getDbUsername(), config.getDbPassword());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ResultSet executeQuery(String query) throws SQLException {
        return connection.createStatement().executeQuery(query);
    }

    public int updateQuery(String query) throws SQLException {
        return connection.createStatement().executeUpdate(query);
    }

    public void disconnect() throws SQLException {
        connection.close();
    }

    public static void main(String[] args) {
        JDBCConnector connector = new JDBCConnector();
    }
}
