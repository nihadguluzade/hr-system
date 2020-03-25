package app;

import app.controller.*;
import app.resources.Strings;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import java.io.IOException;

public class Manager extends Main {

    private Scene scene;

    public Manager(Scene scene) {
        this.scene = scene;
    }

    public void viewLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/login.fxml"), Strings.GetBundle());
            scene.setRoot(loader.load());
            Login controller = loader.getController();
            //controller.initDB();
            controller.start(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewSignUpPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/employee_import_form.fxml"), Strings.GetBundle());
            scene.setRoot(loader.load());
            EmployeeImport controller = loader.getController();
            controller.start(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewDashboard() {
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

    public Scene getScene() {
        return scene;
    }

}
