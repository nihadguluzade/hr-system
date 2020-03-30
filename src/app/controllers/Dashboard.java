package app.controllers;

import app.DBUtils;
import app.Manager;
import app.tables.Company;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.sql.SQLException;
import java.util.Calendar;

public class Dashboard {

    @FXML private AnchorPane MainPane;
    @FXML private Label todaysDate;
    @FXML private Label userLabel;
    @FXML private Button logOutBtn;
    @FXML private Button importCandidateBtn;
    @FXML private Button createProjectBtn;


    public void start(final Manager manager, final Company user) {

        Stage stage = (Stage) MainPane.getScene().getWindow();
        stage.sizeToScene();
        stage.setTitle("Dashboard");
        stage.setResizable(false);

        Scene scene = MainPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");
        scene.getStylesheets().add("app/resources/styles/dashboard_style.css");

        // set the today's date
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        todaysDate.setText(day + "/" + (month + 1) + "/" + year);

        userLabel.setText(user.getFullName());

        logOutBtn.setOnAction(actionEvent -> {
            user.setLogged(false);
            manager.viewLoginPage();
        });

        importCandidateBtn.setOnAction(actionEvent -> {
            manager.viewSignUpPage(user);
        });

        createProjectBtn.setOnAction(actionEvent -> {
            manager.viewNewProjectPane();
        });
    }
}
