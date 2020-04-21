package app.controllers;

import app.Manager;
import app.classes.Employee;
import app.classes.Team;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.Optional;

public class TeamEdit {

    @FXML private AnchorPane editTeamPane;
    @FXML private TextField nameField;
    @FXML private Button saveBtn;
    @FXML private Button discardBtn;
    @FXML private ChoiceBox managerChoice;
    @FXML private ChoiceBox analystChoice;
    @FXML private ChoiceBox designerChoice;
    @FXML private ChoiceBox coderChoice;
    @FXML private ChoiceBox testerChoice;
    @FXML private Button removeManagerBtn;
    @FXML private Button removeAnalystBtn;
    @FXML private Button removeDesignerBtn;
    @FXML private Button removeCoderBtn;
    @FXML private Button removeTesterBtn;

    public void start(Team team, final Employee user) {

        Stage stage = (Stage) editTeamPane.getScene().getWindow();
        stage.sizeToScene();
        stage.setTitle("Modify Team");
        stage.setResizable(false);

        Scene scene = editTeamPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");

        initialize(team);

        removeManagerBtn.setOnAction(actionEvent -> {
            removeFromTeam("manager", team.getName());
            managerChoice.getSelectionModel().clearSelection();
        });
        removeAnalystBtn.setOnAction(actionEvent -> {
            removeFromTeam("analyst", team.getName());
            analystChoice.getSelectionModel().clearSelection();
        });
        removeDesignerBtn.setOnAction(actionEvent -> {
            removeFromTeam("designer", team.getName());
            designerChoice.getSelectionModel().clearSelection();
        });
        removeCoderBtn.setOnAction(actionEvent -> {
            removeFromTeam("programmer", team.getName());
            coderChoice.getSelectionModel().clearSelection();
        });
        removeTesterBtn.setOnAction(actionEvent -> {
            removeFromTeam("tester", team.getName());
            testerChoice.getSelectionModel().clearSelection();
        });

        discardBtn.setOnAction(actionEvent -> Manager.viewTeams(user));

        saveBtn.setOnAction(actionEvent -> {
            String manager = (String) managerChoice.getSelectionModel().getSelectedItem();
            String analyst = (String) analystChoice.getSelectionModel().getSelectedItem();
            String designer = (String) designerChoice.getSelectionModel().getSelectedItem();
            String programmer = (String) coderChoice.getSelectionModel().getSelectedItem();
            String tester = (String) testerChoice.getSelectionModel().getSelectedItem();
            saveChanges(nameField.getText(), manager, analyst, designer, programmer, tester, team.getName());
        });
    }

    /**
     * Fills the choice boxes and fields on view.
     * @param team Team that will be modified
     */
    private void initialize(Team team) {
        // fill the choiceboxes
        managerChoice.getItems().addAll(Manager.getEmployeeList("Manager"));
        managerChoice.getSelectionModel().select(Dashboard.getEmployeeName(team.getManager()));

        analystChoice.getItems().addAll(Manager.getEmployeeList("Analyst"));
        analystChoice.getSelectionModel().select(Dashboard.getEmployeeName(team.getAnalyst()));

        designerChoice.getItems().addAll(Manager.getEmployeeList("Designer"));
        designerChoice.getSelectionModel().select(Dashboard.getEmployeeName(team.getDesigner()));

        coderChoice.getItems().addAll(Manager.getEmployeeList("Programmer"));
        coderChoice.getSelectionModel().select(Dashboard.getEmployeeName(team.getProgrammer()));

        testerChoice.getItems().addAll(Manager.getEmployeeList("Tester"));
        testerChoice.getSelectionModel().select(Dashboard.getEmployeeName(team.getTester()));

        nameField.setText(team.getName());
    }

    /**
     * Removes employee from the team by setting the record to null on DB.
     * @param title Which title employee belongs
     * @param teamName Name of the team
     */
    private void removeFromTeam(String title, String teamName) {
        try {
            Connection con = Manager.getConnection();
            PreparedStatement preparedStatement = null;
            String sql = "UPDATE teams SET " + title + " = null WHERE t_name = ?";
            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, teamName);

            // show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.APPLICATION_MODAL); // set window on top of everyone
            alert.setHeaderText(null);
            alert.setTitle("Remove");
            alert.setContentText("Confirm to remove employee from team " + teamName + "?");
            Optional<ButtonType> result = alert.showAndWait();

            if (result.get() == ButtonType.OK) {
                preparedStatement.executeUpdate();
                Manager.showAlert(Alert.AlertType.INFORMATION, "",
                        "Employee successfully removed from team. This change has been saved.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            Manager.showAlert(Alert.AlertType.ERROR, "Exception", "Couldn't remove employee from team.");
        }
    }

    /**
     * Update team changes by sending all info to DB.
     * @param newTeamName New team name
     * @param manager Manager name
     * @param analyst Analyst name
     * @param designer Designer name
     * @param programmer Programmer name
     * @param tester Tester name
     * @param teamName Old team name
     */
    private void saveChanges(String newTeamName, String manager, String analyst, String designer, String programmer,
                             String tester, String teamName) {
        try {
            Connection con = Manager.getConnection();
            PreparedStatement preparedStatement = null;
            // get the IDs of the employees because DB table doesn't store names but IDs
            Integer managerID = Manager.getEmployeeId(manager);
            Integer analystID = Manager.getEmployeeId(analyst);
            Integer designerID = Manager.getEmployeeId(designer);
            Integer programmerID = Manager.getEmployeeId(programmer);
            Integer testerID = Manager.getEmployeeId(tester);
            String sql = "UPDATE teams " +
                    "SET t_name = ?, " +
                    "manager = ?, analyst = ?, " +
                    "designer = ?, " +
                    "programmer = ?, " +
                    "tester = ? " +
                    "WHERE t_name = ?";

            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, newTeamName);

            if (managerID != null) preparedStatement.setInt(2, managerID);
            else preparedStatement.setNull(2, Types.INTEGER);

            if (analystID != null) preparedStatement.setInt(3, analystID);
            else preparedStatement.setNull(3, Types.INTEGER);

            if (designerID != null) preparedStatement.setInt(4, designerID);
            else preparedStatement.setNull(4, Types.INTEGER);

            if (programmerID != null) preparedStatement.setInt(5, programmerID);
            else preparedStatement.setNull(5, Types.INTEGER);

            if (testerID != null) preparedStatement.setInt(6, testerID);
            else preparedStatement.setNull(6, Types.INTEGER);

            preparedStatement.setString(7, teamName);

            // show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Save");
            alert.initModality(Modality.APPLICATION_MODAL); // set window on top of everyone
            alert.setHeaderText(null);
            alert.setContentText("Confirm to save all changes?");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                preparedStatement.executeUpdate();
                Manager.showAlert(Alert.AlertType.INFORMATION, "", "Team successfully updated.");
            }

        } catch (Exception e) {
            Manager.showAlert(Alert.AlertType.ERROR, "Error", "Couldn't update team.");
        }
    }
}
