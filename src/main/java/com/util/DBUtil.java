package com.util;

import com.model.UniverseMap;
import org.apache.commons.lang3.tuple.Pair;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a utility class for JDBC connection.
 *
 * @author w3spoint
 */
public class DBUtil {

    private static final String QUERY = "SELECT * FROM routes";

    public static UniverseMap getUniverseMap(String fileName) {
        if (!fileName.endsWith(".db")) {
            return null;
        }
        Connection conn = null;
        try {
//            String url = "jdbc:sqlite:" + DBUtil.class.getClassLoader().getResource("universe_maps/" + fileName);
            String url = "jdbc:sqlite::resource:" + "universe_maps/" + fileName;
            conn = DriverManager.getConnection(url);
            var statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(QUERY);

            return universeMapFromResultSet(resultSet);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return new UniverseMap(Map.of());
    }

    private static UniverseMap universeMapFromResultSet(ResultSet resultSet) throws SQLException {
        Map<Pair<String, String>, Integer> planetsAndDistances = new HashMap<>();

        while (resultSet.next()) {
            String origin = resultSet.getString("origin");
            String destination = resultSet.getString("destination");
            int travelTime = resultSet.getInt("travel_time");
            planetsAndDistances.put(Pair.of(origin, destination), travelTime);
        }

        return new UniverseMap(planetsAndDistances);
    }

}
