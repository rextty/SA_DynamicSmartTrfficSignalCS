package Repository;

import Model.JDBCConnector;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EmergencyRepository {
    private JDBCConnector connector;

    public EmergencyRepository() {
        connector = new JDBCConnector();
    }

    public boolean checkEmergency()  {
        String query = "SELECT * FROM ems_status WHERE ems_status='1';";

        try {
            ResultSet resultSet = connector.executeQuery(query);
            if (resultSet.next())
                return true;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    public void sendEmergencyVehicleDirection()  {
        // TODO:
        String query = "INSERT INTO ems_status (vehicle_id, intersection_id, road_id, ems_status) VALUES ('%s', %d, %d, %d)";

        try {
            connector.executeQuery(query);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws SQLException {
        EmergencyRepository repository = new EmergencyRepository();
        System.out.println(repository.checkEmergency());
    }

}
