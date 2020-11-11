package app.controller;

import app.Manager;
import app.model.Employee;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.sql.*;

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

    public void start(final Employee user) {

        Stage stage = (Stage) newTeamPane.getScene().getWindow();
        stage.sizeToScene();
        stage.setTitle("Form team");
        stage.setResizable(false);

        Scene scene = newTeamPane.getScene();
        scene.getStylesheets().add("/styles/style.css");

        // fill the choiceboxes
        managerChoice.getItems().addAll(Manager.getEmployeeList("Manager"));
        analystChoice.getItems().addAll(Manager.getEmployeeList("Analyst"));
        designerChoice.getItems().addAll(Manager.getEmployeeList("Designer"));
        coderChoice.getItems().addAll(Manager.getEmployeeList("Programmer"));
        testerChoice.getItems().addAll(Manager.getEmployeeList("Tester"));

        backBtn.setOnAction(actionEvent -> {
            Manager.viewDashboard(user);
        });

        submitBtn.setOnAction(actionEvent -> {

            // TODO: check if team name exists

            if (errorCheck()) {
                createTeam();
                Manager.viewDashboard(user);
            }
        });
    }

    /**
     * Checks for all possible errors.
     * @return True if there are no errors found.
     */
    private boolean errorCheck() {

        // check if team name field is empty
        if (teamNameField.getText().isEmpty()) {
            Manager.showAlert(Alert.AlertType.WARNING, "Empty Field", "Team name field cannot be empty.");
            return false;
        }

        // set the minimum number of person
        if (managerChoice.getSelectionModel().isEmpty() && analystChoice.getSelectionModel().isEmpty() &&
            designerChoice.getSelectionModel().isEmpty() && coderChoice.getSelectionModel().isEmpty() &&
            testerChoice.getSelectionModel().isEmpty())
        {
            Manager.showAlert(Alert.AlertType.WARNING, "Empty Team", "At least one member should exist in team.");
            return false;
        }

        return true;
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
                statement.setInt(2, Manager.getEmployeeId(managerChoice.getValue().toString()));
            else statement.setNull(2, Types.INTEGER);

            if (!analystChoice.getSelectionModel().isEmpty())
                statement.setInt(3, Manager.getEmployeeId(analystChoice.getValue().toString()));
            else statement.setNull(3, Types.INTEGER);

            if (!designerChoice.getSelectionModel().isEmpty())
                statement.setInt(4, Manager.getEmployeeId(designerChoice.getValue().toString()));
            else statement.setNull(4, Types.INTEGER);

            if (!coderChoice.getSelectionModel().isEmpty())
                statement.setInt(5, Manager.getEmployeeId(coderChoice.getValue().toString()));
            else statement.setNull(5, Types.INTEGER);

            if (!testerChoice.getSelectionModel().isEmpty())
                statement.setInt(6, Manager.getEmployeeId(testerChoice.getValue().toString()));
            else statement.setNull(6, Types.INTEGER);

            statement.executeUpdate();
            Manager.showAlert(Alert.AlertType.INFORMATION, "", "You created new team successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            Manager.showAlert(Alert.AlertType.ERROR, "Exception", "Couldn't create new team. Try again later.");
        }
    }
}
