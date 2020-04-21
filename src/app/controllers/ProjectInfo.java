package app.controllers;

import app.Manager;
import app.classes.Employee;
import app.classes.Project;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class ProjectInfo {

    @FXML private AnchorPane ProjectInfoPane;
    @FXML private Button modifyBtn;
    @FXML private Button deleteBtn;
    @FXML private Button xBtn;
    @FXML private Label nameLabel;
    @FXML private Label langLabel;
    @FXML private Label managerLabel;
    @FXML private Label teamLabel;
    @FXML private Label dueDateLabel;
    @FXML private Label description;

    public void start(final Project project, final Employee employee) {

        Stage stage = (Stage) ProjectInfoPane.getScene().getWindow();
        stage.sizeToScene();
        stage.setTitle("Project Info");
        stage.setResizable(false);

        Scene scene = ProjectInfoPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");

        initializeLabels(project);

        if (!employee.isAdmin())
            modifyBtn.setVisible(false);

        xBtn.setOnAction(actionEvent -> {
            stage.close();
        });

        modifyBtn.setOnAction(actionEvent -> {
            Manager.viewProjectEdit(project, employee);
        });

        deleteBtn.setOnAction(actionEvent -> deleteProject(project));
    }

    /**
     * Sets the labels on the GUI.
     * @param project Project
     */
    private void initializeLabels(Project project) {
        nameLabel.setText(project.getName());
        langLabel.setText(project.getLanguage());

        try {
            if (project.getTeam().getName() != null) {
                teamLabel.setText(project.getTeam().getName());
                managerLabel.setText(Dashboard.getEmployeeName(project.getTeam().getManager()));
            }
        } catch (NullPointerException e) {
            System.out.println("Team not found");
            managerLabel.setText("-");
            teamLabel.setText("-");
        }

        dueDateLabel.setText(project.getDueDate().toString());

        try {
            description.setText(project.getDescription());
        } catch (NullPointerException e) {
            System.out.println("Exception occured.");
            description.setText("No description.");
        }
    }

    /**
     * Deletes project from DB.
     * @param project Project to be deleted
     */
    private void deleteProject(Project project) {
        Connection connection = Manager.getConnection();
        String sql = "delete from projects where pr_name = ?";
        PreparedStatement statement;
        try {
            statement = connection.prepareStatement(sql);
            statement.setString(1, project.getName());

            // show confirmation dialog
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Remove");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setHeaderText(null);
            alert.setContentText("Confirm to delete this project?");
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                statement.executeUpdate();
                Manager.showAlert(Alert.AlertType.INFORMATION, "",
                        "Project has been successfully deleted from this company.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Couldn't delete the project records.");
        }
    }
}
