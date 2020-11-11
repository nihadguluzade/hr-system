package app;

import app.util.Strings;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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
    public void start(Stage stage) {

        FXMLLoader loader = new FXMLLoader();
        loader.setResources(Strings.GetBundle());
        Scene scene = new Scene(new StackPane());
        Manager manager = new Manager(scene);

        try
        {
            DBUtils.createDatabase();
            manager.openConnection();
            manager.DBCheckCompany();
            manager.DBCheckProjects();
            manager.DBCheckTeams();
            manager.viewLoginPage();

            //stage.getIcons().add(new Image(Main.class.getResourceAsStream("/app/resources/icon.png")));
            stage.setTitle(Strings.MAIN_APP_TITLE);
            stage.sizeToScene();
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
        catch (Exception e)
        {
            Manager.showAlert(Alert.AlertType.ERROR, "No connection",
                    "No connection to database. To continue using this application, please make sure that the MySQL is running.");
            e.printStackTrace();
        }
    }
}