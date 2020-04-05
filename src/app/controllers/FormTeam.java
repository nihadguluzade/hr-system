package app.controllers;

import app.Manager;
import app.classes.Employee;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;
import java.util.ArrayList;

public class FormTeam {

    @FXML private GridPane newTeamPane;
    @FXML private ChoiceBox managerChoice;
    @FXML private ChoiceBox analystChoice;
    @FXML private ChoiceBox designerChoice;
    @FXML private ChoiceBox coderChoice;
    @FXML private ChoiceBox testerChoice;
    @FXML private TextField teamNameField;
    @FXML private Button backBtn;
    @FXML private Button submitBtn;

    public void start(final Manager manager, final Employee user) {

        Stage stage = (Stage) newTeamPane.getScene().getWindow();
        stage.sizeToScene();
        stage.setTitle("New Project");
        stage.setResizable(false);

        Scene scene = newTeamPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");

        // fill the choiceboxes
        managerChoice.getItems().addAll(getEmployeeList("Manager"));
        analystChoice.getItems().addAll(getEmployeeList("Analyst"));
        designerChoice.getItems().addAll(getEmployeeList("Designer"));
        coderChoice.getItems().addAll(getEmployeeList("Programmer"));
        testerChoice.getItems().addAll(getEmployeeList("Tester"));

        backBtn.setOnAction(actionEvent -> {
            manager.viewDashboard(user);
        });

        submitBtn.setOnAction(actionEvent -> {

            // check if team name field is empty
            if (teamNameField.getText().isEmpty()) {
                Manager.showAlert(Alert.AlertType.WARNING, "Empty Field", "Team name field cannot be empty.");
                return;
            }

            // TODO: check if team name exists

            createTeam();
            manager.viewDashboard(user);
        });
    }

    /**
     * Sends entered values to database to create team.
     */
    private void createTeam() {
        try {

            Connection connection = Manager.getConnection();
            String sql = "insert into teams (t_name, manager, analyst, designer, programmer, tester) values (?,?,?,?,?,?)";
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, teamNameField.getText());

            // check if there's no selection in choice boxes
            if (!managerChoice.getSelectionModel().isEmpty())
                statement.setInt(2, getEmployeeId(managerChoice.getValue().toString()));
            else statement.setNull(2, Types.INTEGER);
            if (!analystChoice.getSelectionModel().isEmpty())
                statement.setInt(3, getEmployeeId(analystChoice.getValue().toString()));
            else statement.setNull(3, Types.INTEGER);
            if (!designerChoice.getSelectionModel().isEmpty())
                statement.setInt(4, getEmployeeId(designerChoice.getValue().toString()));
            else statement.setNull(4, Types.INTEGER);
            if (!coderChoice.getSelectionModel().isEmpty())
                statement.setInt(5, getEmployeeId(coderChoice.getValue().toString()));
            else statement.setNull(5, Types.INTEGER);
            if (!testerChoice.getSelectionModel().isEmpty())
                statement.setInt(6, getEmployeeId(testerChoice.getValue().toString()));
            else statement.setNull(6, Types.INTEGER);

            statement.executeUpdate();
            Manager.showAlert(Alert.AlertType.INFORMATION, "", "You created new team successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            Manager.showAlert(Alert.AlertType.ERROR, "Exception", "Couldn't create new team. Try again later.");
        }
    }

    /**
     * Return employee's id given his/her full name.
     * @param fullName First and last name
     * @return ID
     */
    protected static int getEmployeeId(String fullName) {
        try {

            String firstName = "";
            String lastName = "";

            if(fullName.split("\\w+").length>1)
            {
                lastName = fullName.substring(fullName.lastIndexOf(" ") + 1);
                firstName = fullName.substring(0, fullName.lastIndexOf(' '));
            }
            else firstName = fullName;

            Connection connection = Manager.getConnection();
            String sql = "select * from company where first_name = ? and last_name = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, firstName);
            statement.setString(2, lastName);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt("id");
            }

            return -1;
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Gets employee names according to title from database.
     * @param title Title; Manager, Analyst..
     * @return Employee names in ArrayList.
     */
    protected static ArrayList<String> getEmployeeList(String title) {
        try {

            Connection connection = Manager.getConnection();
            String sql = "select * from company where title = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, title);
            ResultSet resultSet = statement.executeQuery();

            ArrayList<String> employees = new ArrayList<>(); // store employee names here

            while (resultSet.next()) {
                String fullName = resultSet.getString("first_name") + " " + resultSet.getString("last_name");
                employees.add(fullName);
            }

            return employees;
        } catch (SQLException e) {
            Manager.showAlert(Alert.AlertType.WARNING, "Exception", "ERR_GET_EMPLOYEES");
            e.printStackTrace();
            return null;
        }
    }

}
