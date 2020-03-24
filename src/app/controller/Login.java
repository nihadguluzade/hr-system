package app.controller;

import app.DBUtils;
import app.Manager;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;

public class Login {

    @FXML private TextField fullNameField;
    private Connection connection;

    public void start(final Manager manager) {
        Scene scene = fullNameField.getScene();
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
