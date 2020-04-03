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

    public void start(final Manager manager, final Project project, final Employee employee) {

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
            manager.viewProjectEdit(project, employee);
        });
    }

    /**
     * Sets the labels on the GUI.
     * @param project Project
     */
    private void initializeLabels(Project project) {
        nameLabel.setText(project.getName());
        langLabel.setText(project.getLanguage());

        if (project.getTeam().getName() == null)
        {
            managerLabel.setText("-");
            teamLabel.setText("-");
        }
        else
        {
            teamLabel.setText(project.getTeam().getName());
            managerLabel.setText(getEmployeeName(project.getTeam().getManager()));
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
     * Get employee's name from database given his/her id.
     * @param id ID of employee
     * @return Full name of the employee
     */
    private String getEmployeeName(int id) {
        try {
            Connection connection = Manager.getConnection();
            String sql = "select * from company where id = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet result = statement.executeQuery();
            result.next();
            return result.getString("first_name") + " " + result.getString("last_name");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
