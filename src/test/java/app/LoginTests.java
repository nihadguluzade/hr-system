package app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginTests {

    private static Connection connection;
    private static PreparedStatement statement;
    private static ResultSet resultSet;

    @BeforeAll
    public static void set() {
        try {
            connection = DBUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String db = "use ysofthr;";
        statement = Assertions.assertDoesNotThrow(() -> connection.prepareStatement(db));
        Assertions.assertDoesNotThrow((ThrowingSupplier<Boolean>) statement::execute);
    }

    @Test
    void testConnection() {
        Assertions.assertNotNull(connection);
    }

    @Test
    void authorizeTest() {
        String firstName = "admin";
        String lastName = "";
        String password = "admin";
        boolean isAdmin = true;
        String sql = "SELECT * FROM company WHERE first_name = ? and last_name = ? and password = ? and admin = ?";

        statement = Assertions.assertDoesNotThrow(() -> connection.prepareStatement(sql));
        Assertions.assertDoesNotThrow(() -> statement.setString(1, firstName));
        Assertions.assertDoesNotThrow(() -> statement.setString(2, lastName));
        Assertions.assertDoesNotThrow(() -> statement.setString(3, password));
        Assertions.assertDoesNotThrow(() -> statement.setBoolean(4, isAdmin));
        resultSet = Assertions.assertDoesNotThrow(() -> statement.executeQuery()); // sends sql code to database
        Assertions.assertDoesNotThrow(() -> Assertions.assertTrue(resultSet.next()));
    }

    @Test
    void authorizeFailTest() {
        String firstName = "john";
        String lastName = "doe";
        String password = "fake";
        boolean isAdmin = true;
        String sql = "SELECT * FROM company WHERE first_name = ? and last_name = ? and password = ? and admin = ?";

        statement = Assertions.assertDoesNotThrow(() -> connection.prepareStatement(sql));
        Assertions.assertDoesNotThrow(() -> statement.setString(1, firstName));
        Assertions.assertDoesNotThrow(() -> statement.setString(2, lastName));
        Assertions.assertDoesNotThrow(() -> statement.setString(3, password));
        Assertions.assertDoesNotThrow(() -> statement.setBoolean(4, isAdmin));
        resultSet = Assertions.assertDoesNotThrow(() -> statement.executeQuery()); // sends sql code to database
        Assertions.assertDoesNotThrow(() -> Assertions.assertFalse(resultSet.next()));
    }

    @Test
    void isFirstTime() {
        String sql = "SELECT * FROM company WHERE admin = 1";
        statement = Assertions.assertDoesNotThrow(() -> connection.prepareStatement(sql));
        resultSet = Assertions.assertDoesNotThrow(() -> statement.executeQuery());
        Assertions.assertDoesNotThrow(() -> Assertions.assertTrue(resultSet.next()));
    }

}
