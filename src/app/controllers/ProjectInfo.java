package app.controllers;

import app.Manager;
import app.tables.Project;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

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

    public void start(final Manager manager, final Project project) {

        Stage stage = (Stage) ProjectInfoPane.getScene().getWindow();
        stage.sizeToScene();
        stage.setTitle("Project Info");
        stage.setResizable(false);

        Scene scene = ProjectInfoPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");

        xBtn.setOnAction(actionEvent -> {
            stage.close();
        });
    }
}
