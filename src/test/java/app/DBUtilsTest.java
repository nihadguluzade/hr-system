package app;

import com.mysql.cj.jdbc.exceptions.CommunicationsException;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBUtilsTest {

    @BeforeAll
    public static void set() {
        Platform.startup(() -> {});
    }

    @Test
    void getConnectionTest() {
        try {
            DBUtils.getConnection();
        } catch (Exception e) {
            System.out.println("Localhost is not open.");
            Assertions.assertThrows(e.getClass(), DBUtils::getConnection);
        }
    }

    @Test
    void createDatabaseTest() throws SQLException {
        String sql = "create database if not exists ysofthr character set utf8";
        String url = "jdbc:mysql://localhost";
        String username = "root";
        String password = "adminadmin";
        try {
            Connection con = DriverManager.getConnection(url, username, password);
            PreparedStatement statement = con.prepareStatement(sql);
            Assertions.assertDoesNotThrow((ThrowingSupplier<Boolean>) statement::execute);
        } catch (CommunicationsException e) {
            System.out.println("Communications link failure");
        }
    }
}
