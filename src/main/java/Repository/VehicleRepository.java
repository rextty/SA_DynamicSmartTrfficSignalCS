package Repository;

import Model.JDBCConnector;
import POJO.Intersection.Road;
import POJO.Vehicle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VehicleRepository {
    private final JDBCConnector connector;

    public VehicleRepository() {
        connector = new JDBCConnector();
    }

    public ArrayList<Vehicle> getAllVehicle()  {
        ArrayList<Vehicle> vehicles = new ArrayList<>();

        String query = "SELECT * FROM vehicle;";
        try {
            ResultSet resultSet = connector.executeQuery(query);
            while (resultSet.next()) {
                int weight = resultSet.getInt("weight");
                String name = resultSet.getString("name");

                Vehicle vehicle = new Vehicle(name, weight);
                vehicles.add(vehicle);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return vehicles;
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

    public static void main(String[] args) {
        VehicleRepository repository = new VehicleRepository();
        System.out.println(repository.getAllVehicle());
    }

}
