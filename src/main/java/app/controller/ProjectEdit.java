package app.controller;

import app.Manager;
import app.model.Employee;
import app.model.Project;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

public class ProjectEdit {

    @FXML private AnchorPane ProjectEditPane;
    @FXML private Button saveBtn;
    @FXML private Button discardBtn;
    @FXML private TextField nameField;
    @FXML private ChoiceBox langField;
    @FXML private ChoiceBox teamChooser;
    @FXML private DatePicker dueDatePicker;
    @FXML private TextArea descriptionArea;

    public void start(final Project project, final String[] programmingLangs,
                      final Employee employee) {

        Stage stage = (Stage) ProjectEditPane.getScene().getWindow();
        stage.sizeToScene();
        stage.setTitle("Modify Project");
        stage.setResizable(false);

        Scene scene = ProjectEditPane.getScene();
        scene.getStylesheets().add("/styles/style.css");

        initializeFields(project, programmingLangs);

        saveBtn.setOnAction(actionEvent -> {
            Project modifiedProject = saveChanges(project); // 'project' should be final, java rule
            Manager.viewProjectInfo(modifiedProject, employee);
        });

        discardBtn.setOnAction(actionEvent -> {
            Manager.viewProjectInfo(project, employee);
        });
    }

    private void initializeFields(Project project, String[] programmingLangs) {
        nameField.setText(project.getName());
        langField.getItems().addAll(programmingLangs);
        langField.getSelectionModel().select(project.getLanguage());
        teamChooser.getItems().addAll(Manager.getTeams());
        teamChooser.getSelectionModel().selectFirst();
        dueDatePicker.setValue(project.getDueDate());
        if (project.getDescription() != null)
            descriptionArea.setText(project.getDescription());
    }

    private Project saveChanges(Project project) {
        try {
            Connection con = Manager.getConnection();
            PreparedStatement preparedStatement = null;
            String sql = "UPDATE projects SET pr_name = ?, lang = ?, team = ?, dueDate = ?, description = ? WHERE pr_name = ?";

            preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, nameField.getText());
            preparedStatement.setString(2, langField.getValue().toString());
            preparedStatement.setString(3, teamChooser.getValue().toString());
            preparedStatement.setDate(4, Date.valueOf(dueDatePicker.getValue()));
            preparedStatement.setString(5, descriptionArea.getText());
            preparedStatement.setString(6, project.getName());
            preparedStatement.executeUpdate();

            Manager.showAlert(Alert.AlertType.INFORMATION, "", "Project successfully modified.");

            // modify the values of the object too
            project.setName(nameField.getText());
            project.setLanguage(langField.getValue().toString());
            project.setTeam(Dashboard.findTeam(teamChooser.getValue().toString()));
            project.setDueDate(dueDatePicker.getValue());
            project.setDescription(descriptionArea.getText());

        } catch (Exception e) {
            Manager.showAlert(Alert.AlertType.ERROR, "Exception", "Couldn't send message to database.");
        }

        return project;
    }

}
