package app.controllers;

import app.DBUtils;
import app.Manager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;

import java.sql.Connection;
import java.sql.SQLException;

public class EmployeeInfo {

    @FXML
    private GridPane EmployeeInfoPane;

    private Connection connection;

    public void start(final Manager manager) {
        Scene scene = EmployeeInfoPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");

    }

}
