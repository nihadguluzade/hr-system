package app.controllers;

import app.Manager;
import app.classes.Employee;
import app.classes.Project;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProjectInfo {

    @FXML private AnchorPane ProjectInfoPane;
    @FXML private Button modifyBtn;
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
}
