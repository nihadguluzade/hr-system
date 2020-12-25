package app;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBUtilsTest {

    @Test
    void getConnectionTest() {
        Assertions.assertDoesNotThrow(DBUtils::getConnection);
    }

    @Test
    void createDatabaseTest() throws SQLException {
        String sql = "create database if not exists ysofthr character set utf8";
        String url = "jdbc:mysql://localhost";
        String username = "root";
        String password = "adminadmin";
        Connection con = DriverManager.getConnection(url, username, password);
        PreparedStatement statement = con.prepareStatement(sql);
        Assertions.assertDoesNotThrow((ThrowingSupplier<Boolean>) statement::execute);
    }
}
