package app;

import app.model.Team;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import app.Manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBUtilsTest {

    public static DBUtils db;
    private static String url = "jdbc:mysql://localhost";
    private static String username = "root";
    private static String password = "adminadmin";

    @BeforeAll
    public static void set(){
        db = new DBUtils();
    }

    @Test
    void getConnectionExceptionTest() {
        // if db is closed, throw exception
        Assertions.assertThrows(SQLException.class, DBUtils::getConnection);
    }

    @Test
    void createDatabaseExceptionTest() {
        // if db is closed, throw exception
        Assertions.assertThrows(Exception.class, DBUtils::createDatabase);
    }

    @Test
    void getConnectionTest() {
        Assertions.assertDoesNotThrow(DBUtils::getConnection);
    }

    @Test
    void createDatabaseTest() throws SQLException {
        String sql = "create database if not exists ysofthr character set utf8";
        Connection con = DriverManager.getConnection(url, username, password);
        PreparedStatement statement = con.prepareStatement(sql);
        Assertions.assertDoesNotThrow(() -> statement.execute());
    }
}
