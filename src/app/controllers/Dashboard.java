package app.controllers;

import app.DBUtils;
import app.Manager;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.sql.Connection;
import java.sql.SQLException;

public class Dashboard {

    @FXML
    private AnchorPane MainPane;
    @FXML
    private Button logOutBtn;

    private Connection connection;

    public void start(final Manager manager) {
        Scene scene = MainPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");
        scene.getStylesheets().add("app/resources/styles/dashboard_style.css");

        logOutBtn.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                manager.viewSignUpPage();
            }
        });
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
