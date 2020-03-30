package app;

import app.controllers.*;
import app.resources.Strings;
import app.tables.Company;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Manager extends Main {

    private Scene scene;
    private static Connection connection;

    public Manager(Scene scene) {
        this.scene = scene;
    }

    protected void initDB() {
        DBUtils.createDatabase();
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
                "    create_date Date," +
                "    due_date Date," +
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
            controller.start(this, loggedUser);
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

    public void viewNewProjectPane() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/new_project.fxml"), Strings.GetBundle());
            scene.setRoot(loader.load());
            NewProject controller = loader.getController();
            controller.start(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewProjectInfo() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/project_info.fxml"), Strings.GetBundle());
            scene.setRoot(loader.load());
            ProjectInfo controller = loader.getController();
            controller.start(this);
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
