package app.controllers;

import app.Manager;
import app.classes.Employee;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;

public class NewProject {

    @FXML private AnchorPane NewProjectPane;
    @FXML private TextField projectNameField;
    @FXML private ChoiceBox langSelector;
    @FXML private TextArea descriptionArea;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker dueDatePicker;
    @FXML private ChoiceBox teamSelector;
    @FXML private Label creationDateLabel;
    @FXML private Button createBtn;
    @FXML private Button closeBtn;

    public void start(final String[] programmingLangs, final Employee user) {

        Stage stage = (Stage) NewProjectPane.getScene().getWindow();
        stage.sizeToScene();
        stage.setTitle("New Project");
        stage.setResizable(false);

        Scene scene = NewProjectPane.getScene();
        scene.getStylesheets().add("app/resources/styles/style.css");

        // TODO: fill the content of teams

        // fill the choiceBoxes
        langSelector.getItems().addAll(programmingLangs);
        langSelector.getSelectionModel().selectFirst(); // sets default value of choicebox
        teamSelector.getItems().addAll(Manager.getTeams());
        teamSelector.getSelectionModel().selectFirst(); // sets default value of choicebox

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DATE);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        creationDateLabel.setText(day + "/" + (month + 1) + "/" + year);

        createBtn.setOnAction(actionEvent ->
        {
            // check if fields are empty
            if (projectNameField.getText().isEmpty()) {
                Manager.showAlert(Alert.AlertType.ERROR, "Empty Field", "Project name cannot be empty.");
                return;
            }

            // check whether project with this name exists
            if (checkProjectName(projectNameField.getText())) {
                Manager.showAlert(Alert.AlertType.ERROR, "Name exists", "Project with this name ");
                return;
            }

            // TODO: check if start date exceeds due date

            LocalDate creationDate = LocalDate.of(year, month + 1, day);
            String lang = "temp_lang", team = "temp_team";

            try {
                lang = langSelector.getValue().toString();
            } catch (NullPointerException e) {
                Manager.showAlert(Alert.AlertType.WARNING, "Exception", "ERR_PROJECT_LANGS_NOT_DEFINED");
                e.printStackTrace();
            }

            try {
                team = teamSelector.getValue().toString();
            } catch (NullPointerException e) {
                Manager.showAlert(Alert.AlertType.WARNING, "Exception", "ERR_NO_TEAM_FOUND");
                e.printStackTrace();
            }

            createProject(projectNameField.getText(), descriptionArea.getText(), startDatePicker.getValue(),
                    dueDatePicker.getValue(), creationDate, lang, team);
            Manager.viewDashboard(user);
        });

        closeBtn.setOnAction(actionEvent -> {
            //manager.showAlert(Alert.AlertType.CONFIRMATION, "Confirmation", "Do you really want to close?");
            Manager.viewDashboard(user);
        });
    }

    /**
     * Check if the project with this name already exists on database.
     * @param name Project name
     * @return True if exists
     */
    private boolean checkProjectName(String name) {
        try {
            String sql = "SELECT * FROM projects WHERE pr_name = ?";
            Connection connection = Manager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();

        } catch (SQLException e) {
            e.printStackTrace();
            Manager.showAlert(Alert.AlertType.WARNING, "Exception", "ERR_CHECK_PROJECT_NAME");
            return false;
        }
    }

    /**
     * Creates project by storing record in database.
     * @param name Project name
     * @param description Project description
     * @param startDate Start date of project
     * @param dueDate Finish date of project
     * @param creationDate Creation date of project
     * @param language Programming language of project
     * @param team Team that will work on project
     */
    private void createProject(String name, String description, LocalDate startDate, LocalDate dueDate, LocalDate creationDate,
                               String language, String team) {
        Connection connection = Manager.getConnection();
        PreparedStatement preparedStatement = null;
        String sql = "INSERT INTO projects (pr_name, lang, team, startDate, dueDate, creationDate, description) VALUES (?,?,?,?,?,?,?)";

        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, language);
            preparedStatement.setString(3, team);
            preparedStatement.setDate(4, Date.valueOf(startDate));
            preparedStatement.setDate(5, Date.valueOf(dueDate));
            preparedStatement.setDate(6, Date.valueOf(creationDate));
            preparedStatement.setString(7, description);
            preparedStatement.executeUpdate();
            Manager.showAlert(Alert.AlertType.INFORMATION, "", "New project created successfully.");
        } catch (Exception e) {
            Manager.showAlert(Alert.AlertType.WARNING, "Exception", "ERR_CREATE_PROJECT");
            e.printStackTrace();
        }
    }
}
