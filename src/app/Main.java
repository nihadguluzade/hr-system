package app;

import app.resources.Strings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 * Software Engineering Course Project
 * @author nihadguluzade
 */
public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setResources(Strings.GetBundle());

        Scene scene = new Scene(new StackPane());

        Manager manager = new Manager(scene);
        manager.viewSignUpPage();

        //stage.getIcons().add(new Image(Main.class.getResourceAsStream("/app/resources/icon.png")));
        stage.setTitle(Strings.MAIN_APP_TITLE);
        stage.sizeToScene();
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
    }
}