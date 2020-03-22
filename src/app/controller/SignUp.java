package app.controller;

import app.DBUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.SQLException;

public class SignUp {

    @FXML
    private TextField nameField;
    private Connection connection;

    public void start() {
        Scene scene = nameField.getScene();
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
