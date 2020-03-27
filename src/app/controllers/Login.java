package app.controllers;

import app.DBUtils;
import app.Manager;
import app.tables.Company;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

    @FXML private GridPane loginGridPane;
    @FXML private Button employeeBtn;
    @FXML private Button adminBtn;
    @FXML private TextField idField;
    @FXML private TextField loginPassField;
    @FXML private Button logInBtn;
    @FXML private ChoiceBox langBtn;

    private Connection connection;
    private boolean isAdmin;

    public void initDB() {
        DBUtils.createDatabase();
    }

    private void openConnection() {
        try {
            connection = DBUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void checkCompanyTable() {
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
                "    salary int" +
                "    accounting varchar(255)" +
                "    lang varchar(255)" +
                ");";
        try {
            PreparedStatement statement = connection.prepareStatement(db);
            statement.execute();
            statement = connection.prepareStatement(sql);
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Table `company` checked.");
    }

    public void start(final Manager manager) {
        Scene scene = loginGridPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");

        //openConnection();
        checkCompanyTable();

        employeeBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                employeeBtn.setStyle("-fx-border-color: #4FC3F7");
                adminBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
                isAdmin = false;
            }
        });

        adminBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                adminBtn.setStyle("-fx-border-color: #4FC3F7");
                employeeBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
                isAdmin = true;
            }
        });

        logInBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                // check if fields are empty
                if (idField.getText().isEmpty() || loginPassField.getText().isEmpty())
                {
                    manager.showAlert(Alert.AlertType.ERROR, "Empty field(s)",
                            "Please, do not leave any empty field.");
                    return;
                }

                Company user = null;
                user = authorize(idField.getText(), loginPassField.getText(), isAdmin);

                if (user != null)
                {
                    manager.viewDashboard(user);
                }
                else {
                    manager.showAlert(Alert.AlertType.ERROR, "Invalid user",
                            "No records found. Try again.");
                }
            }
        });

        langBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                manager.showAlert(Alert.AlertType.INFORMATION, "Change language",
                        "Language selection is not available right now.");

            }
        });
    }

    /**
     * Sends the values to the database to login
     * @param id Entered ID of worker
     * @param password Password of worker
     * @return All user information
     */
    private Company authorize(String id, String password, boolean isAdmin) {

        // Open connection to database
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT * FROM company WHERE id = ? and password = ? and admin = ?";

        try{
            // changing ? marks with real values in String sql
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, id);
            preparedStatement.setString(2, password);
            preparedStatement.setBoolean(3, isAdmin);
            resultSet = preparedStatement.executeQuery(); // sends sql code to database

            System.out.printf("id: %s\npassword: %s\n", id, password);

            if(!resultSet.next())
            {
                return null;
            }
            else {
                // store resulted value
                Company session;
                session = new Company(resultSet.getString("id"),
                        resultSet.getString("title"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getLong("phone"),
                        resultSet.getString("nationality"),
                        resultSet.getInt("salary"),
                        resultSet.getString("accounting"),
                        resultSet.getString("lang"),
                        resultSet.getBoolean("admin"));
                return session;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }

}
