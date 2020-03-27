package app;

import app.controllers.*;
import app.resources.Strings;
import app.tables.Company;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Modality;

import java.io.IOException;

public class Manager extends Main {

    private Scene scene;

    public Manager(Scene scene) {
        this.scene = scene;
    }

    public void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        // Set window on top of everyone
        alert.initModality(Modality.APPLICATION_MODAL);
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
            //controllers.initDB();
            controller.start(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewSignUpPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/new_employee_form.fxml"), Strings.GetBundle());
            scene.setRoot(loader.load());
            EmployeeImport controller = loader.getController();
            controller.start(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewDashboard(Company user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/dashboard.fxml"), Strings.GetBundle());
            scene.setRoot(loader.load());
            Dashboard controller = loader.getController();
            controller.start(this);
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

}
