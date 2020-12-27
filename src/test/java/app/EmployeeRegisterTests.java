package app;

import javafx.application.Platform;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.*;

public class EmployeeRegisterTests {

    private static Connection connection;
    private static PreparedStatement statement;

    @BeforeAll
    public static void set() {
        Platform.startup(() -> {});
        try {
            connection = DBUtils.getConnection();
        } catch (SQLException e) {
            System.out.println("BeforeAll: No connection to database");
        }
    }

    @Test
    void identityCheckTest() {
        String sql = "SELECT * FROM company WHERE email = ?";
        statement = Assertions.assertDoesNotThrow(() -> connection.prepareStatement(sql));
        Assertions.assertDoesNotThrow(() -> statement.setString(1, "johndoe@mail.com"));
        ResultSet resultSet = Assertions.assertDoesNotThrow(() -> statement.executeQuery());
    }

    @Test
    void signUp() {
        Connection connection = Manager.getConnection();
        String sql = "INSERT INTO company (id, acceptdate, title, first_name, last_name, email, password, phone, " +
                "birthdate, nationality, salary, accounting, skills, admin) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            statement = connection.prepareStatement(sql);
            statement.setInt(1, 1000);
            statement.setDate(2, Date.valueOf("31-12-2020"));
            statement.setString(3, "Manager");
            statement.setString(4, "Nihad");
            statement.setString(5, "G");
            statement.setString(6, "nihad@mail.com");
            statement.setString(7, "nihad");
            statement.setLong(8, 123456);
            statement.setNull(9, Types.DATE);
            statement.setString(10, "Az");
            statement.setInt(11, 12000);
            statement.setString(12, "Accounting");
            statement.setNull(13, Types.VARCHAR);
            statement.setBoolean(14, true);
            statement.executeUpdate();

        } catch (Exception e) {
            Assertions.assertThrows(Exception.class, () -> statement.setInt(1, 1000));
        }
    }

}
