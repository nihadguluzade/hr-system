package app;

import app.controller.Login;
import app.controller.SignUp;
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
            controller.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void viewSignUpPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("view/signup.fxml"), Strings.GetBundle());
            scene.setRoot(loader.load());
            SignUp controller = loader.getController();
            //controller.initDB();
            controller.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Scene getScene() {
        return scene;
    }

}
