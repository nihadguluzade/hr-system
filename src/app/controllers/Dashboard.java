package app.controllers;

import app.DBUtils;
import app.Manager;
import app.tables.Company;
import app.tables.Project;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class Dashboard {

    @FXML private AnchorPane MainPane;
    @FXML private TilePane projectsPane;
    @FXML private Label todaysDate;
    @FXML private Label userLabel;
    @FXML private Button logOutBtn;
    @FXML private Button importCandidateBtn;
    @FXML private Button createProjectBtn;

    private ObservableList<Project> projectList = FXCollections.observableArrayList();


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

        loadProjects();

        for (Node node: projectsPane.lookupAll(".project-tile"))
        {
            node.setOnMouseEntered(mouseEvent -> {
                node.setStyle("-fx-border-width: 2px; -fx-border-color: #01579B");
            });

            node.setOnMouseExited(mouseEvent ->  {
                node.setStyle("-fx-border-width: 1px; -fx-border-color: #4FC3F7");
            });

            node.setOnMouseClicked(mouseEvent -> {
                manager.popProjectInfo();
            });
        }

        /*projectTile.setOnMouseEntered(mouseEvent -> {
            projectTile.setStyle("-fx-border-width: 2px; -fx-border-color: #01579B");
        });
        projectTile.setOnMouseExited(mouseEvent ->  {
            projectTile.setStyle("-fx-border-width: 1px; -fx-border-color: #4FC3F7");
        });*/

        logOutBtn.setOnAction(actionEvent -> {
            user.setLogged(false);
            manager.viewLoginPage();
        });

        importCandidateBtn.setOnAction(actionEvent -> {
            manager.viewSignUpPage(user);
        });

        createProjectBtn.setOnAction(actionEvent -> {
            manager.viewNewProjectPane(user);
        });
    }

    /**
     * Load projects from DB and display them on dashboard.
     */
    private void loadProjects() {

        projectsPane.getChildren().clear(); // clear the view
        projectList.clear();

        try {
            Connection connection = DBUtils.getConnection();
            PreparedStatement statement = connection.prepareStatement("use ysofthr");
            statement.execute();
            ResultSet rs = connection.createStatement().executeQuery("SELECT * FROM projects");

            // store all data in ProductsTable Observable List
            while (rs.next())
            {
                projectList.add(new Project(rs.getString("pr_name"), rs.getString("lang"), rs.getString("team"),
                        rs.getDate("startDate").toLocalDate(), rs.getDate("dueDate").toLocalDate(),
                        rs.getDate("creationDate").toLocalDate(), rs.getString("description")));
            }

            // adds projects to view
            for (Project p: projectList)
            {
                // load view
                AnchorPane loader = FXMLLoader.load(getClass().getResource("/app/view/project.fxml"));
                GridPane gridPane = (GridPane) loader.getChildren().get(0);

                gridPane.getStyleClass().add("project-tile");

                // set the project image (same for all)
                Image image = new Image("/app/resources/images/icons8-cashbook-80.png", 70, 70, true, false);
                ImageView imageView = (ImageView) gridPane.getChildren().get(0);
                imageView.setImage(image);
                //centerImage(imageView);

                // set the name of project
                Label nameLabel = (Label) gridPane.getChildren().get(1);
                nameLabel.setText(p.getName());

                // add these elements to the view now
                projectsPane.getChildren().add(loader);
            }

        } catch (SQLException | IOException e) {
            Manager.showAlert(Alert.AlertType.WARNING, "Exception", "Network disconnected. Please try again later.");
        }
    }

    /**
     * Centers image inside of ImageView in FXML.
     * @author https://stackoverflow.com/questions/32674393/centering-imageview-using-javafx
     */
    private static void centerImage(ImageView imgView) {

        Image img = imgView.getImage();

        if (img != null) {
            double w = 0;
            double h = 0;

            double ratioX = imgView.getFitWidth() / img.getWidth();
            double ratioY = imgView.getFitHeight() / img.getHeight();

            double reducCoeff = 0;
            reducCoeff = Math.min(ratioX, ratioY);

            w = img.getWidth() * reducCoeff;
            h = img.getHeight() * reducCoeff;

            imgView.setX((imgView.getFitWidth() - w) / 2);
            imgView.setY((imgView.getFitHeight() - h) / 2);

        }
    }
}
