package app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.sql.*;

public class NewProjectTests {

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
    void checkProjectNameTest() {
        String sql = "SELECT * FROM projects WHERE pr_name = ?";
        statement = Assertions.assertDoesNotThrow(() -> connection.prepareStatement(sql));
        Assertions.assertDoesNotThrow(() -> statement.setString(1, "HR System"));
        resultSet = Assertions.assertDoesNotThrow(() -> statement.executeQuery());
        Assertions.assertDoesNotThrow(() -> Assertions.assertTrue(resultSet.next()));
    }

}
