package app.controllers;

import app.DBUtils;
import app.Manager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.SQLException;

public class ProjectEdit {

    @FXML
    private AnchorPane ProjectEditPane;

    private Connection connection;

    public void start(final Manager manager) {
        Scene scene = ProjectEditPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");

    }

}
