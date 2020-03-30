package app.controllers;

import app.Manager;
import app.tables.Company;
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

    private boolean isTypeChosen = false;
    private boolean isAdmin = false;
    private Company user = null;

    /**
     * This method is called whenever the view linked to it is opened.
     * @param manager For calling another pages.
     */
    public void start(final Manager manager) {
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
                manager.viewDashboard(user);
            }
            else
                Manager.showAlert(Alert.AlertType.ERROR, "Invalid user", "No records found. Try again.");
        });

        langBtn.setOnMouseClicked(mouseEvent -> Manager.showAlert(Alert.AlertType.INFORMATION, "Change language",
                "Language selection is not available right now."));

        firstTimeLink.setOnMouseEntered(e -> firstTimeLink.setStyle("-fx-underline: true"));
        firstTimeLink.setOnMouseExited(e -> firstTimeLink.setStyle("-fx-underline: false"));
        firstTimeLink.setOnMouseClicked(mouseEvent -> {
            if (isFirstTime())
            {
                manager.viewSignUpPage(new Company(true, false));
            }
            else
            {
                manager.showAlert(Alert.AlertType.INFORMATION, "Senior exists",
                        "There is already at least one Senior exists on the system.");
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
    private Company authorize(String firstName, String lastName, String password, boolean isAdmin) {

        // Open connection to database
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
                Company session;
                session = new Company(resultSet.getInt("id"),
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
}
