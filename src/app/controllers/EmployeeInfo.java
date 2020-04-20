package app.controllers;

import app.Manager;
import app.classes.Employee;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import java.sql.Connection;
import java.sql.SQLException;

public class EmployeeInfo {

    @FXML
    private GridPane EmployeeInfoPane;

    private Connection connection;

    public void start( Employee employee) {
        Scene scene = EmployeeInfoPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");

    }

}
