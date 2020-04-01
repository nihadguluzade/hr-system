package app;

import app.controllers.*;
import app.resources.Strings;
import app.tables.Company;
import app.tables.Project;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Manager extends Main {

    private Scene scene;
    private static Connection connection;

    private static String[] TEAMS = {};
    private final static String[] ACCOUNTING_PROGRAMS = {"Program 1", "Program 2"};
    private final static String[] PROGRAMMING_LANGS = {"C", "Java", "Python", "Go", "JavaScript", ".NET"};

    public Manager(Scene scene) {
        this.scene = scene;
    }

    public static void openConnection() {
        try {
            connection = DBUtils.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void DBCheckCompany() {
        String db = "use ysofthr;";
        String sql = "create table if not exists company (" +
                "    id int," +
                "    acceptdate Date," +
                "    title varchar(255)," +
                "    first_name varchar(255)," +
                "    last_name varchar(255)," +
                "    email varchar(255)," +
                "    password varchar(255)," +
                "    phone bigint," +
                "    birthdate Date," +
                "    nationality varchar(255)," +
                "    salary int," +
                "    accounting varchar(255)," +
                "    lang varchar(255)," +
                "    admin boolean" +
                ");";
        try {
            PreparedStatement statement = connection.prepareStatement(db);
            statement.execute();
            statement = connection.prepareStatement(sql);
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Table `company` checked.");
    }

    public void DBCheckProjects() {
        String db = "use ysofthr;";
        String sql = "create table if not exists projects (" +
                "    pr_name varchar(255)," +
                "    lang varchar(255)," +
                "    team varchar(255)," +
                "    startDate Date," +
                "    dueDate Date," +
                "    creationDate Date," +
                "    description varchar(255)" +
                ");";
        try {
            PreparedStatement statement = connection.prepareStatement(db);
            statement.execute();
            statement = connection.prepareStatement(sql);
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Table `projects` checked.");
    }

    public void DBCheckTeams() {
        String db = "use ysofthr;";
        String sql = "create table if not exists teams (" +
                "    teams_name varchar(255)," +
                "    members varchar(255)" +
                ");";
        try {
            PreparedStatement statement = connection.prepareStatement(db);
            statement.execute();
            statement = connection.prepareStatement(sql);
            statement.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Table `teams` checked.");
    }

    public static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.initModality(Modality.APPLICATION_MODAL); // set window on top of everyone
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }

    public void viewLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/login.fxml"), Strings.GetBundle());
            scene.setRoot(loader.load());
            Login controller = loader.getController();
            controller.start(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewSignUpPage(Company loggedUser) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/new_employee_form.fxml"), Strings.GetBundle());
            scene.setRoot(loader.load());
            EmployeeRegister controller = loader.getController();
            controller.start(this, loggedUser, ACCOUNTING_PROGRAMS, PROGRAMMING_LANGS);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewDashboard(Company user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/dashboard.fxml"), Strings.GetBundle());
            scene.setRoot(loader.load());
            Dashboard controller = loader.getController();
            controller.start(this, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewEmployeeInfo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/employee_info.fxml"), Strings.GetBundle());
            scene.setRoot(loader.load());
            EmployeeInfo controller = loader.getController();
            controller.start(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewNewProjectPane(Company user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/new_project.fxml"), Strings.GetBundle());
            scene.setRoot(loader.load());
            NewProject controller = loader.getController();
            controller.start(this, PROGRAMMING_LANGS, TEAMS, user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void popProjectInfo(Project project) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/project_info.fxml"), Strings.GetBundle());
            Scene scene = new Scene(loader.load()); // create new scene, otherwise scene will not change after
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(scene);
            stage.show();
            ProjectInfo controller = loader.getController();
            controller.start(this, project);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewProjectEdit() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/project_edit.fxml"), Strings.GetBundle());
            scene.setRoot(loader.load());
            ProjectEdit controller = loader.getController();
            controller.start(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scene getScene() {
        return scene;
    }

    public static Connection getConnection() {
        return connection;
    }
}
