package app.controllers;

import app.Manager;
import app.classes.Employee;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

    @FXML private GridPane loginGridPane;
    @FXML private Button employeeBtn;
    @FXML private Button adminBtn;
    @FXML private TextField fullNameField;
    @FXML private TextField loginPassField;
    @FXML private Button logInBtn;
    @FXML private ChoiceBox langBtn;
    @FXML private Label firstTimeLink;
    @FXML private Button testBtn;

    private boolean isTypeChosen = false;
    private boolean isAdmin = false;
    private Employee user = null;

    /**
     * This method is called whenever the view linked to it is opened.
     */
    public void start() {
        Scene scene = loginGridPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");

        // TODO: find efficient solution
        // Bugfix to prevent incorrect window size on logout.
        scene.setOnMouseMoved(e -> {
            Stage stage = (Stage) scene.getWindow();
            stage.sizeToScene();
            stage.setResizable(false);
            stage.setTitle("YSOFT Login");
        });

        employeeBtn.setOnAction(actionEvent -> {
            employeeBtn.setStyle("-fx-border-color: #4FC3F7");
            adminBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            isTypeChosen = true;
            isAdmin = false;
        });

        adminBtn.setOnAction(actionEvent -> {
            adminBtn.setStyle("-fx-border-color: #4FC3F7");
            employeeBtn.setStyle("-fx-border-color: transparent; -fx-opacity: 0.5");
            isTypeChosen = true;
            isAdmin = true;
        });

        logInBtn.setOnAction(actionEvent -> {

            // check whether type is chosen
            if (!isTypeChosen)
            {
                Manager.showAlert(Alert.AlertType.ERROR, "Type Invalid",
                        "Please, choose employee type to continue.");
                return;
            }

            // check whether fields are empty
            if (fullNameField.getText().isEmpty() || loginPassField.getText().isEmpty())
            {
                Manager.showAlert(Alert.AlertType.ERROR, "Empty field(s)",
                        "Please, do not leave any empty field.");
                return;
            }

            String fullName = fullNameField.getText();
            String firstName = "";
            String lastName = "";

            if(fullName.split("\\w+").length>1)
            {
                lastName = fullName.substring(fullName.lastIndexOf(" ") + 1);
                firstName = fullName.substring(0, fullName.lastIndexOf(' '));
            }
            else firstName = fullName;

            user = authorize(firstName, lastName, loginPassField.getText(), isAdmin);

            if (user != null)
            {
                user.setLogged(true);
                Manager.viewDashboard(user);
            }
            else
                Manager.showAlert(Alert.AlertType.ERROR, "Invalid user", "No records found. Try again.");
        });

        langBtn.setVisible(false);
        //langBtn.setOnMouseClicked(mouseEvent -> Manager.showAlert(Alert.AlertType.INFORMATION, "Change language",
        //        "Language selection is not available right now."));

        firstTimeLink.setOnMouseEntered(e -> firstTimeLink.setStyle("-fx-underline: true"));
        firstTimeLink.setOnMouseExited(e -> firstTimeLink.setStyle("-fx-underline: false"));
        firstTimeLink.setOnMouseClicked(mouseEvent -> {
            if (isFirstTime())
            {
                Manager.viewSignUpPage(new Employee(true, false));
            }
            else
            {
                Manager.showAlert(Alert.AlertType.INFORMATION, "Senior exists",
                        "There is already at least one Senior exists on the system.");
            }
        });

        // TODO: make test button funtional
        testBtn.setVisible(false);
        // test button is for development purpose. Creates random employees, projects, teams etc...
        testBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                //createEmployees();
                //createProjects();
                //createTeams();
            }
        });
    }

    /**
     * Sends the values to the database to login
     * @param firstName First and middle name of the worker
     * @param lastName Surname of the worker
     * @param password Password of worker
     * @return All user information
     */
    private Employee authorize(String firstName, String lastName, String password, boolean isAdmin) {

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String sql = "SELECT * FROM company WHERE first_name = ? and last_name = ? and password = ? and admin = ?";
        Connection connection = Manager.getConnection();

        try{
            // changing ? marks with real values in String sql
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setString(3, password);
            preparedStatement.setBoolean(4, isAdmin);
            resultSet = preparedStatement.executeQuery(); // sends sql code to database

            System.out.printf("Full Name: %s %s\npassword: %s\n", firstName, lastName, password);

            if(!resultSet.next())
            {
                return null;
            }
            else {
                // store resulted value
                Employee session;
                session = new Employee(resultSet.getInt("id"),
                        resultSet.getDate("acceptDate").toLocalDate(),
                        resultSet.getString("title"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("email"),
                        resultSet.getString("password"),
                        resultSet.getLong("phone"),
                        resultSet.getDate("birthDate").toLocalDate(),
                        resultSet.getString("nationality"),
                        resultSet.getInt("salary"),
                        resultSet.getString("accounting"),
                        resultSet.getString("skills"),
                        resultSet.getBoolean("admin"));
                return session;
            }
        }
        catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Check if it is the first time opening the system.
     */
    private boolean isFirstTime() {
        try {
            String sql = "SELECT * FROM company WHERE admin = 1";
            Connection connection = Manager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery();
            return !resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // TODO: finish it
    private void createEmployees() {
        Connection connection = Manager.getConnection();
        String sql = "insert into company" +
                "values (" +
                "        000001," +
                "        '2020-04-21'," +
                "        'Manager'," +
                "        'Durga'," +
                "        'Kristian'," +
                "        'durga@mail.com'," +
                "        'durga1234'," +
                "        5301002030," +
                "        '2020-03-01'," +
                "        'UK'," +
                "        2100," +
                "        'Program 1'," +
                "        null," +
                "        1" +
                "       )";


    }
}
