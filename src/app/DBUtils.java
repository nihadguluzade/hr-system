package app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Opens connection to database.
 */
public class DBUtils {

    private static String url = "jdbc:mysql://localhost";
    private static String username = "root";
    private static String password = "mysql";

    public static void createDatabase() {
        // creates 'ysoft' database when program launchs
        String sql = "create database if not exists ysofthr character set utf8";

        try (Connection con = DriverManager.getConnection(url, username, password)) {
            PreparedStatement statement = con.prepareStatement(sql);
            statement.execute();
            System.out.println("Database checked.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }

}
