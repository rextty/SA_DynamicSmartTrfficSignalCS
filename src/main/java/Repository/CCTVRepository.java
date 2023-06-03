package Repository;

import Model.JDBCConnector;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CCTVRepository {
    private JDBCConnector connector;
    private Connection connection;

    public CCTVRepository() {
        connector = new JDBCConnector();
        connection = connector.getConnection();
    }

    public ArrayList<String> getCCTV()  {
        String query = "SELECT * FROM sa_smart_traffic_signal_cs.cctv;";

        ArrayList<String> results = new ArrayList<>();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(query);
            while (resultSet.next()) {
                results.add(resultSet.getString("image"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return results;
    }

    public static void main(String[] args) throws SQLException {
        CCTVRepository repository = new CCTVRepository();
        System.out.println(repository.getCCTV().get(1));
    }

}
