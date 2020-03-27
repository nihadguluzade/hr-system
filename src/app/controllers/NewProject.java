package app.controllers;

import app.DBUtils;
import app.Manager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.SQLException;

public class NewProject {

    @FXML
    private AnchorPane NewProjectPane;

    private Connection connection;

    public void start(final Manager manager) {
        Scene scene = NewProjectPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");

    }

    public void initDB() {
        DBUtils.createDatabase();
    }

    private void openConnection() {
        try {
            connection = DBUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
