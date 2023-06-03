package Repository;

import Model.JDBCConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmergencyRepository {
    private JDBCConnector connector;
    private Connection connection;

    public EmergencyRepository() {
        connector = new JDBCConnector();
        connection = connector.getConnection();
    }

    public boolean checkEmergency()  {
        String query = "SELECT * FROM ems_status WHERE ems_status='1';";

        try {
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            if (resultSet.next())
                return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public static void main(String[] args) throws SQLException {
        EmergencyRepository repository = new EmergencyRepository();
        System.out.println(repository.checkEmergency());
    }

}
