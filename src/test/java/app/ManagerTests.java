package app;

import app.controller.Login;
import app.util.Strings;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ManagerTests {

    private static Scene mainScene;
    private static Connection connection;
    private static PreparedStatement statement;

    @BeforeAll
    public static void set() {
        Platform.startup(() -> { // JavaFX runtime thread
            mainScene = new Scene(new StackPane());
        });
        try {
            connection = DBUtils.getConnection();
        } catch (SQLException e) {
            System.out.println("BeforeAll: No connection to database");
        }
    }

    @Test
    void testConnection() {
        Assertions.assertNotNull(connection);
    }

    @Test
    void DBCheckCompanyTest() {
        String db = "use ysofthr;";
        String sql = "create table if not exists company (" +
                "    id int," +
                "    acceptdate Date," +
                "    title varchar(255)," +
                "    first_name varchar(255)," +
                "    last_name varchar(255)," +
                "    email varchar(255)," +
                "    password varchar(255)," +
                "    phone bigint," +
                "    birthdate Date," +
                "    nationality varchar(255)," +
                "    salary int," +
                "    accounting varchar(255)," +
                "    skills varchar(255)," +
                "    admin boolean" +
                ");";

        statement = Assertions.assertDoesNotThrow(() -> connection.prepareStatement(db));
        Assertions.assertDoesNotThrow(() -> statement.execute());

        statement = Assertions.assertDoesNotThrow(() -> connection.prepareStatement(sql));
        Assertions.assertDoesNotThrow(() -> statement.execute());
    }

    @Test
    void DBCheckProjectsTest() {
        String db = "use ysofthr;";
        String sql = "create table if not exists projects (" +
                "    pr_name varchar(255)," +
                "    lang varchar(255)," +
                "    team varchar(255)," +
                "    startDate Date," +
                "    dueDate Date," +
                "    creationDate Date," +
                "    description varchar(255)" +
                ");";

        statement = Assertions.assertDoesNotThrow(() -> connection.prepareStatement(db));
        Assertions.assertDoesNotThrow(() -> statement.execute());

        statement = Assertions.assertDoesNotThrow(() -> connection.prepareStatement(sql));
        Assertions.assertDoesNotThrow(() -> statement.execute());
    }

    @Test
    void DBCheckTeamsTest() {
        String db = "use ysofthr;";
        String sql = "create table if not exists teams (" +
                "    t_name varchar(255) not null," +
                "    manager int references company(id)," +
                "    analyst int references company(id)," +
                "    designer int references company(id)," +
                "    programmer int references company(id)," +
                "    tester int references company(id)" +
                ");";
        try {
            statement = connection.prepareStatement(db);
            Assertions.assertDoesNotThrow(() -> statement.execute());

            statement = connection.prepareStatement(sql);
            Assertions.assertDoesNotThrow(() -> statement.execute());
        } catch (Exception e) {
            e.printStackTrace();
            Assertions.assertThrows(Exception.class, statement::execute);
        }
    }

    @Test
    void DBGetTitlesTest() {
        String db = "use ysofthr;";
        String sql = "select column_name from information_schema.columns where table_name = 'teams'";

        statement = Assertions.assertDoesNotThrow(() -> connection.prepareStatement(db));
        Assertions.assertDoesNotThrow(() -> statement.execute());

        statement = Assertions.assertDoesNotThrow(() -> connection.prepareStatement(sql));
        Assertions.assertDoesNotThrow(() -> statement.executeQuery());
    }

    @Test
    void showAlertTest() {
        Platform.runLater(() -> {
            Alert.AlertType alertType = Alert.AlertType.INFORMATION;
            Alert alert = new Alert(alertType);
            alert.initModality(Modality.APPLICATION_MODAL); // set window on top of everyone
            alert.setTitle("Test title");
            alert.setHeaderText(null);
            alert.setContentText("If you see this then test is succeeded");
            Assertions.assertDoesNotThrow(alert::show);
        });
    }

    @Test
    void getTeamsTest() {
        String sql = "select * from teams";
        ArrayList<String> teamList = new ArrayList<>();
        statement = Assertions.assertDoesNotThrow(() -> connection.prepareStatement(sql));
        ResultSet resultSet = Assertions.assertDoesNotThrow(() -> statement.executeQuery());

        Assertions.assertNotNull(resultSet);

        while (Assertions.assertDoesNotThrow(resultSet::next)) {
            teamList.add(Assertions.assertDoesNotThrow(() -> resultSet.getString("t_name")));
        }

        Assertions.assertNotNull(teamList);
        Assertions.assertEquals("Admins", teamList.get(0));
        Assertions.assertEquals("Turbo", teamList.get(1));
        Assertions.assertEquals("Junior", teamList.get(2));
    }

    @Test
    void viewLoginPageTest() {
        FXMLLoader loader = new FXMLLoader(Manager.class.getResource("/view/login.fxml"), Strings.GetBundle());
        Assertions.assertDoesNotThrow(() -> mainScene.setRoot(loader.load()));
        Login controller = Assertions.assertDoesNotThrow((ThrowingSupplier<Login>) loader::getController);
        Assertions.assertDoesNotThrow(controller::start);
    }

}
